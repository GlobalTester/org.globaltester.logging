package org.globaltester.logging.tags;

/**
 * 
 * LogTag objects MUST be immutable !!!
 * 
 * @author slutters
 *
 */
public abstract class LogTag {
	
	private LogLevel logLevel;
	private String id;
	
	public LogTag(String id, LogLevel logLevel) {
		this.id = id;
		this.logLevel = logLevel;
	}
	
	public LogLevel getLogLevel() {
		return logLevel;
	}
	
	public String getId() {
		return id;
	}

}
