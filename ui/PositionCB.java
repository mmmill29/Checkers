package ui;

import javafx.geometry.Point2D;

/**
 * Creates extension of Point2D to support implementation for
 * checker pieces. PositionCB object stores a reference to a checker,
 * as well as x and y coordinates for the location of the checker.
 * This allows the program to build positions as objects for later use.
 * @author Melonie Miller
 * Completion time: 30 minutes
 * @version 1.0
 *
 */
public class PositionCB extends Point2D{

	/**Checker object to be assigned in constructor.*/
	 public final Checker checker;
	 
	 /**
	  * Constructor for PositionCB class. Takes a Checker object,
	  * x coordinate, and y coordinate and assigns the class variable checker.
	  * Calls the super constructor of Point2D on x and y. 
	  * @param tmp checker parameter to be assigned to checker.
	  * @param x x coordinate (col)
	  * @param y y coordinate (row)
	  */
	    PositionCB(Checker tmp, int col, int row) {
	        super(col, row);
	        checker = tmp;
	        checker.currentCol = col;
	        checker.currentRow = row;
	    }
	    
	   
	    @Override
	    /**
	     * Overrides equals method for objects.
	     * Allows comparison or Point2D objects.
	     * @param o object being compared.
	     */
	    public boolean equals(Object o) {
	        if(o.getClass() == PositionCB.class) {
	        	return super.equals(o) && ((PositionCB)(o)).checker == checker;
	        }
	        else return false;
	    }
	}

