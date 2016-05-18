package org.globaltester.logging.tags;

import java.util.Arrays;

public class LogTag {

	protected String id;
	private String [] additionalData;

	public LogTag(String id, String ... additionalData) {
		this.id = id;
		this.additionalData = additionalData;
	}

	public String getId() {
		return id;
	}

	public String [] getAdditionalData (){
		return Arrays.copyOf(additionalData, additionalData.length);
	}

}