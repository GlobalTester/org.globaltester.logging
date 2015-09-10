package org.globaltester.logging;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

/**
 * The class provides a standard {@link #logged(LogEntry)} method that formats
 * and filters incoming log entries. Those entries can be printed with
 * {@link #displayLogMessage(String)}.
 * 
 * @author jkoch
 *
 */
public abstract class AbstractLogListener implements LogListener {
	//FIXME SLS why the change from private? There is a setter.
	protected LogListenerConfig lrc = new LogListenerConfigImpl();
	
	@Override
	public void logged(LogEntry entry) {
		if (lrc.getFilter().logFilter(entry) == true) {
			// format the entry
			String msg = lrc.getFormat().format(entry);
			displayLogMessage(msg);
		}
	}
	
	public LogListenerConfig getLrc() {
		return lrc;
	}

	public void setLrc(LogListenerConfig lrc) {
		this.lrc = lrc;
	}

	/**
	 * is used to print a filtered and formatted log entry to a specific target
	 * 
	 * @param msg
	 *            the log entry to print
	 */
	public abstract void displayLogMessage(String msg);
}
