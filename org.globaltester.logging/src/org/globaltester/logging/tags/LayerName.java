package org.globaltester.logging.tags;

public enum LayerName {

	IOMANAGER("IoManager"),
	SECUREMESSAGING("SecureMessaging"),
	COMMANDPROCESSOR("AbstractCommandProcessor");
	
	private String name;
	
	private LayerName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
