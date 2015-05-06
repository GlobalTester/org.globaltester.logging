package org.globaltester.logging.filterservice;

import org.globaltester.logging.formatservice.LogFormat;
import org.osgi.service.log.LogEntry;

public class LogReaderConfig {
	
	byte logLevels [] ={1,2,3,4,5,6,120};
	String bundleList [] = {"de.persosim.simulator"};
	
	public LogFormat formatter = new LogFormat();
	public BundleFilter bundleFilter = new BundleFilter(bundleList);
	public LevelFilter levelFilter = new LevelFilter(logLevels);
	
	
	LogFilterService [] filters = {bundleFilter, levelFilter};
	public AndFilter andFilter = new AndFilter(filters);
	
	public boolean checkFilter(LogEntry entry){		
		return andFilter.perform(entry);
	}
}
