package estuary.model;

/**
 * This is the Rectangle class, which is used to provide hitboxes for Entities.
 * These hitboxes are then used to determine collision.
 * 
 * @author Sean
 *
 */
public class Rectangle implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public Vector2 topLeft;
	public Vector2 bottomRight;

	/**
	 * Creates a Rectangle by defining its top-left corner and bottom-right corner
	 * 
	 *      o-------------------
	 *      |                  |
	 *      |                  | 
	 *      -------------------o
	 *      
	 * @param topLeft
	 * @param bottomRight
	 */
	public Rectangle(Vector2 topLeft, Vector2 bottomRight) {
		this.topLeft     = new Vector2(topLeft);
		this.bottomRight = new Vector2(bottomRight);
	}
	
	/**
	 * Creates a Rectangle by defining its top-left corner and its width and height
	 * 
	 *      o------(width)------
	 *      |                  |
	 *      |              (height)
	 *      |                  | 
	 *      --------------------
	 * 
	 * @param topLeft
	 * @param width
	 * @param height
	 */
	public Rectangle(Vector2 topLeft, double width, double height) {
		this.topLeft = topLeft;
		this.bottomRight = new Vector2(topLeft.x + width, topLeft.y + height);
	}
	
	/**
	 * Getter for Width
	 */
	public double getWidth() {
		return bottomRight.x - topLeft.x;
	}
	
	/**
	 * Getter for Height
	 */
	public double getHeight() {
		return bottomRight.y - topLeft.y;
	}
	
	/**
	 * Getter for Right-side (x coordinate)
	 */
	public double getRight() {
		return bottomRight.x;
	}
	
	/**
	 * Getter for Bottom-side (y coordinate)
	 */
	public double getBottom() {
		return bottomRight.y;
	}
	
	/**
	 * Determines whether this Rectangle is intersecting (colliding) with another Rectangle
	 * Based on SFML's Rect::intersects() method
	 * 
	 * @param other Rectangle that we are testing intersection (collision) with
	 * @return boolean true or false to indicate whether it intersects or not
	 */
	public boolean intersects(Rectangle other) {
		
		// Assuming positive getWidth() and getHeight()
		// will fail otherwise
		
		double this_left   = this.topLeft.x;
		double this_top    = this.topLeft.y;
		double this_width  = this.getWidth();
		double this_height = this.getHeight();
		
		double other_left   = other.topLeft.x;
		double other_top    = other.topLeft.y;
		double other_width  = other.getWidth();
		double other_height = other.getHeight();
		
		double intersectionLeft   = Math.max(this_left, other_left);
		double intersectionTop    = Math.max(this_top, other_top);
		double intersectionRight  = Math.min(this_left + this_width, other_left + other_width);
		double intersectionBottom = Math.min(this_top + this_height, other_top + other_height);
	
		return intersectionLeft < intersectionRight && intersectionTop < intersectionBottom;

	}

	/**
	 * Determines if a point is within the bounds of a Rectangle
	 * 
	 * @param position
	 * @return boolean true or false to indicate whether it contains the point or not
	 */
	boolean contains(Vector2 position) {
		if((position.x >= this.topLeft.x && position.x <= this.bottomRight.x) &&
				(position.y >= this.topLeft.y && position.y <= this.bottomRight.y)) {
			return true;
		}
		else {
			return false;
		}
	
	}

}

