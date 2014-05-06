package org.ygame;

import org.game_api.GameApi.ContainerConnector;
import org.game_api.GameApi.Game;
import org.game_api.GameApi.IteratingPlayerContainer;
import org.game_api.GameApi.UpdateUI;
import org.game_api.GameApi.VerifyMove;
import org.game_api.GameApi;
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
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
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
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Y_Game implements EntryPoint {
	ContainerConnector container;
	YPresenter yPresenter;

	@Override
	public void onModuleLoad() {
//		MGWT.applySettings(MGWTSettings.getAppSetting());
		Window.enableScrolling(false);
//		MGWT.addOrientationChangeHandler(new OrientationChangeHandler() {
//			@Override
//			public void onOrientationChanged(OrientationChangeEvent event) {
//				scaleGame();
//			}
//		});
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				scaleGame();
			}
		});
		Game game = new Game() {

			@Override
			public void sendVerifyMove(VerifyMove verifyMove) {
				container.sendVerifyMoveDone(new YGameLogic()
						.verify(verifyMove));
			}

			@Override
			public void sendUpdateUI(UpdateUI updateUI) {
				yPresenter.updateUI(updateUI);
			}

		};
		container = new ContainerConnector(game);
		YGraphics yGraphics = new YGraphics();
		yPresenter = new YPresenter(yGraphics, container);

		RootPanel.get("mainDiv").add(yGraphics);

		container.sendGameReady();
		scaleGame();
	}

	private void scaleGame() {
//		 double scaleX = (double) Window.getClientWidth() / Board.GAME_WIDTH;
//		 double scaleY = (double) Window.getClientHeight() /Board.GAME_HEIGHT;
//		 double scale = Math.min(scaleX, scaleY);
//		 Board.setScale(scale);
	}
}
