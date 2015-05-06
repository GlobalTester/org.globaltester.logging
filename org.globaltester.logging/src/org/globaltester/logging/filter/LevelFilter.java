package org.globaltester.logging.filter;

import java.util.Arrays;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.log.LogEntry;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class LevelFilter implements LogFilter{

	private byte[] logLevels;

	// place for persistent storage of filter data
	Preferences preferences = InstanceScope.INSTANCE
			.getNode("de.persosim.simulator.ui.utils.logging");
	
	// Constructor
	public LevelFilter(byte[] logLevels) {
		setLogLevels(logLevels);
//		loadLogLevels();
	}
	
	
	public void setLogLevels(byte[] logLevels) {
		this.logLevels = Arrays.copyOf(logLevels, logLevels.length);
	}
	
	
	public byte[] getLogLevels() {
		return logLevels;
	}

	
	public void saveLogLevels() {
		preferences.putByteArray("loglevels", logLevels);

		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

	}
	
	public void loadLogLevels() {
		setLogLevels(preferences.getByteArray("loglevels", logLevels));
	}
	
	@Override
	public boolean logFilter(LogEntry entry) {
		// check if log level is ok
		for (int currentLevel : logLevels) {
			if (entry.getLevel() == currentLevel) {
				// the entry is ok and should be logged
				return true;
			}
		}
		return false;
	}

}
