package org.globaltester.logging.ui.editors;

import org.eclipse.jface.text.rules.IToken;
import org.globaltester.base.ui.editors.RegexRule;

public class LogfileTimeStampRule extends RegexRule{
	
	public LogfileTimeStampRule(IToken token) {
		super("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}", token);
	}
}
