package org.globaltester.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.globaltester.logging.tags.LogTag;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Specifically tests the JSON encoding of log Messages 
 * @author amay
 *
 */
public class MessageCoderJsonTest extends MessageCoderTest {
	
	@Override
	public String encode(Message msg) {
		return MessageCoderJson.encode(msg);
	}

	@Override
	public Message decode(String messageRep) {
		return MessageCoderJson.decode(messageRep);
	}
	
	@Test
	public void testEncode_messageContent(){
		String CONTENT = "messageContentTestData";
		Message message = new Message(CONTENT);
		
		String encoded = MessageCoderJson.encode(message);
		
		assertEquals(CONTENT, new JSONObject(encoded).getString(MessageCoderJson.MSG));
	}
	
	@Test
	public void testEncode_noTags(){
		Message message = new Message("message with no tags");
		
		String encoded = MessageCoderJson.encode(message);
		
		assertFalse(new JSONObject(encoded).has(MessageCoderJson.TAGS));
	}
	
	@Test
	public void testEncode_simpleTag(){
		Message message = new Message("message with simple tag");
		String logTagId = "logTagId";
		LogTag logTag = new LogTag(logTagId);
		message.addLogTag(logTag);
	
		String encoded = MessageCoderJson.encode(message);
		
		JSONObject encodedJson =new JSONObject(encoded); 
		assertNotNull(encodedJson.getJSONArray(MessageCoderJson.TAGS));
		assertEquals(1, encodedJson.getJSONArray(MessageCoderJson.TAGS).length());
		
		JSONObject jsonTag = (JSONObject) encodedJson.getJSONArray(MessageCoderJson.TAGS).get(0);
		verifyJsonTag(logTag, jsonTag);		
	}
	
	@Test
	public void testEncode_tagWithAdditionalData(){
		Message message = new Message("message with simple tag");
		LogTag logTag = new LogTag("logTagId","some", "other", "data");
		message.addLogTag(logTag);
		
		String encoded = MessageCoderJson.encode(message);
		
		JSONObject encodedJson =new JSONObject(encoded); 
		assertNotNull(encodedJson.getJSONArray(MessageCoderJson.TAGS));
		assertEquals(1, encodedJson.getJSONArray(MessageCoderJson.TAGS).length());
		
		JSONObject jsonTag = (JSONObject) encodedJson.getJSONArray(MessageCoderJson.TAGS).get(0);
		verifyJsonTag(logTag, jsonTag);
	}
	
	@Test
	public void testEncode_multipleTags(){
		Message message = new Message("message with simple tag");
		LogTag firstLogTag = new LogTag("firstLogTag");
		message.addLogTag(firstLogTag);
		LogTag secondLogTag = new LogTag("logTagId","some", "other", "data");
		message.addLogTag(secondLogTag);
		LogTag thirdLogTag = new LogTag("thirdLogTag");
		message.addLogTag(thirdLogTag);
		
		String encoded = MessageCoderJson.encode(message);
		
		JSONObject encodedJson =new JSONObject(encoded); 
		assertNotNull(encodedJson.getJSONArray(MessageCoderJson.TAGS));
		assertEquals(3, encodedJson.getJSONArray(MessageCoderJson.TAGS).length());
		
		verifyJsonTag(firstLogTag, (JSONObject) encodedJson.getJSONArray(MessageCoderJson.TAGS).get(0));
		verifyJsonTag(secondLogTag, (JSONObject) encodedJson.getJSONArray(MessageCoderJson.TAGS).get(1));
		verifyJsonTag(thirdLogTag, (JSONObject) encodedJson.getJSONArray(MessageCoderJson.TAGS).get(2));
	}

	private static void verifyJsonTag(LogTag logTag, JSONObject jsonTag) {
		assertEquals(logTag.getId(), jsonTag.getString(MessageCoderJson.TAG_ID));
		if (logTag.getAdditionalData().length > 0) {
			String[] expectedAdditionalData = logTag.getAdditionalData();
			JSONArray actualAdditioanlData = jsonTag.getJSONArray(MessageCoderJson.TAG_ADDITIONALDATA);
			
			for (int i = 0; i < expectedAdditionalData.length; i++) {
				assertEquals(expectedAdditionalData[i], actualAdditioanlData.get(i));
			}
		}
	}
	
	@Test
	public void testDecode_nonJsonMessage(){
		assertNull(MessageCoderJson.decode("non JSON message"));
	}

}
