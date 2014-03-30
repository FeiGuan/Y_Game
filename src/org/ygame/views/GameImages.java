package org.ygame.views;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface GameImages extends ClientBundle {
	@Source("blackSource.png")
	ImageResource blackSource();
	
	@Source("whiteSource.png")
	ImageResource whiteSource();
}
