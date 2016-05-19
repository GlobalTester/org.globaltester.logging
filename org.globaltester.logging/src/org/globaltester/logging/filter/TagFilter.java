package org.globaltester.logging.filter;

import java.util.Arrays;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.globaltester.logging.Message;
import org.globaltester.logging.MessageCoderJson;
import org.globaltester.logging.tags.LogTag;
import org.osgi.service.log.LogEntry;
import org.osgi.service.prefs.Preferences;

public class TagFilter implements LogFilter{

	private String [] logTags;
	
	// place for persistent storage of filter data
	Preferences preferences = InstanceScope.INSTANCE
			.getNode("de.persosim.simulator.ui.utils.logging");
	
	// Constructor
	public TagFilter(String ... logTagId) {
		setLogLevels(logTagId);
	}
	
	
	public void setLogLevels(String [] logTagId) {
		this.logTags = Arrays.copyOf(logTagId, logTagId.length);
	}
	
	
	public String [] getLogTags() {
		return logTags;
	}

	@Override
	public boolean logFilter(LogEntry entry) {
		for (String currentTag : logTags) {
			Message m = MessageCoderJson.decode(entry.getMessage());
			if (m != null) {
				for (LogTag l : m.getLogTags()){
					if (l.getId().equals(currentTag)){
						return true;
					}
				}
			}
		}
		return false;
	}

}
