package Game;

import StateSolver.StateSolver;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class State extends RecursiveAction {
    private Tile[][] board;
    private Tile[] tiles;
    private ArrayList<Tile> movesMade;
    private int moveCounter;
    private boolean isSolved;
    private State parent;
    private ArrayList<Tile> initSolution;

    public State() {
        parent = null;
        isSolved = false;
        moveCounter = 0;
        board = new Tile[5][5];
        tiles = new Tile[25];
        movesMade = new ArrayList<>();
        initSolution = new ArrayList<>();

        int counter = -1;

        for(int i = 0; i < 5; i ++) {                   //Create board of all true values
            for (int j = 0; j < 5; j++) {
                Tile tile= new Tile(i, j, true);
                board[i][j] = tile;
                ++counter;
                tiles[counter] = tile;
            }
        }

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(tiles.length/2, tiles.length); i++) {
            int x = ThreadLocalRandom.current().nextInt(5);
            int y = ThreadLocalRandom.current().nextInt(5);
            if (!board[x][y].getFlipped()) {
                flip(x, y);
                initSolution.add(board[x][y]);
            }
        }

        for(int i = 0; i < initSolution.size(); i++) {
            initSolution.get(i).setHasFlipped();
            System.out.println(initSolution.get(i).getCoordinate());

        }
        movesMade = new ArrayList<>();                  //Reset previous moves as no moves have actually been made


    }

    @Override
    protected void compute() {
        StateSolver stateSolver = new StateSolver(this);
        stateSolver.solveGame();
    }

    public State(State parent) {
        this.parent = parent;
        moveCounter = parent.getMoveCounter();
        isSolved = parent.getIsSolved();
        movesMade = new ArrayList<>();
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
        movesMade.add(board[x][y]);
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

    public ArrayList<Tile> getMovesMade() {
        return movesMade;
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
