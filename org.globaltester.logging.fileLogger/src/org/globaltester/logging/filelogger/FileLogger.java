package org.globaltester.logging.filelogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.globaltester.logging.AbstractLogListener;
import org.globaltester.logging.LogListenerConfigImpl;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

/**
 * This {@link LogListener} implementation writes all {@link LogEntry}s into a logfile.
 * @author mboonk
 *
 */
public class FileLogger extends AbstractLogListener {
	
	LogListenerConfigImpl lrc = new LogListenerConfigImpl();
	DateFormat format = DateFormat.getDateTimeInstance();
	String logFileName = "logs" + File.separator + "PersoSim_OSGi_" + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".log";	
	
	File file = new File(logFileName);
	PrintWriter writer;
	
	public FileLogger() {
	}

	@Override
	public void displayLogMessage(String msg) {

		if (!file.exists()) {
			File parent = file.getParentFile();
			if(parent != null) {
				parent.mkdirs();
			}
			
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				writer = new PrintWriter(file);
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (msg != null) {
			// write formatted entry
			writer.println(msg);
		}

	}

}
