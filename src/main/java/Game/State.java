package Game;

import java.util.concurrent.ThreadLocalRandom;

public class State {
    private Tile[][] board;
    private Tile[] tiles;
    private int moveCounter;
    private boolean isSolved;

    public State() {
        isSolved = false;
        moveCounter = 0;
        board = new Tile[5][5];
        tiles = new Tile[25];

        int counter = -1;
        for(int i = 0; i < 5; i ++) {
            for (int j = 0; j < 5; j++) {
                Tile tile= new Tile(i, j, ThreadLocalRandom.current().nextBoolean());
                board[i][j] = tile;
                ++counter;
                tiles[counter] = tile;
            }
        }
    }

    public State(State parent) {
        moveCounter = parent.getMoveCounter();
        isSolved = parent.getIsSolved();
        board = new Tile[5][5];
        tiles = new Tile[25];

        int counter = -1;
        for(int i = 0; i < 5; i ++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = new Tile(parent.getBoard()[i][j].getX(), parent.getBoard()[i][j].getY(), parent.getBoard()[i][j].getValue());
                ++counter;
                tiles[counter] = board[i][j];
            }
        }
    }

    public String asString() {
        String out = "";
        for (int i = 0; i < 5; i++) {
            out += "\n";
            for (int j = 0; j < 5; j++) {
                out += board[j][i].getValue() + " ";
            }
        }
        return out;
    }

    public void incramentMoves() {
        ++moveCounter;
    }

    public void flip(int x, int y) {
        board[x][y].flip();                     //Flip clicked square
        board[x][y].setHasFlipped();
        if (x > 0)
            board[x - 1][y].flip();         //Flip left square if not on left edge
        if (x < 4)
            board[x + 1][y].flip();         //Flip right square if not on right edge
        if (y > 0)
            board[x][y - 1].flip();         //Flip above square if not on top edge
        if (y < 4)
            board[x][y + 1].flip();         //Flip below square if not on bottom edge

    }


    public int getMoveCounter() {
        return moveCounter;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public boolean getIsSolved() {
        return isSolved();
    }

    private boolean isSolved() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!board[i][j].getValue()) {
                    return isSolved;
                }
            }
        }
        return !isSolved;
    }

}
