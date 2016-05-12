package org.globaltester.logging;

import org.globaltester.logging.tags.LogTag;
import org.globaltester.logging.tags.OriginTag;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.log.LogService;
/**
 * This class is a Logger with basic functionalities.
 * 
 * @author amay
 * 
 */
public class BasicLogger {
	/*
	 * Note: This collection of log levels is beyond OSGI platform
	 * specifications. In case this should ever result in any problems the
	 * implementation of a proper alternative should be possible without any
	 * problems.
	 */
	public static final byte TRACE =   1;
	public static final byte DEBUG =   2;
	public static final byte INFO  =   3;
	public static final byte WARN  =   4;
	public static final byte ERROR =   5;
	public static final byte FATAL =   6;
	public static final byte UI    = 120;
	
	public static final byte APDU  =  99;
	public static final String PREFIX_IN      = "<in>";
	public static final String PREFIX_IN_DEC  = "<indec>";
	public static final String PREFIX_OUT_DEC = "<outdec>";
	public static final String PREFIX_OUT     = "<out>";
	
	private static final byte LOGLEVEL_DFLT = DEBUG;
	
	private static MessageEncoder messageEncoder;
	
	
	
	/**
	 * Ensure that this type can not be instantiated
	 */
	private BasicLogger() {
	}
	
	public static void log(String messageContent, LogTag... logTags) {
		if(messageEncoder == null) {
			messageEncoder = new MessageEncoderJson();
		}
		
		Message newMessage = new Message(messageContent, logTags);
		newMessage.addLogTag(new OriginTag(getFirstExternalClass()));
		String encodedMessage = messageEncoder.encode(newMessage);
		logPlain(encodedMessage, org.osgi.service.log.LogService.LOG_INFO);
	}

	private static String getFirstExternalClass(){
		StackTraceElement [] stack = Thread.currentThread().getStackTrace();
		
		for (StackTraceElement e : stack){
			Class<?> classFromStackTraceElement;
			try {
				classFromStackTraceElement = Activator.getContext().getBundle().loadClass(e.getClassName());
			} catch (ClassNotFoundException e1) {
				return e.getClassName();
			}
			
			if (!FrameworkUtil.getBundle(classFromStackTraceElement).equals(Activator.getContext().getBundle())){
				return e.getClassName();
			}
		}
		return null;
	}
	
	/**
	 * Write message to the log, including origin of that message.
	 * 
	 * This method uses LOGLEVEL_DLFT as LogLevel.
	 * 
	 * @param source
	 *            origin of this log message
	 * @param message
	 *            the message to be logged
	 */
	public static void log(InfoSource source, String message) {
		log(source, message, LOGLEVEL_DFLT);
	}

	/**
	 * Write message to the log, including origin of that message.
	 * 
	 * @param source
	 *            origin of this log message
	 * @param message
	 *            the message to be logged
	 * @param logLevel
	 *            log level on which the message is shown
	 */
	public static void log(InfoSource source, String message, byte logLevel) {
		log(source.getIDString(), message, logLevel);
	}
	
	/**
	 * Write message to the log, including originating class of that message.
	 * 
	 * @param className
	 *            originating class of this log message
	 * @param message
	 *            the message to be logged
	 * @param logLevel
	 *            log level on which the message is shown
	 */
	public static void log(Class<?> className, String message, byte logLevel) {
		log(className.getCanonicalName(), message, logLevel);
	}
	
	/**
	 * Write message to the log, including originating class of that message.
	 * 
	 * This method uses LOGLEVEL_DLFT as LogLevel.
	 * 
	 * @param className
	 *            originating class of this log message
	 * @param message
	 *            the message to be logged
	 */
	public static void log(Class<?> className, String message) {
		log(className, message, LOGLEVEL_DFLT);
	}

	/*--------------------------------------------------------------------------------*/

	/**
	 * Write exception to the log, including origin of that message.
	 * 
	 * This method uses LOGLEVEL_DLFT as LogLevel.
	 * 
	 * @param source
	 *            origin of this log message
	 * @param e
	 *            the Exception to be logged
	 */
	public static void logException(InfoSource source, Exception e) {
		logException(source, e, LOGLEVEL_DFLT);
	}

	/**
	 * Write exception to the log, including origin of that message.
	 * 
	 * @param source
	 *            origin of this log message
	 * @param e
	 *            the Exception to be logged
	 * @param logLevel
	 *            log level on which the exception is shown
	 */
	public static void logException(InfoSource source, Exception e, byte logLevel) {
		logException(source.getIDString(), e, logLevel);
	}
	
	/**
	 * Write exception to the log, including origin of that message.
	 * 
	 * @param className
	 *            originating class of this log message
	 * @param e
	 *            the Exception to be logged
	 * @param logLevel
	 *            log level on which the exception is shown
	 */
	public static void logException(Class<?> className, Exception e, byte logLevel) {
		logException(className.getCanonicalName(), e, logLevel);
	}
	
	/**
	 * Write message to the log, formatted including origin of that message.
	 * 
	 * @param source
	 *            originating origin of this log message
	 * @param message
	 *            the message to be logged
	 * @param logLevel
	 *            log level on which the message is shown
	 */
	private static void log(String source, String message, byte logLevel) {
		logPlain(String.format("%s: %s", source, message), logLevel);
	}
	
	/**
	 * Transform an Exception into user readable form and write it to the log,
	 * including origin of that message.
	 * 
	 * @param source
	 *            origin of this log message
	 * @param message
	 *            the message to be logged
	 * @param logLevel
	 *            log level on which the message is shown
	 */
	private static void logException(String source, Exception e, byte logLevel) {
		StringBuilder sb;

		sb = new StringBuilder();

		sb.append("encountered the following exception: ");
		sb.append(e.getClass().getCanonicalName());
		sb.append(" at");
		
		StackTraceElement[] stackTrace = e.getStackTrace();
		
		for(StackTraceElement elem : stackTrace) {
			sb.append("\n" + elem.toString());
		}

		log(source, sb.toString(), logLevel);

		String message = e.getMessage();
		if ((message != null) && (message.length() > 0)) {
			log(source, "Additional info provided is:" + message, logLevel);
		}
		
		/* Same output but in "red" */
//		System.err.println(source.getIDString() + " " + sb.toString());
	}
	
	/**
	 * Write exception to the log, including origin of that message.
	 * 
	 * This method uses LOGLEVEL_DLFT as LogLevel.
	 * 
	 * @param className
	 *            originating class of this log message
	 * @param e
	 *            the Exception to be logged
	 */
	public static void logException(Class<?> className, Exception e) {
		logException(className, e, LOGLEVEL_DFLT);
	}
	
	/**
	 * This message provides direct unprocessed write access to the log.
	 * Use this method only if this is exactly what you want and you know what
	 * you are doing. Otherwise try any other log method provided by this class,
	 * e.g. {@link #log(InfoSource, String, byte)}
	 * 
	 * @param message
	 *            the message to be logged
	 * @param logLevel
	 *            log level on which the message is shown
	 */
	public static void logPlain(String message, int logLevel) {
		if(messageEncoder == null) {
			messageEncoder = new MessageEncoderJson();
		}
		
		LogService logService = Activator.getLogservice();
		if (logService != null){
			logService.log(logLevel, message);
		} else {
			System.out.println(message);
		}
		
	}
}
