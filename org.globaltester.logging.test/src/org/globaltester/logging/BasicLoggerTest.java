package org.globaltester.logging;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BasicLoggerTest {
	@Test
	public void testLineBreaksShortString() {
		String string = "short";
		assertEquals(string, BasicLogger.breakLines(string, 100));
	}
	@Test
	public void testLineBreaksEmptyString() {
		String string = "";
		assertEquals(string, BasicLogger.breakLines(string, 100));
	}
	
	@Test
	public void testLineBreaksLongString() {
		BasicLogger.setLongLineMarker("LONG");
		String string = "string";
		assertEquals("str\nLONGing", BasicLogger.breakLines(string, 3));
		BasicLogger.setLongLineMarker("");
	}
	
	@Test
	public void testLineBreaksLongStringWithLinebreaks() {
		BasicLogger.setLongLineMarker("LONG");
		String string = "line1\nl2\nlongline3";
		assertEquals("lin\nLONGe1\nl2\nlon\nLONGgli\nLONGne3", BasicLogger.breakLines(string, 3));
		BasicLogger.setLongLineMarker("");
	}
}
