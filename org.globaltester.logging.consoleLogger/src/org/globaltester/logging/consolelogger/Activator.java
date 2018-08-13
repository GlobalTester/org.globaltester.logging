package org.globaltester.logging.consolelogger;

import org.globaltester.logging.BasicLogger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;

/**
 * The activator class controls the plug-in life cycle and manages the tracking
 * of the {@link LogReaderService}.
 */
public class Activator implements BundleActivator {
	
	private ConsoleLogger consoleLogger = new ConsoleLogger();
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		BasicLogger.addLogListener(consoleLogger);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		BasicLogger.removeLogListener(consoleLogger);
	}

}
