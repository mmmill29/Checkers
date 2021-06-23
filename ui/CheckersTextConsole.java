package ui;

import java.util.NoSuchElementException;
import java.util.Scanner;
import core.CheckersLogic;
import core.CheckersLogic.CheckersComputerPlayer;

/**
 * Controls User Interface for Checkers Game. Starting point for 
 * text console program execution.
 * Completion time: 22.5 hours (Version 1)
 * Completion time: 23.75 hours (Version 2)
 * Completion time: 42 hours (Version 3)
 * @version 3.0
 * @author Melonie Miller
 *
 */
public class CheckersTextConsole {


	/** current CheckersLogic game being played. 	 */
	static CheckersLogic newGame;
	/**Symbol for computer/AI player. x or o value 	 */
	static char computerSymbol;
	/** int representing current player, either x (1) or o (0)	 */
	static int currentPlayer;
	/**Constant int for player x number 	 */
	static final int PLAYER_X =1;
	/**Constant int for player o number	 */
	static final int PLAYER_O = 0; 

	/** Game difficulty level: 1-3	 */
	public static int difficultyLevel=1;
			
	
	/**
	 * CheckersTextConsole constructor. Calls all methods to 
	 * create a checkers game.
	 */
	public CheckersTextConsole() {
		try{
			
			System.out.print("Start a new game? (y/n). Q for 'QUIT'.");
			
			Scanner scanner = new Scanner(System.in);		
			String input = scanner.next();
			if(input.contains("y")) {
				
				System.out.println("Please choose 'multiplayer' or 'computer' opponent: ");
					input = scanner.next();
					if(input.contains("computer")) {
				
						System.out.println("Please select a difficulty level 1-3: ");
						difficultyLevel = scanner.nextInt();
						while(difficultyLevel != 1 && difficultyLevel != 2 && difficultyLevel !=3 ) {
							System.out.println("Incorrect input. Must be 1, 2, or 3. Please try again.");
							difficultyLevel = scanner.nextInt();
						}
						
						System.out.println("Please choose your piece, x or o: ");
						String piece = scanner.next();
						if(piece.contains("x")) {
							computerSymbol = 'o';
							currentPlayer = PLAYER_X;
						}
						else if(piece.contains("o")) {
							computerSymbol = 'x';
							currentPlayer = PLAYER_O;
							}
						
						else {
							computerSymbol = ' ';
							System.out.println("Incorrect input. Please try again.");
						}
						
						
						 newGame = newPCGame(difficultyLevel);
						
					}
					
					/*
					 * if player input is "multiplayer", start new game with default ctor
					 * 
					 */
					else if (input.contains("multiplayer")) {
						
						 newGame = newMultiplayerGame();
					}
					
					else {
						System.out.println("No proper input selected.");
					}

				
				while(true) {
					
					switch (input) {
						case "multiplayer" : { 
							
							
							
							//Check for different game ending cases.
							if(newGame.numPiecesO==0 || newGame.numPiecesX == 0) {
								newGame.gameInProgress = false;
								
								if(newGame.numPiecesO==0 && newGame.numPiecesX != 0) newGame.gameOver("Player O is out of moves. Player X wins!");
								else if(newGame.numPiecesX == 0 && newGame.numPiecesO != 0) newGame.gameOver("Player X out of moves. Player O Wins!");
								else { 
									if(newGame.numPiecesO == 0 && newGame.numPiecesX == 0) newGame.gameOver("No players have moves left. Game is tied.");
								}
									
								break;
								}
					
						//Check	
						if(currentPlayer == PLAYER_X) {
							
							//Grab starting position or resign input
							System.out.println("Player X: Enter move 'oldrowcol-newrowcol'. Enter 'resign' to resign from match.");
							String move = scanner.next();
							
							if(move.equals("resign")) {
								newGame.resign(PLAYER_X);
								break;
							}

						
							//If move is valid, call helper move, then display the new board and change the player.
							boolean isValid = newGame.validMove(currentPlayer, move);
								if(isValid) {
									newGame.helperMove(currentPlayer, move);
									currentPlayer = PLAYER_O;
									newGame.board.displayBoard();
								}
								else {
									newGame.board.displayBoard();
								}
							
						}
						
						else if(currentPlayer == PLAYER_O) {
							System.out.println("Player O: Enter move 'oldrowcol-newrowcol'. Enter 'resign' to resign from match.");
							String move = scanner.next();
							
							if(move.equals("resign")) {
								newGame.resign(PLAYER_O);
								break;
							}

							
							boolean isValid = newGame.validMove(currentPlayer, move);
							
							if(isValid) {
								newGame.helperMove(currentPlayer, move);
								currentPlayer = PLAYER_X;
								newGame.board.displayBoard();
								
							}
							else {
								newGame.board.displayBoard();
							}
							
						}
						
					}
							
						case "computer": {
							if(newGame.gameInProgress() == true) {
							CheckersComputerPlayer computer;
							
							
							if(currentPlayer == PLAYER_X && computerSymbol == PLAYER_O) {			
								
								//Instantiate the computer player object.
								computer = newGame.new CheckersComputerPlayer(PLAYER_O);		
								
								//Get starting position from player x (user)
								if(computerSymbol != 'x') {
								System.out.println("Player X: Enter move 'oldrowcol-newrowcol'. Enter 'resign' to resign from match.");
								String move = scanner.next();
								
								//resign case
								if(move.equals("resign")) {
									newGame.resign(newGame.PLAYER_X);
									break;
								}

								//if move is valid, make move and change current player to player o
								boolean isValid = newGame.validMove(currentPlayer, move);
									if(isValid) {
										newGame.isPC = false;
										newGame.helperMove(currentPlayer, move);
										newGame.board.displayBoard();
										currentPlayer = PLAYER_O;

										if(newGame.numPiecesO==0 || newGame.numPiecesX == 0) {
											if(newGame.numPiecesO == 0 && newGame.numPiecesX !=0)
												newGame.gameOver("Player o is out of pieces. Player x wins!");
											else if(newGame.numPiecesO !=0 && newGame.numPiecesX == 0) 
												newGame.gameOver("Player x is out of pieces. Player o wins!");
											break;
										}
									}
									else {
										newGame.board.displayBoard();
								}
									//Lastly, generate a computer move.
									newGame.isPC = true;
									computer.computerMove(PLAYER_O);
									currentPlayer = PLAYER_X;
									if(newGame.numPiecesO==0 || newGame.numPiecesX == 0) {
										if(newGame.numPiecesO == 0 && newGame.numPiecesX !=0)
											newGame.gameOver("Player o is out of pieces. Player x wins!");
										else if(newGame.numPiecesO !=0 && newGame.numPiecesX == 0) 
											newGame.gameOver("Player x is out of pieces. Player o wins!");
										break;
									}
									
									System.out.println("Computer move: ");
									newGame.board.displayBoard();
									
								}
							}
							

							if(currentPlayer ==PLAYER_O && computerSymbol == newGame.SYMBOL_X) {			
								
								//Instantiate the computer player object.
								computer = newGame.new CheckersComputerPlayer(newGame.PLAYER_X);		
								
								//X goes first-- cast computer move.
								newGame.isPC = true;
								computer.computerMove(newGame.PLAYER_X);
								System.out.println("Computer move: ");
								newGame.board.displayBoard();
								if(newGame.numPiecesO==0 || newGame.numPiecesX == 0) {
									if(newGame.numPiecesO == 0 && newGame.numPiecesX !=0)
										newGame.gameOver("Player o is out of pieces. Player x wins!");
									else if(newGame.numPiecesO !=0 && newGame.numPiecesX == 0) 
										newGame.gameOver("Player x is out of pieces. Player o wins!");
									break;
								}
								newGame.currentPlayer = PLAYER_O;
								
								//Get starting position from player x (user)
								System.out.println("Player O: Enter move 'oldrowcol- newrowcol'. Enter 'resign' to resign from match.");

								String move = scanner.next();
								
								//resign case
								if(move.equals("resign")) {
									newGame.resign(newGame.PLAYER_O);
									break;
								}

							
								//if move is valid, make move and change current player to player o
								boolean isValid = newGame.validMove(currentPlayer, move);
									if(isValid) {
										newGame.isPC = false;
										newGame.helperMove(currentPlayer, move);
										if(newGame.numPiecesO==0 || newGame.numPiecesX == 0) {
											if(newGame.numPiecesO == 0 && newGame.numPiecesX !=0)
												newGame.gameOver("Player o is out of pieces. Player x wins!");
											else if(newGame.numPiecesO !=0 && newGame.numPiecesX == 0) 
												newGame.gameOver("Player x is out of pieces. Player o wins!");
											break;
										}
										newGame.board.displayBoard();
										
									}
									else {
										while(!isValid) {
											System.out.print("Please enter a new valid move. ");
											move = scanner.next();
											isValid = newGame.validMove(currentPlayer, move);
											if(isValid) {
												newGame.isPC = false;
												newGame.helperMove(currentPlayer, move);
												break;
											}
										}
										newGame.board.displayBoard();
								}
								
								}
									
							}
							
						}
							
							
						}
					}
					
			}
			
				
			
			if(input.contains("Q")) {
				scanner.close();
				return;
				
			}
			else if (input.contains("n") && !input.contains("y")) {
			System.out.println("No new game started. Please try again.");
			scanner.close();
			}
			}
		
			catch(NoSuchElementException e) {
				e.printStackTrace();
			}
			
			catch(IllegalArgumentException e) {
				System.out.println("Adequate input not entered. Try again.");
				e.printStackTrace();		
			}
			
			catch(NullPointerException e) {
				e.printStackTrace();
			}
			
			catch(StringIndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println("String input is not correct length");
			}
			
			
				
			
		}	
	
	
	/**
	 * Begins new multiplayer game by calling constructor of CheckersLogic.
	 * @return CheckersLogic object, new game (Multiplayer)
	 */
		public static CheckersLogic newMultiplayerGame() {
		return new CheckersLogic();
	
	}
	
		/**
		 * Begins new PC game by calling constructor of CheckersLogic
		 * Takes difficulty level of current game, selected by user.
		 * @param difficultyLevel difficulty level of PC game
		 * @return CheckersLogic object, new Game (multiplayer)
		 */
	public static CheckersLogic newPCGame(int difficultyLevel) {
		return new CheckersLogic(currentPlayer, true, difficultyLevel);
		
	}
}