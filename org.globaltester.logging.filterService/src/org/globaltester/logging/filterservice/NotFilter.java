package org.globaltester.logging.filterservice;

import org.osgi.service.log.LogEntry;

public class NotFilter {
	
	private LogFilterService[] filters;

	public NotFilter(LogFilterService[] filters) {
		this.filters = filters;
	}
	
	/**
	 * This method checks all filters with a 'not' logic.
	 * If one filter answers with 'true' the entry should not be logged 
	 * 
	 * @param entry
	 *            Log entry that should be checked
	 * @return true if entry should be logged or false if not
	 */
	public boolean perform(LogEntry entry) {
		
		boolean log = true;
		
		for (LogFilterService filter : filters) {
			
			// check bundles
			if (filter instanceof BundleFilter) {
				if (filter.logFilter(entry)) return false;
			}

			// check log level
			if (filter instanceof LevelFilter) {
				if (filter.logFilter(entry)) return false;
			}
		}
		
		return log;
	}

}
