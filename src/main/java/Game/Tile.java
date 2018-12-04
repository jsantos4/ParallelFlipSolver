package Game;

public class Tile {
    private int x, y;
    private boolean value;

    public Tile(int x, int y, boolean value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public void flip() {
        value = !value;
    }

    public boolean getValue() {
        return value;
    }

    public String getCoordinate() {
        return "(" + x + ", " + y + ")";
    }
}
