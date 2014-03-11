package org.ygame.client.shared;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.ygame.client.shared.GameApi.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class YGameLogic {
	private final String KEYPIECES = "KEYPIECES";
	private final String KEYPLAYERID = "KEYPLAYERID";
	private final String KEYPLAYERS = "KEYPLAYERS";

	public VerifyMoveDone verify(VerifyMove verifyMove) {
		try {
			checkMoveIsLegal(verifyMove);
			return new VerifyMoveDone();
		} catch (Exception e) {
			return new VerifyMoveDone(verifyMove.getLastMovePlayerId(),
					e.getMessage());
		}
	}

	public void checkMoveIsLegal(VerifyMove verifyMove) throws Exception {
		// Check the operations are as expected
		Map<String, Object> currentStateMap = verifyMove.getState();
		Map<String, Object> lastStateMap = verifyMove.getLastState();
		List<Operation> lastMove = verifyMove.getLastMove();
		List<Integer> playerIds = verifyMove.getPlayerIds();
		int lastPlayerId = verifyMove.getLastMovePlayerId();

		// if last state is empty, no need to check
		if (lastStateMap.isEmpty() && currentStateMap.isEmpty()) {
			return;
		}

		String lastPieces = (String) lastStateMap.get(KEYPIECES);
		int currentPlayerId = (Integer) currentStateMap.get(KEYPLAYERID);
		String currentPieces = (String) currentStateMap.get(KEYPIECES);

		if (lastStateMap.isEmpty() && !currentStateMap.isEmpty())
			return;

		System.out.println("lastState:");
		int k = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j <= 10 - i; j++)
				System.out.print(" ");
			for (int j = 0; j <= i; j++)
				System.out.print("" + lastPieces.charAt(k++) + " ");
			System.out.println();
		}
		System.out.println("lastPlayerId:" + lastPlayerId);
		System.out.println("currentState:");
		k = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j <= 10 - i; j++)
				System.out.print(" ");
			for (int j = 0; j <= i; j++)
				System.out.print("" + currentPieces.charAt(k++) + " ");
			System.out.println();
		}
		System.out.println("currentPlayerId:" + currentPlayerId);

		for (Operation op : lastMove) {
			if (op instanceof SetTurn) {
				// Set player Id
				check(currentPlayerId == ((SetTurn) op).getPlayerId());
			} else if (op instanceof Set) {
				// Set the board pieces
				if (((Set) op).getKey().equals(KEYPIECES))
					check(currentPieces.equals(((Set) op).getValue()));
				else if (((Set) op).getKey().equals(KEYPLAYERID)){
					int setValue = (Integer)((Set) op).getValue();
					check(currentPlayerId == setValue);
				}
				else if (((Set) op).getKey().equals(KEYPLAYERS)){
					List<Integer> setValue = (List<Integer>) ((Set) op).getValue();
					check(setValue.get(0) == playerIds.get(0));
					check(setValue.get(1) == playerIds.get(1));
				}
				else
					throw new Exception("Hacker!");
			} else if (op instanceof EndGame) {
				// game end
			} else
				throw new Exception("Hacker!");
		}
	}

	public boolean checkIfWhiteWin(String pieces, int row, int col) {
		initCheck(pieces);
		checkWHelper(row, col);
		return leftConnected && rightConnected && downConnected;
	}

	private boolean leftConnected;
	private boolean rightConnected;
	private boolean downConnected;
	HashSet<String> traversed;
	boolean found;
	private String pieces;

	private void initCheck(String pieces) {
		leftConnected = false;
		rightConnected = false;
		downConnected = false;
		traversed = new HashSet<String>();
		found = false;
		this.pieces = pieces;
	}

	private void checkWHelper(int row, int col) {
		if (found)
			return;
		traversed.add("" + row + "" + col);
		if (col == 0)
			leftConnected = true;
		if (col == row)
			rightConnected = true;
		if (row == 9)
			downConnected = true;
		if (leftConnected && rightConnected && downConnected) {
			found = true;
			return;
		}
		HashMap<String, Integer> neighbors = new HashMap<String, Integer>();
		if (row > 0 && row != col)
			neighbors.put("" + (row - 1) + ("" + col),
					pieces.charAt(getIndex(row - 1, col)) - '0');
		if (col > 0) {
			neighbors.put("" + (row - 1) + "" + (col - 1),
					pieces.charAt(getIndex(row - 1, col - 1)) - '0');
			neighbors.put("" + row + "" + (col - 1),
					pieces.charAt(getIndex(row, col - 1)) - '0');
		}
		if (row < 9) {
			neighbors.put("" + (row + 1) + "" + col,
					pieces.charAt(getIndex(row + 1, col)) - '0');
			neighbors.put("" + (row + 1) + "" + (col + 1),
					pieces.charAt(getIndex(row + 1, col + 1)) - '0');
		}
		if (col != row) {
			neighbors.put("" + row + "" + (col + 1),
					pieces.charAt(getIndex(row, col + 1)) - '0');
		}
		for (String neighbor : neighbors.keySet()) {
			if (neighbors.get(neighbor) == 1 && !traversed.contains(neighbor)) {
				int a = neighbor.charAt(0) - '0';
				int b = neighbor.charAt(1) - '0';
				checkWHelper(a, b);
			}
		}
	}

	private int getIndex(int row, int col) {
		if (row == 0)
			return 0;
		return (1 + row) * row / 2 + col;
	}

	public boolean checkIfBlackWin(String pieces, int row, int col) {
		initCheck(pieces);
		checkBHelper(row, col);
		return leftConnected && rightConnected && downConnected;
	}

	private void checkBHelper(int row, int col) {
		if (found)
			return;
		traversed.add("" + row + "" + col);
		if (col == 0)
			leftConnected = true;
		if (col == row)
			rightConnected = true;
		if (row == 9)
			downConnected = true;
		if (leftConnected && rightConnected && downConnected) {
			found = true;
			return;
		}
		HashMap<String, Integer> neighbors = new HashMap<String, Integer>();
		if (row > 0 && row != col)
			neighbors.put("" + (row - 1) + ("" + col),
					pieces.charAt(getIndex(row - 1, col)) - '0');
		if (col > 0) {
			neighbors.put("" + (row - 1) + "" + (col - 1),
					pieces.charAt(getIndex(row - 1, col - 1)) - '0');
			neighbors.put("" + row + "" + (col - 1),
					pieces.charAt(getIndex(row, col - 1)) - '0');
		}
		if (row < 9) {
			neighbors.put("" + (row + 1) + "" + col,
					pieces.charAt(getIndex(row + 1, col)) - '0');
			neighbors.put("" + (row + 1) + "" + (col + 1),
					pieces.charAt(getIndex(row + 1, col + 1)) - '0');
		}
		if (col != row) {
			neighbors.put("" + row + "" + (col + 1),
					pieces.charAt(getIndex(row, col + 1)) - '0');
		}
		for (String neighbor : neighbors.keySet()) {
			if (neighbors.get(neighbor) == 2 && !traversed.contains(neighbor))
				checkBHelper(neighbor.charAt(0) - '0', neighbor.charAt(1) - '0');
		}
	}

	public List<Operation> getInitialOperations(List<Integer> playerIds) {
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(playerIds.get(0)));
		String initialPieces = "";
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j <= i; j++) {
				initialPieces = initialPieces + "0";
			}
		}
		operations.add(new Set(KEYPIECES, initialPieces));
		operations.add(new Set(KEYPLAYERID, playerIds.get(0)));
		operations.add(new Set(KEYPLAYERS, playerIds));
		return operations;
	}

	public List<Operation> getMoveOperations(int row, int col,
			List<Integer> playerIds, int playerId, String pieces) {
		int index = getIndex(row, col);
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn(playerIds.get(1 - playerIds
				.indexOf(playerId))));
		operations.add(new Set(KEYPIECES, pieces.substring(0, index)
				+ (playerIds.indexOf(playerId) + 1)
				+ pieces.substring(index + 1)));
		operations.add(new Set(KEYPLAYERID, playerIds.get(1 - playerIds
				.indexOf(playerId))));
		operations.add(new Set(KEYPLAYERS, playerIds));
		return operations;
	}

	private void check(boolean val, Object... debugArguments) {
		if (!val) {
			throw new RuntimeException("We have a hacker! debugArguments="
					+ Arrays.toString(debugArguments));
		}
	}
}
