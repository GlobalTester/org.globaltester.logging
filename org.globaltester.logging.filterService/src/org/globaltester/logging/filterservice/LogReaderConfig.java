package org.globaltester.logging.filterservice;

import org.globaltester.logging.formatservice.LogFormat;

public class LogReaderConfig {
	
	byte logLevels [] ={1};
	String bundleList [] = {"de.persosim.simulator"};
	
	public LogFormat formatter = new LogFormat();
	public BundleFilter bundleFilter = new BundleFilter(bundleList);
	public LevelFilter levelFilter = new LevelFilter(logLevels);
	
	
	LogFilterService [] filters = {bundleFilter, levelFilter};
	public NotFilter andFilter = new NotFilter(filters);
}
