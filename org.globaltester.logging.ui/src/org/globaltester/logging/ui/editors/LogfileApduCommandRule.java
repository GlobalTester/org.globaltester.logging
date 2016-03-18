package org.globaltester.logging.ui.editors;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.globaltester.base.ui.editors.GtScanner.TokenType;

/**
 * MultiLineRule that matches a single APDU in GtLogfile
 * 
 * @author amay
 * 
 */
public class LogfileApduCommandRule extends MultiLineRule {

	public LogfileApduCommandRule(String contentType) {
		this(TestLogScanner.getTokenForContentType(contentType, TokenType.CONTENT_TYPE));
	}
	
	public LogfileApduCommandRule(IToken token) {
		super("=> Command APDU [", "] C-APDU", token);
	}

}
