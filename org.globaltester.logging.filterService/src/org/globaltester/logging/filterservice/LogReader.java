package org.globaltester.logging.filterservice;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

public abstract class LogReader implements LogListener {
	
	public abstract void logged(LogEntry entry);
}
