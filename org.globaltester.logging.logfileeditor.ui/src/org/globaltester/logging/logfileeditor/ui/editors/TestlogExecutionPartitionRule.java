package org.globaltester.logging.logfileeditor.ui.editors;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.globaltester.base.ui.editors.GtScanner.TokenType;

/**
 * MultiLineRule that matches the execution of one executable in GtLogfile
 * 
 * @author amay
 * 
 */
public class TestlogExecutionPartitionRule extends MultiLineRule {

	public TestlogExecutionPartitionRule(String contentType) {
		this(TestLogScanner.getTokenForContentType(contentType, TokenType.CONTENT_TYPE));
	}
	
	public TestlogExecutionPartitionRule(IToken token) {
		super("- >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> -", "- <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -", token);
	}

}
