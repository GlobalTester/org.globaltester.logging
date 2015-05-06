package org.globaltester.logging.consolelogger;

import org.globaltester.logging.filterservice.LogReader;
import org.globaltester.logging.filterservice.LogReaderConfig;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;


/**
 * This {@link LogListener} implementation writes all {@link LogEntry}s into {@link System.out}.
 * @author mboonk
 *
 */
public class ConsoleLogger extends LogReader {
	
	LogReaderConfig lrc = new LogReaderConfig();
	
	@Override
	public void logged(LogEntry entry) {		
		if(lrc.checkFilter(entry)){
			// format the entry
			String logEntry = lrc.formatter.format(entry);
			
			// cut at line breaks and print
			String[] splitResult = logEntry.split("(\\n|\\r)");
			for (int i = 0; i < splitResult.length; i++) {
				System.out.println(splitResult[i]);
			}
		}
	}
}
