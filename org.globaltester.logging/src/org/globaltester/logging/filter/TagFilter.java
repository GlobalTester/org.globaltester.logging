package org.globaltester.logging.filter;

import java.util.Arrays;
import java.util.List;

import org.globaltester.logging.Message;
import org.globaltester.logging.MessageCoderJson;
import org.globaltester.logging.tags.LogTag;
import org.osgi.service.log.LogEntry;

public class TagFilter implements LogFilter {

	private String logTagId;
	private String[] logTagData;

	// Constructor
	public TagFilter(String logTagId, String... data) {
		this.logTagId = logTagId;
		this.logTagData = data;
	}
	
	@Override
	public boolean logFilter(LogEntry entry) {
		Message m = MessageCoderJson.decode(entry.getMessage());
		if (m != null) {
			for (LogTag l : m.getLogTags()) {
				if (l.getId().equals(logTagId)) {
					return checkTagForData(l);
				}
			}
		}
		return false;
	}

	private boolean checkTagForData(LogTag l) {
		if (logTagData.length == 0) return true;
		
		List<String> actualTagData = Arrays.asList(l.getAdditionalData());
		for (String current : logTagData) {
			if (actualTagData.contains(current)) {
				return true;
			}
		}
		
		
		return false;
	}

}
