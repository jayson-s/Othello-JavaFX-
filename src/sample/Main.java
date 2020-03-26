package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.Serializable;

public class Main extends Application {

    public GridPane pane = new GridPane();
    private long chatPort, gamePort;
    private String ip = "localhost";
    private Text invalid = new Text();
    private Board board;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Othello");
        primaryStage.getIcons().add(new Image("/images/logo.png"));

        HBox hBox = new HBox(50);
        hBox.setPadding(new Insets(10,10,10,10));

        Image imgNew = new Image(getClass().getResourceAsStream("/images/newGame.png"));
        Image imgJoin = new Image(getClass().getResourceAsStream("/images/joinGame.png"));

        Button create = new Button("Create New Game", new ImageView(imgNew));
        Button join = new Button("Join Existing Game", new ImageView(imgJoin));

        create.setStyle("-fx-background-color: grey;-fx-text-fill: white; -fx-font-size: 2em; -fx-border-color: white");
        join.setStyle("-fx-background-color: grey;-fx-text-fill: white; -fx-font-size: 2em; -fx-border-color: white");
        hBox.setStyle("-fx-background-color: dimgrey; -fx-border-color: white");

        hBox.getChildren().addAll(create, join);
        hBox.setAlignment(Pos.CENTER);

        Scene initialScene = new Scene(hBox,600,500);
        primaryStage.setScene(initialScene);
        primaryStage.show();

        GridPane createPane = new GridPane();
        createPane.setAlignment(Pos.CENTER);
        createPane.setPadding(new Insets(10,10,10,10));
        createPane.setHgap(10);
        createPane.setVgap(10);
        createPane.setStyle("-fx-background-color: dimgrey; -fx-border-color: white");

        Label lblPortName1 = new Label("Port Name (Default is 8080):");
        lblPortName1.setStyle("-fx-text-fill: white");
        createPane.add(lblPortName1,0,0);

        TextField txtPortName1  = new TextField();
        txtPortName1.setText("8080");
        createPane.add(txtPortName1,1,0);

        Button createGame = new Button("Create Game");
        Scene createScene = newPane(createPane, createGame);
        createPane.add(invalid, 3, 11, 1, 1);

        GridPane joinPane = new GridPane();
        joinPane.setAlignment(Pos.CENTER);
        joinPane.setPadding(new Insets(10,10,10,10));
        joinPane.setHgap(10);
        joinPane.setVgap(10);
        joinPane.setStyle("-fx-background-color: dimgrey; -fx-border-color: white");

        Label lblPortName2 = new Label("Port Name of Existing Game:");
        lblPortName2.setStyle("-fx-text-fill: white");
        joinPane.add(lblPortName2,0,0);

        TextField txtPortName2  = new TextField();
        txtPortName2.setText("8080");
        joinPane.add(txtPortName2,1,0);

        Button joinGame = new Button("Join Game");

        Scene joinScene = newPane(joinPane, joinGame);

        create.setOnAction(e -> {
            primaryStage.setScene(createScene);
            createGame.setOnAction(e1 -> {
                try {
                    chatPort = Long.parseLong(txtPortName1.getText());
                    gamePort = Long.parseLong(txtPortName1.getText()) - 1;
                } catch (Exception ex1){
                    ex1.printStackTrace();
                }

                if (chatPort > 1 && chatPort < 65534) {
                    int port = (int) chatPort;
                    int port1 = (int) gamePort;

                    InitializeNetwork gameConnection = new Server(port, (data) -> {
                        Platform.runLater(() -> {
                            try {
                                handleOutputStream(data);
                            } catch (Exception ex2) {
                                ex2.printStackTrace();
                            }
                        });
                    });

                    primaryStage.setTitle("Othello Player 1");

                    try {
                        game(gameConnection, primaryStage);
                        gameConnection.start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    invalid.setText("Invalid Port. Enter new Port.");
                }
            });

        });

        join.setOnAction(e -> {
            primaryStage.setScene(joinScene);
            joinGame.setOnAction(e1 -> {
                try {
                    chatPort = Long.parseLong(txtPortName2.getText());
                    gamePort = Long.parseLong(txtPortName2.getText()) - 1;
                } catch (Exception ex1){
                    ex1.printStackTrace();
                }

                if (chatPort > 1 && chatPort < 65534) {
                    int port = (int) chatPort;
                    int port1 = (int) gamePort;

                    InitializeNetwork gameConnection = new Server(port, (data) -> {
                        Platform.runLater(() -> {
                            try {
                                handleOutputStream(data);
                            } catch (Exception ex2) {
                                ex2.printStackTrace();
                            }
                        });
                    });

                    try {
                        game(gameConnection, primaryStage);
                        gameConnection.start();
                    } catch (Exception ex) {
                        invalid.setText("Invalid IP.");
                    }

                    try {
                        Thread.sleep(100);
                        if (gameConnection.connected.socket.isConnected()){
                            primaryStage.setTitle("Othello Player 2");
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    invalid.setText("Invalid IP or Port.");
                }
            });
        });

        primaryStage.setTitle("Othello");
        primaryStage.show();
    }

    private void handleOutputStream(Serializable data) throws Exception {
        if (data instanceof Integer[]) {
            // TODO: Figure out how to synchronize board data
        }
        if (data instanceof Boolean){
            board.player = 1;
        }
    }

    
    protected void game(InitializeNetwork gameConn, Stage primaryStage) throws Exception{
        board = new Board(gameConn, pane, primaryStage);

        primaryStage.setMinHeight(600);
        primaryStage.setMaxHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setMaxWidth(600);

        Scene scene = new Scene(pane, 600, 600);
        scene.setFill(Color.GREEN);

        int c = 0;
        while(c < 1) {
            pane.setOnMouseClicked(e -> {
                System.out.println(Math.round(e.getX()) / 75);
                System.out.println(Math.round(e.getY()) / 75);
                int colIndex = (int) Math.round(e.getX()) / 75;
                int rowIndex = (int) Math.round(e.getY()) / 75;

                board.placement(pane, colIndex, rowIndex);

            });

            c++;

        }

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
    }

    private Scene newPane(GridPane pane, Button button) {
        button.setDefaultButton(true);
        button.setStyle("-fx-background-color: grey;-fx-text-fill: white; -fx-font-size: 2em; -fx-border-color: white");
        pane.add(button,1,1);

        Button backButton = new Button();
        backButton.setCancelButton(true);
        backButton.setPadding(new Insets(0,0,1,1));
        Image imgBack = new Image(getClass().getResourceAsStream("/images/back.png"));
        backButton.setGraphic(new ImageView(imgBack));
        backButton.setStyle("-fx-background-color: grey;-fx-text-fill: white; -fx-font-size: 2em; -fx-border-color: white");
        pane.add(backButton, 0, 19);

        Scene scene = new Scene(pane,500,500);

        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }


}