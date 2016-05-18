package org.globaltester.logging.tags;

/**
 * 
 * LogTag objects MUST be immutable !!!
 * 
 * @author slutters
 *
 */
public abstract class LeveledLogTag extends LogTag {
	
	private LogLevel logLevel;
	
	public LeveledLogTag(String id, LogLevel logLevel, String ... additionalData) {
		super(id, additionalData);
		this.logLevel = logLevel;
	}
	
	public LogLevel getLogLevel() {
		return logLevel;
	}
	
}
