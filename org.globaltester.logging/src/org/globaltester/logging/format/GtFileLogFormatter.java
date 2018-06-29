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
				logLevel = extractTag(curTag, BasicLogger.LOG_LEVEL_TAG_ID);
				stackTrace = extractTag(curTag, BasicLogger.EXCEPTION_STACK_TAG_ID);
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

	/**
	 * Check if the tag has the given ID and if so extract the first additional data string.
	 * 
	 * @param curTag tag to be checked
	 * @param id 
	 * @return the first additional data string if available and the ID matches the tag, else null 
	 */
	private static String extractTag(LogTag curTag, String id) {
		if (id.equals(curTag.getId()) && curTag.getAdditionalData().length >= 1) {
			return curTag.getAdditionalData()[0];
		}
		return null;
	}

	
}
