package Game;

public class Tile {
    private int x, y;
    private boolean value, hasFlipped;

    public Tile(int x, int y, boolean value) {
        this.x = x;
        this.y = y;
        this.value = value;
        hasFlipped = false;
    }

    public void flip() {
        value = !value;
    }

    public void setHasFlipped() {
        hasFlipped = !hasFlipped;
    }

    public boolean getFlipped() {
        return hasFlipped;
    }

    public boolean getValue() {
        return value;
    }

    public String getCoordinate() {
        return "(" + x + ", " + y + ")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
