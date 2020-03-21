package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main extends Application {

    public GridPane pane = new GridPane();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Board board = new Board(pane, primaryStage);
        int c = 0;
        while(c < 1) {
            pane.setOnMouseClicked(e -> {
                int colIndex = (int) Math.round(e.getX()) / 90;
                int rowIndex = (int) Math.round(e.getY()) / 90;
                board.placement(pane, colIndex, rowIndex);
            });
            c++;
        }
        BorderPane root = new BorderPane();

        //Set game pane in center and set background color
        pane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setAlignment(Pos.CENTER);

        //Create menu bar
        final Menu menu1 = new Menu("File");
        final Menu menu2 = new Menu("Options");
        final Menu menu3 = new Menu("Help");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2, menu3);

        //Create side tab to right of GridPane
        Text t = new Text();
        t.setText("Leader Board:");
        t.setFont(Font.font("Century Gothic", FontWeight.BOLD, 36));
        t.setFill(Color.BLACK);
        t.setTextAlignment(TextAlignment.CENTER);

        Text t2 = new Text();
        t2.setText("Count:");
        t2.setFont(Font.font("Century Gothic", FontWeight.BOLD, 36));
        t2.setFill(Color.BLACK);
        t2.setTextAlignment(TextAlignment.CENTER);

        Circle circle = new Circle(750, 100, 30);
        circle.setFill(Color.BLACK);

        VBox sideTab = new VBox(300, t, t2);
        sideTab.getChildren().add(circle);
        sideTab.setMinWidth(300);
        sideTab.setAlignment(Pos.BASELINE_CENTER);
        
        //Add all panes to scene
        root.setCenter(pane);
        root.setTop(menuBar);
        root.setRight(sideTab);
        Scene scene = new Scene(root, 1050, 750);

        //Setup stage
        primaryStage.setTitle("Othello");
        primaryStage.getIcons().add(new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcT3pUrDq671YtRKXsaUpVNoi6MIL6S9k1KbRWmxAo64MR44EDyu"));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}