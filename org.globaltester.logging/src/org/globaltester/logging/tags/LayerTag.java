package org.globaltester.logging.tags;

public class LayerTag extends LogTag {
	
	private LayerName layerName;
	
	public LayerTag(LayerName layerName, LogLevel logLevel) {
		super(layerName.getName(), logLevel);
		this.layerName = layerName;
	}

	public LayerName getLayerName() {
		return layerName;
	}

}
