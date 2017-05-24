package org.globaltester.logging.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.globaltester.logging.BasicLogger;
import org.globaltester.logging.Message;
import org.globaltester.logging.MessageCoderJson;
import org.osgi.service.log.LogEntry;

/**
 * This formats log messages for file output.
 * @author mboonk
 *
 */
public class GtFileLogFormatter implements LogFormatService {

	public static final DateFormat DATE_FORMAT_GT = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss,SSS");
	public static final DateFormat DATE_FORMAT_ISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	DateFormat dateFormat;
	
	public GtFileLogFormatter(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@Override
	public String format(LogEntry entry) {
		String date = dateFormat.format(new Date(entry.getTime())) + " - ";
		Message message = MessageCoderJson.decode(entry.getMessage());
		if (message != null){
			// extracts log level and message from the encoded message
			return date + String.format("%-5s", message.getLogTags().stream()
							.filter(p -> p.getId().equals(BasicLogger.LOG_LEVEL_TAG_ID)).findFirst().get()
							.getAdditionalData()[0])
					+ " - " + message.getMessageContent();	
		} else {
			return date + String.format("%-5s", BasicLogger.convertOsgiToLogLevel(entry.getLevel()).name()) + " - " + entry.getMessage();
		}
	}

}
