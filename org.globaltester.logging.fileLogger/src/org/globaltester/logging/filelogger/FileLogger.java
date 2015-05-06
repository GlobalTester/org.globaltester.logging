package org.globaltester.logging.filelogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.LogRecord;

import org.globaltester.logging.filterservice.LogReader;
import org.globaltester.logging.filterservice.LogReaderConfig;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

/**
 * This {@link LogListener} implementation writes all {@link LogEntry}s into a logfile.
 * @author mboonk
 *
 */
public class FileLogger extends LogReader {
	
	LogReaderConfig lrc = new LogReaderConfig();
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
		
		if(lrc.checkFilter(entry)){
			// format the entry
			String logEntry = lrc.formatter.format(entry);			
			if (entry.getMessage() != null){
				//write formatted entry
				writer.println(logEntry);
			}
		}

	}

}
