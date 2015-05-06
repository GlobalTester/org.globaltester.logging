package org.globaltester.logging.filterservice;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

public abstract class LogReader implements LogListener {
	
	LogReaderConfig lrc = new LogReaderConfig();
	
	@Override
	public void logged(LogEntry entry) {
		if (lrc.getFilter().logFilter(entry) == true) {
			// format the entry
			String msg = lrc.getFormat().format(entry);
			displayLogMessage(msg);
		}
	}

	/**
	 * is used to print a filtered and formatted log entry to a specific target
	 * 
	 * @param msg
	 *            the log entry to print
	 */
	public abstract void displayLogMessage(String msg);
}
