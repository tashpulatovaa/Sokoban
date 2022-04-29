public class Area {
    private int x;
    private int y;
    private boolean isInsideBoard;
    private final boolean isBoxSlot;

    public Area(int x, int y, boolean isInsideBoard,boolean isBoxSlot) {
        this.x = x;
        this.y = y;
        this.isInsideBoard = isInsideBoard;
        this.isBoxSlot = isBoxSlot;
    }

    public boolean isInsideBoard() {
        return isInsideBoard;
    }

    public void setInsideBoard(boolean insideBoard) {
        isInsideBoard = insideBoard;
    }

    public boolean isBoxSlot() {
        return isBoxSlot;
    }
}
