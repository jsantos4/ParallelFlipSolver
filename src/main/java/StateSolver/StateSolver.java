package StateSolver;

import Game.*;

import java.util.ArrayList;

public class StateSolver {
    private State gameState;

    public StateSolver(State gameState) {
        this.gameState = gameState;
    }

    public void solveGame() {
        ArrayList<State> nextStates = getNextStates();
        for (State state: nextStates) {
            if(!gameState.getMovesMade().contains(state)) {
                if (state.getIsSolved()) {                                      //If we've solved
                    System.out.println("Flip the following tiles (unordered):");
                    for(Tile tile: state.getMovesMade()){
                        System.out.println(tile.getCoordinate());
                    }

                    return;
                } else if (state.getMovesMade().size() == 25){
                    state.join();
                    System.out.println("Joined");
                } else {
                    state.fork();
                    System.out.println(state.getMovesMade().size());
                }
            }
        }

    }

    private ArrayList<State> getNextStates() {
        ArrayList<State> states = new ArrayList<>();
        State newState = null;

        for (Tile tile: gameState.getTiles()) {
            newState = new State(gameState);
            newState.flip(tile.getX(), tile.getY());
            states.add(newState);
        }
        return states;

    }
}
