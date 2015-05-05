package org.globaltester.logging.filterservice;

import org.osgi.service.log.LogEntry;

public class OrFilter {

	private LogFilterService[] filters;

	public OrFilter(LogFilterService[] filters) {
		this.filters = filters;
	}

	/**
	 * This method checks all filters with an 'or' logic.
	 * If one filter answers with true the entry should be logged
	 * @param entry
	 * @return a true (log entry ) or false (don't log)
	 */
	public boolean perform(LogEntry entry) {

		boolean log = false;

		for (LogFilterService filter : filters) {

			// check bundles
			if (filter instanceof BundleFilter) {
				if (filter.logFilter(entry)) return true;
			}

			// check log level
			if (filter instanceof LevelFilter) {
				if (filter.logFilter(entry)) return true;
			}
		}
		return log;
	}
}
