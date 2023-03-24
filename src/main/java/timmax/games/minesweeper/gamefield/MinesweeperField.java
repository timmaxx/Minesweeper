package timmax.games.minesweeper.gamefield;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.*;
import java.util.stream.Collectors;

public class MinesweeperField {
    private final int sideOfWidth;
    private final int sideOfHeight;

    private int countMinesOnField;

    private static final String MINE = "\uD83D\uDCA3";

    private static final String FLAG = "\uD83D\uDEA9";

    private int countFlags;

    private final MinesweeperTile[][] minesweeperTiles;

    private boolean isGameStopped;

    private int countClosedTiles;// = SIDE_OF_WIDTH * SIDE_OF_HEIGHT;

    private int score;

    private static final Random random = new Random();


    private final Game game;

    public MinesweeperField(Game game, int sideOfWidth, int sideOfHeight, int restOfMineInstallationInPercents) {
        this.game = game;
        this.sideOfWidth = sideOfWidth;
        this.sideOfHeight = sideOfHeight;
        score = 0;

        minesweeperTiles = new MinesweeperTile[sideOfHeight][sideOfWidth];
        for (int y = 0; y < sideOfHeight; y++) {
            for (int x = 0; x < sideOfWidth; x++) {
                minesweeperTiles[y][x] = new MinesweeperTile();
            }
        }

        countClosedTiles = sideOfWidth * sideOfHeight;

        do {
            int x = random.nextInt(sideOfWidth);
            int y = random.nextInt(sideOfHeight);
            if (!minesweeperTiles[y][x].isMine()) {
                minesweeperTiles[y][x].setMine();
                countMinesOnField++;
            }
        } while (countMinesOnField < sideOfHeight * sideOfWidth * restOfMineInstallationInPercents / 100 );

        countFlags = countMinesOnField;
        countMineNeighbors();
    }

    public MinesweeperTile getTileByXY(int x, int y) {
        return minesweeperTiles[y][x];
    }

    public boolean isGameStopped() {
        return isGameStopped;
    }

    private void countMineNeighbors() {
        for (int y = 0; y < sideOfHeight; y++) {
            for (int x = 0; x < sideOfWidth; x++) {
                MinesweeperTile minesweeperTile = getTileByXY(x, y);
                for (MinesweeperTile neighborMinesweeperTile : getTileNeighbors(x, y)) {
                    if ( neighborMinesweeperTile.isMine()) {
                        minesweeperTile.incCountMineNeighbors();
                    }
                }
            }
        }
    }

    private List<MinesweeperTile> getTileNeighbors(int xx, int yy) {
        return getXYNeighbors(xx, yy)
                .stream()
                .map(xy -> getTileByXY(xy.getX(), xy.getY()))
                .collect(Collectors.toList());
    }

    private List<XY> getXYNeighbors(int xx, int yy) {
        List<XY> result = new ArrayList<>();
        for (int y = yy - 1; y <= yy + 1; y++) {
            for (int x = xx - 1; x <= xx + 1; x++) {
                if (    (y < 0 || y >= sideOfHeight)
                    ||  ( x < 0 || x >= sideOfWidth)
                    ||  ( x == xx && y == yy)) {
                    continue;
                }
                result.add( new XY(x, y));
            }
        }
        return result;
    }

    public void showBegin() {
        for (int y = 0; y < sideOfHeight; y++) {
            for (int x = 0; x < sideOfWidth; x++) {
                game.setCellColor(x, y, Color.ORANGE);
                game.setCellValue(x, y, "");
            }
        }
        game.setScore(score);
    }

    public void openTile(int x, int y) {
        MinesweeperTile minesweeperTile = getTileByXY(x, y);
        if (minesweeperTile.isOpen() || minesweeperTile.isFlag() || isGameStopped) {
            return;
        }
        minesweeperTile.open();
        countClosedTiles--;
        game.setCellColor(x, y, Color.GREEN);
        if (minesweeperTile.isMine()) {
            game.setCellValueEx(x, y, Color.RED, MINE);
            gameOver();
        } else {
            score += 5;
            game.setScore(score);
            game.setCellNumber(x, y, minesweeperTile.getCountMineNeighbors());
            if (minesweeperTile.getCountMineNeighbors() == 0) {
                for (XY xy : getXYNeighbors(x, y)) {
                    MinesweeperTile neighborMinesweeperTile = getTileByXY(xy.getX(), xy.getY());
                    if (!neighborMinesweeperTile.isMine() && !neighborMinesweeperTile.isOpen()) {
                        openTile(xy.getX(), xy.getY());
                    }
                }
            }
        }
        if ( countClosedTiles == countMinesOnField && !minesweeperTile.isMine()) {
            win();
        }
    }

    public void markTile(int x, int y) {
        MinesweeperTile minesweeperTile = getTileByXY(x, y);
        if (        minesweeperTile.isOpen()
                ||  ( countFlags == 0 && !minesweeperTile.isFlag())
                ||  isGameStopped) {
            return;
        }
        if (!minesweeperTile.isFlag()) {
            minesweeperTile.setFlag(true);
            countFlags--;
            game.setCellValue(x, y, FLAG);
            game.setCellColor(x, y, Color.YELLOW);
        } else {
            minesweeperTile.setFlag(false);
            countFlags++;
            game.setCellValue(x, y, "");
            game.setCellColor(x, y, Color.ORANGE);
        }
    }

    private void gameOver() {
        isGameStopped = true;
        game.showMessageDialog(Color.AQUA, "Game over!", Color.WHITE, 30);
    }

    private void win() {
        isGameStopped = true;
        game.showMessageDialog(Color.AQUA, "Win!", Color.WHITE, 30);
    }
}
