package org.globaltester.logging.filter;

import java.util.Arrays;

import org.osgi.service.log.LogEntry;

public class LevelFilter implements LogFilter{

	private byte[] logLevels;

	// Constructor
	public LevelFilter(byte... logLevels) {
		setLogLevels(logLevels);
	}
	
	
	public void setLogLevels(byte[] logLevels) {
		this.logLevels = Arrays.copyOf(logLevels, logLevels.length);
	}
	
	
	public byte[] getLogLevels() {
		return logLevels;
	}
	
	@Override
	public boolean logFilter(LogEntry entry) {
		// check if log level is ok
		for (int currentLevel : logLevels) {
			if (entry.getLevel() == currentLevel) {
				// the entry is ok and should be logged
				return true;
			}
		}
		return false;
	}

}
