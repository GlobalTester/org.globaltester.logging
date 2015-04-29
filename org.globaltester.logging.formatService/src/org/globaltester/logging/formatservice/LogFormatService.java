package org.globaltester.logging.formatservice;

import org.osgi.service.log.LogEntry;

/**
 * Service for formatting log messages
 * 
 * @author jkoch
 *
 */
public interface LogFormatService {
	
	/**
	 * returns a formatted LogEntry object. 
	 * 
	 * @param LogEntry
	 *            	the Log message to format
	 * @return the formatted log message as a String
	 */
	public String format(LogEntry entry);

}
