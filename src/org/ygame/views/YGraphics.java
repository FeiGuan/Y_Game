package org.ygame.views;

import java.util.List;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Resize;
import org.ygame.presenters.YPresenter;
import org.ygame.presenters.YPresenter.ViewState;

public class YGraphics extends Composite implements YPresenter.View {

	private final static int PLAYER_NUMBER = 2;
	private final static String TRY_BLOCK_STYLE = "color-try";
	private final static String PLAYER_A_BLOCK_STYLE = "color-icon";
	private final static String PLAYER_B_BLOCK_STYLE = "color-icon2";
	private final static String CENTER = "center";
	private final static String MARGIN = "margin";

	private YPresenter presenter;
	private List<Button> btns = Lists.newArrayList();
	private Label sign = new Label();
	private Label sign2 = new Label();
	private Button blackSourceBtn;
	private Button whiteSourceBtn;
	private int tryRow = -1;
	private int tryCol = -1;

	private int outRow = -1;
	private int outCol = -1;

	private static GameImages gameImages = GWT.create(GameImages.class);

	private PieceMovingAnimation animation;

	private Image blackSource = new Image();
	private Image whiteSource = new Image();

	private TextBox txt = new TextBox();

	private SoundController soundController = new SoundController();

	private Sound sound = soundController.createSound(
			Sound.MIME_TYPE_AUDIO_WAV_PCM, "pieceDown.wav");
	
	private static GameSounds gameSounds = GWT.create(GameSounds.class);

	private PickupDragController dragController;
	private SimpleDropController dropController;

	interface YGraphicsUiBinder extends UiBinder<Widget, YGraphics> {

	}

	private static YGraphicsUiBinder uiBinder = GWT
			.create(YGraphicsUiBinder.class);

	@UiField
	VerticalPanel buttonContainer;

	@UiField
	Label dragFrom;

	@UiField
	Label dragTo;
	
	@UiField
	Label youWin;
	
	@UiField
	Label youLose;
	
	@UiField
	Label tieGame;

	public YGraphics() {
		initWidget(uiBinder.createAndBindUi(this));

		dragController = new PickupDragController(RootPanel.get(), false);
		dragController.makeDraggable(dragFrom);
		dropController = new TestDropController(dragTo);
		dragController.registerDropController(dropController);
		initBoard();
		whiteSource.setResource(getImage("white"));
		blackSource.setResource(getImage("black"));
	}

	class TestDropController extends SimpleDropController {
		public TestDropController(Widget dropTarget) {
			super(dropTarget);
		}

		@Override
		public void onDrop(DragContext context) {
			for (Widget w : context.selectedWidgets) {
				if (w instanceof Label) {
					w.setVisible(false);
					dragFrom.setText("You have dropped here!");
				}
			}
			super.onDrop(context);
		}
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
		youWin.setVisible(false);
		youLose.setVisible(false);
		tieGame.setVisible(false);
		sign.setVisible(true);
		sign2.setVisible(true);
		for (int i = 0; i < 10; i++) {
			HorizontalPanel hpInner = new HorizontalPanel();
			for (int j = 0; j <= i; j++) {
				Button btn = new Button();
				btn.setSize("50px", "50px");
				// final FadeAnimation fadeAnimation = new
				// FadeAnimation(btn.getElement());
				btn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {		
						Button btn = (Button) event.getSource();
						
						int index = btns.indexOf(btn);

						final int row = getRowFrom(index);
						final int col = getColFrom(index);
						if (blackSource.isVisible() && presenter.state.getPieces().charAt(index) == '0') {
							sound.play();
							Resize resize = new Resize(blackSource.getElement());
							resize.setDuration(1);
							resize.setStartPercentage(100);
							resize.setEndPercentage(0);
							resize.addEffectCompletedHandler(new EffectCompletedHandler() {

								@Override
								public void onEffectCompleted(
										EffectCompletedEvent event) {
									presenter.makeMove(row, col);
									blackSource.setSize("50px", "50px");
									blackSource.setVisible(true);
									
								}

							});
							resize.play();
						}
						if (whiteSource.isVisible() && presenter.state.getPieces().charAt(index) == '0') {
							sound.play();
							Resize resize = new Resize(whiteSource.getElement());
							resize.setDuration(1);
							resize.setStartPercentage(100);
							resize.setEndPercentage(0);
							resize.addEffectCompletedHandler(new EffectCompletedHandler() {

								@Override
								public void onEffectCompleted(
										EffectCompletedEvent event) {
									presenter.makeMove(row, col);
									whiteSource.setSize("50px", "50px");
									whiteSource.setVisible(true);
								}

							});
							resize.play();
						}

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

		sourcePanel.add(blackSource);
		sourcePanel.add(whiteSource);
		// sourcePanel.add(sign);
		// sourcePanel.add(sign2);
		// sign.setText("aaaa");
		// sign2.setText("bbbb");

		sourcePanel.setStyleName(CENTER);

		buttonContainer.add(sourcePanel);

		HorizontalPanel dragFromPanel = new HorizontalPanel();

		dragFromPanel.add(dragFrom);

		dragFromPanel.setStyleName(CENTER);

		buttonContainer.add(dragFromPanel);

		HorizontalPanel dragToPanel = new HorizontalPanel();

		dragToPanel.add(dragTo);

		dragToPanel.setStyleName(CENTER);

		buttonContainer.add(dragToPanel);

		BoundaryDropController dropController = new BoundaryDropController(
				RootPanel.get(), false);
		dragController.registerDropController(dropController);
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
		if (vs == ViewState.MAKE_MOVE) {
			for (Button btn : btns)
				btn.setEnabled(true);
			if (color != null && color.equals("black")) {
				blackSource.setVisible(true);
				whiteSource.setVisible(false);
			}
			if (color != null && color.equals("white")) {
				blackSource.setVisible(false);
				whiteSource.setVisible(true);
			}
		} else {
			for (Button btn : btns)
				btn.setEnabled(false);
			// blackSource.setEnabled(false);
			blackSource.setVisible(false);
			// whiteSource.setEnabled(false);
			whiteSource.setVisible(false);
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
		youLose.setVisible(true);
	}

	@Override
	public void win() {
		youWin.setVisible(true);
	}

	@Override
	public void tie() {
		tieGame.setVisible(true);
	}

	@Override
	public void animateMove(int row, int col, int color) {
		Button endButton = btns.get(getIndex(row, col));
		if (color == 1) {
			animation = new PieceMovingAnimation(blackSourceBtn, endButton,
					gameImages.blackSource());
			txt.setText("start:" + animation.startX + "." + animation.startY
					+ "." + animation.endX + "." + animation.endY);
		} else
			animation = new PieceMovingAnimation(whiteSourceBtn, endButton,
					gameImages.whiteSource());
		animation.run(1000);
	}
	
	public void showSign(String s){
		sign.setText(s);
	}
	
	public void showSign2(String s){
		sign2.setText(s);
	}

}
