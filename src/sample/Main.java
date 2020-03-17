package sample;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main extends Application {

    public GridPane pane = new GridPane();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Othello");

        Board board = new Board(pane, primaryStage);

        primaryStage.setMinHeight(600);
        primaryStage.setMaxHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setMaxWidth(600);

        Scene scene = new Scene(pane, 600, 600);
        scene.setFill(Color.GREEN);

        int c = 0;
        while(c < 1) {
            pane.setOnMouseClicked(e -> {
                int colIndex = (int) Math.round(e.getX()) / 75;
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