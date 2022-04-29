import java.util.Stack;

public class Player {
    private int x, y;
    private final Board board;
    private final Stack<Integer[]> moveHistory = new Stack<>();
    private final Stack<String> positionHistory = new Stack<>();
    private boolean onBoxSlot;
    private String position;

    public Player(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
        position = "U";
        moveHistory.push(new Integer[]{x, y});
        positionHistory.push(position);
    }

    void setMoveHistory() {
        moveHistory.push(new Integer[]{this.x, this.y});
        positionHistory.push(position);
    }

    // MOVEMENTS

    void move(int nextX, int nextY) {

        if (onBoxSlot) {
            board.setElements(x, y, new Area(x, y, true, true));
        } else {
            board.setElements(x, y, new Area(x, y, true, false));
        }
        if (board.getElements()[nextY][nextX] instanceof Area && ((Area) (board.getElements()[nextY][nextX])).isBoxSlot()){
            onBoxSlot = true;
        }else {
            onBoxSlot = false;
        }
        this.x = nextX;
        this.y = nextY;

        board.setElements(x, y, this);
    }

    void moveRight() {
        int nX = this.x + 1;

        if (board.getElements()[y][nX] instanceof Box) {
            board.getBox(nX, y).moveRight();
        }
        if (board.areCoordsInside(nX, this.y) && !(board.getElements()[this.y][nX] instanceof Wall)
                && !((board.getElements()[this.y][nX] instanceof Box) && (((Box) (board.getElements()[this.y][nX])).isUnmovableToRight()))) {

            move(nX, this.y);
        }
        position = "R";
    }

    void moveLeft() {
        int nX = this.x - 1;

        if (board.getElements()[y][nX] instanceof Box) {
            board.getBox(nX, y).moveLeft();
        }
        if (board.areCoordsInside(nX, this.y) && !(board.getElements()[this.y][nX] instanceof Wall)
                && !((board.getElements()[this.y][nX] instanceof Box) && (((Box) (board.getElements()[this.y][nX])).isUnmovableToLeft()))) {

            move(nX, this.y);
        }
        position = "L";
    }

    void moveUp() {
        int nY = this.y - 1;

        if (board.getElements()[nY][x] instanceof Box) {
            board.getBox(this.x, nY).moveUp();
        }
        if (board.areCoordsInside(this.x, nY) && !(board.getElements()[nY][this.x] instanceof Wall)
                && !((board.getElements()[nY][this.x] instanceof Box) && (((Box) (board.getElements()[nY][this.x])).isUnmovableToUP()))) {

            move(this.x, nY);
        }
        position = "U";
    }

    void moveDown() {
        int nY = this.y + 1;

        if (board.getElements()[nY][x] instanceof Box) {
            board.getBox(x, nY).moveDown();
        }
        if (board.areCoordsInside(this.x, nY) && !(board.getElements()[nY][this.x] instanceof Wall)
                && !((board.getElements()[nY][this.x] instanceof Box) && (((Box) (board.getElements()[nY][this.x])).isUnmovableToDown()))) {

            move(this.x, nY);
        }
        position = "D";
    }

    // UNDO AND REDO

    boolean f = true;
    void undo() {
        if (!moveHistory.isEmpty()) {
            if(f) {
                moveHistory.remove(moveHistory.size() - 1);
                positionHistory.remove(positionHistory.size() - 1);
                f = false;
            }
            Integer[] lastCoords = moveHistory.pop();
            if(moveHistory.size() - 2 > 0) {
                if (isThereBox(x, y)) {
                    board.setElements(x, y, new Area(x, y, true, false));
                }else {
                    board.setElements(x, y, new Area(x, y, true, false));
                }
            }else {
                board.setElements(x, y, new Area(x, y, true, false));
            }

            if(onBoxSlot){
                board.setElements(x, y, new Area(x, y, true, true));
            }

            x = lastCoords[0];
            y = lastCoords[1];

            if (board.getElements()[y][x] instanceof Area && ((Area) (board.getElements()[y][x])).isBoxSlot()) {
                onBoxSlot = true;
            }else {
                onBoxSlot = false;
            }
            String pos = positionHistory.pop();
            setPosition(pos);
            board.setElements(x, y, this);
        }
        int counter = moveHistory.size() - 1;
    }

    void redo() {
//        if (moveHistoryForRedo.size() > counter) {
//            Integer[] nextCoords = moveHistory.elementAt(counter);
//            x = nextCoords[0];
//            y = nextCoords[1];
//            move(x,y);
//        }
    }

    boolean isThereBox(int x, int y){
        boolean exist = false;
        for(int i = 0; i < board.getHeight(); ++i){
            for(int j = 0; j < board.getWidth(); ++j){
                if(board.getElements()[i][j] instanceof Box){
                    Integer[] cords = ((Box)(board.getElements()[i][j])).getMoveHistory().elementAt(moveHistory.size() - 2);
                    if(x == cords[0] && y == cords[1]){
                        exist = true;
                    }
                }
            }
        }
        return exist;
    }

    //  ALL GETTERS AND SETTERS

    public boolean isOnBoxSlot() {
        return onBoxSlot;
    }

    public void setOnBoxSlot(boolean onBoxSlot) {
        this.onBoxSlot = onBoxSlot;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
