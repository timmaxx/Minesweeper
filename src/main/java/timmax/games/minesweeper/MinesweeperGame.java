package timmax.games.minesweeper;

import com.javarush.engine.cell.*;
import timmax.games.minesweeper.gamefield.GameField;

public class MinesweeperGame extends Game {
    private static final int SIDE_OF_WIDTH = 30;
    private static final int SIDE_OF_HEIGHT = 5;

    private static final int REST_OF_MINE_INSTALLATION_IN_PERCENTS = 10;

    //private int countClosedTiles = SIDE_OF_WIDTH * SIDE_OF_HEIGHT;


    private GameField gameField;

    @Override
    public void initialize() {
        setScreenSize(SIDE_OF_WIDTH, SIDE_OF_HEIGHT);
        createGame();
    }

    private void createGame() {
        gameField = new GameField(this, SIDE_OF_WIDTH, SIDE_OF_HEIGHT, REST_OF_MINE_INSTALLATION_IN_PERCENTS);
        gameField.showBegin();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (x < 0 || x >= SIDE_OF_WIDTH || y < 0 || y >= SIDE_OF_HEIGHT) {
            return;
        }
        if (gameField.isGameStopped()) {
            createGame();
        } else {
            gameField.openTile(x, y);
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        if (x < 0 || x >= SIDE_OF_WIDTH || y < 0 || y >= SIDE_OF_HEIGHT) {
            return;
        }
        gameField.markTile(x, y);
    }
/*
    private void restart() {
        //isGameStopped = false;
        //countClosedTiles = 0; //
        //score = 0;
        //countMinesOnField = 0; //
        //setScore( score);
        gameField.showBegin();
    }
    */
}
