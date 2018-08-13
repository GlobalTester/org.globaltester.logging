package org.globaltester.logging.format;

import java.text.DateFormat;

import org.globaltester.logging.BasicLogger;
import org.globaltester.logging.Message;

/**
 * This formats log messages for file output.
 * @author mboonk
 *
 */
public class GtFileLogFormatter implements LogFormatService {

	public static final String DATE_FORMAT_GT_STRING = "yyyy-MM-dd' 'HH:mm:ss,SSS";
	public static final String DATE_FORMAT_GT_ISO_STRING = "yyyy-MM-dd'T'HH:mm:ss";

	DateFormat dateFormat;
	
	public GtFileLogFormatter(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@Override
	public String format(Message msg) {
		String date = LogFormat.getTimestamp(msg, dateFormat) + " - ";
		String logLevel = LogFormat.getLogLevel(msg);
		String stackTrace = LogFormat.extractTag(msg, BasicLogger.EXCEPTION_STACK_TAG_ID);
		
		String logMessage = date + String.format("%-5s", logLevel) + " - " + msg.getMessageContent();
		if (stackTrace != null) {
			logMessage += "\n" + stackTrace;
		}
		return logMessage;	
	}

	
}
