public class Wall {
    private int x;
    private int y;

    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "#";
    }
}
