package org.globaltester.logging;

import org.globaltester.lib.xstream.XstreamFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;



public class MessageEncoderJson implements MessageEncoder {
	
	private XStream xstream;
	
	public MessageEncoderJson() {
		HierarchicalStreamDriver hsd = new JsonHierarchicalStreamDriver();
		
		xstream = XstreamFactory.get(hsd);
		
		((CompositeClassLoader) xstream.getClassLoader()).add(MessageEncoderJson.class.getClassLoader());
		((CompositeClassLoader) xstream.getClassLoader()).add(XstreamFactory.class.getClassLoader());
	}
	
	@Override
	public String encode(Message messageObject) {
		return xstream.toXML(messageObject);
	}

}
