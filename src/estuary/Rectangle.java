package estuary;

public class Rectangle {
	
	Vector2 topLeft;
	Vector2 bottomRight;

	Rectangle(Vector2 topLeft, Vector2 bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}
	
	Rectangle(Vector2 topLeft, double length, double height) {
		
	}
	
	double getLength() {
		return 0.0;
	}
	double getHeight() {
		return 0.0;
	}
}
