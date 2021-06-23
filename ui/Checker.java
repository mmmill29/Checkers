package ui;

import core.CheckerBoard;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.When;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Contains information about a single checker piece.
 * Constructs checker and builds radius binding and 
 * event listeners for checkers.
 * Extends Circle class, the main shape of checker.
 * 
 * @author Melonie Miller
 * @Version 1.0
 *
 */

public class Checker extends Circle {

	/** Checker team, red or black*/
    public final String team;
    
    /** Symbol relating to CheckersGame board for syncing*/
    final char symbol;
    
    /** Player in control of the piece */
    final int player;
    
    //for binding the checkers to the grid
    private DoubleBinding radiusBinding;

    /**Checker's current row location */
    public int currentRow;
    /** Checker's current col location */
    public int currentCol;
    
    /**
     * Constructor for checker. Takes a CheckerBoard object
     * and string representing the color of the checker to assign
     * value to the checker and place it on the board.
     * @param board current GUI CheckerBoard component.
     * @param color color of checker
     */
    public Checker(CheckerBoard board, String color) {
        //decide if checker is red or black
        if(color.equals("red")) {
            this.team = color;
            setFill(Color.RED);
            player = 1;
            symbol = 'x';
        }
        else if(color.equals("black")) {
            this.team = color;
            setFill(Color.BLACK);
            player = 0;
            symbol = 'o';
        }
        else {
        	symbol = ' ';
        	player = -1;
        	throw new Error("team must be red or black");
        }
        //center piece
        GridPane.setHalignment(this, HPos.CENTER);
        GridPane.setValignment(this, VPos.CENTER);
        radiusBinding = (DoubleBinding)new When(board.getChildCellWidthProperty().lessThan(board.getChildCellHeightProperty()))
            .then(board.getChildCellWidthProperty().multiply(.4))
           .otherwise(board.getChildCellHeightProperty().multiply(.4)
        );
       // bind the radius of the checker to .8 * width of it's container
       radiusProperty().bind(radiusBinding);

        //add local event handlers to highlight and unhighlight the checker when dragged
        addEventHandler(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                toFront();
                setStroke(Color.YELLOW);
                startFullDrag();
                transferEvent(e);
            }
        });
        
        addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                setStroke(null);
                transferEvent(e);
            }
        });
        //register static eventHandler to forward control of the action
        addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                transferEvent(e);
            }
        });
        //add change listener to team 
    }

    /** convienence method to transfer event to checkerboard 
     * @param event T class of event extending MouseEvent */  
    private <T extends MouseEvent> void transferEvent(T event) {
        //cheating to get dynamic reference within a static object
        CheckerBoard dynamicBoard = (CheckerBoard)((Checker)event.getSource()).getParent();
        dynamicBoard.switchControl(event);
    }
}

