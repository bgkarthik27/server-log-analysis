package ch.karthi.dufry.implementation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.karthi.dufry.model.Datetime;
import ch.karthi.dufry.model.Request;
import ch.karthi.dufry.model.TraceLog;
import ch.karthi.dufry.service.ServerAccessAnalyser;

@Component
public class ServerAccessAnalyserImpl implements ServerAccessAnalyser {

	private static final String USERNAME = "anonymous";

	private static final Logger LOG = LoggerFactory.getLogger(ServerAccessAnalyserImpl.class);

	private static final String EXTRACTED_FILE = "downloads/epa-http.txt";

	private static final String DOWNLOADED_Z_FILE = "downloads/epa-http.txt.Z";

	private static final int PORT = 21;

	private static final String ITA_EE_LBL_GOV = "ita.ee.lbl.gov";

	@Value("${download.log.file:false}")
	private boolean downloadLogFile;

	@Override
	public void downloadLogFile() {
		if (downloadLogFile) {
			doDownload();
			doExtract();
		}
	}

	private void doExtract() {
		try (InputStream fin = Files.newInputStream(Paths.get(DOWNLOADED_Z_FILE));
				BufferedInputStream in = new BufferedInputStream(fin);
				OutputStream out = Files.newOutputStream(Paths.get(EXTRACTED_FILE), StandardOpenOption.CREATE);
				ZCompressorInputStream zIn = new ZCompressorInputStream(in);) {
			final byte[] buffer = new byte[1024];
			int n = 0;
			while (-1 != (n = zIn.read(buffer))) {
				out.write(buffer, 0, n);
			}
		} catch (IOException e) {
			LOG.error("downloaded file could be extracted due to:", e);
		}

	}

	private void doDownload() {
		FTPClient client = new FTPClient();
		try {
			client.connect(ITA_EE_LBL_GOV, PORT);
			client.login(USERNAME, "");
			client.setFileType(FTP.BINARY_FILE_TYPE);
			File file = new File(DOWNLOADED_Z_FILE);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			client.retrieveFile("/traces/epa-http.txt.Z", fos);
			client.disconnect();
		} catch (IOException e) {
			LOG.error("file could be downloaded due to:", e);
		}

	}

	@Override
	public void convertLogToJson() {
		Path path = Paths.get(EXTRACTED_FILE);
		try {
			List<TraceLog> traceLogs = new ArrayList<>();
			try (Stream<String> lines = Files.lines(path);) {
				lines.forEach(populateTraceLog(traceLogs));
			}

			doWriteJson(traceLogs);
		} catch (IOException e) {
			LOG.error("Exception occured while parsing", e);
		}

	}
	
	@Override
	public List<TraceLog> getTraceLogs() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			File resultFile = new File("downloads/epa-http.json");
			return mapper.readValue(resultFile, new TypeReference<List<TraceLog>>() { });
		} catch (IOException e) {
			LOG.error("Exception occured while mapping", e);
		}
		return Collections.emptyList();
	}

	private void doWriteJson(List<TraceLog> traceLogs)
			throws IOException, JsonGenerationException, JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		File resultFile = new File("downloads/epa-http.json");
		resultFile.createNewFile();
		mapper.writerWithDefaultPrettyPrinter().writeValue(resultFile, traceLogs);
	}

	private Consumer<? super String> populateTraceLog(List<TraceLog> traceLogs) {
		return line -> {
			try {
				TraceLog traceLog = new TraceLog();
				String[] splittedStrings = line.split("\"");
				String requestString = splittedStrings[1];
				String[] methodAndParameter = requestString.split(" ");
				Request request = new Request();
				request.setMethod(methodAndParameter[0]);
				request.setUrl(methodAndParameter[1]);
				int indexOfProtocolType = requestString.indexOf("HTTP/1.0");
				if (indexOfProtocolType > 0) {
					String httpVersionString = requestString.substring(indexOfProtocolType, requestString.length());
					String[] httpVersionAndTypeSplitted = httpVersionString.split("/");
					request.setProtocol(httpVersionAndTypeSplitted[0]);
					request.setProtocolVersion(httpVersionAndTypeSplitted[1]);
				}
				String[] ipAndDate = splittedStrings[0].split(" ");
				traceLog.setHost(ipAndDate[0]);
				String[] dateAndTime = ipAndDate[1].replaceAll("[\\[\\]]", "").split(":");
				traceLog.setDatetime(new Datetime(dateAndTime[0], dateAndTime[1], dateAndTime[2], dateAndTime[3]));
				traceLog.setRequest(request);
				String[] responseAndSize = splittedStrings[2].split(" ");
				traceLog.setResponseCode(responseAndSize[1]);
				traceLog.setDocumentSize(responseAndSize[2]);
				traceLogs.add(traceLog);
			} catch (Exception e) {
				LOG.error("Improper line format to parse: {}", line);
			}

		};
	}

}
