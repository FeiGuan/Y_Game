package org.ygame.views;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.ygame.presenters.YPresenter;
import org.ygame.presenters.YPresenter.ViewState;
import org.ygame.sounds.GameSounds;

public class YGraphics extends Composite implements YPresenter.View {

	private final static int PLAYER_NUMBER = 2;
	private final static String TRY_BLOCK_STYLE = "color-try";
	private final static String PLAYER_A_BLOCK_STYLE = "color-icon";
	private final static String PLAYER_B_BLOCK_STYLE = "color-icon2";
	private final static String CENTER = "center";

	private YPresenter presenter;
	private List<Button> btns = Lists.newArrayList();
	private Button blackSourceBtn;
	private Button whiteSourceBtn;
	private Audio pieceDown;
	private Audio pieceCaptured;
	private int tryRow = -1;
	private int tryCol = -1;

	private int outRow = -1;
	private int outCol = -1;

	private static GameImages gameImages = GWT.create(GameImages.class);
	private static GameSounds gameSounds = GWT.create(GameSounds.class);

	private PieceMovingAnimation animation;

	private Image blackSource = new Image();
	private Image whiteSource = new Image();

	interface YGraphicsUiBinder extends UiBinder<Widget, YGraphics> {

	}

	private static YGraphicsUiBinder uiBinder = GWT
			.create(YGraphicsUiBinder.class);

	@UiField
	VerticalPanel buttonContainer;

	public YGraphics() {
		initWidget(uiBinder.createAndBindUi(this));
		initBoard();
		if (Audio.isSupported()) {
			pieceDown = Audio.createIfSupported();
			pieceDown.addSource(gameSounds.pieceDownMp3().getSafeUri()
					.asString(), AudioElement.TYPE_MP3);
			pieceCaptured = Audio.createIfSupported();
			pieceCaptured.addSource(gameSounds.pieceCaptureMp3().getSafeUri()
					.asString(), AudioElement.TYPE_MP3);

		}

		whiteSource.setResource(getImage("white"));
		blackSource.setResource(getImage("black"));
	}

	private ImageResource getImage(String kind) {
		if (kind.equals("black"))
			return gameImages.blackSource();
		if (kind.equals("white"))
			return gameImages.whiteSource();
		return null;
	}

	private boolean sourceClicked = false;

	private void initBoard() {
		for (int i = 0; i < 10; i++) {
			HorizontalPanel hpInner = new HorizontalPanel();
			for (int j = 0; j <= i; j++) {
				Button btn = new Button();
				btn.setSize("50px", "50px");
				btn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Button btn = (Button) event.getSource();
						int index = btns.indexOf(btn);
						int row = getRowFrom(index);
						int col = getColFrom(index);
//						if(blackSourceBtn.isVisible())
//							animateMove(row, col, 1);
//						else
//							animateMove(row, col, 2);
						presenter.makeMove(row, col);
					}
				});

				btn.addMouseOverHandler(new MouseOverHandler() {

					@Override
					public void onMouseOver(MouseOverEvent event) {
						Button btn = (Button) event.getSource();
						int index = btns.indexOf(btn);
						tryRow = getRowFrom(index);
						tryCol = getColFrom(index);
						presenter.tryCurrentPieceWithPosition(tryRow, tryCol);
					}
				});

				btn.addMouseOutHandler(new MouseOutHandler() {

					@Override
					public void onMouseOut(MouseOutEvent event) {
						Button btn = (Button) event.getSource();
						int index = btns.indexOf(btn);
						outRow = getRowFrom(index);
						outCol = getColFrom(index);
						presenter.outTryPieceWithPosition(outRow, outCol);
					}

				});
				btns.add(btn);
				hpInner.add(btn);
			}
			hpInner.setStyleName(CENTER);
			buttonContainer.add(hpInner);
		}
		HorizontalPanel sourcePanel = new HorizontalPanel();
		blackSourceBtn = new Button();
		blackSourceBtn.setSize("50px", "50px");
		blackSourceBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (sourceClicked) {
					sourceClicked = false;
				} else {
					sourceClicked = true;
				}
			}

		});
		blackSourceBtn.setStyleName(PLAYER_A_BLOCK_STYLE);

		whiteSourceBtn = new Button();
		whiteSourceBtn.setSize("50px", "50px");
		whiteSourceBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (sourceClicked) {
					sourceClicked = false;
				} else {
					sourceClicked = true;
				}
			}

		});
		whiteSourceBtn.setStyleName(PLAYER_B_BLOCK_STYLE);

		sourcePanel.add(blackSourceBtn);
		sourcePanel.add(whiteSourceBtn);
		sourcePanel.setStyleName(CENTER);

		buttonContainer.add(sourcePanel);

	}

	private int getIndex(int row, int col) {
		if (row == 0)
			return 0;
		return (1 + row) * row / 2 + col;
	}

	private int getRowFrom(int index) {
		if (index == 0)
			return 0;
		if (index <= 2)
			return 1;
		if (index <= 5)
			return 2;
		if (index <= 9)
			return 3;
		if (index <= 14)
			return 4;
		if (index <= 20)
			return 5;
		if (index <= 27)
			return 6;
		if (index <= 35)
			return 7;
		if (index <= 44)
			return 8;
		if (index <= 54)
			return 9;
		else
			return -1;
	}

	private int getColFrom(int index) {
		if (index == 0)
			return 0;
		if (index <= 2)
			return index - 1;
		if (index <= 5)
			return index - 3;
		if (index <= 9)
			return index - 6;
		if (index <= 14)
			return index - 10;
		if (index <= 20)
			return index - 15;
		if (index <= 27)
			return index - 21;
		if (index <= 35)
			return index - 28;
		if (index <= 44)
			return index - 36;
		if (index <= 54)
			return index - 45;
		else
			return -1;
	}

	@Override
	public void setPresenter(YPresenter yPresenter) {
		this.presenter = yPresenter;
	}

	@Override
	public void setViewerState(ViewState vs, String color) {
		if (vs == ViewState.MAKE_MOVE){
			for (Button btn : btns)
				btn.setEnabled(true);
			if(color != null && color.equals("black")){
				blackSourceBtn.setEnabled(true);
				blackSourceBtn.setVisible(true);
				whiteSourceBtn.setEnabled(false);
				whiteSourceBtn.setVisible(false);
			}
			if(color != null && color.equals("white")){
				whiteSourceBtn.setEnabled(true);
				whiteSourceBtn.setVisible(true);
				blackSourceBtn.setEnabled(false);
				blackSourceBtn.setVisible(false);
			}
		}
		else{
			for (Button btn : btns)
				btn.setEnabled(false);
			blackSourceBtn.setEnabled(false);
			blackSourceBtn.setVisible(false);
			whiteSourceBtn.setEnabled(false);
			whiteSourceBtn.setVisible(false);
		}
	}

	@Override
	public void updateTryBlock(int row, int col) {
		Button btn = btns.get(this.getIndex(row, col));
		btn.setStyleName(TRY_BLOCK_STYLE);
	}

	@Override
	public void outTryBlock(int row, int col) {
		Button btn = btns.get(this.getIndex(row, col));
		btn.removeStyleName(TRY_BLOCK_STYLE);
	}

	@Override
	public void setGameBoard(String yourPlayerId, List<String> playerList,
			String pieces) {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j <= i; j++) {
				int index = this.getIndex(i, j);
				Button btn = btns.get(index);
				btn.setStyleName("");
				char c = pieces.charAt(index);
				if (c == '1')
					btn.setStyleName(PLAYER_A_BLOCK_STYLE);
				if (c == '2')
					btn.setStyleName(PLAYER_B_BLOCK_STYLE);
			}

	}

	@Override
	public void cleanTryBlock() {
		btns.get(this.getIndex(tryRow, tryCol))
				.removeStyleName(TRY_BLOCK_STYLE);
	}

	@Override
	public void lose() {
		Window.alert("You lose");
	}

	@Override
	public void win() {
		Window.alert("You win");
	}

	@Override
	public void tie() {
		Window.alert("Tied game");
	}

	@Override
	public void animateMove(int row, int col, int color) {
		Button endButton = btns.get(getIndex(row, col));
		ImageResource transformImage = null;
		if (color == 1)
			animation = new PieceMovingAnimation(blackSourceBtn, endButton,
					gameImages.blackSource(), pieceDown);
		else
			animation = new PieceMovingAnimation(whiteSourceBtn, endButton,
					gameImages.whiteSource(), pieceDown);
		animation.run(1000);
	}
	
}
