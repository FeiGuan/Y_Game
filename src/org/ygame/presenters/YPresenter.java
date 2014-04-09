package org.ygame.presenters;

import java.util.List;

import org.game_api.GameApi.Container;
import org.game_api.GameApi.UpdateUI;
import org.ygame.client.shared.YGameLogic;
import org.ygame.client.shared.YState;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

public class YPresenter {
	public enum ViewState {
		VIEW_ONLY, MAKE_MOVE,
	}

	public interface View {
		public void setPresenter(YPresenter yPresenter);

		public void setViewerState(ViewState vs, String color);

		public void updateTryBlock(int row, int col);

		public void outTryBlock(int row, int col);

		public void setGameBoard(String yourPlayerId, List<String> playerList,
				String pieces);

		public void cleanTryBlock();

		public void lose();

		public void win();

		public void tie();

		public void animateMove(int row, int col, int color);
	}

	/**
	 * presenter driven by logic
	 */
	private final YGameLogic gameLogic = new YGameLogic();

	private final View view;

	private final Container container;

	public YState state;

	private String yourPlayerId;

	private List<String> playerIds;

	private final String KEYPIECES = "KEYPIECES";

	private final String KEYPLAYERID = "KEYPLAYERID";

	private final String KEYPLAYERS = "KEYPLAYERS";

	/**
	 * constructor, link view with container
	 * 
	 * @param view
	 * @param container
	 */
	public YPresenter(View view, Container container) {
		this.view = view;
		this.container = container;
		view.setPresenter(this);
	}

	public void updateUI(UpdateUI updateUI) {
		yourPlayerId = updateUI.getYourPlayerId();
		playerIds = updateUI.getPlayerIds();

		if (updateUI.getState().isEmpty()
				&& yourPlayerId.equals(Ordering.<String> natural().min(
						updateUI.getPlayerIds()))) {
			System.out.println("Players:" + updateUI.getPlayerIds());
			System.out.println("Your player Id:" + yourPlayerId);
			sendInitialMove(updateUI.getPlayerIds());
			return;
		}

		String mapPieces = (String) updateUI.getState().get(KEYPIECES);
		String mapPlayerId = (String) updateUI.getState().get(KEYPLAYERID);
		List<String> mapPlayerIds = (List<String>) updateUI.getState().get(
				KEYPLAYERS);

		state = new YState(mapPlayerId, ImmutableList.copyOf(mapPlayerIds),
				mapPieces);

		if (!updateUI.getPlayerIds().contains(yourPlayerId)) {
			disableUiAndWatch();
			updateGameBoard(yourPlayerId, updateUI.getPlayerIds(),
					state.getPieces());
			return;
		}

		if (state.blackWin()) {
			if (updateUI.getYourPlayerId().equals(playerIds.get(0)))
				showWinScene();
			if (!(updateUI.getYourPlayerId().equals(playerIds.get(0))))
				showLoseScene();
			return;
		}

		if (state.whiteWin()) {
			if (!(updateUI.getYourPlayerId().equals(playerIds.get(0))))
				showWinScene();
			if (updateUI.getYourPlayerId().equals(playerIds.get(0)))
				showLoseScene();
			return;
		}

		if (state.getPieces().indexOf('0') == -1) {
			showTieScene();
			return;
		}

		updateGameBoard(yourPlayerId, state.getPlayerIds(), state.getPieces());

		if (!(yourPlayerId.equals(state.getCurrentPlayer())))
			// showPickAndMakeMoveScene(yourPlayerId, state.get);
			// else
			disableUiAndWatch();

		if (yourPlayerId.equals(state.getCurrentPlayer()))
			if (yourPlayerId.equals(Ordering.<String> natural().min(
					updateUI.getPlayerIds())))
				enableUi("black");
			else
				enableUi("white");
	}

	public void makeMove(int row, int col) {
		container.sendMakeMove(gameLogic.getMoveOperations(row, col, playerIds,
				yourPlayerId, state.getPieces()));
	}

	public void tryCurrentPieceWithPosition(int row, int col) {
		view.cleanTryBlock();
		if (state.getPieces().charAt(row * (row + 1) / 2 + col) == '0')
			view.updateTryBlock(row, col);
	}

	public void outTryPieceWithPosition(int row, int col) {
		view.cleanTryBlock();
		if (state.getPieces().charAt(row * (row + 1) / 2 + col) == '0')
			view.outTryBlock(row, col);
	}

	private void sendInitialMove(List<String> playerIds) {
		container.sendMakeMove(gameLogic.getInitialOperations(playerIds));
	}

	private void showLoseScene() {
		view.lose();
	}

	private void showWinScene() {
		view.win();
	}

	private void showTieScene() {
		view.tie();
	}

	private void disableUiAndWatch() {
		view.setViewerState(ViewState.VIEW_ONLY, null);
	}

	private void enableUi(String color) {
		view.setViewerState(ViewState.MAKE_MOVE, color);
	}

	private void updateGameBoard(String yourPlayerId, List<String> playerIds,
			String pieces) {
		view.setGameBoard(yourPlayerId, playerIds, pieces);
	}

}
