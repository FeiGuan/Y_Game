package org.ygame;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.ygame.client.shared.GameApi.Container;
import org.ygame.client.shared.GameApi.Operation;
import org.ygame.client.shared.GameApi.UpdateUI;
import org.ygame.client.shared.YGameLogic;
import org.ygame.presenters.YPresenter;
import org.ygame.presenters.YPresenter.View;
import org.ygame.client.shared.GameApi.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@RunWith(JUnit4.class)
public class PresenterTest {
	private String USER_ID = "USER_ID";
	private final Map<String, Object> info1 = ImmutableMap.<String, Object> of(
			USER_ID, 1);
	private final Map<String, Object> info2 = ImmutableMap.<String, Object> of(
			USER_ID, 2);
	private final List<Map<String, Object>> playersInfo = ImmutableList.of(
			info1, info2);
	private final ImmutableMap<String, Object> emptyState = ImmutableMap
			.<String, Object> of();

	private YPresenter presenter;
	private View mockView;
	private Container mockContainer;
	private YGameLogic logic;

	@Before
	public void runBefore() {
		mockView = Mockito.mock(View.class);
		mockContainer = Mockito.mock(Container.class);
		presenter = new YPresenter(mockView, mockContainer);
		verify(mockView).setPresenter(presenter);
	}

	@After
	public void runAfter() {
		verifyNoMoreInteractions(mockContainer);
		verifyNoMoreInteractions(mockView);
	}

	@Test
	public void testEmptyStateForA() {
		presenter.updateUI(createUpdateUI(1, 0, emptyState));
		verify(mockContainer).sendMakeMove(
				logic.getInitialOperations(ImmutableList.<Integer> of(1, 2)));
	}

	@Test
	public void testEmptyStateForB() {
		presenter.updateUI(createUpdateUI(2, 0, emptyState));
		verify(mockContainer).sendMakeMove(
				logic.getInitialOperations(ImmutableList.<Integer> of(1, 2)));
	}

	private UpdateUI createUpdateUI(int yourPlayerId, int turnOfPlayerId,
			Map<String, Object> state) {
		return new UpdateUI(yourPlayerId, playersInfo, state, emptyState,
				ImmutableList.<Operation> of(new SetTurn(turnOfPlayerId)), 0,
				ImmutableMap.<Integer, Integer> of());
	}
}
