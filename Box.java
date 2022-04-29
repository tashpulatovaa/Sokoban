import java.util.Stack;

public class Box {
    private int x, y;
    private final Board board;
    private final Stack<Integer[]> moveHistory = new Stack<>();
    private final Stack<Boolean> stateHistory = new Stack<>();
    private boolean unmovableToRight;
    private boolean unmovableToLeft;
    private boolean unmovableToUP;
    private boolean unmovableToDown;
    private boolean inGoal;

    public Box(int x, int y, Board board, boolean inGoal) {
        this.x = x;
        this.y = y;
        this.board = board;
        this.inGoal = inGoal;
        moveHistory.push(new Integer[]{x, y});
        stateHistory.push(inGoal);
    }

    void setMoveHistory() {
        moveHistory.push(new Integer[]{this.x, this.y});
        stateHistory.push(inGoal);
    }

    // MOVEMENTS

    void move(int nextX, int nextY) {
        if (isInGoal()) {
            board.setElements(x, y, new Area(x, y, true, true));
            inGoal = false;
        }
        this.x = nextX;
        this.y = nextY;
        if (board.getElements()[y][x] instanceof Area && ((Area) (board.getElements()[y][x])).isBoxSlot()) {
            inGoal = true;
            board.setnOfSucceedBox(board.getnOfSucceedBox() + 1);
        }
        board.setElements(x, y, this);
    }

    void moveRight() {
        int nX = this.x + 1;
        if (board.areCoordsInside(nX, this.y) && !(board.getElements()[this.y][nX] instanceof Wall)
                && !(board.getElements()[this.y][nX] instanceof Box)) {
            move(nX, this.y);
        } else {
            unmovableToRight = true;
        }
    }

    void moveLeft() {
        int nX = this.x - 1;
        if (board.areCoordsInside(nX, this.y) && !(board.getElements()[this.y][nX] instanceof Wall)
                && !(board.getElements()[this.y][nX] instanceof Box)) {
            move(nX, this.y);
        } else {
            unmovableToLeft = true;
        }
    }

    void moveUp() {
        int nY = this.y - 1;
        if (board.areCoordsInside(this.x, nY) && !(board.getElements()[nY][this.x] instanceof Wall)
                && !(board.getElements()[nY][this.x] instanceof Box)) {
            move(this.x, nY);
        } else {
            unmovableToUP = true;
        }
    }

    void moveDown() {
        int nY = this.y + 1;
        if (board.areCoordsInside(this.x, nY) && !(board.getElements()[nY][this.x] instanceof Wall)
                && !(board.getElements()[nY][this.x] instanceof Box)) {
            move(this.x, nY);
        } else {
            unmovableToDown = true;
        }
    }

    // UNDO AND REDO    ->  BUG -> LAST POSITION CHANGES

    boolean first = true;

    void undo() {
        if (!moveHistory.isEmpty()) {
            if (first) {
                moveHistory.remove(moveHistory.size() - 1);
                stateHistory.remove(stateHistory.size() - 1);
                first = false;
            }
            Integer[] lastCoords = moveHistory.pop();
            inGoal = stateHistory.pop();

            if (isInGoal()) {
                board.setElements(x, y, new Area(x, y, true, true));
            } else {
                board.setElements(x, y, new Area(x, y, true, false));
            }
            x = lastCoords[0];
            y = lastCoords[1];
            board.getElements()[y][x] = this;
            board.setElements(x, y, this);
        }
        int counter = moveHistory.size() - 1;
    }

    void redo() {
//        if (sateHistory.size() > counter) {
//            Integer[] nextCoords = moveHistory.elementAt(counter);
//            x = nextCoords[0];
//            y = nextCoords[1];
//            move(x,y);
//        }
    }

    // GETTERS AND SETTERS

    public boolean isUnmovableToRight() {
        return unmovableToRight;
    }

    public void setUnmovableToRight(boolean unmovableToRight) {
        this.unmovableToRight = unmovableToRight;
    }

    public boolean isUnmovableToLeft() {
        return unmovableToLeft;
    }

    public void setUnmovableToLeft(boolean unmovableToLeft) {
        this.unmovableToLeft = unmovableToLeft;
    }

    public boolean isUnmovableToUP() {
        return unmovableToUP;
    }

    public void setUnmovableToUP(boolean unmovableToUP) {
        this.unmovableToUP = unmovableToUP;
    }

    public boolean isUnmovableToDown() {
        return unmovableToDown;
    }

    public void setUnmovableToDown(boolean unmovableToDown) {
        this.unmovableToDown = unmovableToDown;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Stack<Integer[]> getMoveHistory() {
        return moveHistory;
    }

    public boolean isInGoal() {
        return inGoal;
    }

    public void setInGoal(boolean inGoal) {
        this.inGoal = inGoal;
    }

}
