package core;

import ui.GridLayout;
import ui.CBEntry;
import ui.Checker;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.StringProperty;
import core.CheckersLogic.CheckersGame;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * CheckersBoard class stores functionality for a CheckerBoard GUI object
 * Extends GridLayout, passes control to CheckersLogic for execution of 
 * game moves.
 * @author Melonie Miller
 * @version 1
 *
 */
public class CheckerBoard extends GridLayout {

		/**	Constant int for red checkers	 */
		public final int RED = 1;
		/**	Constant int for black checkers	 */
		public final int BLACK = 0;
		/**	Constant int for empty space	 */
		public final int EMPTY = -1;
		/**Int representing a player-- 0 for black, 1 for red	 */
		public int player;
		/**	Current board object	 */
		static CheckerBoard newBoard;
		
		/**Current checkersGame board*/
		CheckersGame boardArray;
		
		/**Current CheckersLogic object*/
		CheckersLogic gameData;
		
		/**Number of pieces of RED checkers left.*/
		public int numPiecesX;
		
		/** Number of pieces of BLACK checkers left. */
		public int numPiecesO;
		
		/** returns true if the game is a PC game */
		boolean isPC;
		
		/** current selected player for game */
		int currentPlayer;
		
		/**Constant for tile dimensions */
		final static int TILEDIMENSIONS = 100;
	
	    /** Keep track of the turn and alert GUIMove when it changes */
	    private StringProperty turn;
	    


	   /**
	    * Instantiates and creates board-- adds tiles and checkers and initializes number of pieces.
	    * @return
	    */
	    public CheckerBoard createBoard() {
	        newBoard.numPiecesO = 12;
	        newBoard.numPiecesX = 12;
	        newBoard.setTiles();
	        newBoard.addCheckers();
	        return newBoard;
	    }
	    
	    
	    //create 8x8 Grid 
	    //wrap in a factory method
	    /**
	     * Checkerboard constructor for a PC opponent single player game.
	     * @param isPC
	     * @param playerNumber
	     */
	    public CheckerBoard() {
	        super();
	        newBoard = this;	

	        currentPlayer = 1;
	    }

		/**
	     * Adds checkers to the board at a certain position.
	     */
	    private void addCheckers() {
	        String color = "red";
	        //Only initiailze the first and last three rows with checkers
	        for(int j=0; j<8; j++){
	            if(j > 2 && j <5) {
	                color = "black";
	                continue;
	            }
	            for(int i=0; i<8; i+=2) {
	                if(j%2 == 0) this.add(new Checker(this,color), i+1, j);
	                else this.add(new Checker(this,color), i, j);
	            }
	        }
	    }
	    
	    /**
	     * Sets layout of the tiles.
	     */
	    private void setTiles() {
	        for(int j=0; j<8; j++) {
	            for(int i=0; i<8; i++) {
	                Rectangle rect = new Rectangle();

	                rect.widthProperty().bind(getChildCellWidthProperty());
	                rect.heightProperty().bind(getChildCellHeightProperty());
	              
	             
	                if(j%2 == 0) {
	                    if(i%2 == 0) {
	                        rect.setFill(Color.MAROON);
	                        add(rect, i, j);
	                    }
	                    else {
	                        rect.setFill(Color.BEIGE);
	                        add(rect, i, j);
	                    }
	                }
	                else {
	                    if(i%2 == 0) {
	                        rect.setFill(Color.BEIGE);
	                        add(rect, i, j);
	                    }
	                    else {
	                        rect.setFill(Color.MAROON);
	                        add(rect, i, j);
	                    }
	                }
	            }
	        }
	    }
	   
	    /**
	     * Transfers moves of mouse events to GUIMove class
	     * @param <T> Class extending Mouse events acted upon
	     * @param e object of type T
	     */
	    public <T extends MouseEvent> void switchControl(T e) {
	       try {
	    	   gameData.recieveControl(e);      
	       }
	       catch(NullPointerException f) {
	    	   System.out.println("CheckersLogic object has not been initialized.");
	    	   f.printStackTrace();
	       }
	    }
	    
	    

	    /**
	     * Gets the turn property
	     * @return StringProperty turn
	     */
	    public StringProperty getTurnProperty() {
	        return turn;
	    }
	    /**
	     * Gets the turn value (string)
	     * @return String turn
	     */
	    public String getTurn() {
	        return turn.getValue();
	    }
	    
	    /**
	     * Gets the GUIMove object
	     * @return move
	     */
	    public CheckersLogic getGUIMove() {
	        return CBEntry.game;
	    }
	    
	    /**
	     * Sets the GUIMOVE object
	     * @param pGameData GUIMove object being passed
	     */
	    public void setGUIMove(CheckersLogic pGameData) {
	        this.gameData = pGameData;
	    }
	}

