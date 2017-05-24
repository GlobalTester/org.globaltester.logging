package org.globaltester.logging.filelogger;

import java.util.Iterator;
import java.util.LinkedList;

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
	private LinkedList<LogReaderService> readers = new LinkedList<>();
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
						readers.add(readerService);
						readerService.addLogListener(logListener);
					} else if (event.getType() == ServiceEvent.UNREGISTERING){
						readerService.removeLogListener(logListener);
						readers.remove(readerService);
					}
				}
			}
		};
	}
	
	public void start(){
		
		logReaderTracker = new ServiceTracker<>(context, LogReaderService.class.getName(), null);
		logReaderTracker.open();
		Object[] readers = logReaderTracker.getServices();
		if (readers != null){
			for (int i=0; i<readers.length; i++){
				LogReaderService readerService = (LogReaderService) readers [i];
				this.readers.add(readerService);
				readerService.addLogListener(logListener);
			}
		}
		
		
        String filter = "(objectclass=" + LogReaderService.class.getName() + ")";
        try {
            context.addServiceListener(serviceListener, filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

	}
	
	public void stop() {
		Iterator<LogReaderService> iterator = readers.iterator();
        while (iterator.hasNext())
        {
            LogReaderService readerService = iterator.next();
            readerService.removeLogListener(logListener);
            iterator.remove();
        }
		logReaderTracker.close();

	}
}
