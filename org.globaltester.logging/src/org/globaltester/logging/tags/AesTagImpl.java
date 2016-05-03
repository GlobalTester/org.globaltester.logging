package org.globaltester.logging.tags;

public enum AesTagImpl implements AesTag {
	
	AES_1(1), AES_2(2), AES_3(3), AES_4(4), AES_5(5), AES_6(6);
	
	
	
	public static final String AES = "AES";
	
	private int logLevel;
	
	
	
	private AesTagImpl(int logLevel) {
		this.logLevel = logLevel;
	}

}
