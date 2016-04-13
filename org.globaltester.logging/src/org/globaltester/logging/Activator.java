package org.globaltester.logging;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	public static BundleContext context;
	
	//TODO move this service tracking to the class BasicLogger (similar to Crypto)
	private static ServiceTracker<LogService, LogService> logServiceTracker;
	
	public static LogService getLogservice() {
		if (logServiceTracker != null){
			return logServiceTracker.getService();
		}
		return null;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		//get LogService
		logServiceTracker = new ServiceTracker<LogService, LogService>(context, LogService.class.getName(), null);
		logServiceTracker.open();
}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
		logServiceTracker.close();
	}
	
	public static BundleContext getContext() {
		return context;
	}

}
