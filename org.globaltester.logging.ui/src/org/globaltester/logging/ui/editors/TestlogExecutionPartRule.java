package org.globaltester.logging.ui.editors;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IToken;
import org.globaltester.base.ui.editors.GtScanner.TokenType;
import org.globaltester.base.ui.editors.OrRule;

/**
 * MultiLineRule that matches the execution of one executable in GtLogfile
 * 
 * @author amay
 * 
 */
public class TestlogExecutionPartRule extends OrRule {

	public TestlogExecutionPartRule(String contentType) {
		this(TestLogScanner.getTokenForContentType(contentType,
				TokenType.CONTENT_TYPE));
	}

	public TestlogExecutionPartRule(IToken token) {
		super(token);
		
		this.addRule(new EndOfLineRule("- TestStep: ", token));
		this.addRule(new EndOfLineRule("- Command: ", token));
		this.addRule(new EndOfLineRule("- Description: ", token));
		this.addRule(new EndOfLineRule("- APDU: ", token));
		this.addRule(new EndOfLineRule("- Code: ", token));
		this.addRule(new EndOfLineRule("- ExpectedResult: ", token));
		
	}

}
