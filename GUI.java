import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;


public class GUI extends Application
{
    private Button startButton; // button for starting new game
    private Button quitButton; //button for exiting application
    private Label message; // label to display messages to the user
    //GameBoard board; // canvas the board is drawn on

    public static void main(String[] args) {
        launch(args);  // Run this Application.
    }


    public void start(Stage stage)
    {
        /* Label to show messages */
        message = new Label( "Click \"Start\" to begin" );
        message.setTextFill( Color.rgb( 255,255,255 ) );
        message.setFont( Font.font(null, FontWeight.BOLD, 18) );

        /* The board */
        //board = new Board()

        /* Creates the Buttons to start and quit */
        Button startButton = new Button( "Start" );
        startButton.setOnAction( e -> message.setText("Hello World!") ); //Game.play() );
        Button quitButton = new Button( "Quit" );
        quitButton.setOnAction( e -> Platform.exit() );

        HBox buttonBar = new HBox( 20, startButton, quitButton );
        buttonBar.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane();
        root.setTop(message);
        root.setCenter(board);
        root.setBottom(buttonBar);
        root.setStyle ( "-fx-background-color: black;" );

        Scene scene = new Scene(root, 450, 200);
        stage.setScene(scene);
        stage.setTitle("Rogue");
        stage.show();

    } // end start();

}
