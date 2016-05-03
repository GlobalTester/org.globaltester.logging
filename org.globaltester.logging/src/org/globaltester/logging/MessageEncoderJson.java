package org.globaltester.logging;

import org.globaltester.lib.xstream.XstreamFactory;
import org.globaltester.logging.tags.LayerName;
import org.globaltester.logging.tags.LayerTag;
import org.globaltester.logging.tags.LogLevel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

import com.thoughtworks.xstream.core.util.CompositeClassLoader;



public class MessageEncoderJson implements MessageEncoder {
	
	private XStream xstream;
	
	public MessageEncoderJson() {
		System.out.println("START MessageEncoderJson");
		
		HierarchicalStreamDriver hsd = new JettisonMappedXmlDriver();
//		HierarchicalStreamDriver hsd = new JsonHierarchicalStreamDriver();
		
//		xstream = new XStream(hsd);
		xstream = XstreamFactory.get(hsd);
		
		Message m = new Message("Hello world!", new LayerTag(LayerName.SECUREMESSAGING, LogLevel.INFO));
		
		String s = encode(m);
		
		System.out.println("JSON response:\n" + s + "\n--------");
		
		System.out.println("JSON READ");
//		Message m2 = (Message) xstream.fromXML(s);
		Object m2 = xstream.fromXML(s);
//		System.out.println("m2: " + m2.toString());
		
//		xstream.setMode(XStream.NO_REFERENCES);
//		xstream.alias("product", Product.class);
		
		((CompositeClassLoader) xstream.getClassLoader()).add(MessageEncoderJson.class.getClassLoader());
		((CompositeClassLoader) xstream.getClassLoader()).add(XstreamFactory.class.getClassLoader());
//		((CompositeClassLoader) xstream.getClassLoader()).add(org.codehaus.jettison.class.getClassLoader());
		
		System.out.println("END MessageEncoderJson");
	}
	
	@Override
	public String encode(Message messageObject) {
		return xstream.toXML(messageObject);
//		return "";
	}

}
