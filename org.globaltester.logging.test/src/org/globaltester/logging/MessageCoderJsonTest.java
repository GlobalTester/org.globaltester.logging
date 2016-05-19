package org.globaltester.logging;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageCoderJsonTest {
	
	@Test
	public void testCoding(){
		Message message = new Message("justThis");
		Message result = MessageCoderJson.decode(MessageCoderJson.encode(message));
		assertEquals(message, result);
	}

}
