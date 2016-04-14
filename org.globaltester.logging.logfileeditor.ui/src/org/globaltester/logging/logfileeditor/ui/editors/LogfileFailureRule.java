package org.globaltester.logging.logfileeditor.ui.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class LogfileFailureRule implements IRule, IPredicateRule{
	
	protected IToken token;
	
	public LogfileFailureRule(IToken token) {
		this.token = token;
	}

	@Override
	public IToken getSuccessToken() {
		return token;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		
		if(resume) {
			return Token.UNDEFINED;
		} else{
			int curCharInt;
			Character curChar;
			int charCounter = 0;
			StringBuilder sb = new StringBuilder();
			
			do {
				curCharInt = scanner.read();
				curChar = (char) curCharInt;
				charCounter++;
				sb.append(curChar);
				
				//basic k.o. pre-checks for performance enhancement
				if(charCounter == 1) {
					if(Character.compare(curChar, '@') != 0) {
						break;
					}
				} else if(charCounter <= 10) {
					if (!Character.isLetter(curChar)){
						break;
					}
				} else if (!Character.isDigit(curChar)){
					//failureid found
					scanner.unread();
					return token;
				}
				
			} while (!(curCharInt == '\n' || curCharInt == '\r' || curCharInt == ICharacterScanner.EOF));
			
			for(int i=0; i<charCounter; i++) {
				scanner.unread();
			}
			
			return Token.UNDEFINED;
		}
		
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}
	
}
