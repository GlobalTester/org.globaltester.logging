package org.globaltester.logging.format;

import java.text.DateFormat;

import org.osgi.service.log.LogEntry;

/**
 * "Formats" the {@link LogEntry} to consist only of the message.
 * @author jkoch
 */
public class NullFormat implements LogFormatService{

	DateFormat format = DateFormat.getDateTimeInstance();
	
	@Override
	public String format(LogEntry entry) {
		return entry.getMessage();
	}
}
