import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.*;

/** Lets the user play a Rogue-like as the Rogue against a game defined player,
 *  the Monster. The Rogue moves first and has to reach the exit to be victorious.
 *  The Monster's objective is to catch the rogue, and when it does, the Rogue
 *  loses.
 */
public class Main extends Application
{
    /* Lookback
     * 
     * 
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    RogueBoard board; // The canvas where the rogue game is drawn
    private Button startButton; // Button to start game
    private Button dungeonA;
    private Button dungeonB;
    private Button dungeonC;
    private Label message; // Label to display messages

    public void start(Stage stage)
    {
        // Creates label to give messages to the user
        message = new Label( "Welcome to A Rogue-like by Steven Chau." );
        message.setTextFill( Color.rgb( 0, 0, 0 ) ); // Black
        message.setFont( Font.font( null, FontWeight.BOLD, 16 ) );

        // Create the buttons
        startButton = new Button( "Start" );
        dungeonA = new Button( "Dungeon A" );
        dungeonB = new Button( "Dungeon B" );
        dungeonC = new Button( "Dungeon C" );

        // Creates the board
        board = new RogueBoard( );
        board.drawBoard();

        // Event handlers
        startButton.setOnAction( e -> board.startGame() );
        dungeonA.setOnAction( e ->
            {
                board.loadDungeon( "dungeonA.txt" );
                dungeonA.setDisable( true );
                dungeonB.setDisable( false );
                dungeonC.setDisable( false );
            } );
        dungeonB.setOnAction( e ->
            {
                board.loadDungeon( "dungeonB.txt" );
                dungeonA.setDisable( false );
                dungeonB.setDisable( true );
                dungeonC.setDisable( false );
            } );
        dungeonC.setOnAction( e ->
            {
                board.loadDungeon( "dungeonC.txt" );
                dungeonA.setDisable( false );
                dungeonB.setDisable( false );
                dungeonC.setDisable( true );
            } );
        board.setOnKeyPressed( e -> board.keyPressed( e ) );

        dungeonA.setDisable( true );

        // Sets the location of each child
        board.relocate( 30, 50 );
        startButton.relocate( 460, 60 );
        dungeonA.relocate( 460, 200 );
        dungeonB.relocate( 460, 260 );
        dungeonC.relocate( 460, 320 );
        message.relocate( 30, 20 );

        // Manages the size of the buttons
        startButton.setManaged( false );
        startButton.resize( 150, 30 );
        dungeonA.setManaged( false );
        dungeonA.resize( 150, 30 );
        dungeonB.setManaged( false );
        dungeonB.resize( 150, 30 );
        dungeonC.setManaged( false );
        dungeonC.resize( 150, 30 );

        // Creates the pane, the application window and gives it a size.
        Pane root = new Pane();
        root.setPrefWidth(640); // the application's width
        root.setPrefHeight(480); // the application's height

        root.getChildren().addAll( board, startButton, message, dungeonA, dungeonB, dungeonC );
        root.setStyle( "-fx-background-color: lavender;"
                    +  "-fx-border-color: black;"
                    +  "-fx-border-width: 3" );

        Scene scene = new Scene( root );
        stage.setScene( scene );
        stage.setResizable( false );
        stage.setTitle( "A Rogue-like by Steven Chau!" );
        stage.show();
    }

    //Nested Classes

    /** Displays a canvas that is 400 x 400 pixels.
     */
    private class RogueBoard extends Canvas
    {
        DungeonData board;  // The data for the Dungeon
        boolean gameInProgress; // Stores the current state of the game
        String currentPlayer;   // Stores the current player's turn

        /** RogueBoard constructor.
         *  Creates a DungeonData to represent the contents of the Dungeon.
         */
        RogueBoard( )
        {
            super(400,400);  // canvas is 400 x 400 pixels
            board = new DungeonData( "dungeonA.txt" );
            //drawBoard();
        }

        /** Draws the board according to the DungeonData.
         *  Open space is represented by light gray tiles.
         *  Walls are represented by black tiles.
         *  The exit is represented by a yellow tile.
         *  The Rogue is represented by a Red circle with a white "R".
         *  The Monster is represented by a Green circle with a white "M".
         */
        public void drawBoard()
        {
            GraphicsContext g = getGraphicsContext2D();
            g.setFont( Font.font(18) );

            for ( int row = 0; row < 10; row ++ )
            {
                for ( int col = 0; col < 10; col++ )
                {
                    g.setFill( Color.LIGHTGRAY );
                    g.fillRect( 2 + col*40, 2 + row*40, 40, 40);
                    g.setStroke( Color.BLACK );
                    g.setLineWidth( 2 );
                    g.strokeRect(2 + col*40, 2 + row*40, 40, 40);


                    switch ( board.tileAt( row, col ))
                    {
                        case DungeonData.Wall:
                            g.setFill(Color.BLACK);
                            g.fillRect(2 + col*40, 2 + row*40, 40, 40);
                            break;

                        case DungeonData.Exit:
                            g.setFill(Color.YELLOW);
                            g.fillRect(2 + col*40, 2 + row*40, 40, 40);
                            break;

                        case DungeonData.Monster:
                            g.setFill(Color.GREEN);
                            g.fillOval(10 + col*40, 10 + row*40, 28, 28);
                            g.setFill(Color.WHITE);
                            g.fillText("M", 15 + col*40, 29 + row*40);
                            break;

                        case DungeonData.Rogue:
                            g.setFill(Color.RED);
                            g.fillOval(10 + col*40, 10 + row*40, 28, 28);
                            g.setFill(Color.WHITE);
                            g.fillText("R", 15 + col*40, 29 + row*40);
                            break;
                    }
                }
            }
        }

        /** Initializes the game
         *
         */
        void startGame()
        {
            System.out.println ( "Start");

            // Disable all the fields and buttons to not interfere with the board
            startButton.setDisable( true );
            dungeonA.setDisable( true );
            dungeonB.setDisable( true );
            dungeonC.setDisable( true );

            // Make it so that the key functions work on the canvas
            setFocusTraversable( true );
            message.setText ( "Game in progress. Your turn, Rogue." );
            gameInProgress = true;
            currentPlayer = "Rogue";
            message.setText ( "Your turn, Rogue." );
        }

        /** Creates a new DungeonData
         *
         */
        void loadDungeon( String dungeon )
        {
            board = new DungeonData( dungeon );
            drawBoard();
            startButton.setDisable( false );
        }

        void endGame( String type )
        {
            if ( type == "Victory" )
            {
                message.setText( "Congratulations. You have escaped the dungeon." );
            }
            else
            {
                message.setText( "Game Over. You have been caught by the monster.");
            }
            drawBoard();
            gameInProgress = false;
            setFocusTraversable( false );
            dungeonA.setDisable( false );
            dungeonB.setDisable( false );
            dungeonC.setDisable( false );
        }

        /** Responds to the user's keypress.
         *  If the game is in progress or if the player, the Rogue,
         *  is out of turn, an error message is displayed.
         *  Another error message will be displayed if the key press indicates
         *  movement towards a wall. Otherwise, the Rogue moves in accordance
         *  with the keypress.
         *  @param evt the keypress event
         */
        public void keyPressed( KeyEvent evt )
        {
            KeyCode key = evt.getCode();  // keyboard code for the pressed key
            System.out.println( "Key Pressed: " + key );  // for testing
            if ( gameInProgress != true )
            {
                message.setText( "Click \"Start\" to begin." );
            }
            else if ( currentPlayer != "Rogue" )
            {
                message.setText("Please, wait your turn.");
            }
            else if ( currentPlayer == "Rogue" )
            {
                if ( key == KeyCode.LEFT || key == KeyCode.RIGHT || key == KeyCode.UP || key == KeyCode.DOWN )
                {
                    Site next = board.rogueMove ( key );
                    if ( next.equals( board.exitSite ) )
                    {
                        board.changeTile( board.rogueSite.row(), board.rogueSite.col(), '0');
                        endGame( "Victory" );
                        System.out.println ("Victory");
                    }
                    else if ( !next.equals( board.rogueSite ) )
                    {
                        board.switchTile( board.rogueSite, next );
                        board.rogueSite = next;
                        drawBoard();
                        monsterTurn();
                    }
                    else
                    {
                        message.setText( "That is an illegal move. Try again." );
                    }
                }
                else
                {
                    message.setText( "Please input a valid command" );
                }
            }
        }

        /** Used after Rogue's turn has been completed. Changes the player to
         *  the monster and calls monsterMove() to find the monster's next move.
         *
         */
        void monsterTurn()
        {
            message.setText ( "Monster's Turn." );
            currentPlayer = "Monster";
            Site next = board.monsterMove();
            if ( next.equals( board.rogueSite ) )
            {
                board.changeTile( board.rogueSite.row(), board.rogueSite.col(), '0');
                drawBoard();
                System.out.println ( "Game Over." );
                endGame( "Defeat" );
            }
            board.switchTile( board.monsterSite, next );
            board.monsterSite = next;
            message.setText ( "Your turn, Rogue." );
            currentPlayer = "Rogue";
            drawBoard();
        }
    }

    /** An object of this class holds the data of dungeon of the Rogue-like.
     *  It knows what tiles are walls and are not along with the locations of
     *  the two players. Moreover, a method is provided that can check if
     *  movement from one tile to another is legal
     */
    private static class DungeonData
    {
        static final char
                    Open = '0',
                    Wall = '1',
                    Monster = 'M',
                    Rogue = 'R',
                    Exit = 'E';
        char [][] board; // board [ row ] [ col ]
        Site rogueSite;
        Site monsterSite;
        Site exitSite;

        /** DungeonData constructor. Creates and sets up the board
         *  No input defaults the dungeon to dungeonA.txt
         */
        /*
        DungeonData( v)
        {
            board = new int [ 10 ] [ 10 ];
            setUp();
        }
        */

        /** DungeonData constructor. Creates and sets up the board
         *  Input is determined by the user input in the text field.
         */
        DungeonData( String file )
        {
            board = new char [ 10 ] [ 10 ];
            setUp( file );
        }

        /** Sets up the board with the walls, the exit, and the players,
         *  the Monster and the Rogue.
         */
        void setUp( String file )
        {
            Input in = new Input( file );
            for ( int row = 0; row < 10; row++ )
            {
                String str = in.readLine();
                //System.out.println( str );
                for ( int col = 0; col < 10; col++ )
                {
                    board[ row ][ col ] = str.charAt( col );
                    //System.out.println(  str.charAt( col ) );
                    if ( board[ row ][ col ] == 'M' )
                    {
                        monsterSite = new Site( row, col );
                    }
                    if ( board[ row ][ col ] == 'R' )
                    {
                        rogueSite = new Site( row, col );
                    }
                    if ( board[ row ][ col ] == 'E' )
                    {
                        exitSite = new Site( row, col );
                    }
                }
            }
        }

        /** Finds the tile at a Site coordinate
         *  @param site the Site containing the row, col coordinate of the desired tile
         *  @return the tile's char value
         */
        char tileAt( Site site )
        {
            int row = site.row();
            int col = site.col();
            return board[ row ][ col ];
        }

        /** Finds the tile at a row, col coordinate
         *  @param row the row coordinate of the desired tile
         *  @param col the col coordinate of the desired tile
         *  @return the tile's int value
         */
        char tileAt( int row, int col  )
        {
            return board[ row ][ col ];
        }

        /** Changes the tile type of a selected tile
         *  @param row  the row coordinate of the selected tile
         *  @param col  the col coordinate of the selected tile
         *  @param newTileType the desired new type
         */
        void changeTile( int row, int col, char newTileType )
        {
            board [ row ] [ col ] = newTileType;
        }

        /** Exchanges the location of two tiles: current and destination.
         *  @param fromSite the row, col coordinate of the current tile
         *  @param toSite the row, col coordinate of the destination tile
         */
        void switchTile ( Site fromSite, Site toSite )
        {
            int fromRow = fromSite.row();
            int fromCol = fromSite.col();
            int toRow =  toSite.row();
            int toCol = toSite.col();
            char tile1 = tileAt ( fromRow, fromCol);
            char tile2 = tileAt ( toRow, toCol );
            board[ fromRow ][ fromCol ] = tile2;
            board[ toRow ][ toCol ] = tile1;
        }

        /** Checks if a move from the current tile to the destination tile is valid.
         *  If toSite is a Wall, then the move is not valid. If the tosite is
         *  the exitSite and the fromSite is the monster, the move is not valid.
         *  @param fromSite the row, col coordinate of the current tile
         *  @param toSite the row, col coordinate of the destination tile
         *  @return Legal the boolean value if the move is Legal
         */
        boolean isLegalMove ( Site fromSite, Site toSite )
        {
            int fromRow = fromSite.row();
            int fromCol = fromSite.col();
            int toRow =  toSite.row();
            int toCol = toSite.col();

            boolean Legal = false;
            if ( toRow >= 0 && toCol >= 0 && toRow <= 9 && toCol <= 9 )
            {
                if ( tileAt ( toSite ) != Wall )
                {
                    Legal = true;
                }
            }
            if ( fromSite.equals( monsterSite ) && toSite.equals( exitSite ) )
            {
                Legal = false;
            }
            // System.out.println( Legal );
            return Legal;
        }

        /** Checks for the shortest path to the rogueSite from this site
         *  Breadth First Search
         *  @return distance between the sites including pathing around obstacles
         */
        int shortestPathToRogue( Site fromSite )
        {
            int fromRow = fromSite.row();
            int fromCol = fromSite.col();
            int count = 0;
            Queue<int[]> nextTile = new LinkedList<>();
            nextTile.offer( new int[] { fromRow, fromCol } );
            Set<int[]> visitedTiles = new HashSet<>();
            Queue<int[]> temp = new LinkedList<>();

            while ( !nextTile.isEmpty() )
            {
                int[] position = nextTile.poll();
                int row = position[0];
                int col = position[1];

                if ( board[row][col] == 'R' )
                {
                    return count;
                }
                // check in bounds, then check if in visitedTiles, then check if not a wall
                if (row > 0 && !visitedTiles.contains( new int[] { row - 1, col } ) && board[ row - 1 ][ col ] != '1')
                {
                    temp.offer( new int[] { row - 1, col } );
                }
                if (row < board.length - 1 && !visitedTiles.contains( new int[] { row + 1, col } ) && board[ row + 1 ][ col ] != '1')   
                {
                    temp.offer( new int[] { row + 1, col } );
                }
                if (col > 0 && !visitedTiles.contains( new int[] { row, col - 1} ) && board[ row ][ col - 1 ] != '1')
                {
                    temp.offer( new int[] { row, col - 1 } );
                }
                if (col < board[ 0 ].length - 1 && !visitedTiles.contains( new int[] { row, col + 1 } ) && board[ row ][ col + 1 ] != '1')
                {
                    temp.offer(new int[] { row, col + 1 } );
                }
                if (nextTile.isEmpty() && !temp.isEmpty())
                {
                  nextTile = temp;
                  temp = new LinkedList<>();
                  count++;
                }
            }
            return count;
        }


        /** Used to obtain the next move for the monster
         *  Calls shortestPathToRogue() for each move the monster can make
         *  to determine the shortest current path available
         *  @return next the nextSite
         */
        Site monsterMove()
        {
            Site next = null;
            while ( next == null )
            {
                int toRow = monsterSite.row();
                int toCol = monsterSite.col();
                Site temp = new Site( toRow - 1, toCol ); // currently up
                Site up = isLegalMove(monsterSite, temp) ? temp : monsterSite;

                temp = new Site( toRow + 1, toCol );
                Site down = isLegalMove(monsterSite, temp) ? temp : monsterSite;

                temp = new Site( toRow, toCol - 1 );
                Site left = isLegalMove(monsterSite, temp) ? temp : monsterSite;

                temp = new Site( toRow, toCol + 1 );
                Site right = isLegalMove(monsterSite, temp) ? temp : monsterSite;

                Site vert = shortestPathToRogue( up ) < shortestPathToRogue( down ) ? up : down;
                Site hor = shortestPathToRogue( left ) < shortestPathToRogue( right ) ? left : right;

                if ( shortestPathToRogue( vert ) < shortestPathToRogue( hor ) )
                {
                    next = vert;
                }
                else
                {
                    next = hor;
                }
            }
            return next;
        }

        /** Used to obtain the next move for the rogue tile using user key input.
         *  If the arroy chosen is not legal, rogueSite will be returned
         *  @param key the KeyCode of an arroy key
         *  @return next the nextSite
         */
        Site rogueMove ( KeyCode key )
        {
            Site next = rogueSite;

            int toRow = rogueSite.row();
            int toCol = rogueSite.col();
            //System.out.println( "Rogue site: " + rogueSite.row() + ", " + rogueSite.col() );
            if (key == KeyCode.UP) // left arrow key
            {
                toRow--;
            }
            else if (key == KeyCode.DOWN) // right arrow key
            {
                toRow++;
            }
            else if (key == KeyCode.LEFT) // up arrow key
            {
                toCol--;
            }
            else if (key == KeyCode.RIGHT) // down arrow key
            {
                toCol++;
            }
            Site site = new Site( toRow, toCol );
            if ( isLegalMove( rogueSite, site ) )
            {
                next = site;
            }
            //System.out.println( "Next site: " + next.row() + ", " + next.col() );
            return next;
        }
    }
}