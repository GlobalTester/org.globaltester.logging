package org.globaltester.logging;

import org.globaltester.lib.xstream.XstreamFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;



public class MessageCoderJson {
	
	private static XStream xstream;
	
	static {
		HierarchicalStreamDriver hsd = new JettisonMappedXmlDriver();
		
		xstream = XstreamFactory.get(hsd);

		((CompositeClassLoader) xstream.getClassLoader()).add(XstreamFactory.class.getClassLoader());
		((CompositeClassLoader) xstream.getClassLoader()).add(Activator.class.getClassLoader());
	}
	
	public static String encode(Message messageObject) {
		return xstream.toXML(messageObject);
	}

	public static Message decode(String messageRep) {
		try {
			return (Message) xstream.fromXML(messageRep);
		} catch (Exception e) {
			return null;
		}
	}

}
