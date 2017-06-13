package org.globaltester.logging.filelogger;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.globaltester.logging.BasicLogger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * This class wraps a given {@link LogListener} and contains the logic needed to
 * attach it to the OSGi logging system.
 * 
 * @author mboonk
 *
 */
public class OsgiLogger {
	private LinkedList<LogReaderService> readerServices = new LinkedList<>();
	private ServiceTracker<LogReaderService, LogReaderService> logReaderTracker;
	
	private ServiceListener serviceListener;
	private BundleContext context;
	private LogListener logListener;
	
	public OsgiLogger(BundleContext context, LogListener logListener) {
		this.context = context;
		this.logListener = logListener;

		// This will be used to keep track of listeners as they are un/registering
		serviceListener = new ServiceListener() {
			@Override
			public void serviceChanged(ServiceEvent event) {
				BundleContext bundleContext = event.getServiceReference().getBundle().getBundleContext();
				LogReaderService readerService = (LogReaderService) bundleContext.getService(event.getServiceReference());
				if (readerService != null){
					if (event.getType() == ServiceEvent.REGISTERED){
						readerServices.add(readerService);
						readerService.addLogListener(logListener);
					} else if (event.getType() == ServiceEvent.UNREGISTERING){
						readerService.removeLogListener(logListener);
						readerServices.remove(readerService);
					}
				}
			}
		};
	}
	
	public void start(){
		
		logReaderTracker = new ServiceTracker<>(context, LogReaderService.class.getName(), null);
		logReaderTracker.open();
		Object[] trackerServices = logReaderTracker.getServices();
		if (trackerServices != null){
			for (int i=0; i<trackerServices.length; i++){
				LogReaderService readerService = (LogReaderService) trackerServices [i];
				this.readerServices.add(readerService);
				readerService.addLogListener(logListener);
			}
		}
		
		
        String filter = "(objectclass=" + LogReaderService.class.getName() + ")";
        try {
            context.addServiceListener(serviceListener, filter);
        } catch (InvalidSyntaxException e) {
			BasicLogger.logException(this.getClass(), e);
        }

	}
	
	public void stop() {
		Iterator<LogReaderService> iterator = readerServices.iterator();
		if (logListener instanceof Closeable){
			try {
				((Closeable) logListener).close();
			} catch (IOException e) {
				// try closing the listeners resources
				BasicLogger.logException(this.getClass(), e);
			}
		}
        while (iterator.hasNext())
        {
            LogReaderService readerService = iterator.next();
            readerService.removeLogListener(logListener);
            iterator.remove();
        }
		logReaderTracker.close();

	}
}
