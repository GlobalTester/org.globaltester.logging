package org.globaltester.logging.filter;

import java.util.Arrays;
import java.util.List;

import org.globaltester.logging.Message;
import org.globaltester.logging.MessageCoderJson;
import org.globaltester.logging.tags.LogTag;
import org.osgi.service.log.LogEntry;

public class TagFilter implements LogFilter {

	public enum Mode{
		EXACT, AT_LEAST_ONE, ALL
	}
	
	private String logTagId;
	private String[] logTagData;
	private Mode mode = Mode.AT_LEAST_ONE;

	// Constructor
	public TagFilter(String logTagId, String... data) {
		this.logTagId = logTagId;
		this.logTagData = data;
	}
	
	public TagFilter(String logTagId, Mode mode, String...data) {
		this(logTagId, data);
		this.mode = mode;
	}

	@Override
	public boolean logFilter(LogEntry entry) {
		Message m = MessageCoderJson.decode(entry.getMessage());
		if (m != null) {
			for (LogTag l : m.getLogTags()) {
				if (l.getId().equals(logTagId)) {
					return checkTagForData(l, logTagData);
				}
			}
		}
		return false;
	}

	private boolean checkTagForData(LogTag l, String[] dataToSearchFor) {
		boolean result;
		
		switch(mode){
		case EXACT:
		case ALL:
			result = true;
			break;
		case AT_LEAST_ONE:
		default:
			result = false;
			break;
		}
		
		List<String> actualTagData = Arrays.asList(l.getAdditionalData());
		for (String current : dataToSearchFor) {
			switch(mode){
			case ALL:
			case EXACT:
				if (!actualTagData.contains(current)) {
					return false;
				}
				break;
			case AT_LEAST_ONE:
				if (actualTagData.contains(current)) {
					return true;
				}
				break;
			}
		}
		
		if (mode.equals(Mode.EXACT)){
			return actualTagData.size() == dataToSearchFor.length; 
		}
		
		return result;
	}

}
