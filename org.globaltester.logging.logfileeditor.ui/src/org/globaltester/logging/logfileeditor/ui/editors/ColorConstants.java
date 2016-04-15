package org.globaltester.logging.logfileeditor.ui.editors;

import org.eclipse.swt.graphics.RGB;

public interface ColorConstants {

	RGB DEFAULT = new RGB(0, 0, 0);
	RGB STRING = new RGB(0, 128, 0);
	RGB UNKNOWN_CONTENT = new RGB(255, 0, 0);

	RGB LOG_LEVEL = new RGB(128, 0, 0);
	RGB LOG_LEVEL_FATAL = new RGB(128, 0, 0);
	RGB LOG_LEVEL_ERROR = new RGB(128, 0, 0);
	RGB LOG_LEVEL_WARN = new RGB(128, 0, 0);
	RGB LOG_LEVEL_INFO = new RGB(128, 0, 0);
	RGB LOG_LEVEL_DEBUG = new RGB(128, 0, 0);
	RGB LOG_LEVEL_TRACE = new RGB(128, 0, 0);

	RGB LOG_TIMESTAMP = new RGB(128, 128, 128);
	RGB LOG_FAILURE = new RGB(255, 0, 0);
	RGB LOG_APDU_COMMAND = new RGB(128, 128, 0);
	RGB LOG_APDU_RESPONSE = new RGB(128, 128, 0);

	RGB TEST_EXECUTION = new RGB(50, 100, 150);
	RGB TEST_EXECUTION_PART = new RGB(50, 100, 150);
	RGB TEST_METADATA = new RGB(50, 100, 150);
	
	RGB APDU_HEAD = new RGB(200, 200, 255);
	RGB APDU_LC = new RGB(150, 200, 255);	
	RGB APDU_LE = new RGB(200, 150, 255);

}
