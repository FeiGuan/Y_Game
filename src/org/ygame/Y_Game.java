package org.ygame;

import org.ygame.client.shared.GameApi.Game;
import org.ygame.client.shared.GameApi.IteratingPlayerContainer;
import org.ygame.client.shared.GameApi.UpdateUI;
import org.ygame.client.shared.GameApi.VerifyMove;
import org.ygame.client.shared.GameApi;
import org.ygame.client.shared.YGameLogic;
import org.ygame.presenters.YPresenter;
import org.ygame.views.YGraphics;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Y_Game implements EntryPoint {
	IteratingPlayerContainer container;
	YPresenter yPresenter;
	
	@Override
	public void onModuleLoad() {
		Game game = new Game(){

			@Override
			public void sendVerifyMove(VerifyMove verifyMove) {
				container.sendVerifyMoveDone(new YGameLogic().verify(verifyMove));
			}

			@Override
			public void sendUpdateUI(UpdateUI updateUI) {
				yPresenter.updateUI(updateUI);
			}
			
		};
		YGraphics yGraphics = null;
		try{
		container = new IteratingPlayerContainer(game, 2);
		yGraphics = new YGraphics();
		yPresenter = new YPresenter(yGraphics, container);
		}
		catch(Exception e){
			System.out.println(e);
		}
		final ListBox playerSelect = new ListBox();
		playerSelect.addItem("WhitePlayer");
		playerSelect.addItem("BlackPlayer");
		playerSelect.addItem("Viewer");
		playerSelect.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = playerSelect.getSelectedIndex();
				int playerId = selectedIndex == 2 ? GameApi.VIEWER_ID : 
					container.getPlayerIds().get(selectedIndex);
				container.updateUi(playerId);
			}
		});
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(yGraphics);
		flowPanel.add(playerSelect);
		RootPanel.get("mainDiv").add(flowPanel);
		container.sendGameReady();
		container.updateUi(container.getPlayerIds().get(0));
	}
}
