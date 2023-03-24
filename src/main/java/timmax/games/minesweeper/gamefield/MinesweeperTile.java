package timmax.games.minesweeper.gamefield;

public class MinesweeperTile {
    private boolean isMine;
    private int countMineNeighbors;
    private boolean isOpen;
    private boolean isFlag;

    public MinesweeperTile() {
        isOpen = false;
        isFlag = false;
    }

    void setMine() {
        isMine = true;
    }

    public boolean isMine() {
        return isMine;
    }

    void incCountMineNeighbors() {
        this.countMineNeighbors++;
    }

    public int getCountMineNeighbors() {
        return countMineNeighbors;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        isOpen = true;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean flag) {
        isFlag = flag;
    }

}
