package org.globaltester.logging.logfileeditor.ui.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.globaltester.base.ui.editors.GtScanner;
import org.globaltester.base.ui.editors.GtScanner.TokenType;

public class GtLogfileDocumentProvider extends TextFileDocumentProvider {

	private GtScanner partitionScanner;
	
	public GtLogfileDocumentProvider(){
		super(new FileDocumentProvider());
	}
	
	@Override
	public IDocument getDocument(Object element) {
		IDocument document = super.getDocument(element);
		if (document != null) {
			GtScanner pScanner = getPartitionScanner();
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					pScanner, pScanner.getLegalContentTypes());
			document.setDocumentPartitioner(partitioner);
			partitioner.connect(document);
		}
		return document;
	}

	protected GtScanner getPartitionScanner() {
		if (partitionScanner == null) {
			partitionScanner = new GtScanner(TokenType.CONTENT_TYPE);

			TestLogScanner.addAllPartitionRules(partitionScanner, TokenType.CONTENT_TYPE);
			LogfileScanner.addAllPartitionRules(partitionScanner, TokenType.CONTENT_TYPE);
			
		}
		return partitionScanner;
	}
	
}
