package org.globaltester.logging.ui.editors;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IToken;
import org.globaltester.base.ui.editors.GtScanner.TokenType;
import org.globaltester.base.ui.editors.OrRule;

public class TestlogMetaDataRule extends OrRule {

	public TestlogMetaDataRule(String contentType) {
		this(TestLogScanner.getTokenForContentType(contentType,
				TokenType.CONTENT_TYPE));
	}

	public TestlogMetaDataRule(IToken token) {
		super(token);
		
		this.addRule(new EndOfLineRule("- Testcase ", token));
		this.addRule(new EndOfLineRule("- ------------------------------------------------------------------", token));
		
	}

}
