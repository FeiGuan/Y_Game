package org.ygame.client.shared;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class YState {
	private final int playerId;
	private final ImmutableList<Integer> playerIds;
	private String pieces;

	public YState(int playerId, ImmutableList<Integer> playerIds, String pieces) {
		this.playerId = playerId;
		this.playerIds = playerIds;
		this.pieces = pieces;
	}

	public int getCurrentPlayer() {
		return playerId;
	}

	public ImmutableList<Integer> getPlayerIds() {
		return playerIds;
	}

	public String getPieces() {
		return pieces;
	}

	private String checkPieces;
	private boolean leftConnected;
	private boolean rightConnected;
	private boolean downConnected;
	private boolean found;

	public void setPieces(String pieces) {
		this.pieces = pieces;
	}

	public boolean whiteWin() {
		initCheck();
		for (int i = 0; i < checkPieces.length(); i++) {
			if (checkPieces.charAt(i) == '2')
				checkWhiteHelper(i);
			if (found)
				return true;
		}
		return false;
	}

	public boolean blackWin() {
		initCheck();
		for (int i = 0; i < checkPieces.length(); i++) {
			if (checkPieces.charAt(i) == '1')
				checkBlackHelper(i);
			if (found)
				return true;
		}
		return false;
	}

	private void checkBlackHelper(int index) {
		if (checkPieces.charAt(index) != '1')
			return;
		checkPieces = checkPieces.substring(0, index) + '0'
				+ checkPieces.substring(index + 1);
		int row = getRowFrom(index);
		int col = getColFrom(index);
		if (found)
			return;
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
		if (row > 0 && row != col)
			checkBlackHelper(getIndex(row - 1, col));
		if (col > 0) {
			checkBlackHelper(getIndex(row - 1, col - 1));
			checkBlackHelper(getIndex(row, col - 1));
		}
		if (row < 9) {
			checkBlackHelper(getIndex(row + 1, col));
			checkBlackHelper(getIndex(row + 1, col + 1));
		}
		if (col != row) {
			checkBlackHelper(getIndex(row, col + 1));
		}
	}

	private void checkWhiteHelper(int index) {
		if (checkPieces.charAt(index) != '2')
			return;
		checkPieces = checkPieces.substring(0, index) + '0'
				+ checkPieces.substring(index + 1);
		int row = getRowFrom(index);
		int col = getColFrom(index);
		if (found)
			return;
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
		if (row > 0 && row != col)
			checkWhiteHelper(getIndex(row - 1, col));
		if (col > 0) {
			checkWhiteHelper(getIndex(row - 1, col - 1));
			checkWhiteHelper(getIndex(row, col - 1));
		}
		if (row < 9) {
			checkWhiteHelper(getIndex(row + 1, col));
			checkWhiteHelper(getIndex(row + 1, col + 1));
		}
		if (col != row) {
			checkWhiteHelper(getIndex(row, col + 1));
		}
	}

	private void initCheck() {
		leftConnected = false;
		rightConnected = false;
		downConnected = false;
		found = false;
		checkPieces = pieces;
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
}
