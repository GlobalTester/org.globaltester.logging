package org.globaltester.logging.filelogger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;

/**
 * The activator class controls the plug-in life cycle and manages the tracking
 * of the {@link LogReaderService}.
 */
public class Activator implements BundleActivator {
	
	String logFileName = "logs" + File.separator + "OSGi_" + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".log";	
	
	private OsgiLogger logger;

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
		logger = new OsgiLogger(context, new FileLogger(new File(logFileName)));
		logger.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		logger.stop();
	}

}
