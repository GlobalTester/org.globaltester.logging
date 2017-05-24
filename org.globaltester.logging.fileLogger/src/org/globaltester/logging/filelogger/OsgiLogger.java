package org.globaltester.logging.filelogger;

import java.util.Iterator;
import java.util.LinkedList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

public class OsgiLogger {
	private LinkedList<LogReaderService> readers = new LinkedList<>();
	private ServiceTracker<LogReaderService, LogReaderService> logReaderTracker;
	
	private ServiceListener serviceListener;
	private BundleContext context;
	private FileLogger fileLogger;
	
	public OsgiLogger(BundleContext context, FileLogger fileLogger) {
		this.context = context;
		this.fileLogger = fileLogger;

		// This will be used to keep track of listeners as they are un/registering
		serviceListener = new ServiceListener() {
			@Override
			public void serviceChanged(ServiceEvent event) {
				BundleContext bundleContext = event.getServiceReference().getBundle().getBundleContext();
				LogReaderService readerService = (LogReaderService) bundleContext.getService(event.getServiceReference());
				if (readerService != null){
					if (event.getType() == ServiceEvent.REGISTERED){
						readers.add(readerService);
						readerService.addLogListener(fileLogger);
					} else if (event.getType() == ServiceEvent.UNREGISTERING){
						readerService.removeLogListener(fileLogger);
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
				readerService.addLogListener(fileLogger);
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
            readerService.removeLogListener(fileLogger);
            iterator.remove();
        }
		logReaderTracker.close();

	}
}
