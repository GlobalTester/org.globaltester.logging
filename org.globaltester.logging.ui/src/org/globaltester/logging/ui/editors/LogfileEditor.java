package org.globaltester.logging.ui.editors;

import org.globaltester.base.ui.editors.FoldingEditor;

public class LogfileEditor extends FoldingEditor {
	
	public static final String EDITOR_ID = "org.globaltester.logging.ui.logFileEditor";

	public LogfileEditor() {
		super();
		setDocumentProvider(new GtLogfileDocumentProvider());
		setSourceViewerConfiguration(new GtLogfileViewerConfiguration(this));
	}
}
