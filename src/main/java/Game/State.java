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
	private AtomicBoolean isSolved = new AtomicBoolean(false);
	private static AtomicBoolean done;

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
		for (int i = 0; i < ThreadLocalRandom.current().nextInt(tiles.length/2, tiles.length); i++) {       //For a random number of tiles, flip a random tile
			int x = ThreadLocalRandom.current().nextInt(5);
			int y = ThreadLocalRandom.current().nextInt(5);
			if (!board[x][y].getFlipped()) {
				flip(x, y);
				initSolution.add(board[x][y]);
			}
		}

		for(Tile tile: initSolution) {
			tile.setHasFlipped();                       //Reset tile's hasFlipped value
			System.out.println(tile.getCoordinate());

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
		ArrayList<State> nextStates = getNextStates();              //Get next possible states
		for (State state: nextStates) {
			if (!state.isSolved() && !done.get()) {                 //If state in nextState is not solved or done, fork
				state.fork();
			} else if (state.isSolved()){                           //If state is in a solved state, print tiles to flip, set done to true, join task
				stateSolution = state.getMovesMade();
				System.out.println("Flip tiles:");
				for (Tile tile: stateSolution) {
					System.out.println(tile.getCoordinate());
				}
				done.compareAndSet(false, true);
				state.join();
			} else if (state.getMovesMade().size() == 25 && state.parent != null){      //Otherwise is 25 moves have been made and parent exists, join
			    state.join();
			}
		}
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
				if (!board[i][j].getValue()) {          //Run through tiles, if one is black, return false
					return isSolved.get();
				}
			}
		}
		done.compareAndSet(false, true);        //Otherwise set done, return true
		return !isSolved.get();
	}

	private ArrayList<State> getNextStates() {
		ArrayList<State> states = new ArrayList<>();
		State newState;

		for (Tile tile: tiles) {                      //For each tile on the board, create new state, flip that tile, add to next states
			newState = new State(this);
			if (!tile.getFlipped()) {
				newState.flip(tile.getX(), tile.getY());
				states.add(newState);
			}
		}
		return states;

	}

}
