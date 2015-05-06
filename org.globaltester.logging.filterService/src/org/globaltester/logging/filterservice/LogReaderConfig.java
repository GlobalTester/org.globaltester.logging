package org.globaltester.logging.filterservice;

import org.globaltester.logging.formatservice.LogFormat;

public class LogReaderConfig {
	
	byte logLevels [] ={1,2,3,4,5,6,120};
	String bundleList [] = {"de.persosim.simulator"};
	
	public LogFormat format = new LogFormat();
	public BundleFilter bundleFilter = new BundleFilter(bundleList);
	public LevelFilter levelFilter = new LevelFilter(logLevels);
	
	
	LogFilter [] filters = {bundleFilter, levelFilter};
	public AndFilter filter = new AndFilter(filters);
	
	public LogFilter getFilter() {		
		return filter;
	}
	
	public LogFormat getFormat() {		
		return format;
	}
}
