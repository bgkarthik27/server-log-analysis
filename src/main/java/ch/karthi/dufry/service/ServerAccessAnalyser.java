package ch.karthi.dufry.service;

import java.io.IOException;
import java.util.List;

import ch.karthi.dufry.model.TraceLog;

public interface ServerAccessAnalyser {

	public void downloadLogFile() throws IOException;
	
	public void convertLogToJson();
	
	public List<TraceLog> getTraceLogs();
	
}
