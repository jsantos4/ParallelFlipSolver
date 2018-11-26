package Game;

import Solver.Solver;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private Board board = new Board();
    private GridPane grid = new GridPane();
    private Stage stage1 = new Stage();
    private Label counter = new Label("Moves: ");
    private Label solved = new Label("");


    public static void main(String[] args){ launch(args);}


    public void start(Stage stage) {
        stage1.setScene(new Scene(displayBoard(board)));
        stage1.show();
    }


    private Pane displayBoard(Board board) {
        GridPane newGrid = new GridPane();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button button = new Button(String.valueOf(board.board[i][j]));
                button.setPrefSize(120, 120);
                if (board.board[i][j])
                    button.setStyle("-fx-base: #FBFBFB");
                else
                    button.setStyle("-fx-base: #202020");
                button.setOnMouseClicked(event -> flip(event));
                newGrid.add(button, i, j);
            }
        }

        if (board.isSolved()) {
            solved.setText("Solved in " + Integer.toString(board.getMoveCounter()) + " moves!");
        }

        counter.setText("Moves: " + Integer.toString(board.getMoveCounter()));
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
        board.board[x][y] = !board.board[x][y];                     //Flip clicked square
        if (x > 0)
            board.board[x - 1][y] = !board.board[x - 1][y];         //Flip left square if not on left edge
        if (x < 4)
            board.board[x + 1][y] = !board.board[x + 1][y];         //Flip right square if not on right edge
        if (y > 0)
            board.board[x][y - 1] = !board.board[x][y - 1];         //Flip above square if not on top edge
        if (y < 4)
            board.board[x][y + 1] = !board.board[x][y + 1];         //Flip below square if not on bottom edge

        board.incramentMoves();
        stage1.setScene(new Scene(displayBoard(board)));
        stage1.show();
    }

    private void newGame() {
        board = new Board();
        stage1.setScene(new Scene(displayBoard(board)));
        stage1.show();
    }

    private void solveGame() {
        Solver solver = new Solver();
        solver.solveGame();
    }

}
