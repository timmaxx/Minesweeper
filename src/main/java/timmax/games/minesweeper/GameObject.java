package timmax.games.minesweeper;

public class GameObject {
    private final int x;
    private final int y;

    private final boolean isMine;
    private int countMineNeighbors;
    private boolean isOpen;

    public GameObject(int x, int y, boolean isMine) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
        isOpen = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMine() {
        return isMine;
    }

    public void incCountMineNeighbors() {
        this.countMineNeighbors++;
    }

    public int getCountMineNeighbors() {
        return countMineNeighbors;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void close() {
        isOpen = true;
    }
}
