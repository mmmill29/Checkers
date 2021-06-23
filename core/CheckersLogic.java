package core;

import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import ui.Checker;




/**
 * @author Melonie Miller
 * {@summary Logic based component of Checkers game. Validates game moves
 * and contains information for movement of pieces. Contains multiple logic 
 * validation methods (boolean) to validate moves, as well as methods to make 
 * and display moves on the board.}
 * 
 * @version 6.07.21.2
 * Completion time: 22.5 hours (Version 1)
 * Completion time: 23.75 hours (Version 2)
 * Completion time: 40.2 Hours (Version 3)
 *
 */
public class CheckersLogic {


	
	/**	 PLAYER_O: constant value for player O (0) */
	
	/**PLAYER_O: constant value for player O (0) */
	public final int PLAYER_O = 0;
	
	/** PLAYER_X: constant value for player X (1) 	 */
	public final int PLAYER_X = 1;
	
	/** SYMBOL_O: symbol 'o' for player o checker	 */
	public final char SYMBOL_O = 'o';
	
	/** SYMBOL_X: symbol 'x' for player x checker */
	public final char SYMBOL_X = 'x';
	
	/** startingPieces: starting number of pieces in game	 */
	private int startingPieces = 0;

	/** number of pieces player o has */
	public int numPiecesO;
	
	/** Marked true when given inputs are valid to be passed to other
	 * logic components.
	 */
	boolean isValid;
	
	/** number of pieces player x has */
	public int numPiecesX;
	
	/** Current player for moving pieces	 */
	public int currentPlayer;
	
	/** boolean t/f if game is in progress already.	 */
	public boolean gameInProgress;
	
	/** Represents checker board and functionality to populate board. */
	public CheckersGame board;
	
	/** Game difficulty level declared at start of game.*/
	public final int difficultyLevel;

	/** Boolean to tell if game is a PC opponent or multiplayer game.*/
	public boolean isPC;
	
	/** Stores the number of the computer player.*/
	public int computerPlayer;
	
	/** Checkerboard object that game is played on.*/
	public CheckerBoard checkerBoard;
	
	/** Recently moved checker. */
	public Checker cleared; 
	
	/** List of all valid moves for a piece. */
	public Vector<Move> validMoves;
	
	/**
	 * Controls code that should not be executed if the game is played with GUI.
	 */
	public boolean isGUI;


	/** Used for GUI controls-- column being moved from.*/
	private int prevCol;

	/** Used for GUI controls-- row being moved from.*/
	private int prevRow; 
	
	/**Object holding data and methods for checkers computer player*/
	private CheckersComputerPlayer opponent;
	
	/** Used to pass column to takePiece from MouseEvent. */
	private int moveCol;
	
	/** Used to pass row to takePiece from MouseEvent. */
	private int moveRow;
	
	
	/**
	 * Default constructor for CheckersLogic used for multiplayer games 
	 * using text console. Takes no parameters, instantiates all fields
	 * necessary for a text console multiplayer game.
	 */
	public CheckersLogic() {
		currentPlayer = PLAYER_X;
		numPiecesX = 12;
		numPiecesO = 12;
		board = new CheckersGame();
		isPC = false;
		isGUI = false;
		difficultyLevel = 0;
		startNewGame();
	}
	
	/** Constructor for GUI multiplayer game. Instantiates all values needed
	 * to start a new GUI multiplayer game.
	 * @param isGUI-- true if it is a GUI game
	 * @param cb CheckerBoard object referred to for GUI game
	 */
	public CheckersLogic(boolean isGUI, CheckerBoard cb) {
		numPiecesX = 12;
		checkerBoard = cb;
		numPiecesO = 12;
		board = new CheckersGame();
		this.isGUI = true;
		this.difficultyLevel = 0;
		isPC = false;
		currentPlayer = PLAYER_X;
		startNewGame();
		
		
	}
	
	/**
	 * Parameterized constructor for a TextConsole PC Game.
	 * Instantiates all fields needed to start a new game.
	 * @param player current player playing against PC
	 * @param isPC returns true (is PC Game)
	 * @param difficultyLevel current difficulty level selected by player
	 */
	public CheckersLogic(int player, boolean isPC, int difficultyLevel) {
		currentPlayer = player;
		board = new CheckersGame();
		isGUI = false;
		this.difficultyLevel = difficultyLevel;
		this.isPC = true;
		startNewGame();

		
	}
	
	/**
	 * Parameterized Constructor for GUI PC Game.
	 * Initializes CheckersGame object (board) for a computer opponent game, which contains the checker board.
	 * Initialize number of pieces for each player, and starting pieces. Calls
	 * startNewGame() to display board.
	 * @param diffLevel 1-3, difficulty level for AI game
	 * @param player, current player of the game.
	 * @param cb for the checkerBoard object.
	 */
	public CheckersLogic(int player, CheckerBoard cb, int diffLevel) {
		
		checkerBoard = cb;	
		board = new CheckersGame();
		currentPlayer = PLAYER_X;
		isGUI = true;
		numPiecesX = 12;
		numPiecesO = 12;
		startingPieces = 12;
		if(diffLevel !=0) isPC = true;
		else if (diffLevel == 0) isPC = false;
		if(isPC) this.difficultyLevel = diffLevel;
		else difficultyLevel = 0;
		startNewGame();
		if(player == 1) {
			opponent = new CheckersComputerPlayer(0);
			computerPlayer = 0;
		}
			
		else if(player ==0) {
			computerPlayer = 1;
			opponent = new CheckersComputerPlayer(computerPlayer);
			opponent.computerMove(computerPlayer);
		}
	
	}
	
	/**
	 * Starts the game. Checks if game is already in progress. Should not happen.
	 * Otherwise, sets gameInProgress to ture and displays board.
	 */
	public void startNewGame() {
		if(gameInProgress==true) {
		if(!isGUI) {
			System.out.println("Cannot start game while game is already running");
			return;
		}
		}
		
		else {
			gameInProgress= true;
			if(!isGUI)
			board.displayBoard();
		}
		
	}
	
	/**
	 * Returns the current player
	 * @return int player type (0 or 1)
	 */
	public int getPlayer() {
		return currentPlayer;
	}
	
		
	
	
		/**
		 * Takes a player and their move and decides to jump or move.
		 * Splits string for move into rows and cols.
		 * @param player int, player number corresponding to x or o
		 * @param move String  representation of move (oldRowOldCol, newRowNewCol)
		 */
		public void helperMove(int player, String move) {
			
			if(!hasMove(player, move)) {
				if(currentPlayer == PLAYER_X) gameOver("Player "  + SYMBOL_X + " is out of moves. Player o wins!.");
				if(currentPlayer == PLAYER_O) gameOver("Player " + SYMBOL_O + " is out of moves. Player x wins!");
			}
				
				String oldRow = move.substring(0,1);
				String oldCol = move.substring(1,2);
				String newRow = move.substring(3,4);
				String newCol = move.substring(4,5);
				int oldRowInt = convertToInt(Integer.parseInt(oldRow));
				int oldColInt = convertToInt(oldCol);
				int newRowInt = convertToInt(Integer.parseInt(newRow));
				int newColInt = convertToInt(newCol);
				if(moveIsJump(player, oldRowInt, oldColInt, (oldRowInt+newRowInt)/2, (oldColInt+newColInt)/2, newRowInt, newColInt))
					takePiece(player, oldRowInt, oldColInt, newRowInt, newColInt);
				else {
					helperMove(player, oldRowInt, oldColInt, newRowInt, newColInt);
				}
		}
			
			
		
		/**
		 * Helper method for helperMove, responsible for actually moving checker
		 * Takes the player number, starting row and col and ending row and col,
		 * modifies the board. Handles jump case.
		 * @param player player piece being moved
		 * @param fromR int starting row
		 * @param fromC int starting col
		 * @param toR int row to move to
		 * @param toC int col to move to
		 */
		public void helperMove(int player, int fromR, int fromC, int toR, int toC) {
			try {
		if(hasMove(player, fromR, fromC)) {
			if(player == PLAYER_X) {
				if(numPiecesO == 0) { gameOver("Player O has no pieces. X wins!");
					return;
				}
				}
				if(numPiecesX == 0) {
					gameOver("Player X has no pieces. O wins!");
					return;
				}
				
			else if(player == PLAYER_O) {
				if(numPiecesO == 0) {
					gameOver("Player O has no pieces. X wins!");
					return;
				}
				if(numPiecesX == 0) {
					gameOver("Player X has no pieces. O wins!");
					return;
				}
				
				}
				
			//Set new position to char at old position
			board.board[toR][toC] = board.board[fromR][fromC];
			//Set old position to "_"
			board.board[fromR][fromC] = '_';
			
			//Jump 
			if(fromR-toR == 2 || fromR-toR == -2) {
				int jumpC = (fromC + toC)/2;
				int jumpR = (fromR + toR)/2;
			
				//Set intermed. jump space empty
				board.board[jumpR][jumpC] = '_';
				if(player == PLAYER_X) {
					numPiecesO-= 1;
					System.out.println("O pieces left: " + numPiecesO);

				}
				else if(player == PLAYER_O) {
					numPiecesX -= 1;
					System.out.println("X pieces left: " + numPiecesX);

				}
				
				if(player == PLAYER_X) player = PLAYER_O;
				if(player == PLAYER_O) player = PLAYER_X;
			}
		}
		}
		
			catch(IndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println("An out of bounds index was entered: ");
				if(fromR <0) System.out.println(fromR);
				if(fromC <0) System.out.println(fromC);
				if(toR <0) System.out.println(toR);
				if(toC <0) System.out.println(toC);
			}
		
			
		}

		
		/**
		 * Check if game is in progress
		 * @return boolean 
		 */
		public boolean gameInProgress() {
			return gameInProgress;
		}
		
		/**
		 * Boolean to determine if a move is valid. Passsed from a helper boolean, validMove,
		 * with different parameters. Uses multiple
		 * helpers to validate that a move is legal.
		 * @param player current player
		 * @param row1 beginning row (before move)
		 * @param col1 beginning column (before move)
		 * @param row2 ending row (after move)
		 * @param col2 ending column (after move)
		 * @return boolean t/f if it is a valid move.
		 */
		public boolean validMove(int player, int row1, int col1, int row2, int col2){
			
			if(positionChecker(row1, col1, row2, col2))
					if(checkMoves(player, row1, col1, row2, col2))
						if(moveSelection(player, row1, col1, row2, col2))
							return true;
			
			return false;
		}
		
		
		/**
		 * Called to check that the checker can be moved legally. Passes control
		 * to a method that validates the given positions extracted from the string
		 * move.
		 * @param player current player
		 * @param move String, concatenated string form of move
		 * @return boolean t/f if move is valid, passes to above validMove method.
		 */
		public boolean validMove(int player, String move) {
			if(player == PLAYER_O) startingPieces = numPiecesO;
			
			if(player == PLAYER_X) startingPieces = numPiecesX;
			
			
			String oldRow = move.substring(0,1);
			String oldCol = move.substring(1,2);
			String newRow = move.substring(3,4);
			String newCol = move.substring(4,5);
			
			int oldRowInt = convertToInt(Integer.parseInt(oldRow));
			int oldColInt = convertToInt(oldCol);
			int newRowInt = convertToInt(Integer.parseInt(newRow));
			int newColInt = convertToInt(newCol);

			return validMove(player, oldRowInt, oldColInt, newRowInt, newColInt);
		}
		
		/**
		 * Checks if player has any moves left on the board.
		 * If not, returns false. (Game will be ended in above methods).
		 * 
		 * @param player current player.
		 * @param move current position to check for moves
		 * @return true if player has moves left. False if no moves left (game over). 
		 */
		public boolean hasMove(int player, String move) {
			
			String oldRow = move.substring(0, 1);
			String oldCol = move.substring(1,2);
			String newRow = move.substring(3,4);
			String newCol = move.substring(4,5);
			
			int row1 = convertToInt(Integer.parseInt(oldRow));
			int col1 = convertToInt(oldCol);
			int row2 = convertToInt(Integer.parseInt(newRow));
			int col2 = convertToInt(newCol);
			
			if(moveIsJump(player, row1, col1, Math.abs((row1+row2)/2), Math.abs((col1+col2)/2), row2, col2)) {
				return true;
			}
			else if(validMove(player, move) || validMove(player, row1, col1, row1+1, col1+1)
					|| validMove(player, row1, col1, row1-1, col1+1) ||
					validMove(player, row1, col1, row1-1, row1-1) || validMove(player, row1, col1, row1+1, col1-1))
				return true;
			
			else {
				return false;
			}
		}
		
		/**
		 * Checks if the player has any move from a given location by
		 * checking if there are any valid moves for the piece.
		 * hasMove method for GUI
		 * @param player current player
		 * @param row current selected row
		 * @param col current selected col 
		 * @return true if the piece has a move left.
		 */
		public boolean hasMove(int player, int row, int col) {
			if(moveIsJump(player, row,col, row+1, col+1, row+2,col+2) || moveIsJump(player, row, col, row-1,col+1, row-2,col+2)|| moveIsJump(player,row,col,row+1, col-1,row+2,col-2) || moveIsJump(player, row, col, row-1,col-1,row-2,col-2))
				return true;
			else if(validMove(player, row, col, row+1, col+1) || validMove(player, row, col, row-1, col+1) || validMove(player, row, col, row+1, col-1) || validMove(player, row, col, row-1,col-1))
				return true;
			else return false;
		}
		
		/**
		 * Verify that user is not moving to an invalid square. Checks both old and new
		 * positions for correct value.
		 * @param oldRow row position moved from
		 * @param oldCol col position moved from
		 * @param newRow row position moved to
		 * @param newCol col position moved to
		 * @return boolean true if move position is valid
		 */
		public boolean positionChecker(int oldRow, int oldCol, int newRow, int newCol){
		
			if(oldRow%2 == 0 && oldCol%2 ==0) {
				if(!isPC)
					System.out.println("Invalid move: cannot move to this square.");
				return false;
			}
			if(newRow%2 == 0 && newCol%2 == 0){
				if(!isPC)
				System.out.println("Invalid move: cannot move to this square.");
				return false;
			}
			
			return true;
		}
			
		/**
		 * Verifies move is in the right format (length of string is 5, format: (row,col),(row,col))
		 * @param move string move passed to method
		 * @return boolean true if move string is in the right format
		 */
			public boolean formatChecker(String move) {
				int length = move.length();
				if(length != 5) { System.out.println("Input is not the correct length.");
				return false;
				}
				
				char oldRowC = move.charAt(0);
				char oldColC = move.charAt(1);
				char dash = move.charAt(2);
				char newRowC = move.charAt(3);
				char newColC = move.charAt(4);
				
				//Check row inputs are in correct format
				if(oldRowC < '1' || oldRowC > '8' || newRowC < '1' || newRowC > '8') {
					System.out.println("Enter a row between 1 and 8.");
					return false;
				}
				
				//Check column inputs are in correct format
				if(oldColC < 'a' || oldColC > 'h' || newColC < 'a' || newColC > 'h') {
					System.out.println("Enter a column value between a and h.");
					return false;
				}
				
				//Check that column is inputed correctly.
				if(dash < '-' || dash > '-') {
					System.out.println("Middle of inputs must be a '-'. Please try again.");
					return false;
				}
				
				return true;
			}
			
			/**
			 * Check that player is not attempting to move opponent pieces or empty space.
			 * @param player current player
			 * @param oldRow position being moved from (row)
			 * @param oldCol position being moved from (column)
			 * @param newRow position being moved to (row) 
			 * @param newCol position being moved to (column)
			 * @return boolean true if move is valid/not moving opponent pieces/empty spaces.
			 */
			public boolean moveSelection(int player, int oldRow, int oldCol, int newRow, int newCol) {
				if(newRow < 0 || newCol < 0) return false; 
				
				char symbol = board.board[oldRow][oldCol];
				char symbol2 = board.board[newRow][newCol];
				
				if(player == PLAYER_X && symbol2 != '_') {
					if(!isPC) System.out.println("Cannot move onto another piece!");
					return false;
				}
				
				if(player == PLAYER_O && symbol2 != '_') {
					if(!isPC) System.out.println("Cannot move onto another piece!");
					return false;
				}
				
				if(player == PLAYER_X && symbol == SYMBOL_O) {
					if(!isPC)
					System.out.println("Invalid move: cannot select opponent pieces.");
					return false;
				}
				if(player == PLAYER_O && symbol == SYMBOL_X) {
					if(!isPC)
					System.out.println("Invalid move: cannot select opponent piece.");
					return false;
				}
				
				if(symbol == '_') {
					if(!isPC)
					System.out.println("Invalid move: Cannot select empty space.");
					return false;
				}
				
				return true;
			}
			
			/**
			 * Checks pieces are only moved diagonally. Also checks that player does
			 * not move an invalid distance. Also updates number of pieces when
			 * appropriate.
			 * @param player current player
			 * @param oldRow row moved from
			 * @param oldCol column moved from
			 * @param newRow row moved to
			 * @param newCol column moved to
			 * @return boolean true if move passes conditions.
			 */
			public boolean checkMoves(int player, int oldRow, int oldCol, int newRow, int newCol) {
			try {	
				//Check move is diagonal
				if(oldRow == newRow || oldCol == newCol) {
					if(!isPC)
					System.out.println("Invalid move: moves must be to diagonal spaces.");
					return false;
				}
				
				int distRow = Math.abs(oldRow - newRow);
				int distCol = Math.abs(oldCol - newCol);
				
				//Check move is not more than 2 spaces.
				if(distRow > 2 || distCol > 2) {
					if(!isPC)
					System.out.println("Invalid move: Must be between 1 and 2 spaces.");
					return false;
				}
				
				//Check move is not backwards
					if((player == PLAYER_O && oldRow < newRow &&
							(board.board[Math.abs((newRow+oldRow)/2)][Math.abs((newCol + oldCol)/2)]) != SYMBOL_X) ||
							(player == PLAYER_X && newRow < oldRow && (board.board[Math.abs((newRow+oldRow)/2)][Math.abs((newCol + oldCol)/2)]) != SYMBOL_O)) {
					if(!isPC)
						System.out.println("Invalid move: Cannot go back unless taking opponent piece.");
					return false;
				}
				
					//Check O jump moves
				if(player == PLAYER_O) {
					if(distRow > 1) {
						char symbol = board.board[Math.abs((newRow + oldRow)/2)][Math.abs((newCol+oldCol)/2)];
						if(symbol != SYMBOL_X) {
							if(!isPC)
							System.out.println("Invalid move: cannot move more than one space unless taking a piece.");
							return false;
						}
						else {
							return true;
						}
					}
				}
				
				if(player == PLAYER_X) {
					if(distRow > 1) {
						char symbol = board.board[Math.abs((newRow + oldRow)/2)][Math.abs((newCol+oldCol)/2)];
						if(symbol != SYMBOL_O || board.board[newRow][newCol] != '_') {
							if(!isPC)
							System.out.println("Invalid move: cannot move more than one space unless taking a piece..");
							return false;
						}
						else {
							isValid = true;
							return true;
						}
					}
				}
				
			}
			
			catch(IndexOutOfBoundsException e) {
				System.out.println("Array index out of bounds at Method: checkMoves");
			}
			
			return true;
			
			}
			
		/**
		 * Method to take a piece from opponent and move player's
		 * piece to new position.
		 * @param player type of player (1 or 0 representing x or o)
		 * @param oldRow starting row
		 * @param oldCol starting col
		 * @param row row player's checker is moving to
		 * @param col col player's checker is moving to
		 */
		public void takePiece(int player, int oldRow, int oldCol, int row, int col) {
			//Check for a win
		try {	
			helperMove(player,oldRow, oldCol, row,col);
			
			if(numPiecesO == 0 || numPiecesX == 0) {
				gameOver("Game Over!");
				if(numPiecesO == 0) System.out.println("Player X Wins!");
				if(numPiecesX == 0) System.out.println("Player O Wins!");
				return;
			}
			
			while(true) {
			
					board.displayBoard();
					if(!isPC) {
					Scanner scanner = new Scanner(System.in);
					System.out.println("You took an opponent piece. To move again, enter 'Y', or "
							+ "'N' to skip.");
					String input = scanner.next();
					
					if(input.equals("Y")) {
				
						if(player == PLAYER_X && !isPC) {
							System.out.println("Player X: Please enter piece to move");
							String movePosition = scanner.next();
							String oldRow1 = movePosition.substring(0,1);
							String oldCol1 = movePosition.substring(1,2);
							String newRow = movePosition.substring(3,4);
							String newCol = movePosition.substring(4,5);
							
							int oldRowInt = convertToInt(Integer.parseInt(oldRow1));
							int oldColInt = convertToInt(oldCol1);
							int newRowInt = convertToInt(Integer.parseInt(newRow));
							int newColInt = convertToInt(newCol);
						
						
				
							int dist1 = Math.abs(oldRowInt - newRowInt);
							char tempChar = board.board[Math.abs(newRowInt + oldRowInt)/2][(Math.abs(newColInt+oldColInt)/2)];
							if(tempChar == 'o' && dist1 ==2) {
								if(validMove(player, oldRowInt, oldColInt, newRowInt, newColInt)) {
									board.board[Math.abs((newRowInt + oldRowInt)/2)][Math.abs((newColInt + oldColInt)/2)] = 'x';
									helperMove(player, oldRowInt, oldColInt, newRowInt, newColInt);
									break;
								}
							}
							
							else {	System.out.println("Invalid entry, try again.");
						}
					}
					
				
					
					else if(player == PLAYER_O && !isPC) {
						System.out.println("Player O: Please enter old and new position separated by ','.");
						String movePosition = scanner.next();
						
						char oldRow1 = movePosition.charAt(0);
						char oldCol1 = movePosition.charAt(1);
						char newRow = movePosition.charAt(3);
						char newCol = movePosition.charAt(4);
						
						int oldRowInt = convertToInt((int)oldRow1);
						int oldColInt = convertToInt(new StringBuilder().append(oldCol1).toString());
						int newRowInt = convertToInt((int) newRow);
						int newColInt = convertToInt(new StringBuilder().append(newCol).toString());
						
			
							int dist1 = Math.abs(oldRowInt - newRowInt);
							char tempChar = board.board[Math.abs(newRowInt + oldRowInt)/2][(Math.abs(newColInt+oldColInt)/2)];
							if(tempChar == 'x' && dist1 ==2) {
								if(validMove(player, movePosition)) {
									board.board[Math.abs((newRowInt + oldRowInt)/2)][Math.abs((newColInt + oldColInt)/2)] = 'o';
									helperMove(player, oldRowInt, oldColInt, newRowInt, newColInt);
									break;
								}
							}
							else {	System.out.println("Invalid entry, try again.");
						}
						scanner.close();
					}
					
				else {
					System.out.println("Invalid move. Try again.");
				}
				}
				
				else if (input.equals("N")) {
					break;
				}
			}
			}
		}
				
		
		catch(IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("Invalid input was entered into method takePiece.");
		}
		
		catch(StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
					
			
		}
		
		
	

		/**
		 * Takes indices of checker board and converts them 
		 * to proper integer form to use in selecting 2D array
		 * location.
		 * @param input String, input "index" split from input by validMove 
		 * and/or takePiece
		 * @return int, index to be used with array
		 */
		public int convertToInt(String input){
	        int k=0;
	        if (input.equals("a"))       k = 0;
	        else if (input.equals("b") ) k = 1;
	        else if (input.equals("c") ) k = 2;
	        else if (input.equals("d") ) k = 3;
	        else if (input.equals("e") ) k = 4;
	        else if (input.equals("f") ) k = 5;
	        else if (input.equals("g") ) k = 6;
	        else if (input.equals("h") ) k = 7;
	        return k;
	    }
		
		/**
		 * Converts user input values to correct array values 
		 * for rows-- row 8 should have int value 0, 7 should have
		 * 1, and so on.
		 * @param input, an integer
		 * @return int correct array value
		 */
		 public int convertToInt(int input){
		        int i=0;
		        if (input==1)         i = 7;
		        else if (input == 2)  i = 6;
		        else if (input == 3)  i = 5;
		        else if (input == 4)  i = 4;
		        else if (input == 5)  i = 3;
		        else if (input == 6) i = 2;
		        else if (input == 7)  i = 1;
		        else if (input == 8)  i = 0;
		        return i;
		    }

			


		
		/**
		 * [Formatted method] Checks if a player can legally jump from (row1, col1) to (row3, col3)
		 * using row and column constraints and checking symbols against values
		 * in the board.
		 * @param player int, player number (1 or 0, as def with PLAYER_O and PLAYER_X)
		 * @param oldRow int row being moved from
		 * @param oldCol int col being moved from
		 * @param midRow int intermediate row between jump
		 * @param midCol int intermediate col between jump
		 * @param newRow int new row being moved to
		 * @param newCol int new col being moved to 
		 * @return boolean true if move is a jump, false if not
		 */
		boolean moveIsJump(int player, int oldRow, int oldCol, int midRow, int midCol, int newRow, int newCol) {
			if(newRow < 0 || newRow >= 8 || newCol < 0 || newCol >=8)
				return false; //not on board
			
			if(board.board[newRow][newCol] != '_') {
				return false; //already occupied
			}
			
			if(player == PLAYER_O) {
				if(board.board[oldRow][oldCol] == SYMBOL_O) {
					if(board.board[Math.abs((oldRow+newRow)/2)][Math.abs((oldCol+newCol)/2)] == SYMBOL_X) return true;
					else return false; //O's can only move up
				}
				if(board.board[midRow][midCol] == SYMBOL_O || board.board[midRow][midCol] == '_')
					return false; //no x piece to jump
				return true; //legal jump
			}
			else {
				
				//if init position is x
				if(board.board[oldRow][oldCol] == SYMBOL_X) {
					//if intermediate position is o, return true
					if(board.board[Math.abs((oldRow+newRow)/2)][Math.abs((oldCol+newCol)/2)] == SYMBOL_O) {
						return true;
					}else return false; //x can only move down
				}
					
				if(board.board[midRow][midCol] != SYMBOL_O)
					return false; //no o piece to jump
				return true;	//legal jump
			}
			
			
			
		}
		
	
	
	/**
	 * Switches the user: if player is 'o', goes to 'x'
	 * and vice versa
	 * 
	 */
	public void switchPlayer() {
		if(currentPlayer == 0) {
			currentPlayer = 1;
			checkerBoard.currentPlayer = 1;
		}
	
		else {currentPlayer = 0;
			checkerBoard.currentPlayer = 0;
		}
	
	}

	/**
	 * Called when either player runs out of pieces or runs out of moves.
	 * @param s String, message when game over is called.
	 */
	public void gameOver(String s) {
		gameInProgress = false;
		System.out.println(s);
		return;
		
	}
	
	/**
	 * Called if a player resigns from a game.
	 * Calls game over to display game over message.
	 * @param player player resigning.
	 */
	public void resign(int player) {
		if(player == PLAYER_X) {
			gameOver("Player X resigns. Player O wins!");
		}
		else {
			gameOver("Player O resigns. Player X wins!");
		}
	}
	

	/**
	 * CheckersGame class:
	 * Contains the data for a new checkers game.
	 * Responsible for constructing and displaying the board.
	 * @author Melonie Miller
	 * @version 1.0
	 */
	public class CheckersGame{
		
		/**Constant char symbols for different text console pieces.*/
		public static final char EMPTY = ' ', O = 'o', X = 'x';
		/**Initial char board array for text console checker board.*/
		public char[][] board;
		
		/**
		 * Constructor, creates the board and sets it up for 
		 * new game.
		 */
		public CheckersGame() {
			board = new char[8][8];
			setupGame(board);
		}
		
		/**
		 * Logic to check if a place on the board is occupied
		 * @param row int position of piece (row)
		 * @param col int position of piece (col)
		 * @return true if space has a piece.
		 */
		public boolean isOccupied(int row, int col) {
			if(row > 0 && col > 0 && row < 8 && col < 8)
				if(board[row][col] != '_') 
					return true;
			
			return false;		
		}
		
		
		/**
		 * Sets up the current game, populates board with pieces
		 * @param board2 char[][], allows checkerboard to be passed into method for setup
		 */
		public void setupGame(char[][] board2) {
			for(int row = 0; row < 8; row++) {
				for(int col = 0; col < 8; col++) {
					if(row%2 != col%2) {
						if(row < 3) board2[row][col] = 'x';
						else if(row > 4) board2[row][col] = 'o';
						else board2[row][col] = '_';
					}
					else {
						board2[row][col] = '_';
					}
				}
			}
			


		} //end setupGame
		
		/**
		 * Displays the board for the user.
		 */
		public void displayBoard() {
			System.out.print("\n");
			char colName = 'a';
			System.out.print("    ");
			for(int i = 4; i < 28; i+=3) System.out.print(colName++ + "   ");
			System.out.print("\n");
			for(int row = 0; row < 8; ++row) {
				System.out.print((8-row)+" ");
				for(int col = 0; col < 8; ++col) {
					System.out.print("| " + board[row][col] + " ");
				}
				System.out.println("|\n");
			}
		}	 //end CheckersGame class
			}
		
	

/**
 * ****************************CLASS: MOVE************************************
 * Class Move: Isolates row/col of a move (May no longer be needed by end of 
 * implementation?)
 * @author Melonie Miller
 * @version 1.0
 *
 */
public class Move{
	/**
	 * Current row and current column piece is at for instantiating a Move object.
	 */
	public int currRow, currCol;
	
	/** New row and column for piece to move to for instantiating Move Object. */
	public int newCol,newRow;
	
	/** Number representing current player. Can be 0 or 1.*/
	public int currentPlayer;
	
	/** Player or checker board space's current symbol */
	public final char currentSymbol;

	/**CheckerBoard object for a text console game and underlying GUI logic.*/
	private char[][] board;
	
	/**
	 * Move class constructor
	 * @param player player whos move it is
	 * @param board board move is played on 
	 * @param row1 starting row
	 * @param col1 starting col
	 * @param row2 ending row
	 * @param col2 ending col 
	 */
	public Move(int player, char[][] board, int row1, int col1, int row2, int col2) {
		this.board = board;
		currentPlayer = player;
		currRow = row1;
		currCol = col1;
		newRow = row2;
		newCol = col2;
		if(currentPlayer == 0) currentSymbol = 'o';
		else currentSymbol = 'x';
		
	}



	/** 
	 * Determines if move is a jump by testing various conditions on the 
	 * distance between two locations in a move.
	 * @param move current move object.
	 * @return boolean t/f if move is a jump or not.
	 */
	public boolean jump(Move move) {

		//If intermediate space contains an opponent space, move is a jump.
		if(move.newRow - move.currRow == 2 && move.newCol - move.currCol == 2
				|| move.newRow - move.currRow == -2 || move.newCol - move.currCol == -2) {
			if((currentPlayer == 1 &&
					this.board[Math.abs((move.currRow+move.newRow)/2)][Math.abs((move.currCol + move.newCol)/2)] == 'o') || 
					currentPlayer == 0 && this.board[Math.abs((move.currRow+move.newRow)/2)][Math.abs((move.currCol+ move.newCol)/2)] == 'x')
				return true;
		}
	
			return false;	
	}
	
	/**
	 * equals method for class Move. Compares rows and columns
	 * for integer equality and player number to ensure the moves
	 * are equal.
	 * @param o Move object o
	 * @return true if objects are equal.
	 */
	public boolean equals(Move o) {
		if(o.currRow == this.currRow && o.currCol == this.currCol && o.newRow == this.newRow && o.newCol==this.newCol && o.currentPlayer == this.currentPlayer)
			return true;
		else return false;
} 
}//end Move Class





/**
 **************** CHECKERSCOMPUTERPLAYER CLASS*********************
 * @author Melonie Miller
 *
 */
public class CheckersComputerPlayer {

	/** isComputerPlayer is true for CheckersComputerPlayer objects. Allows
	 * methods in CheckersLogic to check that CheckersComputerPlayer object
	 * is a computer player for testing. */
	
	public int playerNum;
	
	/** Stores the value of the computer player's symbol (x or o)*/
	public final char symbol;
	
	
	/**
	 * Initializes a computer player using the type of player (PLAYER_X
	 * or PLAYER_O) as a parameter. Sets the respective symbols and 
	 * assigns the respective player type to currentPlayer.
	 * @param player the current player.
	 */
	public CheckersComputerPlayer(int player) {

		if(player == PLAYER_X) {
			computerPlayer = 1;
			playerNum = PLAYER_X;
			symbol = SYMBOL_X;
		}
		else if(player == PLAYER_O) {
			computerPlayer = 0;
			playerNum = PLAYER_O;
			symbol = SYMBOL_O;
		}
		else {
			symbol = ' ';
			computerPlayer = -1;//player does not exist
			player = -1; 		//player does not exist
			
		}
	}
	
	/**
	 * Computer player version of checkMoves, uses similar logic 
	 * in a way that is formatted specifically for a computer player 
	 * (No input string move)
	 * @param player computer's player type
	 * @param oldRow current row
	 * @param oldCol current col
	 * @param newRow new row
	 * @param newCol new col
	 * @return true if move is valid
	 */
	public boolean checkMoves(int player, int oldRow, int oldCol, int newRow, int newCol) {
		
		//Check move is diagonal
		if(oldRow == newRow || oldCol == newCol) {
			return false;
		}
		
		int distRow = Math.abs(oldRow - newRow);
		int distCol = Math.abs(oldCol - newCol);
		
		//Check move is not more than 2 spaces.
		if(distRow > 2 || distCol > 2) {
			return false;
		}
		
		
		
		//Check move is not backwards
			if((player == PLAYER_O && oldRow < newRow &&
					(board.board[Math.abs((newRow+oldRow)/2)][Math.abs((newCol + oldCol)/2)]) != SYMBOL_X) ||
					(player == PLAYER_X && newRow < oldRow && (board.board[Math.abs((newRow+oldRow)/2)][Math.abs((newCol + oldCol)/2)]) != SYMBOL_O)) 
			return false;
		
		
			//Check O jump moves
		if(player == PLAYER_O) {
			if(distRow > 1) {
				char symbol = board.board[Math.abs((newRow + oldRow)/2)][Math.abs((newCol+oldCol)/2)];
				if(symbol != SYMBOL_X) 
					return false;
				else {
					//try commenting this line out to fix
					//if(board.board[Math.abs((newRow + oldRow)/2)][Math.abs((newCol+oldCol)/2)] =='_')
					return true;
				}
			}
		}
		
		if(playerNum == PLAYER_X) {
			if(distRow > 1) {
				char symbol = board.board[Math.abs((newRow + oldRow)/2)][Math.abs((newCol+oldCol)/2)];
				if(symbol != SYMBOL_O) return false;
				else {
					return true;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks if the checker is on a valid position of the board.
	 * @param row position's row for checking
	 * @param col position's column for checking
	 * @return true if the checker is in a valid position. (On the board)
	 */
	public boolean formatChecker(int row, int col) {
		if(row < 0 || col < 0 || row > 7 || col > 7) return false;
		return true;
	}
	
	/**
	 * Local checker for validating moves in CheckersComputerPlayer. Moves already
	 * in converted format when they enter this method.
	 * @param player current player
	 * @param oldRow row being moved from (converted)
	 * @param oldCol col being moved from (converted)
	 * @param newRow row being moved to (converted)
	 * @param newCol col being moved to (converted)
	 * @return true if move is valid.
	 */
	public boolean validMove(int player, int oldRow, int oldCol, int newRow, int newCol) {
		
		if(formatChecker(oldRow, oldCol) && formatChecker(newRow, newCol))
			if(positionChecker(oldRow, oldCol, newRow, newCol))
				if(moveSelection(player, oldRow, oldCol, newRow, newCol))
						if(!board.isOccupied(newRow, newCol) && Math.abs(oldRow-newRow) <=2 && Math.abs(oldCol-newCol) <=2)
							if(checkMoves(player, oldRow, oldCol, newRow, newCol))
								return true;
		
		return false;
		

	}
	
	

	
	/**
	 * Implementation for a GUI computer player to take a piece. Uses the given checkerBoard object to extract checkers and 
	 * compare valid moves with locations in the array of moves.
	 * Chooses and moves computer pieces based on difficulty level. For level 1, a random move will always be selected from the first 
	 * 3/4ths of the vector of moves. For level 2, a move will be selected from the first half of the vector-- however, if a jump is 
	 * present, the jump will be executed.
	 * For level 3, a move will be selected from the first 1/3rd of the vector of moves, but a jump will always be executed first. 
	 * @param player current player
	 */
	public void GUIcomputerMove(int player) {
		
		Vector<Move> pMoves = getLegalMoves(player);
		if(pMoves.size() == 0) gameOver("Computer out of moves. Player wins!");
		else{
			Random randInt = new Random();
		
		int rand1 = randInt.nextInt(Math.abs(pMoves.size()*3/4));
		int rand2 = randInt.nextInt(Math.abs(pMoves.size()/2));
		int rand3 = randInt.nextInt(Math.abs(pMoves.size()/3));
		
		int tempCol = pMoves.get(0).currCol;
		int tempRow = pMoves.get(0).currRow;
		Checker checker1 = checkerBoard.getChildCell(Checker.class, tempCol,tempRow).get(0);
		
		
		if(difficultyLevel == 1) {
			
			//Level 1 difficulty will not automatically take jumps, will just pick from first 3/4 of moves.
			if(validMove(player, pMoves.get(rand1).currRow, pMoves.get(rand1).currCol, pMoves.get(rand1).newRow, pMoves.get(rand1).newCol)) {
				helperMove(player, pMoves.get(rand1).currRow, pMoves.get(rand1).currCol, pMoves.get(rand1).newRow, pMoves.get(rand1).newCol);
				int row1 = pMoves.get(rand1).currRow;
				int col1 = pMoves.get(rand1).currCol;
				int row2 = pMoves.get(rand1).newRow;
				int col2 = pMoves.get(rand1).newCol;	
				Checker checker = checkerBoard.getChildCell(Checker.class, col1,row1).get(0);
				checkerBoard.getChildren().remove(checker);
				checkerBoard.add(checker,  col2, row2);
				if(moveIsJump(player, pMoves.get(rand1).currRow, pMoves.get(rand1).currCol,(pMoves.get(rand1).currRow+pMoves.get(rand2).newRow)/2,(pMoves.get(rand1).currCol+pMoves.get(rand1).newCol)/2, pMoves.get(rand1).newRow, pMoves.get(rand1).newCol))
						CheckersLogic.this.takePiece(checker, row1, col1, row2, col2);
				}
		}
		
		if(difficultyLevel == 2) {
			int row1 = pMoves.get(rand2).currRow;
			int col1 = pMoves.get(rand2).currCol;
			int row2 = pMoves.get(rand2).newRow;
			int col2 = pMoves.get(rand2).newCol;	
			Checker checker= checkerBoard.getChildCell(Checker.class, col1,row1).get(0);
			
			if(moveIsJump(player, tempRow, tempCol ,(tempRow+ pMoves.get(0).newRow)/2,(tempCol+pMoves.get(0).newCol)/2, pMoves.get(0).newRow, pMoves.get(0).newCol)) {
				takePiece(player, tempRow, tempCol, pMoves.get(0).newRow, pMoves.get(0).newCol);
				CheckersLogic.this.takePiece(checker1,tempRow, tempCol, pMoves.get(0).newRow, pMoves.get(0).newCol);
				checkerBoard.getChildren().remove(checker1);
				checkerBoard.add(checker1,  col2, row2);
			}
			else 
				if(validMove(player, pMoves.get(rand2).currRow, pMoves.get(rand2).currCol, pMoves.get(rand2).newRow, pMoves.get(rand2).newCol)) {
					helperMove(player, pMoves.get(rand2).currRow, pMoves.get(rand2).currCol, pMoves.get(rand2).newRow, pMoves.get(rand2).newCol);
					checkerBoard.getChildren().remove(checker);
					checkerBoard.add(checker,  col2, row2);
				}
		}
		
		if(difficultyLevel == 3) {
			int row1 = pMoves.get(rand3).currRow; //current checker location
			int col1 = pMoves.get(rand3).currCol; 
			int row2 = pMoves.get(rand3).newRow;  // new checker location
			int col2 = pMoves.get(rand3).newCol;
			Checker checker= checkerBoard.getChildCell(Checker.class, col1,row1).get(0); //old checker
			
			if(moveIsJump(player, pMoves.get(0).currRow, pMoves.get(0).currCol, (pMoves.get(0).currRow+pMoves.get(0).newRow)/2, (pMoves.get(0).currCol+pMoves.get(0).newCol)/2, pMoves.get(0).newRow, pMoves.get(0).newCol)) {
				takePiece(player, pMoves.get(0).currRow, pMoves.get(0).currCol, pMoves.get(0).newRow, pMoves.get(0).newCol);
				
				checkerBoard.getChildren().remove(checker1);
				checkerBoard.add(checker1,  col2, row2);
				CheckersLogic.this.takePiece(checker1,tempRow, tempCol, pMoves.get(0).newRow, pMoves.get(0).newCol);
			}	
			else 
				if(validMove(player, row1, col1, row2, col2)) {
					helperMove(player, row1, col1, row2, col2);
					checkerBoard.getChildren().remove(checker);
					checkerBoard.add(checker,  col2, row2);
			}
			
		}
		
		switchPlayer();
		}
		
		
	}
	
	/**
	 * Chooses and moves computer pieces based on difficulty level. For level 1, a random move will always be selected from the first 
	 * 3/4ths of the vector of moves. For level 2, a move will be selected from the first half of the vector-- however, if a jump is 
	 * present, the jump will be executed.
	 * For level 3, a move will be selected from the first 1/3rd of the vector of moves, but a jump will always be executed first. 
	 * @param player current player
	 */
	public void computerMove(int player) {
		
		Vector<Move> pMoves = getLegalMoves(player);
		if(pMoves.size() == 0) gameOver("Computer out of moves. Player wins!");
		else{
			Random randInt = new Random();
		
		int rand = randInt.nextInt(pMoves.size());
		int rand1 = randInt.nextInt(Math.abs(pMoves.size()*3/4));
		int rand2 = randInt.nextInt(Math.abs(pMoves.size()/2));
		int rand3 = randInt.nextInt(Math.abs(pMoves.size()/3));

		
		
		if(difficultyLevel == 1) {
			
			//Level 1 difficulty will not automatically take jumps, will just pick from first 3/4 of moves.
			if(validMove(player, pMoves.get(rand1).currRow, pMoves.get(rand1).currCol, pMoves.get(rand1).newRow, pMoves.get(rand1).newCol)) 
				helperMove(player, pMoves.get(rand1).currRow, pMoves.get(rand1).currCol, pMoves.get(rand1).newRow, pMoves.get(rand1).newCol);
		}
		
		if(difficultyLevel == 2) {
			if(moveIsJump(player, pMoves.get(0).currRow, pMoves.get(0).currCol, (pMoves.get(0).currRow+ pMoves.get(0).newRow)/2, (pMoves.get(0).currCol+pMoves.get(0).newCol)/2, pMoves.get(0).newRow, pMoves.get(0).newCol))
				takePiece(player, pMoves.get(0).currRow, pMoves.get(0).currCol, pMoves.get(0).newRow, pMoves.get(0).newCol);
			else 
				if(validMove(player, pMoves.get(rand2).currRow, pMoves.get(rand2).currCol, pMoves.get(rand2).newRow, pMoves.get(rand2).newCol))
					helperMove(player, pMoves.get(rand2).currRow, pMoves.get(rand2).currCol, pMoves.get(rand2).newRow, pMoves.get(rand2).newCol);
		}
		
		if(difficultyLevel == 3) {
			if(moveIsJump(player, pMoves.get(0).currRow, pMoves.get(0).currCol, (pMoves.get(0).currRow+pMoves.get(0).newRow)/2, (pMoves.get(0).currCol+pMoves.get(0).newCol)/2, pMoves.get(0).newRow, pMoves.get(0).newCol))
				takePiece(player, pMoves.get(0).currRow, pMoves.get(0).currCol, pMoves.get(0).newRow, pMoves.get(0).newCol);
			else 
				if(validMove(player, (char)pMoves.get(rand3).currRow, pMoves.get(rand3).currCol, pMoves.get(rand3).newRow, pMoves.get(rand3).newCol))
					helperMove(player, pMoves.get(rand3).currRow, pMoves.get(rand3).currCol, pMoves.get(rand3).newRow, pMoves.get(rand3).newCol);
		}
		
		switchPlayer();
		}
		
		
	}
	
	/**
	 * Helper method to allow computer player to take a piece
	 * @param player computer player's int value
	 * @param oldRow row being moved from
	 * @param oldCol col being moved from
	 * @param newRow row being moved to
	 * @param newCol col being moved to 
	 */
	public void takePiece(int player, int oldRow, int oldCol, int newRow, int newCol) {
		helperMove(player, oldRow, oldCol, newRow, newCol);
	}

} //end CheckersComputerPlayer class

/*
 * Additional helpers for GUI implementation.
 */

/**
 * Returns the legal moves available for a player.
 * If any jumps exist, only jumps will be returned in vector, as jump moves
 * should be made first. Looks at each space on board and determines if the move
 * is valid for the specified player.
 * @param player current player to retrieve moves for.
 * @return Vector of Moves vector of moves available to player.
 */
public Vector<Move> getLegalMoves(int player){
	
	Vector<Move> pMovesTotal = new Vector<Move>();
	
	//Iterate through each position on the board for player's pieces
	for(int row = 0; row < 8; row++) {
		for(int col = 0; col < 8; col++) {
			Vector<Move> pMoves = getLegalJumps(player, row, col);
			
			//If jumps exist, simply return the jumps as they
			//must be performed if they exist.
			if(pMoves.size()>0) {
				return pMoves;
			}
			
			//Return all moves for piece at position (row,col)
			else if (pMoves.size() == 0) pMoves = getLegalMoves(player, row, col);
		
			//Add all members of pMoves to pMovesTotal
			for(int i = 0; i < pMoves.size(); i++) {
				pMovesTotal.add(pMoves.get(i));
			}
		}
	}
	return pMovesTotal;
	
}




/**
 * Returns legal moves for a player at a given row and column. Checks
 * cases based on player number and adds moves to a vector of Move objects
 * for retrieval.
 * @param player current player
 * @param row current row
 * @param col current column
 * @return Vector of Moves vector of all possible moves a player can make at 
 * specified row,col.
 */
public Vector<Move> getLegalMoves(int player, int row, int col){
	Vector<Move> pMoves = new Vector<Move>();
	
	//For player o, checks if piece is an o piece, then checks
	//If the there are legal moves for that piece.
	//Inputs char values of row,col into validMove.
	if(player == PLAYER_O) {
		if(board.board[row][col] == SYMBOL_O) {
			if(validMove(player, row, col, row+1, col+1))
				pMoves.add(new Move(player, board.board, row, col, row+1, col+1));
			if(validMove(player, row,col,row-1, col-1))
				pMoves.add(new Move(player, board.board, row, col, row-1, col-1));
			if(validMove(player, row, col, row+1, col-1))
				pMoves.add(new Move(player, board.board, row, col, row+1, col-1));
			if(validMove(player, row, col, row-1,row+1))
				pMoves.add(new Move(player, board.board, row, col, row-1, col+1));
		}
	
	}
	
	//For player x, checks if the piece is an x piece, then checks
	//If there are legal moves for that piece.
	else if (player == PLAYER_X) {
		if(board.board[row][col] == SYMBOL_X) {
			if(validMove(player, row, col, row+1,col+1))
				pMoves.add(new Move(player, board.board, row, col, row+1, col+1));
			if(validMove(player, row, col, row-1, col-1))
				pMoves.add(new Move(player, board.board, row, col, row-1, col-1));
			if(validMove(player, row, col, row+1, col-1))
				pMoves.add(new Move(player, board.board, row, col, row+1, col-1));
			if(validMove(player, row, col, row-1, row+1))
				pMoves.add(new Move(player, board.board, row, col, row-1, col+1));
		}
	}
	return pMoves;
}

/**
 * Adds legal jumps to a vector of moves. Verifies that a jump is legal by checking that it is the correct symbol,
 * the move is valid, and the jump is valid before adding to the list. 
 * **Params must be in converted versions!!!!***
 * @param player current player
 * @param row current piece position (row)
 * @param col current piece position (column)
 * @return Vector of Moves pMoves, all possible jumps. 
 */
public Vector<Move> getLegalJumps(int player, int row, int col){
	Vector<Move> pMoves = new Vector<Move>();
	if(player == PLAYER_O) {
		if(board.board[row][col] == SYMBOL_O) {
			if(validMove(player, row, col, row+2, col+2))
				if(moveIsJump(player, row, col, row+1, col+1, row+2, col+2));
					pMoves.add(new Move(player, board.board, row, col, row+2, col+2));
			if(validMove(player, row, col, row-2, col-2))
				if(moveIsJump(player, row, col, row-1, col-1, row-2,col-2))
					pMoves.add(new Move(player, board.board, row, col, row-2, col-2));
			if(validMove(player, row, col, row+2, col-2))
				if(moveIsJump(player, row, col, row+1, col-1, row+2, col-2))
					pMoves.add(new Move(player, board.board, row, col, row+2, row-2));
			if(validMove(player, row, col, row-2, col+2))
				if(moveIsJump(player, row, col, row-1, col+1, row-2, col+2))
						pMoves.add(new Move(player, board.board, row, col, row-2, col+2));
		}
	}
	
	if(player == PLAYER_X) {
		if(board.board[row][col] == SYMBOL_X) {
			if(validMove(player, row, col, row+2, col+2))
				if(moveIsJump(player, row, col, row+1, col+1, row+2, col+2))
					pMoves.add(new Move(player, board.board, row, col, row+2, col+2));
			if(validMove(player, row, col, row-2, col-2))
				if(moveIsJump(player, row, col, row-1, col-1, row-2,col-2))
					pMoves.add(new Move(player, board.board, row, col, row-2, col-2));
			if(validMove(player, row, col, row+2, col-2))
				if(moveIsJump(player, row, col, row+1, col-1, row+2, col-2))
					pMoves.add(new Move(player, board.board, row, col, row+2, col-2));
			if(validMove(player, row, col, row-2, col+2))
				if(moveIsJump(player, row, col, row-1, col+1, row-2, col+2))
						pMoves.add(new Move(player, board.board, row, col, row-2, col+2));
		}
	}
	
	//player X jumps implementation			
	
	return pMoves;
}

/**
 * Helper method to add all the jumps a player has independently of other
 * jump/move methods. Used in recursively allowing a player to make jumps until they 
 * have no jumps left.
 * @param player current player.
 * @return a vector of legal jumps (Move objects).
 */
public Vector<Move> getLegalJumps(int player){
	
	Vector<Move> pMovesTotal = new Vector<Move>();
	
	//Iterate through each position on the board for player's pieces
	for(int row = 0; row < 8; row++) {
		for(int col = 0; col < 8; col++) {
			pMovesTotal.addAll(getLegalJumps(player, row, col));
		}
	}
	return pMovesTotal;
	
}


/**
 * Receives control from the checkerboard, allowing the moves to be executed in 
 * CheckersLogic.
 * @param <T> Class extending MouseEvent
 * @param e MouseEvent event.
 */
public <T extends MouseEvent> void recieveControl(T e) {
	String eventType = e.getEventType().getName();
	switch (eventType) {
	case "DRAG_DETECTED" : dragDetected(e); 
		break;
	
	case "MOUSE_DRAGGED" : mouseDragged(e);
		break;
	
	case "MOUSE_RELEASED" : mouseReleased(e);
		break;
	
	}
}
	

/**
 * Method for detecting a drag event.
 * Takes the source of the checker and computes
 * its row and column. Computes valid moves for the player at 
 * this row/col combination. Sets prevRow and prevCol to allow
 * the board to return to its previous positions if a move is invalid.
 * @param m mouse event.
 */
public void dragDetected(MouseEvent m) {
	Checker checker = (Checker)m.getSource();
	int selCol = GridPane.getColumnIndex(checker);
	int selRow = GridPane.getRowIndex(checker);
	
	int player = currentPlayer;
	
	//Get the valid moves for this piece.
	validMoves = getLegalMoves(player, selRow,selCol);
			
	cleared = checker;
	prevCol = selCol;
	prevRow = selRow;

}
	
/**
 * Called only if the piece/move has been determined valid
 * @param e MouseEvent event being handled
 */
public void mouseDragged(MouseEvent e) {
    Checker checker = (Checker)e.getSource();
    if(checker == cleared) {
        checker.setCenterX(e.getX());
        checker.setCenterY(e.getY());
    }
    else e.consume();
}

/**
 * Gets the source of the mouse being released (a checker). If the checker matches the
 * checker detected/dragged, the checker's new position is assigned and the method looks
 * for a move with the original and new rows/cols. If this position is found, the checker 
 * is moved to the new position. If not, the checker is returned to its previous position.`1
 * @param e Mouse event.
 */
public void mouseReleased(MouseEvent e) {
	Checker checker = (Checker)e.getSource();
	if(checker == cleared) {
		boolean set = false;
		moveCol = (int)(e.getSceneX()/checkerBoard.cellWidth());
		moveRow = (int)(e.getSceneY()/checkerBoard.cellHeight());
		Move currMove = new Move(currentPlayer, board.board, prevRow, prevCol, moveRow, moveCol);
		for(Move m: validMoves) {
			if(currMove.equals(m)) {
				helperMove(currentPlayer,m.currRow,m.currCol, m.newRow,m.newCol);
				checkerBoard.getChildren().remove(checker);
				checkerBoard.add(checker,  moveCol, moveRow);
				set = true;
			}
		}
		if(!set) {
			checkerBoard.getChildren().remove(checker); //if invalid move reset checker
			checkerBoard.add(checker, prevCol, prevRow);
		}
		checker.setManaged(true);
		
		
		//handle jump cases
		if(currMove.jump(currMove)) { 
			takePiece(currentPlayer, prevRow, prevCol, moveRow, moveCol);	//takes checker out of 2D array
			takePiece(checker, prevRow, prevCol, moveRow, moveCol);
			checkerBoard.getChildren().remove(checker);
			checkerBoard.add(checker,  moveCol, moveRow);
			
		}
		//Change control to other player. If PC game, make PC move.	
		switchPlayer();
		if(isPC)
		opponent.GUIcomputerMove(computerPlayer);
		
	}
}

/**
 * Executes a jump and takes a piece. Uses checkerboard to get needed cells (checkers)
 * from the board to take.
 * @param checker current checker object
 * @param row1 starting row
 * @param col1 starting column
 * @param row2 ending row
 * @param col2 ending column
 */
public void takePiece(Checker checker, int row1, int col1, int row2, int col2) {
    //deal with a jump event
	Checker checker1 = checkerBoard.getChildCell(Checker.class, col2, row2).get(0); //new checker space
    checkerBoard.getChildren().removeAll(checkerBoard.getChildCell(Checker.class, (col1+col2)/2, (row1+row2)/2));
    //clear previous valid selection
    //limit new valid selection to this piece and only jumps
    
    validMoves.clear();
    validMoves.addAll(getLegalJumps(currentPlayer));
    if(!validMoves.isEmpty()) {
    	   validMoves.clear();
    	    validMoves.addAll(getLegalJumps(currentPlayer)); //refresh legal jumps
    	    checkerBoard.getChildren().removeAll(checkerBoard.getChildCell(Checker.class, (validMoves.get(0).currCol+validMoves.get(0).newCol)/2, (validMoves.get(0).currRow+validMoves.get(0).newRow)/2));
    	    takePiece(checker1, validMoves.get(0).currRow, validMoves.get(0).currCol, validMoves.get(0).newRow, validMoves.get(0).newCol);
    }
    else {
    	
    }
}




} //end CheckersLogic class