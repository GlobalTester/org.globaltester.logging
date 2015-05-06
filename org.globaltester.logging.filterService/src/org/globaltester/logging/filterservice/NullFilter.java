package org.globaltester.logging.filterservice;

import org.osgi.service.log.LogEntry;

/**
 * Simple filter that just always returns true which means that the log entry
 * should be logged
 * 
 * @author jkoch
 *
 */
public class NullFilter implements LogFilter {

	@Override
	public boolean logFilter(LogEntry entry) {
		return true;
	}

}
