package org.globaltester.logging.ui.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WordRule;
import org.globaltester.base.ui.editors.GtDefaultWordDetector;

/**
 * WordRule that matches all log levels used in GT Logfiles.
 * 
 * @author amay
 * 
 */
public class LogfileLoglevelRule extends WordRule implements IPredicateRule {
	private IToken token;

	public LogfileLoglevelRule(IToken token) {
		super(new GtDefaultWordDetector());
		this.token = token;

		addGtLoglevels();
	}

	private void addGtLoglevels() {
		this.addWord("FATAL", token);
		this.addWord("ERROR", token);
		this.addWord("WARN", token);
		this.addWord("INFO", token);
		this.addWord("DEBUG", token);
		this.addWord("TRACE", token);
	}

	@Override
	public IToken getSuccessToken() {
		return token;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		// resuming is not supported by this rule and therefore ignored
		return evaluate(scanner);
	}

}
