package org.globaltester.logging.logfileeditor.ui.editors;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.globaltester.base.ui.editors.GtScanner;

/**
 * Scanner that identifies logging related content types
 * and returns either content type tokens or text attribute tokens
 * 
 * @author amay
 * 
 */
public class LogfileScanner extends GtScanner {

	public static final String CT_LOG_LEVEL = "__LOG_LEVEL";
	public static final String CT_LOG_TIMESTAMP = "__LOG_TIMESTAMP";
	public static final String CT_LOG_FAILURE = "__LOG_FAILURE";
	public static final String CT_LOG_APDU_COMMAND = "__LOG_APDU_COMMAND";
	public static final String CT_LOG_APDU_RESPONSE = "__LOG_APDU_RESPONSE";
	
	protected static HashMap<String, EnumMap<TokenType, Object>> contentTypes = new HashMap<String, EnumMap<TokenType, Object>>();

	// init supported content types
	static {
		// add required data for content type LOG_LEVEL
		EnumMap<TokenType, Object> eMap = new EnumMap<TokenType, Object>(
				TokenType.class);
		eMap.put(TokenType.CONTENT_TYPE, LogfileLoglevelRule.class);
		eMap.put(TokenType.TEXT_ATTRIBUTES,
				new TextAttribute(new Color(Display.getCurrent(),
						ColorConstants.LOG_LEVEL), null, SWT.BOLD));
		contentTypes.put(CT_LOG_LEVEL, eMap);
		
		// add required data for content type LOG_TIMESTAMP
		eMap = new EnumMap<TokenType, Object>(
				TokenType.class);
		eMap.put(TokenType.CONTENT_TYPE, LogfileTimeStampRule.class);
		eMap.put(TokenType.TEXT_ATTRIBUTES,
				new TextAttribute(new Color(Display.getCurrent(),
						ColorConstants.LOG_TIMESTAMP), null, SWT.BOLD));
		contentTypes.put(CT_LOG_TIMESTAMP, eMap);
		
		// add required data for content type LOG_FAILURE
		eMap = new EnumMap<TokenType, Object>(
				TokenType.class);
		eMap.put(TokenType.CONTENT_TYPE, LogfileFailureRule.class);
		eMap.put(TokenType.TEXT_ATTRIBUTES,
				new TextAttribute(new Color(Display.getCurrent(),
						ColorConstants.LOG_FAILURE), null, SWT.BOLD));
		contentTypes.put(CT_LOG_FAILURE, eMap);
		
		// add required data for content type LOG_APDU_COMMAND
		eMap = new EnumMap<TokenType, Object>(
				TokenType.class);
		eMap.put(TokenType.CONTENT_TYPE, LogfileApduCommandRule.class);
		eMap.put(TokenType.TEXT_ATTRIBUTES,
				new TextAttribute(new Color(Display.getCurrent(),
						ColorConstants.LOG_APDU_COMMAND), null, SWT.BOLD));
		contentTypes.put(CT_LOG_APDU_COMMAND, eMap);
		
		// add required data for content type LOG_APDU_RESPONSE
		eMap = new EnumMap<TokenType, Object>(
				TokenType.class);
		eMap.put(TokenType.CONTENT_TYPE, LogfileApduResponseRule.class);
		eMap.put(TokenType.TEXT_ATTRIBUTES,
				new TextAttribute(new Color(Display.getCurrent(),
						ColorConstants.LOG_APDU_RESPONSE), null, SWT.BOLD));
		contentTypes.put(CT_LOG_APDU_RESPONSE, eMap);

	}

	public LogfileScanner(TokenType tokenType) {
		super(tokenType);
		LogfileScanner.addAllPredicateRules(this, tokenType);
	}

	public String[] getSupportedContentTypes() {
		return contentTypes.keySet().toArray(new String[0]);
	}

	/**
	 * Adds all JS related predicate rules to the given scanner
	 * 
	 * @param scanner
	 *            scanner to add the rules to
	 * @param tokenType
	 *            EnumType of GTRuleBasedPartitionScanner.TokenType that
	 *            represents the type of token to be added
	 */
	public static void addAllPredicateRules(GtScanner scanner,
			TokenType tokenType) {
		for (Iterator<String> contentTypesIter = contentTypes.keySet()
				.iterator(); contentTypesIter.hasNext();) {
			String curContentType = contentTypesIter.next();
			IPredicateRule curRule = getRuleForContentType(curContentType,
					tokenType, contentTypes);
			if (curRule != null) {
				scanner.addPredicateRule(curContentType, curRule);
			}
		}
	}

	/**
	 * Adds all JSs related predicate rules that define document partitions to
	 * the given scanner
	 * 
	 * @param scanner
	 *            scanner to add the rules to
	 * @param tokenType
	 *            EnumType of GTRuleBasedPartitionScanner.TokenType that
	 *            represents the type of token to be added
	 */
	public static void addAllPartitionRules(GtScanner scanner, TokenType tokenType) {
		String curContentType = CT_LOG_APDU_COMMAND;
		IPredicateRule curRule = getRuleForContentType(curContentType, tokenType, contentTypes);
		if (curRule != null) {
			scanner.addPredicateRule(curContentType, curRule);
		}
	}

}
