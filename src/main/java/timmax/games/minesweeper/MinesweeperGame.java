package timmax.games.minesweeper;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE_OF_WIDTH = 30;
    private static final int SIDE_OF_HEIGHT = 5;

    private static final int REST_OF_MINE_INSTALLATION_IN_PERCENTS = 10;

    private int countMinesOnField;

    private static final String MINE = "\uD83D\uDCA3";


    private final GameObject[][] gameField = new GameObject[SIDE_OF_HEIGHT][SIDE_OF_WIDTH];

    @Override
    public void initialize() {
        setScreenSize(SIDE_OF_WIDTH, SIDE_OF_HEIGHT);
        createGame();
    }

    private void createGame() {
        countMinesOnField = 0;
        for (int y = 0; y < SIDE_OF_HEIGHT; y++) {
            for (int x = 0; x < SIDE_OF_WIDTH; x++) {
                boolean isMine = getRandomNumber( 100) < REST_OF_MINE_INSTALLATION_IN_PERCENTS;
                gameField[y][x] = new GameObject(x, y, isMine);
                if (isMine) {
                    countMinesOnField++;
                }
                setCellColor(x, y, Color.ORANGE);
            }
        }
        countMineNeighbors();
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList< >( );
        for ( int y = gameObject.getY() - 1; y <= gameObject.getY() + 1; y++) {
            for ( int x = gameObject.getX() - 1; x <= gameObject.getX() + 1; x++) {
                if ( y < 0 || y >= SIDE_OF_HEIGHT) {
                    continue;
                }
                if ( x < 0 || x >= SIDE_OF_WIDTH) {
                    continue;
                }
                if ( gameField[ y][ x] == gameObject) {
                    continue;
                }
                result.add( gameField[ y][ x]);
            }
        }
        return result;
    }

    private void countMineNeighbors() {
        for (int y = 0; y < SIDE_OF_HEIGHT; y++) {
            for (int x = 0; x < SIDE_OF_WIDTH; x++) {
                for (GameObject gameObject : getNeighbors(gameField[y][x])) {
                    if (gameObject.isMine()) {
                        gameField[y][x].incCountMineNeighbors();
                    }
                }
            }
        }
    }

    private void openTile(int x, int y) {
        if (gameField[y][x].isMine()) {
            setCellValue(x, y, MINE);
        } else {
            setCellNumber(x, y, gameField[y][x].getCountMineNeighbors());
        }
        gameField[y][x].close();
        setCellColor(x, y, Color.GREEN);
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        openTile(x ,y);
    }
}
