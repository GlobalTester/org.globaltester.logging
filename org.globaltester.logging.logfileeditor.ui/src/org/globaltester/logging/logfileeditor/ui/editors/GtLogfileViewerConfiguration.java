package org.globaltester.logging.logfileeditor.ui.editors;

import org.eclipse.jface.internal.text.html.HTMLTextPresenter;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.globaltester.base.ui.editors.GtScanner;
import org.globaltester.base.ui.editors.ReconcilingStrategy;
import org.globaltester.base.ui.editors.GtScanner.TokenType;

//TODO investigate options to remove this SuppressWarnings
@SuppressWarnings("restriction")
public class GtLogfileViewerConfiguration extends TextSourceViewerConfiguration {
	private GtScanner logScanner;
	private ApduScanner apduScanner;

	private LogfileEditor editor;

	public GtLogfileViewerConfiguration(LogfileEditor editor) {
		this.editor = editor;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return getLogfileScanner().getLegalContentTypes();
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		addDamagerRepairer(reconciler, getApduScanner(),
				LogfileScanner.CT_LOG_APDU_COMMAND);
		addDamagerRepairer(reconciler, getLogfileScanner(),
				TestLogScanner.CT_TESTLOG_EXECUTION);
		addDamagerRepairer(reconciler, getLogfileScanner(),
				IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	private void addDamagerRepairer(PresentationReconciler reconciler,
			ITokenScanner tokenScanner, String contentType) {
		DefaultDamagerRepairer contentDamagerRepairer = new DefaultDamagerRepairer(
				tokenScanner);
		reconciler.setDamager(contentDamagerRepairer, contentType);
		reconciler.setRepairer(contentDamagerRepairer, contentType);
	}

	public ITextHover getTextHover(ISourceViewer sv, String contentType) {
		if (LogfileScanner.CT_LOG_APDU_COMMAND.equals(contentType)) {
			return new ApduCommandTextHover();
		} else {
			return null;
		}
	}

	private GtScanner getLogfileScanner() {
		if (logScanner == null) {
			logScanner = new GtScanner(TokenType.TEXT_ATTRIBUTES);

			TestLogScanner.addAllPredicateRules(logScanner,
					TokenType.TEXT_ATTRIBUTES);
			LogfileScanner.addAllPredicateRules(logScanner,
					TokenType.TEXT_ATTRIBUTES);

		}
		return logScanner;
	}

	private ApduScanner getApduScanner() {
		if (apduScanner == null) {
			apduScanner = new ApduScanner();
		}
		return apduScanner;
	}

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		ReconcilingStrategy strategy = new LogFileReconcilingStrategy();
		strategy.setEditor(editor);

		MonoReconciler reconciler = new MonoReconciler(strategy, false);

		return reconciler;
	}

	@Override
	public IInformationControlCreator getInformationControlCreator(
			ISourceViewer sourceViewer) {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent,
						new HTMLTextPresenter(true));
			}
		};
	}

}
