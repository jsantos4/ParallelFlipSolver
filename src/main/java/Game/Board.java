package Game;

import java.util.concurrent.ThreadLocalRandom;

public class Board {
    Tile[][] board;
    private int moveCounter;
    private boolean isSolved;

    public Board() {
        isSolved = false;
        moveCounter = 0;
        board = new Tile[5][5];
        for(int i = 0; i < 5; i ++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = new Tile(i, j, ThreadLocalRandom.current().nextBoolean());
            }
        }
    }

    public String asString() {
        String out = "";
        for (int i = 0; i < 5; i++) {
            out += "\n";
            for (int j = 0; j < 5; j++) {
                out += board[i][j] + " ";
            }
        }
        return out;
    }

    public void incramentMoves() {
        ++moveCounter;
    }

    public void flip(int x, int y) {
        board[x][y].flip();
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public boolean isSolved() {
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
