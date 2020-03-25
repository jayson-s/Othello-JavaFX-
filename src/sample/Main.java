package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public GridPane pane = new GridPane();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Board board = new Board(pane, primaryStage);
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
        Text title = new Text();
        title.setText("Player Scores");
        title.setFont(Font.font("Century Gothic", FontWeight.BOLD, 40));
        title.setFill(Color.WHITE);
        title.setUnderline(true);

        //Setup for black score
        Text bT = new Text();
        bT.setText("Black Player:");
        bT.setFont(Font.font("Century Gothic", FontWeight.BOLD, 36));
        bT.setFill(Color.BLACK);

        Circle bC = new Circle(20);
        bC.setFill(Color.BLACK);

        Text bCount = new Text();
        bCount.setText("Count = ");
        bCount.setFont(Font.font("Century Gothic", FontWeight.BOLD, 36));
        bCount.setFill(Color.BLACK);

        int blackScore = board.scoreB;
        Text bScore = new Text();
        bScore.setText(Integer.toString(blackScore));
        bScore.setFont(Font.font("Century Gothic", FontWeight.BOLD, 36));
        bScore.setFill(Color.BLACK);

        HBox bRow = new HBox(10, bC, bCount, bScore);
        VBox black = new VBox(40, title, bT, bRow);
        black.setAlignment(Pos.BASELINE_LEFT);

        //Setup for white score
        Text wT = new Text();
        wT.setText("White Player:");
        wT.setFont(Font.font("Century Gothic", FontWeight.BOLD, 36));
        wT.setFill(Color.WHITE);

        Circle wC = new Circle(20);
        wC.setFill(Color.WHITE);

        Text wCount = new Text();
        wCount.setText("Count = ");
        wCount.setFont(Font.font("Century Gothic", FontWeight.BOLD, 36));
        wCount.setFill(Color.WHITE);

        int whiteScore = board.scoreW;
        Text wScore = new Text();
        wScore.setText(Integer.toString(whiteScore));
        wScore.setFont(Font.font("Century Gothic", FontWeight.BOLD, 36));
        wScore.setFill(Color.WHITE);

        HBox wRow = new HBox(10, wC, wCount, wScore);
        VBox white = new VBox(40, wT, wRow);
        white.setAlignment(Pos.BASELINE_LEFT);

        VBox sideTab = new VBox(125, black, white);
        sideTab.setMinWidth(300);
        sideTab.setAlignment(Pos.TOP_CENTER);
        sideTab.setPadding(new Insets(50, 20, 50, 20));
        sideTab.setBackground(new Background(new BackgroundFill(Color.SLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        //Add all panes to scene
        root.setCenter(pane);
        root.setTop(menuBar);
        root.setRight(sideTab);
        Scene scene = new Scene(root, 1050, 750);

        //Placing disks and updating score
        int c = 0;
        while(c < 1) {
            pane.setOnMouseClicked(e -> {
                int colIndex = (int) Math.round(e.getX()) / 90;
                int rowIndex = (int) Math.round(e.getY()) / 90;
                board.placement(pane, colIndex, rowIndex);

                //count integration to increment every placement
                int bS = count(blackScore) - 2;
                int wS = count(whiteScore) - 2;

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board.pieces.get(i).get(j).isPlaced && board.pieces.get(i).get(j).isWhite == 1) { wS++; }
                        else if (board.pieces.get(i).get(j).isPlaced && board.pieces.get(i).get(j).isWhite == 0) { bS++; }
                    }
                }
                bScore.setText(Integer.toString(bS));
                wScore.setText(Integer.toString(wS));
            });
            c++;
        }
        //Setup stage
        primaryStage.setTitle("Othello");
        primaryStage.getIcons().add(new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcT3pUrDq671YtRKXsaUpVNoi6MIL6S9k1KbRWmxAo64MR44EDyu"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public int count(int n){
        return n++;
    }
}