package org.globaltester.logging.ui.editors;

import org.eclipse.jface.text.rules.IToken;
import org.globaltester.base.ui.editors.OrRule;
import org.globaltester.base.ui.editors.RegexRule;
import org.globaltester.base.ui.editors.GtScanner.TokenType;

public class TestlogMetaDataRule extends OrRule {

	public TestlogMetaDataRule(String contentType) {
		this(TestLogScanner.getTokenForContentType(contentType,
				TokenType.CONTENT_TYPE));
	}

	public TestlogMetaDataRule(IToken token) {
		super(token);

		this.addRule(new RegexRule("- Testcase .{57,} -", token));
		this.addRule(new RegexRule("- -{64,} -", token));
		
	}

}
