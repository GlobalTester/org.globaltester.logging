package org.globaltester.logging.ui.editors;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class LogfileTimeStampRule implements IRule, IPredicateRule{
	
	public static SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	
	protected IToken token;
	
	public LogfileTimeStampRule(IToken token) {
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
				if(charCounter <= 4) {
					if (!Character.isDigit(curChar)){
						break;
					}
				} else{
					if((charCounter == 5) || (charCounter == 8)) {
						if(Character.compare(curChar, '-') != 0) {
							break;
						}
					} else{
						if(charCounter <= 10) {
							if (!Character.isDigit(curChar)){
								break;
							}
						}
					}
				}
				
				if(charCounter == 23) {
					try {
						parserSDF.parse(sb.toString());
						return token;
					} catch (ParseException e) {
						break; // cleanup will be performed after loop
					}
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
