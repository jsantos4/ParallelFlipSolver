package Game;


import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.concurrent.ForkJoinPool;


public class Main extends Application {
    private State gameState = new State();
    private GridPane grid = new GridPane();
    private Stage stage1 = new Stage();
    private Label counter = new Label("Moves: ");
    private Label solved = new Label("");
    private ForkJoinPool forkJoinPool;



    public static void main(String[] args){
        launch(args);
    }


    public void start(Stage stage) {
        stage1.setScene(new Scene(displayState(gameState)));
        stage1.show();
    }


    private Pane displayState(State gameState) {
        GridPane newGrid = new GridPane();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button button = new Button(String.valueOf(gameState.getBoard()[i][j].getCoordinate()));
                button.setPrefSize(120, 120);
                if (gameState.getBoard()[i][j].getValue())
                    button.setStyle("-fx-base: #FBFBFB");
                else
                    button.setStyle("-fx-base: #202020");
                button.setOnMouseClicked(event -> flip(event));
                newGrid.add(button, i, j);
            }
        }

        if (gameState.isSolved()) {
            solved.setText("Solved in " + Integer.toString(gameState.getMoveCounter()) + " moves");
        }

        counter.setText("Moves: " + Integer.toString(gameState.getMoveCounter()));
        Button newGameButton = new Button("New Game");
        newGameButton.setOnMouseClicked(event -> newGame());
        Button solveButton = new Button("Solve");
        solveButton.setOnMouseClicked(event -> solveGame());
        newGrid.add(counter, 6, 0);
        newGrid.add(newGameButton, 6, 1);
        newGrid.add(solveButton, 6, 2);
        newGrid.add(solved, 6, 5);
        newGrid.setMinHeight(Pane.USE_PREF_SIZE);
        newGrid.setPrefSize(800, 600);
        newGrid.setMaxHeight(Pane.USE_PREF_SIZE);

        grid = newGrid;

        return grid;

    }

    private void flip(MouseEvent click) {
        int x = grid.getColumnIndex((Node)click.getSource());
        int y = grid.getRowIndex((Node)click.getSource());

        gameState.flip(x, y);
        gameState.incrementMoves();
        stage1.setScene(new Scene(displayState(gameState)));
        stage1.show();
    }

    private void newGame() {
        gameState = new State();
        stage1.setScene(new Scene(displayState(gameState)));
        stage1.show();
    }

    private void solveGame() {
        forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(gameState);
    }

}
