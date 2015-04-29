package org.globaltester.logging.consolelogger;

import org.globaltester.logging.filterservice.LogFilterService;
import org.globaltester.logging.formatservice.LogFormatService;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;


/**
 * This {@link LogListener} implementation writes all {@link LogEntry}s into {@link System.out}.
 * @author mboonk
 *
 */
public class ConsoleLogger implements LogListener {
	
	/**
	 * This method is used to show logEntries even if format/filter services are
	 * unavailable.
	 * 
	 * @param entry
	 *            the log message to show
	 */
	public void standardOutput(LogEntry entry){
		String[] splitResult = entry.getMessage().split("(\\n|\\r)");
		for (int i = 0; i < splitResult.length; i++) {
			System.out.println(splitResult[i]);
		}
	}
	
	@Override
	public void logged(LogEntry entry) {
		
		LogFilterService filter = Activator.getLogFilterService();
		LogFormatService format = Activator.getLogFormatService();
		
		if (filter != null && format != null) {
			// use filter on the entry. Checks Bundle and log level
			if (filter.logFilter(entry)) {

				// format the entry
				String logEntry = format.format(entry);

				// cut at line breaks and print
				String[] splitResult = logEntry.split("(\\n|\\r)");
				for (int i = 0; i < splitResult.length; i++) {
					System.out.println(splitResult[i]);
				}
			}
		} else {
			// format or filter service not available...show logs anyway
			standardOutput(entry);
		}
		
	}

}
