package com.tehmou.book.androidtictactoe;

import com.tehmou.book.androidtictactoe.pojo.GameGrid;
import com.tehmou.book.androidtictactoe.pojo.GameState;
import com.tehmou.book.androidtictactoe.pojo.GameStatus;
import com.tehmou.book.androidtictactoe.pojo.GameSymbol;
import com.tehmou.book.androidtictactoe.pojo.GridPosition;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class GameUtilsTest {
    final GameState EMPTY_GAME_STATE;

    BehaviorSubject<GameState> gameStateMock;
    BehaviorSubject<GameStatus> gameStatusMock;
    BehaviorSubject<GameSymbol> playerInTurnMock;
    PublishSubject<GridPosition> touchEventMock;
    Observable<GameState> gameStateObservable;
    TestObserver<GameState> testObserver;

    public GameUtilsTest() {
        GameGrid gameGrid = new GameGrid(3, 3);
        GameSymbol gameSymbol = GameSymbol.EMPTY;
        EMPTY_GAME_STATE = new GameState(
                gameGrid, gameSymbol
        );
    }

    @Before
    public void setup() {
        gameStateMock = BehaviorSubject.create();
        gameStatusMock = BehaviorSubject.createDefault(GameStatus.ongoing());
        playerInTurnMock = BehaviorSubject.createDefault(GameSymbol.BLACK);
        touchEventMock = PublishSubject.create();
        gameStateObservable = GameUtils.processGamesMoves(
                gameStateMock, gameStatusMock, playerInTurnMock, touchEventMock
        );
        testObserver = new TestObserver<>();
    }

    @Test
    public void processGamesMoves_testMarkerDrop() {
        // Assemble
        gameStateMock.onNext(EMPTY_GAME_STATE);
        gameStateObservable.subscribe(testObserver);

        // Act
        touchEventMock.onNext(new GridPosition(0, 0));

        // Assert
        testObserver.assertNotTerminated();
        testObserver.assertValue(
                EMPTY_GAME_STATE.setSymbolAt(new GridPosition(0, 2), GameSymbol.BLACK)
        );
    }

    @Test
    public void processGamesMoves_testMarkerDropOnFullColumn() {
        // Assemble
        gameStateMock.onNext(
                EMPTY_GAME_STATE
                        .setSymbolAt(new GridPosition(1, 0), GameSymbol.BLACK)
                        .setSymbolAt(new GridPosition(1, 1), GameSymbol.BLACK)
                        .setSymbolAt(new GridPosition(1, 2), GameSymbol.BLACK)
        );
        gameStateObservable.subscribe(testObserver);

        // Act
        touchEventMock.onNext(new GridPosition(1, 0));

        // Assert
        testObserver.assertNotTerminated();
        testObserver.assertNoValues();
    }
}
