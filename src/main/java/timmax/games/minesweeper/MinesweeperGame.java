package timmax.games.minesweeper;

import com.javarush.engine.cell.*;

public class MinesweeperGame extends Game {
    private static final int SIDE_OF_WIDTH = 30;
    private static final int SIDE_OF_HEIGHT = 5;
    @Override
    public void initialize() {
        setScreenSize(SIDE_OF_WIDTH, SIDE_OF_HEIGHT);
    }
}
