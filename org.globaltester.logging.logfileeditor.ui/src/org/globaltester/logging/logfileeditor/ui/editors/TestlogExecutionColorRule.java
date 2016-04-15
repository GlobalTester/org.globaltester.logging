package org.globaltester.logging.logfileeditor.ui.editors;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.globaltester.base.ui.editors.OrRule;
import org.globaltester.base.ui.editors.GtScanner.TokenType;

/**
 * MultiLineRule that matches the execution of one executable in GtLogfile
 * 
 * @author amay
 * 
 */
public class TestlogExecutionColorRule extends OrRule {

	public TestlogExecutionColorRule(String contentType) {
		this(TestLogScanner.getTokenForContentType(contentType,
				TokenType.CONTENT_TYPE));
	}

	public TestlogExecutionColorRule(IToken token) {
		super(token);

		this.addRule(new SingleLineRule(
				"- >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", " -",
				token));
		this.addRule(new SingleLineRule(
				"- Starting new test executable", " -",
				 token));
		this.addRule(new SingleLineRule(
				"- End execution of", " -",
				 token));
		this.addRule(new SingleLineRule(
				"- <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<", " -",
				 token));
	}

}
