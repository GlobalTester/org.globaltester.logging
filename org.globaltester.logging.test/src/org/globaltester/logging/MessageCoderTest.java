package org.globaltester.logging;

import static org.junit.Assert.*;

import org.globaltester.logging.tags.LogTag;
import org.junit.Test;

/**
 * Super class for all MessageCoder implementations, essentially tests full
 * roundtrips of encoding and decoding messages
 * 
 * @author amay
 *
 */
public abstract class MessageCoderTest {

	public abstract String encode(Message msg);

	public abstract Message decode(String messageRep);

	@Test
	public void testRoundtrip_simpleMessage() {
		Message message = new Message("simpleMessage");

		Message roundtripResult = decode(encode(message));

		assertEquals(message, roundtripResult);
	}

	@Test
	public void testRoundtrip_complexMessage() {
		Message message = new Message("complexMessage");

		Message roundtripResult = decode(encode(message));

		assertEquals(message, roundtripResult);
	}

	@Test
	public void testRoundtrip_messageContent() {
		String CONTENT = "messageContentTestData";
		Message message = new Message(CONTENT);

		Message roundtripResult = decode(encode(message));

		assertEquals(CONTENT, roundtripResult.getMessageContent());
	}

	@Test
	public void testRoundtrip_noTags() {
		Message message = new Message("message with no tags");
		Message result = decode(encode(message));

		assertNotNull(result.getLogTags());
		assertTrue(result.getLogTags().isEmpty());
	}

	@Test
	public void testRoundtrip_simpleTag() {
		Message message = new Message("message with simple tag");
		LogTag logTag = new LogTag("logTagId");
		message.addLogTag(logTag);
		Message result = decode(encode(message));

		assertNotNull(result.getLogTags());
		assertEquals(1, result.getLogTags().size());
		verifyLogTag(logTag, result.getLogTags().get(0));
	}

	@Test
	public void testRoundtrip_tagWithAdditionalData() {
		Message message = new Message("message with simple tag");
		LogTag logTag = new LogTag("logTagId", "some", "other", "data");
		message.addLogTag(logTag);

		Message result = decode(encode(message));

		assertNotNull(result.getLogTags());
		assertEquals(1, result.getLogTags().size());
		verifyLogTag(logTag, result.getLogTags().get(0));
	}

	@Test
	public void testRoundtrip_multipleTags() {
		Message message = new Message("message with simple tag");
		LogTag firstLogTag = new LogTag("firstLogTag");
		message.addLogTag(firstLogTag);
		LogTag secondLogTag = new LogTag("logTagId", "some", "other", "data");
		message.addLogTag(secondLogTag);
		LogTag thirdLogTag = new LogTag("thirdLogTag");
		message.addLogTag(thirdLogTag);

		Message result = decode(encode(message));

		assertNotNull(result.getLogTags());
		assertEquals(3, result.getLogTags().size());
		verifyLogTag(firstLogTag, result.getLogTags().get(0));
		verifyLogTag(secondLogTag, result.getLogTags().get(1));
		verifyLogTag(thirdLogTag, result.getLogTags().get(2));
	}

	private void verifyLogTag(LogTag expectedLogTag, LogTag actualLogTag) {
		assertEquals(expectedLogTag.getId(), actualLogTag.getId());
		assertArrayEquals(expectedLogTag.getAdditionalData(), actualLogTag.getAdditionalData());

	}

}
