package org.globaltester.logging.format;

import java.text.DateFormat;
import java.util.Date;

import org.globaltester.logging.BasicLogger;
import org.globaltester.logging.Message;
import org.globaltester.logging.MessageCoderJson;
import org.globaltester.logging.tags.LogTag;
import org.osgi.service.log.LogEntry;

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
	public String format(LogEntry entry) {
		String date = dateFormat.format(new Date(entry.getTime())) + " - ";
		Message message = MessageCoderJson.decode(entry.getMessage());
		String stackTrace = null;
		String logLevel = null;
		if (message != null){
			// override log level from the encoded message (if present)
			for (LogTag curTag : message.getLogTags()) {
				if (BasicLogger.LOG_LEVEL_TAG_ID.equals(curTag.getId()) && curTag.getAdditionalData().length >= 1) {
					logLevel = curTag.getAdditionalData()[0];
				}
				if (BasicLogger.EXCEPTION_STACK_TAG_ID.equals(curTag.getId()) && curTag.getAdditionalData().length >= 1) {
					stackTrace = curTag.getAdditionalData()[0];
				}
				if (logLevel != null && stackTrace != null) {
					break;
				}
			}
			
			if (logLevel == null) {
				logLevel = BasicLogger.convertOsgiToLogLevel(entry.getLevel()).name();
			}
			
			String logMessage = date + String.format("%-5s", logLevel) + " - " + message.getMessageContent();
			if (stackTrace != null) {
				logMessage += "\n" + stackTrace;
			}
			return logMessage;	
		} else {
			return date + String.format("%-5s", logLevel) + " - " + entry.getMessage();
		}
	}

}
