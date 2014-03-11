package org.ygame.presenters;

import java.util.List;

import org.ygame.client.shared.GameApi.Container;
import org.ygame.client.shared.GameApi.UpdateUI;
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

		public void setViewerState(ViewState vs);

		public void updateTryBlock(int row, int col);
		
		public void outTryBlock(int row, int col);

		public void setGameBoard(int yourPlayerId, List<Integer> playerList,
				String pieces);

		public void cleanTryBlock();

		public void lose();

		public void win();

		public void tie();
	}

	/**
	 * presenter driven by logic
	 */
	private final YGameLogic gameLogic = new YGameLogic();

	private final View view;

	private final Container container;

	private YState state;

	private int yourPlayerId;

	private List<Integer> playerIds;

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
				&& yourPlayerId == Ordering.<Integer> natural().min(
						updateUI.getPlayerIds())) {
			System.out.println("Players:" + updateUI.getPlayerIds());
			System.out.println("Your player Id:" + yourPlayerId);
			sendInitialMove(updateUI.getPlayerIds());
			return;
		}

		String mapPieces = (String) updateUI.getState().get(KEYPIECES);
		int mapPlayerId = (Integer) updateUI.getState().get(KEYPLAYERID);
		List<Integer> mapPlayerIds = (List<Integer>) updateUI.getState().get(
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
			if (updateUI.getYourPlayerId() == playerIds.get(0))
				showWinScene();
			if (!(updateUI.getYourPlayerId() == playerIds.get(0)))
				showLoseScene();
			return;
		}

		if (state.whiteWin()) {
			if (!(updateUI.getYourPlayerId() == playerIds.get(0)))
				showWinScene();
			if (updateUI.getYourPlayerId() == playerIds.get(0))
				showLoseScene();
			return;
		}

		if (state.getPieces().indexOf('0') == -1) {
			showTieScene();
			return;
		}

		updateGameBoard(yourPlayerId, state.getPlayerIds(), state.getPieces());

		if (!(yourPlayerId == state.getCurrentPlayer()))
			// showPickAndMakeMoveScene(yourPlayerId, state.get);
			// else
			disableUiAndWatch();
		
		if(yourPlayerId == state.getCurrentPlayer())
			enableUi();
	}

	public void makeMove(int row, int col) {
		container.sendMakeMove(gameLogic.getMoveOperations(row, col, playerIds, yourPlayerId, state.getPieces()));
	}

	public void tryCurrentPieceWithPosition(int row, int col) {
		view.cleanTryBlock();
		if (state.getPieces().charAt(row * (row + 1) / 2 + col) == '0')
			view.updateTryBlock(row, col);
	}
	
	public void outTryPieceWithPosition(int row, int col){
		view.cleanTryBlock();
		if(state.getPieces().charAt(row * (row + 1) / 2 + col) == '0')
			view.outTryBlock(row, col);
	}

	private void sendInitialMove(List<Integer> playerIds) {
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
		view.setViewerState(ViewState.VIEW_ONLY);
	}
	
	private void enableUi()	{
		view.setViewerState(ViewState.MAKE_MOVE);
	}

	private void updateGameBoard(int yourPlayerId, List<Integer> playerIds,
			String pieces) {
		view.setGameBoard(yourPlayerId, playerIds, pieces);
	}

}
