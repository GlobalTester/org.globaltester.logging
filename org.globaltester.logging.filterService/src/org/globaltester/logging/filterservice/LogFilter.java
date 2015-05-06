package org.globaltester.logging.filterservice;

import org.osgi.service.log.LogEntry;

/**
 * Service for filtering log messages
 * 
 * @author jkoch
 *
 */
public interface LogFilterService {	
	
//	public void setLogLevels(byte[] loglevels);
//	
//	public byte [] getLogLevels();
//	
//	public void setBundleFilters(LinkedList<String> bundleFilters);
//	
//	/**
//	 * saves filter data in the preferences
//	 */
//	public void saveFilter();
//	
//	/**
//	 * loads filter data from preferences
//	 */
//	public void loadFilter();
	
	/**
	 * Filters a LogEntry object and return true or false if the LogEntry object
	 * doesn't fit the requirements
	 * 
	 * @param entry
	 *            the LogEntry object which contains the message to log
	 */
	public boolean logFilter(LogEntry entry);

}
