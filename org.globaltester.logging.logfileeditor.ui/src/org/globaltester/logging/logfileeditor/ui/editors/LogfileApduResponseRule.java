package org.globaltester.logging.logfileeditor.ui.editors;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.globaltester.base.ui.editors.GtScanner.TokenType;

/**
 * MultiLineRule that matches a single APDU in GtLogfile
 * 
 * @author amay
 * 
 */
public class LogfileApduResponseRule extends MultiLineRule {

	public LogfileApduResponseRule(String contentType) {
		this(TestLogScanner.getTokenForContentType(contentType, TokenType.CONTENT_TYPE));
	}
	
	public LogfileApduResponseRule(IToken token) {
		super("<= Response APDU [", "] R-APDU", token);
	}

}
