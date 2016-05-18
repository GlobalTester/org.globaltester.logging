package org.globaltester.logging;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageCoderJsonTest {
	
	@Test
	public void testCoding(){
		MessageCoderJson coder = new MessageCoderJson();
		Message message = new Message("justThis");
		Message result = coder.decode(coder.encode(message));
		assertEquals(message, result);
	}

}
