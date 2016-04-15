package org.globaltester.logging.logfileeditor.ui.editors;

import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.globaltester.logging.legacy.logger.GtErrorLogger;
import org.globaltester.logging.logfileeditor.ui.Activator;

/**
 * TokenScanner that scans a part of a GT log file and returns tokens for the
 * parts of that APDU
 * 
 * @author amay
 * 
 */
public class ApduScanner implements ITokenScanner {

	public class TokenInformation {

		private IToken token;
		private int offset;
		private int length;

		public TokenInformation(IToken t, int o, int l) {
			this.token = t;
			this.offset = o;
			this.length = l;
		}

		public IToken getToken() {
			return token;
		}

		public int getOffset() {
			return offset;
		}

		public int getLength() {
			return length;
		}

	}

	public static ArrayList<String> regionsProcessed = new ArrayList<String>();

	private static final IToken headToken = new Token(
			new TextAttribute(null, new Color(Display.getCurrent(),
					ColorConstants.APDU_HEAD), SWT.BOLD));
	private static final IToken lcToken = new Token(new TextAttribute(null,
			new Color(Display.getCurrent(), ColorConstants.APDU_LC), SWT.BOLD));
	private static final IToken leToken = new Token(new TextAttribute(null,
			new Color(Display.getCurrent(), ColorConstants.APDU_LE), SWT.BOLD));
	
	private LinkedList<TokenInformation> tokens;
	private TokenInformation currentTokenInfo;

	@Override
	public void setRange(IDocument document, int offset, int length) {
		int regionOffset = offset;
		int regionLength = length;

		try {
			ITypedRegion currentRegion = document.getPartition(offset);
			regionOffset = currentRegion.getOffset();
			regionLength = currentRegion.getLength();
			offset = regionOffset;
			length = regionLength;
		} catch (BadLocationException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
		}

		// reset tokens
		tokens = new LinkedList<TokenInformation>();

		// TODO check leading description and add token
		offset += 24; // skip leading description

		// add token for apdu head
		int headLength = 11;
		tokens.add(new TokenInformation(headToken, offset, headLength));
		offset += headLength + 1;

		try {
			// add token for Lc byte
			String lcString = document.get(offset, 2);

			if (!"  ".equals(lcString)) {
				int lcLength = Integer.parseInt(lcString.replaceAll(" ", ""),
						16);
				lcLength = (lcLength == 0) ? 8 : 2;
				boolean lcFound = false;
				if ("  ".equals(document.get(offset + lcLength, 2))) {
					tokens.add(new TokenInformation(leToken, offset, lcLength));
				} else {
					tokens.add(new TokenInformation(lcToken, offset, lcLength));
					lcFound = true;
				}
				offset += lcLength + 1;

				if (lcFound) {
					// move offset forward to le
					lcString = document.get(offset - lcLength - 1, lcLength);
					int lc = Integer.parseInt(lcString.replaceAll(" ", ""), 16);
					int bytesPerLine = 16;
					int headBytes = 4;
					int lcBytes = (lcLength == 2) ? 1 : 3;
					int skipLines = lc / bytesPerLine;
					int skipBytes = (lc % bytesPerLine);
					if (skipBytes + headBytes + lcBytes >= bytesPerLine) {
						skipLines++;
						skipBytes -= bytesPerLine;
					}

					offset += skipLines * 72;
					offset += skipBytes * 3;

					// add token for Le
					String leString = document.get(offset, 2);
					if (!"  ".equals(leString)) {
						int leLength = (lcLength == 8) ? 5 : 2;
						int leBytes = leLength / 2;
						if (headBytes + lcBytes + skipBytes + leBytes > bytesPerLine) {

							// first part of le
							tokens.add(new TokenInformation(leToken, offset, 2));

							// Undefined token from behind the first le byte till before the second
							int lineBreakLength = 24;
							tokens.add(new TokenInformation(Token.UNDEFINED,
									offset + 3, lineBreakLength));
							
							//second part of le
							tokens.add(new TokenInformation(leToken, offset + 3
									+ lineBreakLength, 2));
							leLength += lineBreakLength;

						} else {
							tokens.add(new TokenInformation(leToken, offset,
									leLength));
						}
						offset += leLength + 1;
					}
				}
			}
			// add default token for rest of region
			 int rangeLength = regionLength-(offset-regionOffset);
			 if (rangeLength > 0) {
				 tokens.add(new TokenInformation(Token.UNDEFINED, offset, rangeLength));
			 }
		} catch (BadLocationException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
			e.printStackTrace();
		}

	}

	@Override
	public IToken nextToken() {
		if (tokens == null || tokens.isEmpty()) {
			return Token.EOF;
		}
		currentTokenInfo = tokens.getFirst();
		tokens.remove(0);
		return currentTokenInfo.getToken();
	}

	@Override
	public int getTokenOffset() {
		return currentTokenInfo.getOffset();
	}

	@Override
	public int getTokenLength() {
		return currentTokenInfo.getLength();
	}

}
