package ui;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Point2D;

/**
 * Class GridLayout builds main functionality of a 
 * CheckerBoard object. Takes the height and width properties,
 * rows and columns, and assigns dimensions of the board. Implements
 * row and column constraints to manage board functionality.
 * @author Melonie Miller
 * @version 1.0
 * Completion time: 42 hours
 *
 */
public class GridLayout extends GridPane {

	 /**cell width property (Helps build squares on board)*/
	 private DoubleProperty cWidth = new SimpleDoubleProperty();
	 
	 /**cell height property (Helps build squares on board) */
	    private DoubleProperty cHeight = new SimpleDoubleProperty();
	    
	    /** Total rows on checkerboard.*/
	    public final int rows = 8;
	    
	    /** Total columns on checkerboard.*/
	    public final int columns = 8;
	
	    /**
	     * Constructor for GridLayout. Initialize rows and cols variables,
	     * and initializes the number of rows/columns.
	     * @param col number of columns
	     * @param row number of rows
	     */
	    public GridLayout() {
	        initializeColumns(columns);
	        initializeRows(rows);
	        this.setMinHeight(700);
	        this.setMinWidth(700);
	    }
	    
	    /**
	     * Fills the grid with columns, ensures there are 
	     * constraints/formatting for the columns
	     * @param c number of columns
	     */
	    private void initializeColumns(int c) {
	        for(int col=0; col<c; col++){
	            getColumnConstraints().add(new ColumnConstraints());
	           getColumnConstraints().get(col).setPercentWidth(100.0/c);
	        }
	    }
	    
	    /**
	     * fill the grid with rows, ensures there are
	     * constraints/formatting for the rows
	     * @param r number of rows
	     */
	    private void initializeRows(int r) {
	        for(int row=0; row<r; row++){
	            getRowConstraints().add(new RowConstraints());
	            getRowConstraints().get(row).setPercentHeight(100.0/r);
	        }
	    }
	    
	    /**
	     * Adds size constraints to rows and columns using 
	     * getRowConstraints and getColumnConstraints.
	     * @param r rows
	     * @param c columns
	     */
	    public void sizeConstraints(int r, int c) {
	    	getRowConstraints().add(new RowConstraints(r));
	    	getColumnConstraints().add(new ColumnConstraints(c));
	    }
	
	    /**
	     * Binds the cell width and height properties to assigned value within scene.
	     * @throws NullPointerException if scene is null.
	     */
	    public void bindToParent() throws NullPointerException {
	       try {
	        cWidth.bind(getScene().widthProperty().divide(columns));
	        cHeight.bind(getScene().heightProperty().divide(rows));
	       }
	       catch (NullPointerException e) {
	    	   e.printStackTrace();
	    	   System.out.println("Scene cannot be null. Make sure this grid belongs to a scene.");
	       }
	    }
	    
	   /**
	    * Getter for the cell width property 
	    * @return DoubleProperty cWidth, cell width
	    * 
	    */
	    public DoubleProperty getChildCellWidthProperty() {
	        return cWidth;
	    }
	    
	    
	    /**
	     * Getter for the cell width value 
	     * @return double cell width (numeric)
	     */
	    public double cellWidth() {
	        return cWidth.getValue();
	    }
	    
	    /**
	     * getter for the cell height property
	     * @return cHeight, DoubleProperty height property
	     */
	    public DoubleProperty getChildCellHeightProperty() {
	        return cHeight;
	    }
	    
	    /**
	     * getter for cell height value
	     * @return double value of cHeight (numeric)
	     */
	    public double cellHeight() {
	        return cHeight.getValue();
	    }
	    

	    /**
	     * Helper method for getClass(below)
	     * Retrieves the children objects contained at (col,row) of the grid.
	     * If T is null, no children will be collected. 
	     * @param <T> allows for passing different classes (such as Point2D)
	     * @param child class/child being passed
	     * @param col x coordinate/column location of object
	     * @param row y coordinate/row location of object
	     * @return List<T>, Arraylist of objects (Children) of parent.
	     */
	    public <T extends Node> List<T> getChildCell(Class<T> child, int col, int row) {
	        List<T> children = new ArrayList<T>();
	        for(Node node : getChildren()) {
	            if(child.isInstance(node)) {   
	                if(getColumnIndex(node) == col) {
	                    if(getRowIndex(node) == row) {
	                        children.add(child.cast(node)); 
	                    }
	                }
	            }
	        }
	        return children;
	    }
	    
	    
	    
	    /**
	     * Wrapper/class for getChildCell(Class<T> child, int col, int row). Uses helper method
	     * getChildCell (above).
	     * @param <T> allows for passing different objects extending Node (such as Point2D)
	     * @param child Class<T> object (extending Node)
	     * @param point Point2D Object
	     * @return List<T> Arraylist of children.
	     */
	    public <T extends Node> List<T> getChildCell(Class<T> child, Point2D point) {
	        return getChildCell(child, (int)point.getX(), (int)point.getY());
	    }
	    /**
	     * remove all children of a given node type at the coordinates
	     * @param <T> class extending node
	     * @param classObj object of class <T>
	     * @param col current column of child nodes
	     * @param row current row of child nodes
	     */
	    public <T extends Node> void removeChildren(Class<T> classObj, int col, int row) {
	        getChildren().removeAll(getChildCell(classObj, col, row));
	    }
}
