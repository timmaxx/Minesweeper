package timmax.games.minesweeper;

import com.javarush.engine.cell.*;
import timmax.games.minesweeper.gamefield.MinesweeperField;

public class MinesweeperGame extends Game {
    private static final int SIDE_OF_WIDTH = 30;
    private static final int SIDE_OF_HEIGHT = 5;

    private static final int REST_OF_MINE_INSTALLATION_IN_PERCENTS = 10;

    private MinesweeperField minesweeperField;

    @Override
    public void initialize() {
        setScreenSize(SIDE_OF_WIDTH, SIDE_OF_HEIGHT);
        createGame();
    }

    private void createGame() {
        minesweeperField = new MinesweeperField(this, SIDE_OF_WIDTH, SIDE_OF_HEIGHT, REST_OF_MINE_INSTALLATION_IN_PERCENTS);
        minesweeperField.showBegin();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (x < 0 || x >= SIDE_OF_WIDTH || y < 0 || y >= SIDE_OF_HEIGHT) {
            return;
        }
        if (minesweeperField.isGameStopped()) {
            createGame();
        } else {
            minesweeperField.openTile(x, y);
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        if (x < 0 || x >= SIDE_OF_WIDTH || y < 0 || y >= SIDE_OF_HEIGHT) {
            return;
        }
        minesweeperField.markTile(x, y);
    }
}
