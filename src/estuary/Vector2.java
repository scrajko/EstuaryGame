package estuary;

public class Vector2 {

	public double x;
	public double y;
	
	Vector2() {
		x = 0.0;
		y = 0.0;
	}
	
	Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double distanceTo(Vector2 estuary) {
		double dx = estuary.x - this.x;
		double dy = estuary.y - this.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
}


