package org.globaltester.logging;

import java.util.ArrayList;
import java.util.List;

import org.globaltester.logging.tags.LogTag;

public class Message {
	
	protected String messageContent;
	protected ArrayList<LogTag> messageTags;
	
	public Message(String messageContent, LogTag... logTags) {
		this.messageContent = messageContent;
		
		messageTags = new ArrayList<>();
		addLogTag(logTags);
	}
	
	public Message(String messageContent, List<LogTag> messageTags) {
		this(messageContent, messageTags.toArray(new LogTag[messageTags.size()]));
	}
	
	
	
	public Message(String messageContent) {
		this(messageContent, new LogTag[0]);
	}
	
	public void addLogTag(LogTag... logTags) {
		for(LogTag logTag : logTags) {
			messageTags.add(logTag);
		}
	}
	
}
