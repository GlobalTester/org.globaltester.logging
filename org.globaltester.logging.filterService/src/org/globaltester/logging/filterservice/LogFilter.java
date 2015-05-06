package org.globaltester.logging.filterservice;

import org.osgi.service.log.LogEntry;

/**
 * Service for filtering log messages
 * 
 * @author jkoch
 *
 */
public interface LogFilter {			
	
	/**
	 * Filters a LogEntry object and return true or false if the LogEntry object
	 * doesn't fit the requirements
	 * 
	 * @param entry
	 *            the LogEntry object which contains the message to log
	 *            
	 * @return 'true' if the entry fits the criteria and should be logged.
	 *         Otherwise 'false'
	 */
	public boolean logFilter(LogEntry entry);

}
