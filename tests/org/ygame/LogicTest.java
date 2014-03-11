package org.ygame;

import org.ygame.client.shared.GameApi.Operation;
import org.ygame.client.shared.GameApi.VerifyMove;
import org.ygame.client.shared.GameApi.VerifyMoveDone;
import org.ygame.client.shared.YGameLogic;
import org.ygame.client.shared.YState;
import org.ygame.client.shared.GameApi.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@RunWith(JUnit4.class)
public class LogicTest {
	public YGameLogic yGameLogic = new YGameLogic();

	public String blackWin = "0210200211012000122011120222100200001000200111000020000";
	private String whiteWin = "0210200111012000122001120222100200001000200111000020000";
	private String blackCornerWin = "2212000211012000122001120220100200001000201111000020000";
	private String whiteCornerNotWin = "2212100210012000122001120220100200001000201111000020000";
	private String blackCornerNotWin = "2212100220012000122001120220100200001000201111000010000";
	private String whiteLegalMoveBoard = "0000200110000000000000000000000000000000000000000000000";
	private String whiteLegalMoveLastBoard = "0000200010000000000000000000000000000000000000000000000";
	private String blackLegalMoveBoard = "2000200110000000000000000000000000000000000000000000000";
	private String blackLegalMoveLastBoard = "0000200110000000000000000000000000000000000000000000000";
	private String blackIllegalMoveBoard = "2100200110000000000000000000000000000000000000000000000";
	private String blackIllegalMoveLastBoard = "2100200110000000000000000000000000000000000000000000000";
	private String blackIllegalMoveBoard2 = "2200200110000000000000000000000000000000000000000000000";
	private String blackIllegalMoveLastBoard2 = "2100200110000000000000000000000000000000000000000000000";
	private String whiteIllegalMoveBoard = "2200200110100000000000000000000000000000000000000000000";
	private String whiteIllegalMoveLastBoard = "2200200110100000000000000000000000000000000000000000000";

	public int blackWinRow = 3;
	public int blackWinCol = 1;
	private int size = 10;

	private int whiteWinRow = 3;
	private int whiteWinCol = 1;

	private int blackJustWinRow = 1;
	private int blackJustWinCol = 0;

	private int whiteJustWinRow = 3;
	private int whiteJustWinCol = 3;

	private void assertMoveOk(VerifyMove verifyMove) throws Exception {
		yGameLogic.checkMoveIsLegal(verifyMove);
	}

	private void assertHacker(VerifyMove verifyMove) {
		VerifyMoveDone verifyDone = yGameLogic.verify(verifyMove);
		assertEquals(verifyMove.getLastMovePlayerId(),
				verifyDone.getHackerPlayerId());
	}

	private ImmutableList<Integer> playerIds = ImmutableList.of(42, 43);

	@Test
	public void testBlackWin() {
		YState state = new YState(playerIds.get(0), playerIds, blackWin);
		assertEquals(true, state.blackWin());
	}

	@Test
	public void testWhiteWin() {
		YState state = new YState(playerIds.get(1), playerIds, whiteWin);
		assertEquals(true, state.whiteWin());
	}

	@Test
	public void testBlackNotWin() {
		YState state = new YState(playerIds.get(0), playerIds, blackWin);
		assertEquals(true, state.blackWin());
	}

	@Test
	public void testWhiteNotWin() {
		YState state = new YState(playerIds.get(1), playerIds, blackWin);
		assertEquals(true, state.whiteWin());
	}

	@Test
	public void testBlackJustWin() {
		YState state = new YState(playerIds.get(0), playerIds, blackWin);
		assertEquals(true, state.blackWin());
	}

	@Test
	public void testWhiteJustWin() {
		YState state = new YState(playerIds.get(1), playerIds, blackWin);
		assertEquals(true, state.whiteWin());
	}

	@Test
	public void testWhiteCornerWin() {
		YState state = new YState(playerIds.get(1), playerIds, blackWin);
		assertEquals(true, state.whiteWin());
	}

	@Test
	public void testBlackCornerWin() {
		YState state = new YState(playerIds.get(0), playerIds, blackWin);
		assertEquals(true, state.blackWin());
	}

	@Test
	public void testWhiteCornerNotWin() {
		YState state = new YState(playerIds.get(1), playerIds,
				whiteCornerNotWin);
		assertEquals(true, state.whiteWin());
	}

	@Test
	public void testBlackCornerNotWin() {
		YState state = new YState(playerIds.get(0), playerIds, blackWin);
		assertEquals(true, state.blackWin());
	}

	String PLAYERID = "PLAYERID";

	private final String TURN = "TURN";
	private final String PIECES = "PIECES";
	private final String SIZE = "SIZE";
	private final String LASTROW = "LASTROW";
	private final String LASTCOL = "LASTCOL";

	private final String SETTURN = "SETTURN";
	private final String SETROW = "SETROW";
	private final String SETCOL = "SETCOL";

	@Test
	public void testWhiteLegalMove() {
		List<Map<String, Object>> playersInfo = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> playerWhiteInfo = new HashMap<String, Object>();
		HashMap<String, Object> playerBlackInfo = new HashMap<String, Object>();
		int playerWhiteId = 1, playerBlackId = 2;
		playerWhiteInfo.put(PLAYERID, playerWhiteId);
		playerBlackInfo.put(PLAYERID, playerBlackId);
		playersInfo.add(playerWhiteInfo);
		playersInfo.add(playerBlackInfo);

		HashMap<String, Object> state = new HashMap<String, Object>();
		state.put(TURN, playerBlackId);
		state.put(PIECES, whiteLegalMoveBoard);
		state.put(SIZE, 10);
		state.put(LASTROW, 3);
		state.put(LASTCOL, 1);
		HashMap<String, Object> lastState = new HashMap<String, Object>();
		lastState.put(TURN, playerWhiteId);
		lastState.put(PIECES, whiteLegalMoveLastBoard);
		lastState.put(SIZE, 10);
		lastState.put(LASTROW, 2);
		lastState.put(LASTCOL, 1);
		List<Operation> lastMove = Lists.newArrayList();
		lastMove.add(new Set(SETTURN, playerWhiteId));
		lastMove.add(new Set(SETROW, 3));
		lastMove.add(new Set(SETCOL, 1));
		int lastMovePlayerId = playerWhiteId;
		VerifyMove verifyMove = new VerifyMove(playersInfo, state, lastState,
				lastMove, lastMovePlayerId, null);
		VerifyMoveDone verifyDone = yGameLogic.verify(verifyMove);
		assertEquals(verifyDone.getHackerPlayerId(), 1);
	}

	@Test
	public void testBlackLegalMove() {
		List<Map<String, Object>> playersInfo = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> playerWhiteInfo = new HashMap<String, Object>();
		HashMap<String, Object> playerBlackInfo = new HashMap<String, Object>();
		int playerWhiteId = 1, playerBlackId = 2;
		playerWhiteInfo.put(PLAYERID, playerWhiteId);
		playerBlackInfo.put(PLAYERID, playerBlackId);
		playersInfo.add(playerWhiteInfo);
		playersInfo.add(playerBlackInfo);

		HashMap<String, Object> state = new HashMap<String, Object>();
		state.put(TURN, playerWhiteId);
		state.put(PIECES, blackLegalMoveBoard);
		state.put(SIZE, 10);
		state.put(LASTROW, 0);
		state.put(LASTCOL, 0);
		HashMap<String, Object> lastState = new HashMap<String, Object>();
		lastState.put(TURN, playerBlackId);
		lastState.put(PIECES, blackLegalMoveLastBoard);
		lastState.put(SIZE, 10);
		lastState.put(LASTROW, 3);
		lastState.put(LASTCOL, 1);
		List<Operation> lastMove = Lists.newArrayList();
		lastMove.add(new Set(SETTURN, playerBlackId));
		lastMove.add(new Set(SETROW, 0));
		lastMove.add(new Set(SETCOL, 0));
		int lastMovePlayerId = playerBlackId;
		VerifyMove verifyMove = new VerifyMove(playersInfo, state, lastState,
				lastMove, lastMovePlayerId, null);
		VerifyMoveDone verifyDone = yGameLogic.verify(verifyMove);
		assertEquals(verifyDone.getHackerPlayerId(), 2);
	}

	@Test
	public void testBlackIllegalMove() {
		List<Map<String, Object>> playersInfo = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> playerWhiteInfo = new HashMap<String, Object>();
		HashMap<String, Object> playerBlackInfo = new HashMap<String, Object>();
		int playerWhiteId = 1, playerBlackId = 2;
		playerWhiteInfo.put(PLAYERID, playerWhiteId);
		playerBlackInfo.put(PLAYERID, playerBlackId);
		playersInfo.add(playerWhiteInfo);
		playersInfo.add(playerBlackInfo);

		HashMap<String, Object> state = new HashMap<String, Object>();
		state.put(TURN, playerWhiteId);
		state.put(PIECES, blackIllegalMoveBoard);
		state.put(SIZE, 10);
		state.put(LASTROW, 0);
		state.put(LASTCOL, 0);
		HashMap<String, Object> lastState = new HashMap<String, Object>();
		lastState.put(TURN, playerBlackId);
		lastState.put(PIECES, blackIllegalMoveLastBoard);
		lastState.put(SIZE, 10);
		lastState.put(LASTROW, 1);
		lastState.put(LASTCOL, 1);
		List<Operation> lastMove = Lists.newArrayList();
		lastMove.add(new Set(SETTURN, playerBlackId));
		lastMove.add(new Set(SETROW, 0));
		lastMove.add(new Set(SETCOL, 0));
		int lastMovePlayerId = playerBlackId;
		VerifyMove verifyMove = new VerifyMove(playersInfo, state, lastState,
				lastMove, lastMovePlayerId, null);
		VerifyMoveDone verifyDone = yGameLogic.verify(verifyMove);
		assertNotEquals(verifyDone.getHackerPlayerId(), 0);
	}

	@Test
	public void testBlackIllegalMove2() {
		List<Map<String, Object>> playersInfo = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> playerWhiteInfo = new HashMap<String, Object>();
		HashMap<String, Object> playerBlackInfo = new HashMap<String, Object>();
		int playerWhiteId = 1, playerBlackId = 2;
		playerWhiteInfo.put(PLAYERID, playerWhiteId);
		playerBlackInfo.put(PLAYERID, playerBlackId);
		playersInfo.add(playerWhiteInfo);
		playersInfo.add(playerBlackInfo);

		HashMap<String, Object> state = new HashMap<String, Object>();
		state.put(TURN, playerWhiteId);
		state.put(PIECES, blackIllegalMoveBoard2);
		state.put(SIZE, 10);
		state.put(LASTROW, 0);
		state.put(LASTCOL, 0);
		HashMap<String, Object> lastState = new HashMap<String, Object>();
		lastState.put(TURN, playerBlackId);
		lastState.put(PIECES, blackIllegalMoveLastBoard2);
		lastState.put(SIZE, 10);
		lastState.put(LASTROW, 1);
		lastState.put(LASTCOL, 1);
		List<Operation> lastMove = Lists.newArrayList();
		lastMove.add(new Set(SETTURN, playerBlackId));
		lastMove.add(new Set(SETROW, 0));
		lastMove.add(new Set(SETCOL, 0));
		int lastMovePlayerId = playerBlackId;
		VerifyMove verifyMove = new VerifyMove(playersInfo, state, lastState,
				lastMove, lastMovePlayerId, null);
		VerifyMoveDone verifyDone = yGameLogic.verify(verifyMove);
		assertNotEquals(verifyDone.getHackerPlayerId(), 0);
	}

	@Test
	public void testBlackHacker() {
		List<Map<String, Object>> playersInfo = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> playerWhiteInfo = new HashMap<String, Object>();
		HashMap<String, Object> playerBlackInfo = new HashMap<String, Object>();
		int playerWhiteId = 1, playerBlackId = 2;
		playerWhiteInfo.put(PLAYERID, playerWhiteId);
		playerBlackInfo.put(PLAYERID, playerBlackId);
		playersInfo.add(playerWhiteInfo);
		playersInfo.add(playerBlackInfo);

		HashMap<String, Object> state = new HashMap<String, Object>();
		state.put(TURN, playerWhiteId);
		state.put(PIECES, blackIllegalMoveBoard2);
		state.put(SIZE, 10);
		state.put(LASTROW, 0);
		state.put(LASTCOL, 0);
		HashMap<String, Object> lastState = new HashMap<String, Object>();
		lastState.put(TURN, playerBlackId);
		lastState.put(PIECES, blackIllegalMoveLastBoard2);
		lastState.put(SIZE, 10);
		lastState.put(LASTROW, 1);
		lastState.put(LASTCOL, 1);
		List<Operation> lastMove = Lists.newArrayList();
		lastMove.add(new Set(SETTURN, playerBlackId));
		lastMove.add(new Set(SETROW, 0));
		lastMove.add(new Set(SETCOL, 0));
		int lastMovePlayerId = playerBlackId;
		VerifyMove verifyMove = new VerifyMove(playersInfo, state, lastState,
				lastMove, lastMovePlayerId, null);
		VerifyMoveDone verifyDone = yGameLogic.verify(verifyMove);
		assertEquals(verifyDone.getHackerPlayerId(), playerBlackId);
	}

	@Test
	public void testWhiteHacker() {
		List<Map<String, Object>> playersInfo = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> playerWhiteInfo = new HashMap<String, Object>();
		HashMap<String, Object> playerBlackInfo = new HashMap<String, Object>();
		int playerWhiteId = 1, playerBlackId = 2;
		playerWhiteInfo.put(PLAYERID, playerWhiteId);
		playerBlackInfo.put(PLAYERID, playerBlackId);
		playersInfo.add(playerWhiteInfo);
		playersInfo.add(playerBlackInfo);

		HashMap<String, Object> state = new HashMap<String, Object>();
		state.put(TURN, playerBlackId);
		state.put(PIECES, whiteIllegalMoveBoard);
		state.put(SIZE, 10);
		state.put(LASTROW, 0);
		state.put(LASTCOL, 0);
		HashMap<String, Object> lastState = new HashMap<String, Object>();
		lastState.put(TURN, playerWhiteId);
		lastState.put(PIECES, whiteIllegalMoveLastBoard);
		lastState.put(SIZE, 10);
		lastState.put(LASTROW, 1);
		lastState.put(LASTCOL, 1);
		List<Operation> lastMove = Lists.newArrayList();
		lastMove.add(new Set(SETTURN, playerWhiteId));
		lastMove.add(new Set(SETROW, 0));
		lastMove.add(new Set(SETCOL, 0));
		int lastMovePlayerId = playerWhiteId;
		VerifyMove verifyMove = new VerifyMove(playersInfo, state, lastState,
				lastMove, lastMovePlayerId, null);
		VerifyMoveDone verifyDone = yGameLogic.verify(verifyMove);
		assertEquals(verifyDone.getHackerPlayerId(), playerWhiteId);
	}

	@Test
	public void testBlackHacker2() {
		List<Map<String, Object>> playersInfo = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> playerWhiteInfo = new HashMap<String, Object>();
		HashMap<String, Object> playerBlackInfo = new HashMap<String, Object>();
		int playerWhiteId = 1, playerBlackId = 2;
		playerWhiteInfo.put(PLAYERID, playerWhiteId);
		playerBlackInfo.put(PLAYERID, playerBlackId);
		playersInfo.add(playerWhiteInfo);
		playersInfo.add(playerBlackInfo);

		HashMap<String, Object> state = new HashMap<String, Object>();
		state.put(TURN, playerWhiteId);
		state.put(PIECES, whiteIllegalMoveBoard);
		state.put(SIZE, 10);
		state.put(LASTROW, 0);
		state.put(LASTCOL, 0);
		HashMap<String, Object> lastState = new HashMap<String, Object>();
		lastState.put(TURN, playerBlackId);
		lastState.put(PIECES, whiteIllegalMoveLastBoard);
		lastState.put(SIZE, 10);
		lastState.put(LASTROW, 1);
		lastState.put(LASTCOL, 1);
		List<Operation> lastMove = Lists.newArrayList();
		lastMove.add(new Set(SETTURN, playerBlackId));
		lastMove.add(new Set(SETROW, 0));
		lastMove.add(new Set(SETCOL, 0));
		int lastMovePlayerId = playerBlackId;
		VerifyMove verifyMove = new VerifyMove(playersInfo, state, lastState,
				lastMove, lastMovePlayerId, null);
		VerifyMoveDone verifyDone = yGameLogic.verify(verifyMove);
		assertEquals(verifyDone.getHackerPlayerId(), playerBlackId);
	}

}