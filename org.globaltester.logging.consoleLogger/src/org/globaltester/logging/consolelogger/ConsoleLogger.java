package org.globaltester.logging.consolelogger;

import org.globaltester.logging.AbstractLogListener;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;


/**
 * This {@link LogListener} implementation writes all {@link LogEntry}s into {@link System.out}.
 * @author mboonk
 *
 */
public class ConsoleLogger extends AbstractLogListener {

	@Override
	public void displayLogMessage(String msg) {
		// cut at line breaks and print
		String[] splitResult = msg.split("(\\n|\\r)");
		for (int i = 0; i < splitResult.length; i++) {
			System.out.println(splitResult[i]);	
		}
	}
}
