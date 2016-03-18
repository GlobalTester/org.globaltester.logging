package org.globaltester.logging.ui.editors;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.globaltester.base.ui.editors.GtScanner;

/**
 * Scanner that identifies content types introduced by GlobalTester TestRunner
 * and returns either content type tokens or text attribute tokens
 * 
 * @author amay
 * 
 */
public class TestLogScanner extends GtScanner {
	//TODO TestLogScanner should be contributed through a new extension point from o.g.testrunner

	public final static String CT_TESTLOG_EXECUTION = "__TESTLOG_EXECUTION";
	public final static String CT_TESTLOG_EXECUTION_COLOR = "__TESTLOG_EXECUTION_COLOR";
	public final static String CT_TESTLOG_EXECUTION_PART = "__TESTLOG_EXECUTION_PART";
	public final static String CT_TESTLOG_METADATA = "__TESTLOG_METADATA";
	
	protected static HashMap<String, EnumMap<TokenType, Object>> contentTypes = new HashMap<String, EnumMap<TokenType, Object>>();

	// init supported content types
	static {
		// add required data for content type TESTLOG_EXECUTION
		EnumMap<TokenType, Object> eMap = new EnumMap<TokenType, Object>(
				TokenType.class);
		eMap.put(TokenType.CONTENT_TYPE, TestlogExecutionPartitionRule.class);
		eMap.put(TokenType.TEXT_ATTRIBUTES,
				new TextAttribute(new Color(Display.getCurrent(),
						ColorConstants.TEST_EXECUTION), null, SWT.BOLD));
		contentTypes.put(CT_TESTLOG_EXECUTION, eMap);
		
		// add required data for content type TESTLOG_EXECUTION_COLOR
		eMap = new EnumMap<TokenType, Object>(
				TokenType.class);
		eMap.put(TokenType.CONTENT_TYPE, TestlogExecutionColorRule.class);
		eMap.put(TokenType.TEXT_ATTRIBUTES,
				new TextAttribute(new Color(Display.getCurrent(),
						ColorConstants.TEST_EXECUTION), null, SWT.BOLD));
		contentTypes.put(CT_TESTLOG_EXECUTION_COLOR, eMap);
		
		// add required data for content type TESTLOG_EXECUTION_PART
		eMap = new EnumMap<TokenType, Object>(
				TokenType.class);
		eMap.put(TokenType.CONTENT_TYPE, TestlogExecutionPartRule.class);
		eMap.put(TokenType.TEXT_ATTRIBUTES,
				new TextAttribute(new Color(Display.getCurrent(),
						ColorConstants.TEST_EXECUTION_PART), null, SWT.NULL));
		contentTypes.put(CT_TESTLOG_EXECUTION_PART, eMap);
		
		// add required data for content type TESTLOG_METADATA
		eMap = new EnumMap<TokenType, Object>(
				TokenType.class);
		eMap.put(TokenType.CONTENT_TYPE, TestlogMetaDataRule.class);
		eMap.put(TokenType.TEXT_ATTRIBUTES,
				new TextAttribute(new Color(Display.getCurrent(),
						ColorConstants.TEST_METADATA), null, SWT.BOLD));
		contentTypes.put(CT_TESTLOG_METADATA, eMap);

	}

	public TestLogScanner(TokenType tokenType) {
		super(tokenType);
		TestLogScanner.addAllPredicateRules(this, tokenType);
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
			if (CT_TESTLOG_EXECUTION.equals(curContentType)){
				//do not include this contenttype in PredicateRules for syntax highlighting
				continue;
			}
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
	public static void addAllPartitionRules(GtScanner scanner,
			TokenType tokenType) {
		ArrayList<String> partitionContentTypes = new ArrayList<String>();
//		partitionContentTypes.add(CT_TESTLOG_EXECUTION);

		for (Iterator<String> contentTypesIter = partitionContentTypes
				.iterator(); contentTypesIter.hasNext();) {
			String curContentType = contentTypesIter.next();
			IPredicateRule curRule = getRuleForContentType(curContentType,
					tokenType, contentTypes);
			if (curRule != null) {
				scanner.addPredicateRule(curContentType, curRule);
			}
		}
	}

	public static IToken getTokenForContentType(String contentType,
			TokenType tokenType) {
		return getTokenForContentType(contentType, tokenType, contentTypes);
	}

}
