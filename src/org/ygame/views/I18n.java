package org.ygame.views;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Messages;

public interface I18n extends Messages{
	public static I18n LANG = GWT.create(I18n.class);
	
	@Key("youWin")
	String youWin();
	
	@Key("youLose")
	String youLose();
	
	@Key("dragMe")
	String dragMe();
	
	@Key("dropHere")
	String dropHere();
}
