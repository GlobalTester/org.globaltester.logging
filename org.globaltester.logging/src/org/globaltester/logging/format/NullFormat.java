package org.globaltester.logging.format;

import org.osgi.service.log.LogEntry;

/**
 * "Formats" the {@link LogEntry} to consist only of the message.
 * @author jkoch
 */
public class NullFormat implements LogFormatService{	
	@Override
	public String format(LogEntry entry) {
		return entry.getMessage();
	}
}
