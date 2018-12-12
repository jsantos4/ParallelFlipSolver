package Game;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class State extends RecursiveAction {
    private Tile[][] board;
    private Tile[] tiles;
    private ArrayList<Tile> movesMade;
    private int moveCounter;
    private State parent;
    private ArrayList<Tile> stateSolution;
    private AtomicBoolean done;

    public State() {
        parent = null;
        moveCounter = 0;
        board = new Tile[5][5];
        tiles = new Tile[25];
        movesMade = new ArrayList<>();
        ArrayList<Tile> initSolution = new ArrayList<>();
        stateSolution = new ArrayList<>();
        done = new AtomicBoolean(false);

        int counter = -1;

        for(int i = 0; i < 5; i ++) {                   //Create board of all true values
            for (int j = 0; j < 5; j++) {
                Tile tile= new Tile(i, j, true, false);
                board[i][j] = tile;
                ++counter;
                tiles[counter] = tile;
            }
        }

        //ThreadLocalRandom.current().nextInt(tiles.length/2, tiles.length)
        for (int i = 0; i < 3; i++) {
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

    private State(State parent) {
        this.parent = parent;
        moveCounter = parent.getMoveCounter();
        stateSolution = new ArrayList<>();
        movesMade = new ArrayList<>(parent.movesMade);
        board = new Tile[5][5];
        tiles = new Tile[25];
        done = new AtomicBoolean(false);

        int counter = -1;
        for(int i = 0; i < 5; i ++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = new Tile(parent.getBoard()[i][j].getX(), parent.getBoard()[i][j].getY(), parent.getBoard()[i][j].getValue(), parent.getBoard()[i][j].getFlipped());
                ++counter;
                tiles[counter] = board[i][j];
            }
        }
    }

    @Override
    protected void compute() {
        solveGame();
    }


    public void incrementMoves() {
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

    public Tile[][] getBoard() {
        return board;
    }

    private ArrayList<Tile> getMovesMade() {
        return movesMade;
    }

    public Boolean isSolved() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!board[i][j].getValue()) {
                    return done.get();
                }
            }
        }
        done.compareAndSet(false, true);
        return done.get();
    }

    private void solveGame() {
        ArrayList<State> nextStates = getNextStates();
        for (State state: nextStates) {
            if (!state.isSolved()) {
                state.fork();
            } else if (state.getMovesMade().size() == 25 && state.parent != null){
                done.compareAndSet(false, true);
                state.join();
            } else {
                stateSolution = state.getMovesMade();
                System.out.println("Flip tiles:");
                for (Tile tile: stateSolution) {
                    System.out.println(tile.getCoordinate());
                }
                state.join();
            }
        }
    }

    private ArrayList<State> getNextStates() {
        ArrayList<State> states = new ArrayList<>();
        State newState;

        for (Tile tile: tiles) {
            newState = new State(this);
            if (!tile.getFlipped()) {
                newState.flip(tile.getX(), tile.getY());
                states.add(newState);
            }
        }
        return states;

    }

}
