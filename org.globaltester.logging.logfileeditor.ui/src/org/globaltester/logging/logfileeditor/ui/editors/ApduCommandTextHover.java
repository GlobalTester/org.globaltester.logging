package org.globaltester.logging.logfileeditor.ui.editors;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.globaltester.base.util.StringUtil;
import org.globaltester.logging.legacy.logger.GtErrorLogger;
import org.globaltester.logging.logfileeditor.ui.Activator;

public class ApduCommandTextHover implements ITextHover {

	public static class HoverInfo {

		public enum FieldType {
			APDU, CLA, INS, P1P2, LC, DATA, LE
		}

		private FieldType type;
		private String content;
		private String descr;

		public HoverInfo(FieldType type, String content, String descr) {
			this.type = type;
			this.content = content;
			this.descr = descr;
		}

		public String getField() {
			return type.toString();
		}

		public String getContent() {
			return content;
		}

		public String getDescription() {
			return descr;
		}

	}

	private static final int OFFSET_LC = 4;
	
	private static HashMap<String, String> aidMap = new HashMap<String, String>();
	{
		aidMap.put("A0 00 00 02 47 10 01","e-passport");
		aidMap.put("E8 07 04 00 7F 00 07 03 02","eID");
		aidMap.put("A0 00 00 01 67 45 53 49 47 4E", "eSign");
	}
	

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		IDocument doc = textViewer.getDocument();
		try {
			String content = doc.get(hoverRegion.getOffset(),
					hoverRegion.getLength());
			return getHoverInfo(content);
		} catch (BadLocationException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
			return e.toString();
		}
	}

	public String getHoverInfo(String content) {
		try {
			HoverInfo[] hoverInfos = parseContentToHoverInfo(content);
			return buildHoverInfoString(hoverInfos);
		} catch (ArrayIndexOutOfBoundsException e) {
			return e.toString();
		}
	}

	private String buildHoverInfoString(HoverInfo[] hoverInfoArray) {
		// get length of apdu Part field
		int apduPartLength = 0;
		for (int i = 0; i < hoverInfoArray.length; i++) {
			int currentLength = hoverInfoArray[i].getContent().length();
			apduPartLength = (currentLength > apduPartLength) ? currentLength
					: apduPartLength;
		}

		// build up the format string
		String fieldFormat = "%5s";
		String contentFormat = "<b><pre>%s</pre></b>";
		String descrFormat = "%s";
		String format = fieldFormat + "\t" + contentFormat + "\t- "
				+ descrFormat + "<br/>\n";

		// format all outputs
		String hoverInfoString = "";
		for (int i = 0; i < hoverInfoArray.length; i++) {
			hoverInfoString += String.format(format,
					hoverInfoArray[i].getField(),
					hoverInfoArray[i].getContent(),
					hoverInfoArray[i].getDescription());
		}

		return hoverInfoString;
	}

	private HoverInfo[] parseContentToHoverInfo(String content) {

		byte[] apdu = extractAPDU(content);

		// analyse APDU structure
		boolean lcPresent = apdu.length > OFFSET_LC;
		boolean extLength = (lcPresent) ? apdu[OFFSET_LC] == 0x00 : false;
		int lcLength = (extLength) ? 3 : 1;
		int dataOffset = OFFSET_LC + lcLength;
		boolean dataPresent = apdu.length > dataOffset;
		lcPresent = lcPresent && dataPresent;
		int lcValue = (extLength) ? (apdu[OFFSET_LC + 1] << 8)
				+ apdu[OFFSET_LC + 2] : apdu[OFFSET_LC];
		int dataLength = (lcPresent) ? lcValue : 0;
		int leOffset = (dataPresent) ? OFFSET_LC + lcLength + dataLength
				: OFFSET_LC;
		boolean lePresent = apdu.length > leOffset;

		// describe APDU parts
		ArrayList<HoverInfo> infos = new ArrayList<HoverInfo>();
		infos.addAll(describeApduCase(lcPresent, lePresent, extLength));
		infos.addAll(describeCLA(apdu));
		infos.addAll(describeINS(apdu));
		infos.addAll(describeParams(apdu));
		infos.addAll(describeLc(apdu, lcPresent, extLength));
		infos.addAll(describeData(apdu, dataPresent, dataOffset, dataLength));
		infos.addAll(describeLe(apdu, lePresent, leOffset, extLength));

		return infos.toArray(new HoverInfo[] {});
	}
	
	private ArrayList<? extends HoverInfo> describeApduCase(boolean lcPresent,
			boolean lePresent, boolean extLength) {

		int isoCase = 0;
		
		if (!lcPresent && !lePresent) {
			isoCase = 1;
		} else if (!lcPresent && lePresent) {
			isoCase = 2;
		} else if (lcPresent && !lePresent) {
			isoCase = 3;
		} else if (lcPresent && lePresent) {
			isoCase = 4;
		}
		
		String descr = "Iso Case "+isoCase+" apdu";
		if (extLength && isoCase != 1) {
			descr += ", using extended length";
		}
		
		// prepare return value
		ArrayList<HoverInfo> infos = new ArrayList<HoverInfo>();
		infos.add(new HoverInfo(HoverInfo.FieldType.APDU, "", descr));
		return infos;
	}

	private ArrayList<? extends HoverInfo> describeCLA(byte[] apdu) {

		String descr = "";
		if ((apdu[0] & 0xE0) == 0x00) {
			if ((apdu[0] & 0x08) == 0x08) {
				descr += "Secure messaging";
			} else {
				descr += "Plain";
			}
			if ((apdu[0] & 0x10) == 0x10) {
				descr += ", command chaining";
			}
			if ((apdu[0] & 0x03) != 0x00) {
				descr += ", logical channel "
						+ StringUtil.getHex((byte) (apdu[0] & 0x03));
			}
		} else {
			// TODO describe further interindustry class byte
		}

		// prepare return value
		ArrayList<HoverInfo> infos = new ArrayList<HoverInfo>();
		infos.add(new HoverInfo(HoverInfo.FieldType.CLA, StringUtil.getHex(
				apdu, 0, 1), descr));
		return infos;
	}

	private ArrayList<? extends HoverInfo> describeINS(byte[] apdu) {

		String descr = "";
		// describe instruction byte
		if ((apdu[0] & 0x80) == 0x00) {
			switch (apdu[1]) {
			case (byte) 0x04:
				descr = "DEACTIVATE FILE";
				break;
			case (byte) 0x0C:
				descr = "ERASE RECORD";
				break;
			case (byte) 0x0E:
			case (byte) 0x0F:
				descr = "ERASE BINARY";
				break;
			case (byte) 0x10:
				descr = "PERFORM SCQL OPERATION";
				break;
			case (byte) 0x12:
				descr = "PERFORM TRANSACTION OPERATION";
				break;
			case (byte) 0x14:
				descr = "PERFORM USER OPERATION";
				break;
			case (byte) 0x20:
			case (byte) 0x21:
				descr = "VERIFY";
				break;
			case (byte) 0x22:
				descr = "MANAGE SECURITY ENVIRONMENT";
				break;
			case (byte) 0x24:
				descr = "CHANGE REFERENCE DATA";
				break;
			case (byte) 0x26:
				descr = "DISABLE VERIFICATION REQUIREMENT";
				break;
			case (byte) 0x28:
				descr = "ENABLE VERIFICATION REQUIREMENT";
				break;
			case (byte) 0x2A:
				descr = "PERFORM SECURITY OPERATION";
				break;
			case (byte) 0x2C:
				descr = "RESET RETRY COUNTER";
				break;
			case (byte) 0x44:
				descr = "ACTIVATE FILE";
				break;
			case (byte) 0x46:
				descr = "GENERATE ASYMMETRIC KEY PAIR";
				break;
			case (byte) 0x70:
				descr = "MANAGE CHANNEL";
				break;
			case (byte) 0x82:
				descr = "EXTERNAL/MUTUAL AUTHENTICATE";
				break;
			case (byte) 0x84:
				descr = "GET CHALLENGE";
				break;
			case (byte) 0x86:
			case (byte) 0x87:
				descr = "GENERAL AUTHENTICATE";
				break;
			case (byte) 0x88:
				descr = "INTERNAL AUTHENTICATE";
				break;
			case (byte) 0xA0:
			case (byte) 0xA1:
				descr = "SEARCH BINARY";
				break;
			case (byte) 0xA2:
				descr = "SEARCH RECORD";
				break;
			case (byte) 0xA4:
				descr = "SELECT";
				break;
			case (byte) 0xB0:
			case (byte) 0xB1:
				descr = "READ BINARY";
				break;
			case (byte) 0xB2:
			case (byte) 0xB3:
				descr = "READ RECORD";
				break;
			case (byte) 0xC0:
				descr = "GET RESPONSE";
				break;
			case (byte) 0xC2:
			case (byte) 0xC3:
				descr = "ENVELOPE";
				break;
			case (byte) 0xCA:
			case (byte) 0xCB:
				descr = "GET DATA";
				break;
			case (byte) 0xD0:
			case (byte) 0xD1:
				descr = "WRITE BINARY";
				break;
			case (byte) 0xD2:
				descr = "WRITE RECORD";
				break;
			case (byte) 0xD6:
			case (byte) 0xD7:
				descr = "UPDATE BINARY";
				break;
			case (byte) 0xDA:
			case (byte) 0xDB:
				descr = "PUT DATA";
				break;
			case (byte) 0xDC:
			case (byte) 0xDD:
				descr = "UPDATE RECORD";
				break;
			case (byte) 0xE0:
				descr = "CREATE FILE";
				break;
			case (byte) 0xE2:
				descr = "APPEND RECORD";
				break;
			case (byte) 0xE4:
				descr = "DELETE FILE";
				break;
			case (byte) 0xE6:
				descr = "TERMINATE DF";
				break;
			case (byte) 0xE8:
				descr = "TERMINATE EF";
				break;
			case (byte) 0xFE:
				descr = "TERMINATE CARD USAGE";
				break;
			default:
				descr = "unknown instruction";
				break;
			}
		}

		// prepare return value
		ArrayList<HoverInfo> infos = new ArrayList<HoverInfo>();
		infos.add(new HoverInfo(HoverInfo.FieldType.INS, StringUtil.getHex(
				apdu, 1, 1), descr));
		return infos;
	}

	private ArrayList<? extends HoverInfo> describeParams(byte[] apdu) {

		String params = StringUtil.getHex(apdu, 2, 2);
		String descr = "";
		// describe P1 and P2
		if ((apdu[0] & 0x80) == 0x00) {
			switch (apdu[1]) {
			case (byte) 0x04:
				descr = "" + describeParamsActivateDeactivate(apdu);
				break;
			case (byte) 0x20:
			case (byte) 0x21:
				descr = "" + describeParamsVerify(apdu);
				break;
			case (byte) 0x22:
				descr = "" + describeParamsManageSecurityEnviroment(apdu);
				break;
			case (byte) 0x24:
				descr = "" + describeParamsChangeReferenceData(apdu);
				break;
			case (byte) 0x2A:
				descr = "" + describeParamsPerformSecurityOperation(apdu);
				break;
			case (byte) 0x2C:
				descr = "" + describeParamsResetRetryCounter(apdu);
				break;
			case (byte) 0x44:
				descr = "" + describeParamsActivateDeactivate(apdu);
				break;
			case (byte) 0x82:
				descr = "" + describeParamsAuthenticate(apdu);
				break;
			// 0x82 External Authenticate references for P1P2 Paragraph 7.5.1 Table 65
			// 0x86 General Authenticate references for P1P2 Paragraph 7.5.1 Table 65
			// 0x87 General Authenticate references for P1P2 Paragraph 7.5.1 Table 65
			// 0x88 Internal Authenticate references for P1P2 Paragraph 7.5.1 Table 65
			case (byte) 0x84:
				descr = "" + describeParamsGetChallenge(apdu);
			break;				
			case (byte) 0x86:
			case (byte) 0x87:
			case (byte) 0x88:
				descr = "" + describeParamsAuthenticate(apdu);
				break;
			case (byte) 0xA4:
				descr = "" + describeParamsSelect(apdu);
				break;
			case (byte) 0xB0:
			case (byte) 0xB1:
				descr = "" + describeParamsReadBinary(apdu);
				break;
			case (byte) 0xB2:
				descr = "" + describeParamsReadRecord(apdu);
				break;
				
			default:
				//TODO check for unimplemented INS
				descr += "unknown instruction";
				break;
			}
		} else if (apdu[0] == 0x8C) {
			
		}

		// prepare return value
		ArrayList<HoverInfo> infos = new ArrayList<HoverInfo>();
		infos.add(new HoverInfo(HoverInfo.FieldType.P1P2, params, descr));
		return infos;
	}

	private String describeParamsChangeReferenceData(byte[] APDU) {
		byte P1 = APDU[2];
		byte P2 = APDU[3];
		String descr = "";
		
		if (P1 == 0x00 || P1 == 0x01) {
			descr = "'00' or '01' (any other value is reserved for future use)";
		}
		
		switch (P2 & 0xFF) {
		case 0x00:
			descr += ", no information given ";
			break;
		case 0x80:
			descr += ", specific reference data(e.g., DF specific password or key)";
			break;
	}

		return descr;
	}

	private String describeParamsManageSecurityEnviroment(byte[] APDU) {
		byte P1 = APDU[2];
		byte P2 = APDU[3];
		String descr = "";
		
		if (P1 == 0xC1 && P2 == 0xA4) {
			descr = "Set Authentication Template for mutual authentication";
		} else if (P1 == 0x41 && P2 == 0xA4) {
			descr = "Set Authentication Template for internal authentication";
		} else if (P1 == 0x41 && P2 == 0xA6) {
			descr = "Set Key Agreement Template for computation";
		} else if (P1 == 0x81 && P2 == 0xA4) {
			descr = "Set Authentication Template for external authentication";
		} else if (P1 == 0x81 && P2 == 0xB6) {
			descr = "Set Digital Signature Template for verification";
		} 
		return descr;
	}

	private String describeParamsVerify(byte[] APDU) {
		byte P1 = APDU[2];
		byte P2 = APDU[3];
		String descr = "";
		
		if (P1 == 0x80 && P2 == 0x00) {
			descr = "Verify authenticated auxiliary data";
		}
		return descr;
	}

	private String describeParamsPerformSecurityOperation(byte[] APDU) {
		byte P1 = APDU[2];
		byte P2 = APDU[3];
		String descr = "";
		
		if (P1 == 0x00 && P2 == 0xBE) {
			descr = "Verify self-descriptive certificate";
		}

		return descr;
	}

	private String describeParamsResetRetryCounter(byte[] APDU) {
		byte P1 = APDU[2];
		byte P2 = APDU[3];
		String descr = "";
		
		// change, unblock
		if (P1 == 0x02) {
			descr = "Change ";
		} else if (P1 == 0x03) {
			descr = "Unblock ";
		}
		
		if (P1 == 0x10 && P2 == 0x01) {
			descr += "MRZ";
		} else if (P1 == 0x10 && P2 == 0x02) {
			descr += "CAN";
		} else if (P1 == 0x10 && P2 == 0x03) {
			descr += "PIN";
		}
		// reset retry counter
		if (P1 == 0x02) {
			if (P2 == 0x02) {
				descr = "new CAN must set in data field";
			} else if (P2 == 0x03) {
				descr = "new PIN must set in data field";
			}
		}
		if (P1 == 0x03) {
			descr = "Data field must be absent";
		}
		return descr;
	}

	private String describeParamsGetChallenge(byte[] APDU) {
		byte P1 = APDU[2];
		byte P2 = APDU[3];
		String descr = "";
		
		switch (P1 & 0x00) {
			case 0x00:
				descr = "No information given ";
		}

		switch (P2 & 0xFF) {
			case 0x00:
				descr += ", '00' (any other value is reserved for future use)";
				break;
			default:
				descr += "unknown instruction in P2";
				break;
		}
			
		return descr;
	}

	private String describeParamsActivateDeactivate(byte[] APDU) {
		String descr = "";
		
		if (APDU[1] == 0x44 && APDU[2] == 0x10 && APDU[3] == 0x03) {
			descr = "Activate PIN, data field must be absent";
		}
		
		if (APDU[1] == 0x04 && APDU[2] == 0x10 && APDU[3] == 0x03) {
			descr = "Deactivate PIN, data field must be absent";
		}
		return descr;
	}

	private String describeParamsAuthenticate(byte[] APDU) {
		byte P1 = APDU[2];
		byte P2 = APDU[3];
		String descr = "";
		
		switch (P1 & 0x00) {
			case 0x00:
				descr = "No information given ";
		}
		
		switch (P2 & 0xFF) {
			case 0x00:
				descr += ", no information given ";
				break;
			case 0x80:
				descr += ", specific reference data(e.g., DF specific password or key)";
				break;
		}

		return descr;
	}

	private String describeParamsReadRecord(byte[] APDU) {
		byte P1 = APDU[2];
		String descr = "";
		
		if (P1 == 0x00) {
			descr = "'00' references the current record";
		} else {
			descr = "read record no. " + (P1 & 0xFF);
		}
		
		return descr;
	}

	private String describeParamsSelect(byte[] APDU) {
		// selection type
		byte P1 = APDU[2];
		byte P2 = APDU[3];
		String descr = "";
		
		if ((P1 & 0x0C) == 0x00) {
			descr = "by file identifier";
		} else if ((P1 & 0x0C) == 0x04) {
			descr = "by DF name";
		} else if ((P1 & 0x0C) == 0x08) {
			descr = "by path";
		}
	
		// which occurence to return
		switch (P2 & 0x03) {
		case 0x00:
			descr += ", first occurence";
			break;
		case 0x01:
			descr += ", last occurence";
			break;
		case 0x02:
			descr += ", next occurence";
			break;
		case 0x03:
			descr += ", previous occurence";
			break;
		}
	
		// return type
		switch (P2 & 0x0C) {
		case 0x00:
			descr += ", return FCI";
			break;
		case 0x04:
			descr += ", return FCP";
			break;
		case 0x08:
			descr += ", return FMD";
			break;
		case 0x0C:
			descr += ", return proprietary or no data";
			break;
		}
		
		return descr;
	}
	
	private String describeParamsReadBinary(byte[] APDU) {
		String descr = "";
		byte INS = APDU[1];
		byte P1 = APDU[2];
		byte P2 = APDU[3];
		
		if (StringUtil.getHex(INS).equals("B0")) {
			// bit 8 of P1 to 1, Read Binary with implicit file selection per SFI and offset in P2 
			if ((P1 & 0x80) == 0x80) {
				descr += "with short file ID 0x" + StringUtil.getHex((byte) (P1 & 0x1F));
	
				int offset = (P2 & 0xFF);
				descr += " and offset " + offset + " bytes";
			}
			
			// bit 8 of P1 to 0, P1-P2 (fifteen bits) encodes an offset from zero to 32.767
			if ((P1 & 0x80) == 0x00) {
				int offset = 0;
				byte[] P1P2 =  {P1, P2};
			   
				offset |= P1P2[0] & 0xFF;
				offset <<= 8;
				offset |= P1P2[1] & 0xFF;
				
				descr += "with offset " + offset;
			} 
			
			// RFU bits are used
			if ((P1 & 0x20) == 0x20 || (P1 & 0x40) == 0x40) {
				descr += " (Warning: RFU bits are set)";
			}
			
			return descr;
		}
			
		if (StringUtil.getHex(INS).equals("B1")) {
			// bit 1 of INS is set to 1, P1-P2 shall identify an EF
			descr += ", with file ID '" + StringUtil.getHex((byte) P1) +" "+ StringUtil.getHex((byte) P2) +"'";
		}
		return descr;
	}

	private ArrayList<? extends HoverInfo> describeLc(byte[] apdu,
			boolean lcPresent, boolean extLength) {
		ArrayList<HoverInfo> infos = new ArrayList<HoverInfo>();

		if (lcPresent) {
			int lcLength = (extLength) ? 3 : 1;
			String params = StringUtil.getHex(apdu, OFFSET_LC, lcLength);
			int lcValue = (extLength) ? (apdu[OFFSET_LC + 1] << 8)
					+ apdu[OFFSET_LC + 2] : apdu[OFFSET_LC];
			String descr = "" + lcValue + " bytes in data field";
			infos.add(new HoverInfo(HoverInfo.FieldType.LC, params, descr));
		} else {
			// do not add Lc field info at all when not present
			// infos.add(new HoverInfo(HoverInfo.FieldType.LC, "", "absent"));
		}

		return infos;
	}

	private ArrayList<? extends HoverInfo> describeData(byte[] apdu,
			boolean dataPresent, int dataOffset, int dataLength) {
		ArrayList<HoverInfo> infos = new ArrayList<HoverInfo>();

		String params = StringUtil.getHex(apdu, 2, 2);
		String data = StringUtil.getHex(apdu, dataOffset, dataLength);
		String descr = "";

		if (dataPresent) {

			if ((apdu[0] & 0x80) == 0x00) {
				switch (apdu[1]) {
				case (byte) 0x22:
					if (params.equals("C1 A4")) {
						descr = "Set Authentication Template";
					} else if (params.equals("81 B6")) {
						descr = "Set Digital Signature Template for verification";
					}
					//TODO analyze data field of MSE
					break;
				case (byte) 0xA4:
					infos.addAll(describeDataSelect(apdu, dataOffset, dataLength));
					break;
				case (byte) 0xB0:
					// read binary if P1 and P2 = 0x00
					if (params.equals("00 00")) {
						descr += ", with file ID '" + data +"'";
					}
					infos.add(new HoverInfo(HoverInfo.FieldType.DATA, data, descr));
					break;
				
				case (byte) 0x2C:
					if (apdu[3] == 0x02) {
						descr = "new CAN";
					} else if (apdu[3] == 0x03) {
						descr = "new PIN";
					} else {
						descr = "unknown instruction";
					}
					infos.add(new HoverInfo(HoverInfo.FieldType.DATA, data, descr));
					break;
				
				case (byte) 0x44:
					if (apdu[2] == 0x00 && apdu[3] == 0x00)
					descr = "Activate file with file ID '" + data + "'";
					infos.add(new HoverInfo(HoverInfo.FieldType.DATA, data, descr));
					break;
					
				default:
					//TODO implement other options
					descr = "unknown instruction";
					infos.add(new HoverInfo(HoverInfo.FieldType.DATA, data, descr));
					break;
				}
			}
			
			
		} else {
			// do not add data field info at all when not present
			// infos.add(new HoverInfo(HoverInfo.FieldType.DATA, "", "absent"));
		}

		return infos;
	}

	private ArrayList<? extends HoverInfo> describeDataSelect(byte[] apdu, int dataOffset, int dataLength) {
		ArrayList<HoverInfo> infos = new ArrayList<HoverInfo>();

		String data = StringUtil.getHex(apdu, dataOffset, dataLength);
		
		// selection type
		if ((apdu[2] & 0x0C) == 0x00) {
			infos.add(new HoverInfo(HoverInfo.FieldType.DATA, data, "File identifier to select"));
		} else if ((apdu[2] & 0x0C) == 0x04) {
			String description = "AID or DF to select";
			if (aidMap.containsKey(data)){
				description += " (" + aidMap.get(data)+")";
			}
			infos.add(new HoverInfo(HoverInfo.FieldType.DATA, data, description));
		} else if ((apdu[2] & 0x0C) == 0x08) {
			infos.add(new HoverInfo(HoverInfo.FieldType.DATA, data, "Path of element to select"));
		}
		
		
		
		return infos;
	}

	private ArrayList<? extends HoverInfo> describeLe(byte[] apdu,
			boolean lePresent, int leOffset, boolean extLength) {
		ArrayList<HoverInfo> infos = new ArrayList<HoverInfo>();

		if (lePresent) {
			int lcLength = (extLength) ? 2 : 1;
			String params = "";
			if (extLength && leOffset == OFFSET_LC) {
				leOffset++;
				params += "00 ";
			}
			params += StringUtil.getHex(apdu, leOffset, lcLength);
			int leValue = (extLength) ? (apdu[leOffset] << 8)
					+ apdu[leOffset + 1] : apdu[leOffset];
			if (leValue == 0) {
				leValue = (extLength) ? 65536 : 256;
			}
			String descr = "expect up to " + leValue + " bytes in response";
			infos.add(new HoverInfo(HoverInfo.FieldType.LE, params, descr));
		} else {
			// do not add Le field info at all when not present
			// infos.add(new HoverInfo(HoverInfo.FieldType.LE, "", "absent"));
		}

		return infos;
	}

	private byte[] extractAPDU(String content) {
		String[] lines = content.split("\n");
		String apduString = "";
		for (int i = 1; i < lines.length - 2; i++) {
			apduString += lines[i].replaceAll("^\\d{4,}(:)?\\s+", "").substring(0,
					48);
		}

		return StringUtil.parseHexString(apduString);
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		IDocument doc = textViewer.getDocument();
		ITypedRegion hoverRegion = null;
		try {
			hoverRegion = doc.getPartition(offset);
		} catch (BadLocationException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
		}
		if (hoverRegion != null && hoverRegion instanceof Region) {
			return hoverRegion;
		} else {
			return new Region(offset, 50);
		}
	}

}
