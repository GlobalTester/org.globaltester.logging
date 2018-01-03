package org.globaltester.logging.consolelogger;

import org.globaltester.logging.AbstractLogListener;
import org.json.JSONObject;
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
		try {
			System.out.println(msg.substring(0, msg.indexOf(']') + 1) + " " + new JSONObject(msg.substring(msg.indexOf(']') + 1)).getJSONObject("org.globaltester.logging.Message").get("messageContent"));
		} catch (Exception e) {
			//NOSONAR: most resilient way for logging is simply printing and we have no control over actual formatting of incoming messages
			System.out.println(msg);
		}
	}
}
