package timmax.games.minesweeper.gamefield;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.*;
import java.util.stream.Collectors;

public class GameField {
    private final int sideOfWidth;// = 10;//30;
    private final int sideOfHeight;// = 10;//5;

    // private int restOfMineInstallationInPercents;// = 10;

    private int countMinesOnField;

    private static final String MINE = "\uD83D\uDCA3";

    private static final String FLAG = "\uD83D\uDEA9";

    private int countFlags;

    private final Tile[][] tiles;

    private boolean isGameStopped;

    private int countClosedTiles;// = SIDE_OF_WIDTH * SIDE_OF_HEIGHT;

    private int score;

    private static final Random random = new Random();


    private final Game game;

    public GameField(Game game, int sideOfWidth, int sideOfHeight, int restOfMineInstallationInPercents) {
        this.game = game;
        this.sideOfWidth = sideOfWidth;
        this.sideOfHeight = sideOfHeight;
        // this.REST_OF_MINE_INSTALLATION_IN_PERCENTS = REST_OF_MINE_INSTALLATION_IN_PERCENTS;
        score = 0;

        tiles = new Tile[sideOfHeight][sideOfWidth];
        for (int y = 0; y < sideOfHeight; y++) {
            for (int x = 0; x < sideOfWidth; x++) {
                tiles[y][x] = new Tile();
            }
        }

        countClosedTiles = sideOfWidth * sideOfHeight;

        do {
            int x = random.nextInt(sideOfWidth);
            int y = random.nextInt(sideOfHeight);
            if (!tiles[y][x].isMine()) {
                tiles[y][x].setMine();
                countMinesOnField++;
            }
        } while (countMinesOnField < sideOfHeight * sideOfWidth * restOfMineInstallationInPercents / 100 );

/*
        tiles[0][0].setMine();
        tiles[2][2].setMine();
        countMinesOnField = 2;
*/
        countFlags = countMinesOnField;
        countMineNeighbors();
    }

    public Tile getTileByXY(int x, int y) {
        return tiles[y][x];
    }

    public boolean isGameStopped() {
        return isGameStopped;
    }

    private void countMineNeighbors() {
        for (int y = 0; y < sideOfHeight; y++) {
            for (int x = 0; x < sideOfWidth; x++) {
                Tile tile = getTileByXY(x, y);
                for (Tile neighborTile : getTileNeighbors(x, y)) {
                    if ( neighborTile.isMine()) {
                        tile.incCountMineNeighbors();
                    }
                }
            }
        }
    }

    private List<Tile> getTileNeighbors(int xx, int yy) {
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
        Tile tile = getTileByXY(x, y);
        if (tile.isOpen() || tile.isFlag() || isGameStopped) {
            return;
        }
        tile.open();
        countClosedTiles--;
        game.setCellColor(x, y, Color.GREEN);
        if (tile.isMine()) {
            game.setCellValueEx(x, y, Color.RED, MINE);
            gameOver();
        } else {
            score += 5;
            game.setScore(score);
            game.setCellNumber(x, y, tile.getCountMineNeighbors());
            if (tile.getCountMineNeighbors() == 0) {
                for (XY xy : getXYNeighbors(x, y)) {
                    Tile neighborTile = getTileByXY(xy.getX(), xy.getY());
                    if (!neighborTile.isMine() && !neighborTile.isOpen()) {
                        openTile(xy.getX(), xy.getY());
                    }
                }
            }
        }
        if ( countClosedTiles == countMinesOnField && !tile.isMine()) {
            win();
        }
    }

    public void markTile(int x, int y) {
        Tile tile = getTileByXY(x, y);
        if (        tile.isOpen()
                ||  ( countFlags == 0 && !tile.isFlag())
                ||  isGameStopped) {
            return;
        }
        if (!tile.isFlag()) {
            tile.setFlag(true);
            countFlags--;
            game.setCellValue(x, y, FLAG);
            game.setCellColor(x, y, Color.YELLOW);
        } else {
            tile.setFlag(false);
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
