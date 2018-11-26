package Game;

import java.util.concurrent.ThreadLocalRandom;

public class Board {
    boolean[][] board;
    private int moveCounter;
    private boolean isSolved;

    public Board() {
        isSolved = false;
        moveCounter = 0;
        board = new boolean[5][5];
        for(int i = 0; i < 5; i ++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = ThreadLocalRandom.current().nextBoolean();
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

    public void decramentMoves() {
        --moveCounter;
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public boolean isSolved() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!board[i][j]) {
                    return isSolved;
                }
            }
        }
        return !isSolved;
    }

}
