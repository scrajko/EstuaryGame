package estuary.model;

/**
 * A vector is a geometric object that has a magnitude (or length) and a direction.
 * Vectors can be added together using vector algebra.
 * See: https://en.wikipedia.org/wiki/Euclidean_vector
 * 
 * Vectors are used to represent positions and velocities in our game.
 * 
 * The "2" represents the number of dimensions (x, y).
 * 
 * @author Sean
 *
 */
public class Vector2 implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public double x;
	public double y;
	
	/**
	 * Default constructor sets the vector [0,0]
	 */
	public Vector2() {
		x = 0.0;
		y = 0.0;
	}
	
	/**
	 * Constructor that creates a vector [x,y]
	 * @param x x-value of vector
	 * @param y y-value of vector
	 */
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy Constructor
	 * @param v Vector2 to be copied
	 */
	public Vector2(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	/**
	 * Returns the magnitude squared
	 * @return x^2 + y^2
	 */
	public double magnitudeSquared() {
		return x*x + y*y;
	}
	

	/**
	 * A Vector is equal to another Vector
	 * if both their x and y coordinates are the same.
	 * 
	 * @return boolean equality of the two vectors
	 */
	@Override
	public boolean equals(Object other) {
		
		if (this == other)
			return true;
		if (!(other instanceof Vector2))
			return false;
		
		Vector2 otherVector2 = (Vector2)other;
		
		return this.x == otherVector2.x && this.y == otherVector2.y;
	}
	
	/**
	 * Displays a String description of the vector when called in print statements 
	 * "(x, y)", ex: "(9.5, 3.5")
	 * 
	 * @return String the String representation
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Determines the distance from one vector to another
	 * @param rhs the other Vector
	 * @return double the length between the two vectors
	 */
	public double distanceTo(Vector2 rhs) {
		double dx = rhs.x - this.x;
		double dy = rhs.y - this.y;
		return Math.sqrt(dx*dx + dy*dy);
	}

	/**
	 * Adds right-hand vector onto this vector
	 * @param rhs other vector being added
	 */
	public void add(Vector2 rhs) {	
		this.x += rhs.x;
		this.y += rhs.y;
	}

	/**
	 * Subtracts the right-hand vector from this vectot
	 * @param rhs other vector being subtracted
	 */
	public void subtract(Vector2 rhs) {
		this.x -= rhs.x;
		this.y -= rhs.y;
	}
	
	/**
	 * Adds two Vectors and returns their vector sum.
	 *   (lhs + rhs)
	 * @param lhs left-hand Vector
	 * @param rhs right-hand Vector
	 * @return Vector2 sum of both Vectors
	 */
	public static Vector2 add(Vector2 lhs, Vector2 rhs) {
		return new Vector2(lhs.x + rhs.x, lhs.y + rhs.y);
	}

	/**
	 * Creates a new Vector that is first vector subtracted by the second
	 *   (lhs - rhs)
	 * @param lhs left-hand Vector
	 * @param rhs right-hand Vector
	 * @return Vector2 the difference between the two vectors
	 */
	public static Vector2 subtract(Vector2 lhs, Vector2 rhs) {
		return new Vector2(lhs.x - rhs.x, lhs.y - rhs.y);
	}
}


