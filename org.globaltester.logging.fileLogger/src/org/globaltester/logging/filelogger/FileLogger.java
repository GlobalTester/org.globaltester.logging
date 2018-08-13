package org.globaltester.logging.filelogger;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.globaltester.logging.AbstractLogListener;

/**
 * This {@link LogListener} implementation writes all {@link LogEntry}s into a
 * logfile.
 * 
 * @author mboonk
 *
 */
public class FileLogger extends AbstractLogListener implements Closeable {

	private PrintWriter writer;
	private File file;

	public FileLogger(File file) throws IOException {
		this.file = file;
		if (!file.exists()) {
			File parent = file.getParentFile();
			if (parent != null) {
				parent.mkdirs();
			}

			file.createNewFile();
		}
		
		writer = new PrintWriter(file);
	}

	@Override
	public void displayLogMessage(String msg) {

		if (msg != null) {
			// write formatted entry
			writer.println(msg);
			writer.flush();
		}

	}
	
	@Override
	public String toString() {
		return "Output: " + file.getAbsolutePath().toString() + " " +  super.toString();
	}

	public void shutdown(){
		writer.close();
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
	
}
