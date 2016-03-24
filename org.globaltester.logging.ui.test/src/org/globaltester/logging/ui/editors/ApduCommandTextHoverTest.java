package org.globaltester.logging.ui.editors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class ApduCommandTextHoverTest {

	private static ApduCommandTextHover textHover;

	@BeforeClass
	public static void setUp() {
		textHover = new ApduCommandTextHover();
	}

	private String buildApduLogfileSegment(String apduString) {
		String retVal = "2012-03-27 15:23:24,289 INFO  - => Command APDU [\n";
		retVal += "0000:  "+apduString+"              ............\n";
		retVal += "\n] C-APDU";
		return retVal;
	}

	@Test
	public void testEmptyAPDU() throws Exception {
		String hoverInfoText = textHover.getHoverInfo("");
		assertNotNull("No HoverInfo returned", hoverInfoText);
	}

	@Test
	public void testSelectApplication() throws Exception {
		String apduString = "00 A4 04 0C 07 A0 00 00 02 47 10 01";
		String apduLogfileSegment = buildApduLogfileSegment(apduString);

		String hoverInfoText = textHover.getHoverInfo(apduLogfileSegment);

		assertNotNull("No HoverInfo returned", hoverInfoText);
		assertTrue("Returned HoverInfo does not describe data field",
				hoverInfoText.contains("AID or DF to select"));
	}
	
	@Test
	public void testSelectApplicationEPassport() throws Exception {
		String apduString = "00 A4 04 0C 07 A0 00 00 02 47 10 01";
		String apduLogfileSegment = buildApduLogfileSegment(apduString);

		String hoverInfoText = textHover.getHoverInfo(apduLogfileSegment);

		assertNotNull("No HoverInfo returned", hoverInfoText);
		assertTrue("Returned HoverInfo does not describe AID correctly",
				hoverInfoText.contains("(e-passport)"));
	}
	
	@Test
	public void testSelectApplicationEID() throws Exception {
		String apduString = "00 A4 04 0C 09 E8 07 04 00 7F 00 07 03 02";
		String apduLogfileSegment = buildApduLogfileSegment(apduString);

		String hoverInfoText = textHover.getHoverInfo(apduLogfileSegment);

		assertNotNull("No HoverInfo returned", hoverInfoText);
		assertTrue("Returned HoverInfo does not describe AID correctly",
				hoverInfoText.contains("(eID)"));
	}
	
	@Test
	public void testSelectApplicationEsign() throws Exception {
		String apduString = "00 A4 04 0C 0A A0 00 00 01 67 45 53 49 47 4E";
		String apduLogfileSegment = buildApduLogfileSegment(apduString);

		String hoverInfoText = textHover.getHoverInfo(apduLogfileSegment);

		assertNotNull("No HoverInfo returned", hoverInfoText);
		assertTrue("Returned HoverInfo does not describe AID correctly",
				hoverInfoText.contains("(eSign)"));
	}

}
