package estuary;

public interface Game {

	void init();
	
	boolean isEnd();
	
	void handleCollisions();
}
