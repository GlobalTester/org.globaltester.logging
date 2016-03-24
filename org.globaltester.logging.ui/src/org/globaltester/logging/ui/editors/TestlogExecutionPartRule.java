package org.globaltester.logging.ui.editors;

import org.eclipse.jface.text.rules.IToken;
import org.globaltester.base.ui.editors.OrRule;
import org.globaltester.base.ui.editors.RegexRule;
import org.globaltester.base.ui.editors.GtScanner.TokenType;

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

		this.addRule(new RegexRule("- TestStep: .{56,} -", token));
		this.addRule(new RegexRule("- Command: .{57,} -", token));
		this.addRule(new RegexRule("- Description: .{53,} -", token));
		this.addRule(new RegexRule("- APDU: .{60,} -", token));
		this.addRule(new RegexRule("- Code: .{60,} -", token));
		this.addRule(new RegexRule("- ExpectedResult: .{50,} -", token));
		
	}

}
