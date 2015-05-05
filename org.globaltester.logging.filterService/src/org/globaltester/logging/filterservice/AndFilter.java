package org.globaltester.logging.filterservice;

import java.util.LinkedList;

import org.osgi.service.log.LogEntry;

public class AndFilter {

	private LogFilterService [] filters;

	
	public AndFilter(LogFilterService [] filters) {
		this.filters=filters;
	}
	

	/**
	 * This method checks all filters with an 'and' logic.
	 * If one filter answers with false the entry shouldn't be logged.
	 * 
	 * @param entry
	 *            Log entry that should be checked
	 * @return true if entry should be logged or false if not
	 */
	public boolean perform(LogEntry entry) {
		
		boolean log = false;
		
		for (LogFilterService filter : filters) {
			
			// check bundles
			if (filter instanceof BundleFilter) {
				if (filter.logFilter(entry)) log = true;
				else return false;
			}

			// check log level
			if (filter instanceof LevelFilter) {
				if (filter.logFilter(entry)) log = true;
				else return false;
			}
		}
		
		return log;
	}
}
