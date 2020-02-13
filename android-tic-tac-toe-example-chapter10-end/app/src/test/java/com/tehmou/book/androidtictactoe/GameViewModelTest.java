package com.tehmou.book.androidtictactoe;

import com.tehmou.book.androidtictactoe.pojo.FullGameState;
import com.tehmou.book.androidtictactoe.pojo.GameGrid;
import com.tehmou.book.androidtictactoe.pojo.GameState;
import com.tehmou.book.androidtictactoe.pojo.GameSymbol;
import com.tehmou.book.androidtictactoe.pojo.GridPosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameViewModelTest {
    final GameState EMPTY_GAME_STATE;

    GameViewModel gameViewModel;
    BehaviorSubject<GameState> gameStateMock;
    Consumer<GameState> putActiveGameStateMock;
    PublishSubject<GridPosition> touchEventMock;

    public GameViewModelTest() {
        GameGrid gameGrid = new GameGrid(7, 7);
        GameSymbol gameSymbol = GameSymbol.EMPTY;
        EMPTY_GAME_STATE = new GameState(
                gameGrid, gameSymbol
        );
    }

    @Before
    public void setup() {
        gameStateMock = BehaviorSubject.create();
        putActiveGameStateMock = Mockito.mock(Consumer.class);
        touchEventMock = PublishSubject.create();
        gameViewModel = new GameViewModel(
                gameStateMock,
                putActiveGameStateMock,
                touchEventMock
        );
    }

    @Test
    public void testInitialState() {
        // Assemble
        TestObserver<FullGameState> testObserver = new TestObserver<>();
        gameViewModel.getFullGameState().subscribe(testObserver);

        // Act
        gameViewModel.subscribe();
        gameStateMock.onNext(EMPTY_GAME_STATE);

        // Assert

        // Check we don't create a cyclic loop of putting back what we received
        try {
            verify(putActiveGameStateMock, never()).accept(any());
        } catch (Exception e) {
            fail();
        }

        // Check that we receive the FullGameState once
        testObserver.assertValueCount(1);
        FullGameState fullGameState = testObserver.values().get(0);
        assertFalse(fullGameState.getGameStatus().isEnded());
        assertEquals(EMPTY_GAME_STATE, fullGameState.getGameState());
    }
}