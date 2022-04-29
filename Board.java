import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Board{
    private final int width;
    private final int height;
    private final Object[][] elements;
    private int nOfBoxSlots;
    private int nOfSucceedBox;


    public Board(StringBuilder model, Integer[] sizes) throws HeadlessException {
        String[] stringModel = model.toString().split("\n");
        width = sizes[0];
        height = sizes[1];
        elements = new Object[height][width];

        for (int y = 0; y < height; ++y) {
            int counter = 0;
            for (int x = 0; x < width; ++x) {
                if (stringModel[y].length() - 1 < counter) {
                    elements[y][x] = new Area(x, y, false,false);
                } else {
                    int firstWall = stringModel[y].indexOf("#");

                    switch (stringModel[y].charAt(counter)) {
                        case ' ':
                            elements[y][x] = new Area(x, y, counter > firstWall,false);
                            break;
                        case '#':
                            elements[y][x] = new Wall(x, y);
                            break;
                        case '$':
                            elements[y][x] = new Box(x, y, this,false);
                            break;
                        case '@':
                            elements[y][x] = new Player(x, y, this);
                            break;
                        case '.':
                            elements[y][x] = new Area(x, y, counter > firstWall,true);
                            nOfBoxSlots++;
                            break;
                        case '*':
                            elements[y][x] = new Box(x,y,this,true);
                            nOfBoxSlots++;
                            nOfSucceedBox++;
                    }
                }
                counter++;
            }
        }
    }

    void undo(){
        getPlayer().undo();

        ArrayList<Box> boxes = getAllBoxes();
        for(Box box : boxes){
            box.undo();
        }
    }

    void redo(){
        getPlayer().redo();

        ArrayList<Box> boxes = getAllBoxes();
        for(Box box : boxes){
            box.redo();
        }
    }

    boolean areCoordsInside(int x, int y) {
        return x < width && x >= 0 && y < height && y >= 0;
    }

    // ALL GETTERS AND SETTERS

    void setMoveHistory(){
        getPlayer().setMoveHistory();

        ArrayList<Box> boxes = getAllBoxes();
        for(Box box : boxes){
            box.setMoveHistory();
        }
    }

    public Object[][] getElements() {
        return elements;
    }

    void setElements(int x,int y,Object object){
        elements[y][x] = object;
    }

    Player getPlayer(){
        Player player = null;
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){
                if(elements[y][x] instanceof Player){
                    player = (Player)(elements[y][x]);
                }
            }
        }
        return player;
    }

    Box getBox(int x,int y){
        return (Box)(elements[y][x]);
    }

    ArrayList<Box> getAllBoxes(){
        ArrayList<Box> boxes = new ArrayList<>();
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){
                if(elements[y][x] instanceof Box){
                    boxes.add((Box)(elements[y][x]));
                }
            }
        }
        return boxes;
    }

    public int getnOfSucceedBox() {
        return nOfSucceedBox;
    }

    public void setnOfSucceedBox(int nOfSucceedBox) {
        this.nOfSucceedBox = nOfSucceedBox;
    }

    public int getnOfBoxSlots() {
        return nOfBoxSlots;
    }

    public void setnOfBoxSlots(int nOfBoxSlots) {
        this.nOfBoxSlots = nOfBoxSlots;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCellSize() {
        int cellSize = 50;
        return cellSize;
    }
}

