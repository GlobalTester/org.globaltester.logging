package org.globaltester.logging.filter;

import java.util.LinkedList;

import org.osgi.service.log.LogEntry;

public class BundleFilter implements LogFilter {

	private LinkedList<String> bundleFilters = new LinkedList<String>();
	
	public BundleFilter(String... bundleList) {
		for(int i=0; i<bundleList.length; i++){
			addBundle(bundleList[i]);
		}
	}
	
	public void setBundleFilters(LinkedList<String> bundleFilters) {
		this.bundleFilters = bundleFilters;
	}
	
	public LinkedList<String> getBundleFilters() {
		return bundleFilters;
	}

	public void addBundle(String filter) {
		bundleFilters.add(filter);
	}
	
	@Override
	public boolean logFilter(LogEntry entry) {
		// check for bundles
		for (String currentBundle : bundleFilters) {
			if (entry.getBundle().getSymbolicName().startsWith(currentBundle)) {
				return true;
			}
		}
		return false;
	}

}
