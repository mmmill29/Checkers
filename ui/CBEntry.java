package ui;

import java.util.Scanner;

import core.CheckerBoard;
import core.CheckersLogic;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * CBEntry is the entry point of the Checkers Program. Creates GUI
 * components and handlers for buttons.
 * @author Melonie Miller
 * @version 1.0
 * Completion Time: 42 hours
 *
 */
public class CBEntry extends Application {

	/**Field for determining a difficulty level for the game. Used with buttons
	 * to store data of the click */
	public static int difficultyLevel;
	
	/**if true, represents that game being played is a Player-vs-Computer game. If false,
	 * the game is assumed to be multiplayer. */
	public boolean isPC;
	
	/**Current player number to be passed to further class constructors. Values can be 1 (RED) or 0 (BLACK).
	 */
	public int player; 
	
	/**CheckersLogic object used for completing game moves.*/
	public static CheckersLogic game;
	
	/**Text displaying current player's turn*/
	public static Text turn; 
	
	/**Scene for resigning from game.*/
	Scene resignScene;
	
	/**Scene for setting difficulty level.*/
	Scene difficultyLevelScene;
	
	Text wins = new Text();
	
	/**
	 * Buttons for determining difficulty level
	 */
	Button dLevel1 = new Button();
	Button dLevel2 = new Button();
	Button dLevel3 = new Button();
	
    //create window, add checkerboard, start game loop
	@Override
    public void start(Stage primaryStage) {
	try {	
		
        Text turn = new Text();
        
        
		Font font = new Font(15);
		primaryStage.setTitle("Checkers");
    		
 
    	VBox layoutGO = new VBox();
    	Text gameOver = new Text();
    	gameOver.setFont(new Font(30));
    	gameOver.setText("Game over!");
    	layoutGO.getChildren().add(gameOver);
    	Scene gameOverScene = new Scene(layoutGO, 500,300);
    	gameOverScene.setFill(Color.GREY);
        FlowPane startingElems = new FlowPane();
        startingElems.prefHeight(100);
       
        
        //Create buttons
        Button multiplayer = new Button();
        Button pc = new Button();
        Button red = new Button();
        red.setText("RED");
        red.setFont(font);
        Button black = new Button();
        black.setText("BLACK");
        black.setFont(font);
        Label gameType = new Label("Choose game type: ");
        gameType.setFont(new Font(30));
       
        pc.setText("PC Opponent");
        pc.setFont(font);
        multiplayer.setFont(font);
        multiplayer.setText("Multiplayer");
        
        //Add buttons to vBox
        startingElems.getChildren().add(gameType);
        startingElems.getChildren().add(multiplayer);
        startingElems.getChildren().add(pc);
        
        
       //Create resign button
        Button resign = new Button();
        resign.setText("Resign");
        resign.setFont(font);
    
        
        //Add elements for top of screen (Title, Resign button)
        Text title = new Text();
        HBox titlePane = new HBox();
        FlowPane titleText = new FlowPane();
        title.setText("CHECKERS GAME");
        titleText.getChildren().add(title);
        titleText.setAlignment(Pos.BASELINE_CENTER);
    	title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(new Font(25));
        titlePane.setPrefHeight(30);
        titlePane.getChildren().add(titleText);
    
        
        //Create pieces text object (set in handlers)
        Text pieces = new Text();
    	pieces.setFont(font);
    	
    	
    	//Main pane to place checkerboard
    	BorderPane main = new BorderPane();
    	CheckerBoard board = new CheckerBoard();
    	board.createBoard();
    

		main.setTop(titlePane);
    	main.setCenter(board);
    	main.setBottom(startingElems);
    
    	startingElems.setPrefHeight(50);
    	Stage optionScreen1 = new Stage();
    	Stage optionScreen2 = new Stage();
    	Scene scene = new Scene(main, 700, 800);
    	scene.setFill(Color.GREY);
        board.bindToParent();
    	primaryStage.setScene(scene);
    	
        
        primaryStage.show();
        
    	if(board.numPiecesO == 0 || board.numPiecesX == 0) {
    	if(board.numPiecesO == 0) {
    		game.gameOver("");
    		wins.setText("Black out of moves. Red wins!");
    	}
    	if(board.numPiecesX == 0) {
    		wins.setText("Red out of moves. Black wins!");
    		game.gameOver("");
    	}
    	layoutGO.getChildren().add(wins);
    	Stage goStage = new Stage();
    	gameOverScene.setFill(Color.GREY);
    	goStage.setScene(gameOverScene);
    	goStage.show();
    	}
        
        multiplayer.setOnAction(e -> {
        	isPC = false;
        	difficultyLevel = 0;
        	startingElems.getChildren().clear();
        	resign.setAlignment(Pos.BASELINE_LEFT);
        	FlowPane piecesText = new FlowPane();
        	piecesText.getChildren().add(pieces);
        	piecesText.setAlignment(Pos.BASELINE_RIGHT);
        	startingElems.getChildren().add(piecesText);
        	game = new CheckersLogic(true, board);
        	board.setGUIMove(game);
        	
        	

        	pieces.setText("RED Pieces: " + board.numPiecesX + "   BLACK Pieces: " 
        	    	+ board.numPiecesO);
 
        });
        
        /**
         * Anonymous class for PC button. Creates Options to choose player type.
         */
        pc.setOnAction(e ->{
        	isPC = true;
			FlowPane final1 = new FlowPane();
			Text playerSelect = new Text();						//Create text obj for selecting player
			playerSelect.setText("Choose pieces to play:");		//Set text for player select
			playerSelect.setFont(new Font(25));

			VBox vb1 = new VBox();								//Create VBox for text/buttons
			HBox hb1 = new HBox();								//Create HBox to nest buttons
			hb1.getChildren().add(red);							//Add red button to HBox
			hb1.getChildren().add(black);						//Add black button to HBox
			hb1.setAlignment(Pos.CENTER);
			vb1.getChildren().add(playerSelect);				//Add text to VBox
			vb1.getChildren().add(hb1);							//Add Hbox to VBox (under text)
			vb1.setAlignment(Pos.CENTER);
			final1.getChildren().add(vb1);
			final1.setAlignment(Pos.CENTER);
			Scene finalScene = new Scene(final1, 400,400);
			finalScene.setFill(Color.GREY);
			optionScreen1.setScene(finalScene);	//Set stage to contain pane
			optionScreen1.show();
			
			
		 	

        });
        
        
        /*
         * If red button is pressed, close the first option window
         * Create board object with pc = true, player = 1 (red). Call helperMove()
         * Create scene to bind these elements to, and set primary stage to 
         * display the board.
         */
        red.setOnAction(e->{
        	player = 1;
        	optionScreen1.close();
	    	pieces.setText("RED Pieces: " + board.numPiecesX + "   BLACK Pieces: " 
	    	    	+ board.numPiecesO);

         
            Text dLevel = new Text();
            dLevel.setText("Choose difficulty Level");
            dLevel.setFont(new Font(15));
          
            dLevel1.setText("1");
          
            dLevel2.setText("2");
          
            dLevel3.setText("3");
            VBox diffBox = new VBox();
            diffBox.getChildren().addAll(dLevel,dLevel1, dLevel2,dLevel3);
            Scene diffScene = new Scene(diffBox);
            diffScene.setFill(Color.GREY);
            optionScreen2.setScene(diffScene);
            optionScreen2.show();
        
        });
        
        
        /*
         * If black button is pressed, close the first option window
         * Create board object with pc = true, player = 0. Call helperMove()
         * Create scene to bind these elements to, and set primary stage to 
         * display the board.
         */
        black.setOnAction(e->{
        	player = 0;
        	optionScreen1.close();
            
            Text dLevel = new Text();
            dLevel.setText("Choose difficulty Level");
            dLevel.setFont(new Font(15));
  
            dLevel1.setText("1");
      
            dLevel2.setText("2");
       
            dLevel3.setText("3");
            VBox diffBox = new VBox();
            diffBox.setAlignment(Pos.BASELINE_CENTER);
            diffBox.getChildren().addAll(dLevel,dLevel1, dLevel2,dLevel3);
            Scene diffScene = new Scene(diffBox);
            diffScene.setFill(Color.DARKGREY);
            optionScreen2.setScene(diffScene);
            optionScreen2.show();
        });
        
        
        dLevel1.setOnAction(e->{
        	difficultyLevel = 1;
        	optionScreen2.close();
        	game = new CheckersLogic(player,board,difficultyLevel);
        	board.setGUIMove(game);
	    	pieces.setText("RED Pieces: " + board.numPiecesX + "   BLACK Pieces: " 
	    	    	+ board.numPiecesO);
	    	pieces.setFont(new Font(20));
	    	pieces.setTextAlignment(TextAlignment.CENTER);
	     	startingElems.getChildren().clear();
        	startingElems.getChildren().add(pieces);
            titlePane.getChildren().add(resign);
            resign.setAlignment(Pos.BASELINE_RIGHT);
	 
            startingElems.getChildren().add(turn);
        });
        
        dLevel2.setOnAction(e->{
        	difficultyLevel = 2;
        	optionScreen2.close();
        	game = new CheckersLogic(player,board,difficultyLevel);
        	board.setGUIMove(game);
	    	pieces.setText("RED Pieces: " + board.numPiecesX + "   BLACK Pieces: " 
	    	    	+ board.numPiecesO);
	    	pieces.setFont(new Font(20));
	     	startingElems.getChildren().clear();
	     	startingElems.setAlignment(Pos.CENTER);
	    	pieces.setTextAlignment(TextAlignment.CENTER);
        	startingElems.getChildren().add(pieces);
        	resign.setAlignment(Pos.BASELINE_RIGHT);
            titlePane.getChildren().add(resign);
	 
            startingElems.getChildren().add(turn);
        });
        
        dLevel3.setOnAction(e->{
        	difficultyLevel = 3;
        	optionScreen2.close();
            titlePane.getChildren().add(resign);
        	game = new CheckersLogic(player,board,difficultyLevel);
        	board.setGUIMove(game);
	    	pieces.setText("RED Pieces: " + board.numPiecesX + "   BLACK Pieces: " 
	    	    	+ board.numPiecesO);
	    	pieces.setFont(new Font(20));
	    	pieces.setTextAlignment(TextAlignment.CENTER);
	    	resign.setAlignment(Pos.BASELINE_RIGHT);
	     	startingElems.getChildren().clear();
	     	startingElems.setAlignment(Pos.CENTER);
        	startingElems.getChildren().add(pieces);
         
	 
            startingElems.getChildren().add(turn);
        });
        
        /**
         * Anonymous class for resign button. Ends the game and prompts a box
         * telling the user who won.
         */
        resign.setOnAction(f->{
        	if(game.gameInProgress == true) {
        		if(player == 1) game.gameOver("Red resigns. Black wins!");
        		if(player == 0) game.gameOver("Black resigns. Red wins!");
        		
        		if(player==1) {
        			Label redLose = new Label("Red resigns. Black wins!");
        			redLose.setFont(new Font(30));
        			VBox resignBox = new VBox();
        			resignBox.getChildren().add(redLose);	
        			resignScene = new Scene(resignBox, 400,400);
        			primaryStage.setScene(resignScene);
        			primaryStage.show();
        		}
        		if(player == 0) {
        			Label blackLose = new Label("Black resigns. Red wins!");
        			blackLose.setFont(new Font(30));
        			VBox resignBox = new VBox();
        			resignBox.getChildren().add(blackLose);
        			resignScene = new Scene(resignBox, 400,400);
        			primaryStage.setScene(resignScene);
        			primaryStage.show();
        		}
        	}
        });
        
        
    }
	catch(NullPointerException e) {
		System.out.println("The object may not have initialized. Please enter option buttons before "
				+ "trying to play.");
		e.printStackTrace();
		
	}

	}
	
	
	public static void main(String[] args){
		System.out.println("Enter ‘G’ for JavaFX GUI or ’T’ for text UI:");
				Scanner scan = new Scanner(System.in);
				char selection = scan.next().charAt(0);
				if (selection == 'G') 
				   Application.launch();
				else if (selection == 'T') 
				   new CheckersTextConsole();
				else
				   System.out.println("Invalid Entry! Try again. \n");
				
				while(selection != 'G' && selection != 'T') {
					selection = scan.next().charAt(0);
					if (selection == 'G') {
						   Application.launch();
							break;
					}
						else if (selection == 'T') {
						   new CheckersTextConsole();
						   break;
						}
						else
						   System.out.println("Invalid Entry! Try again. \n");
	}
				
					scan.close();
	}
	
}

