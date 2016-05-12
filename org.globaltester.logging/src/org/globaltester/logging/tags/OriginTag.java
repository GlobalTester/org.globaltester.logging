package org.globaltester.logging.tags;

public class OriginTag extends LogTag {
	
	private String originatingClass;

	public OriginTag(String originatingClass) {
		super(originatingClass, LogLevel.TRACE);
		this.originatingClass = originatingClass;
	}
	
	public String getOriginatingClass() {
		return originatingClass;
	}

}
