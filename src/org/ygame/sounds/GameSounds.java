package org.ygame.sounds;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

public interface GameSounds extends ClientBundle {
	@Source("org/ygame/sounds/pieceCaptured.wav")
	DataResource pieceCaptureMp3();
	
	@Source("org/ygame/sounds/pieceDown.wav")
	DataResource pieceDownMp3();
}
