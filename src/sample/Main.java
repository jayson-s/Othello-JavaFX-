// Othello
// 03/15/2020

package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    public GridPane pane = new GridPane();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Othello");

        Board board = new Board(pane);

        // sets screen size
        primaryStage.setMinHeight(600);
        primaryStage.setMaxHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setMaxWidth(600);

        Scene scene = new Scene(pane, 600, 600);
        scene.setFill(Color.GREEN);

        //infinite mouse event
        int c = 0;
        while(c < 1) {
            pane.setOnMouseClicked(e -> {
                // gets index to place
                int colIndex = (int) Math.round(e.getX()) / 75; // determines row and column index from mouse click
                int rowIndex = (int) Math.round(e.getY()) / 75;

                board.placement(pane, colIndex, rowIndex);

            });

            c++;

        }

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}