package Solver;

import Game.*;

import java.util.ArrayList;

public class Solver {
    private State gameState;
    //private Tile[]

    public Solver(State gameState) {
        this.gameState = gameState;
    }

    public void solveGame() {
        ArrayList<State> nextStates = new ArrayList<>();
        if (!gameState.getIsSolved()) {
            nextStates = getNextStates();
        }
        for (State state: nextStates) {
            System.out.println(state.asString());
            System.out.println(nextStates.indexOf(state));

        }

        System.out.println("///////////////////////////////////////////////////");
    }

    private ArrayList<State> getNextStates() {

        ArrayList<State> states = new ArrayList<>();
        for (Tile tile: gameState.getTiles()) {
            if (!tile.getFlipped()) {
                State newState = new State(gameState);
                newState.flip(tile.getX(), tile.getY());
                states.add(newState);
            }
        }
        return states;

    }
}
