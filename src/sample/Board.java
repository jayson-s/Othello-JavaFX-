// Othello
// 03/15/2020

package sample;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Board {
    private int gameOverCounter = 0;
    protected int player = 0;
    private boolean started = false;
    protected int scoreB = 0;
    protected int scoreW = 0;
    protected ArrayList<Circle> possibleMoves = new ArrayList<>();

    //array of pieces
    ArrayList<ArrayList<Piece>> pieces = new ArrayList<>(8);

    public void start(GridPane pane) {  // sets up the board

        // starting 4 pieces
        placement(pane, 3, 3);
        placement(pane, 3, 4);
        placement(pane, 4, 4);
        placement(pane, 4, 3);
        started = true;
        showLegal(pane);

    }



    public Board(GridPane pane) throws InterruptedException {       // creates the board

        Thread fillThread = new Thread("fill"){
            @Override
            public void run(){
                // fill board with invisible pieces
                for (int i = 0; i < 8; i++) {
                    pieces.add(new ArrayList<>());  // fills with arrays
                    for (int j = 0; j < 8; j++) {
                        pieces.get(i).add(new Piece());  // fills array with pieces
                    }
                }
            }
        };

        Thread boardCreationThread = new Thread("boardCreation"){
            @Override
            public void run(){
                // create board
                pane.setGridLinesVisible(true);
                for (int i = 0; i < 8; i++) {
                    ColumnConstraints colConst = new ColumnConstraints();
                    colConst.setPercentWidth(100.0 / 8);
                    colConst.setHalignment(HPos.CENTER);
                    pane.getColumnConstraints().add(colConst);

                    RowConstraints rowConst = new RowConstraints();
                    rowConst.setPercentHeight(100.0 / 8);
                    rowConst.setValignment(VPos.CENTER);
                    pane.getRowConstraints().add(rowConst);
                }
            }
        };

        fillThread.start();
        boardCreationThread.start();

        fillThread.join();
        boardCreationThread.join();

        start(pane);
    }

    // placement
    public void placement(GridPane pane, int cI, int rI) {

        if (started) {
            // checks if selected placement is a legal move
            if (isIllegal(cI, rI) == 0) {

                //toggling
                for (int i = cI - 1; i < cI + 2; i++) {     // cells around placed piece
                    for (int j = rI - 1; j < rI + 2; j++) {
                        int diffX = i - cI;                 // gets unit vector direction
                        int diffY = j - rI;

                        // toggle all in a line for a direction
                        int c = 1;
                        for (int i2 = 1; i2 < 8; i2++) {
                            int dx = cI + (diffX * i2);
                            int dy = rI + (diffY * i2);

                            if (dx > 7 || dx < 0 || dy > 7 || dy < 0)  {    // checks if the piece is within the board
                                c = 1;
                                break;
                            }

                            // determines amount to toggle
                            if (pieces.get(dx).get(dy).isPlaced) {
                                if (pieces.get(dx).get(dy).isWhite != player) {
                                    c++;
                                } else { break; }
                            } else {
                                c = 1;
                                break;
                            }
                        }

                        // toggle pieces
                        for (int c2 = 1; c2 < c; c2++) {
                            pieces.get(cI + (diffX * c2)).get(rI + (diffY * c2)).toggle();
                        }
                    }
                }

                // place piece on selected spot
                pieces.get(cI).get(rI).place(player);
                pane.add(pieces.get(cI).get(rI).getCircle(), cI, rI);

                // swap player
                if (player == 0)      { player = 1; }
                else if (player == 1) { player = 0; }

                countScore();
                showLegal(pane);
            }

        } else if (!started) {
            // part of the board setup
            pieces.get(cI).get(rI).place(player);
            pane.add(pieces.get(cI).get(rI).getCircle(), cI, rI);

            if (player == 0)      { player = 1; }
            else if (player == 1) { player = 0; }

            countScore();
        }
    }

    public void showLegal(GridPane pane) {

        int moves = 0;  // determines number of available moves

        // clear possible moves
        possibleMoves.forEach((n) -> pane.getChildren().remove(n));
        possibleMoves.clear();

        // determine legal moves
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if(isIllegal(i, j) == 0) {
                    // make red circle to indicate valid move
                    moves++;
                    Circle circle = new Circle(20, 20, 25);
                    circle.setFill(Color.RED);
                    possibleMoves.add(circle);
                    pane.add(circle, i, j);
                }
            }
        }

        if (moves == 0) {

            // if no moves are available, re-swap the players
            if (player == 0)      { player = 1; }
            else if (player == 1) { player = 0; }

            gameOverCounter++;

            // end game if no moves left, 2 times in a row
            if (gameOverCounter == 2) {
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println("Game Over!");
                System.out.println();
                countScore();
                if (scoreB > scoreW) {
                    System.out.print("Black");
                } else if (scoreW > scoreB) {
                    System.out.print("White");
                } else {
                    System.out.print("Nobody");
                }
                System.out.println(" Wins!");
            } else {
                System.out.println("No moves available, other player goes again");
                showLegal(pane);
            }
        } else { gameOverCounter = 0; }
    }

    public int isIllegal(int cI, int rI) {
        if (pieces.get(cI).get(rI).isPlaced) {return 1;} // check if piece already exists

        // check surroundings for any of opposite color
        for (int i = cI - 1; i < cI + 2; i++) {
            for (int j = rI - 1; j < rI + 2; j++) {
                if (i <= 7 && i >= 0 && j <= 7 && j >= 0 ) {    // checks if within bounds
                    if (pieces.get(i).get(j).isPlaced && pieces.get(i).get(j).isWhite != player) {
                        int diffX = i - cI; // unit vector directions
                        int diffY = j - rI;

                        // check all in a line in that direction
                        for (int x = 2; x < 8; x++) {
                            int dx = cI + (diffX * x);
                            int dy = rI + (diffY * x);

                            if (dx > 7 || dx < 0 || dy > 7 || dy < 0) { // within bounds?
                                x = 8;
                            } else if (!pieces.get(dx).get(dy).isPlaced) {  // not placed?
                                x = 8;
                            } else if (pieces.get(dx).get(dy).isPlaced && pieces.get(dx).get(dy).isWhite == player) { // placed and same as player
                                return 0;
                                // legal move
                            }
                        }

                    }
                }
            }
        }
        return 1;
        // illegal move
    }

    public void countScore() {
        scoreW = 0;
        scoreB = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces.get(i).get(j).isPlaced && pieces.get(i).get(j).isWhite == 1)      { scoreW++; }
                else if (pieces.get(i).get(j).isPlaced && pieces.get(i).get(j).isWhite == 0) { scoreB++; }
            }
        }
        System.out.println();
        System.out.println("B:" + scoreB);
        System.out.println("W:" + scoreW);
        System.out.println();
    }
}