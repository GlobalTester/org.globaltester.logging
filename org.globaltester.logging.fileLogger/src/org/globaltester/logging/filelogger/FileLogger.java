package org.globaltester.logging.filelogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

/**
 * This {@link LogListener} implementation writes all {@link LogEntry}s into a logfile.
 * @author mboonk
 *
 */
public class FileLogger implements LogListener {

	DateFormat format = DateFormat.getDateTimeInstance();
	String logFileName = "logs" + File.separator + "PersoSim_OSGi_" + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".log";
	File file = new File(logFileName);
	PrintWriter writer;
	
	public FileLogger() {
	}
	
	@Override
	public void logged(LogEntry entry) {
		if (!file.exists()){
			try {
				if (writer != null){
					writer.flush();
					writer.close();
				}
				writer = new PrintWriter(file);
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (entry.getMessage() != null){
			writer.println("[" + entry.getBundle().getSymbolicName() + " - " + format.format(new Date(entry.getTime())) + "] "  + entry.getMessage());
		}
	}

}
