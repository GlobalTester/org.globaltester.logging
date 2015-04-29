package org.globaltester.logging.consolelogger;

import java.util.Iterator;
import java.util.LinkedList;

import org.globaltester.logging.filterservice.LogFilterService;
import org.globaltester.logging.formatservice.LogFormatService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle and manages the tracking
 * of the {@link LogReaderService}.
 */
public class Activator implements BundleActivator {
	
	private LinkedList<LogReaderService> readers = new LinkedList<>();
	private ConsoleLogger consoleLogger = new ConsoleLogger();
	private ServiceTracker<LogReaderService, LogReaderService> logReaderTracker;
	private static ServiceTracker<LogFilterService, LogFilterService> logFilterTracker;
	private static ServiceTracker<LogFormatService, LogFormatService> logFormatTracker;
	
	public static LogFilterService getLogFilterService(){
		if (logFilterTracker != null){
			return logFilterTracker.getService();
		}
		return null;
	}
	
	public static LogFormatService getLogFormatService(){
		if (logFormatTracker != null){
			return logFormatTracker.getService();
		}
		return null;
	}
	
	// This will be used to keep track of listeners as they are un/registering
	private ServiceListener serviceListener = new ServiceListener() {
		@Override
		public void serviceChanged(ServiceEvent event) {
			BundleContext bundleContext = event.getServiceReference().getBundle().getBundleContext();
			LogReaderService readerService = (LogReaderService) bundleContext.getService(event.getServiceReference());
			if (readerService != null){
				if (event.getType() == ServiceEvent.REGISTERED){
					readers.add(readerService);
					readerService.addLogListener(consoleLogger);
				} else if (event.getType() == ServiceEvent.UNREGISTERING){
					readerService.removeLogListener(consoleLogger);
					readers.remove(readerService);
				}
			}
		}
	};
	
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
		
        logFilterTracker = new ServiceTracker<>(context, LogFilterService.class.getName(), null);
        logFilterTracker.open();
        
        logFormatTracker = new ServiceTracker<>(context, LogFormatService.class.getName(), null);
        logFormatTracker.open();
        
		logReaderTracker = new ServiceTracker<>(context, LogReaderService.class.getName(), null);
		logReaderTracker.open();
		Object[] readers = logReaderTracker.getServices();
		if (readers != null){
			for (int i=0; i<readers.length; i++){
				LogReaderService readerService = (LogReaderService) readers [i];
				this.readers.add(readerService);
				readerService.addLogListener(consoleLogger);
			}
		}
		
		
        String filter = "(objectclass=" + LogReaderService.class.getName() + ")";
        try {
            context.addServiceListener(serviceListener, filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		Iterator<LogReaderService> iterator = readers.iterator();
        while (iterator.hasNext())
        {
            LogReaderService readerService = iterator.next();
            readerService.removeLogListener(consoleLogger);
            iterator.remove();
        }
		logReaderTracker.close();
		logFilterTracker.close();
		logFormatTracker.close();

	}

}
