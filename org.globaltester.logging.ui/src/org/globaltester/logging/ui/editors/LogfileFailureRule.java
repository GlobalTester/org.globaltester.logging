package org.globaltester.logging.ui.editors;

import org.eclipse.jface.text.rules.IToken;
import org.globaltester.base.ui.editors.RegexRule;

public class LogfileFailureRule extends RegexRule{
	
	public LogfileFailureRule(IToken token) {
		super("@FailureID\\d+:", token);
	}
}
