package org.ygame.presenters;

import java.util.List;

import org.game_api.GameApi.Container;
import org.game_api.GameApi.Operation;
import org.game_api.GameApi.Set;
import org.game_api.GameApi.SetTurn;
import org.game_api.GameApi.UpdateUI;
import org.ygame.client.shared.YGameLogic;
import org.ygame.client.shared.YState;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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

		public void showSign(String s);

		public void showSign2(String s);
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
				&& yourPlayerId.equals(Ordering.<String> natural().max(
						updateUI.getPlayerIds()))) {
			// && !yourPlayerId.equals("0")){
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

		if (updateUI.isViewer()) {
			return;
		}
		// if (updateUI.isAiPlayer()) {
		// view.showSign2("2 AI");
		// }
		// if (!updateUI.isAiPlayer()) {
		// view.showSign2("2 not AI");
		// }
		boolean in = false;
		int one = 0, two = 0;
		for (int i = 0; i < state.getPieces().length(); i++) {
			if (state.getPieces().charAt(i) == '1')
				one++;
			if (state.getPieces().charAt(i) == '2')
				two++;
		}

		if (updateUI.isAiPlayer() && one > two) {
			// TODO: implement AI in a later HW!
			// container.sendMakeMove();
			in = true;
			if (state.getPieces().charAt(0) == '0')
				makeMove(0, 0);
			else if (state.getPieces().charAt(getIndex(9, 0)) == '2'
					&& state.getPieces().charAt(getIndex(9, 0)) == '0')
				makeMove(9, 0);
			else {
				int a = 0, b = 1;
				for (int i = 1; i < 10; i++) {
					if (state.getPieces().charAt(getIndex(i, a)) == '2')
						continue;
					else if (state.getPieces().charAt(getIndex(i, a)) == '0') {
						makeMove(i, a);
						break;
					} else if (state.getPieces().charAt(getIndex(i, b)) == '2') {
						a++;
						b++;
						continue;
					} else if (state.getPieces().charAt(getIndex(i, b)) == '0') {
						makeMove(i, b);
						break;
					} else {
						for (int j = 0; j < state.getPieces().length(); j++) {
							if (state.getPieces().charAt(j) == '0'){
								makeMove(getRowFrom(j), getColFrom(j));
								break;
							}
							
						}
						break;
					}
				}
				return;
			}
			List<Operation> operations = Lists.newArrayList();
			if (state.getPieces().charAt(0) == '0') {
				operations.add(new SetTurn("42"));
				operations.add(new Set(KEYPIECES, state.getPieces().substring(
						0, 0)
						+ '2' + state.getPieces().substring(0 + 1)));
			} else {
				int a = 0, b = 1;

				for (int i = 1; i < 10; i++) {
					if (state.getPieces().charAt(getIndex(i, a)) == '0') {
						operations.add(new SetTurn("42"));
						operations.add(new Set(KEYPIECES, state.getPieces()
								.substring(0, getIndex(i, a))
								+ '2'
								+ state.getPieces().substring(
										getIndex(i, a) + 1)));
						break;
					}
				}
			}
			container.sendMakeMove(operations);
			return;
		}
		// if (in)
		// view.showSign2("88888888888888888888");

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
			// if (yourPlayerId.equals(Ordering.<String> natural().max(
			// updateUI.getPlayerIds())))
			if (yourPlayerId.equals("42"))
				enableUi("black");
			else
				enableUi("white");

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
