package ch.karthi.dufry.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.karthi.dufry.model.TraceLog;
import ch.karthi.dufry.service.ServerAccessAnalyser;

@RestController
public class ServerAccessAnalysisController {

	@Autowired
	private ServerAccessAnalyser serverAccessAnalyer;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "/logs", produces = "application/json")
	public List<TraceLog> getLogs() throws IOException {
		 serverAccessAnalyer.downloadLogFile();
		 serverAccessAnalyer.convertLogToJson();
		return serverAccessAnalyer.getTraceLogs();
	}

}
