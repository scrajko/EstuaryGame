package estuary.view;

public abstract class GameView implements GameViewFunctionality  {

	/**
	 * 	Reference to main View because it is needed for Graphics.drawImage()
	 *  A JFrame is an ImageObserver, and an ImageObserver is needed for Graphics.drawImage()
	 */
	public View view;
	
	Viewport viewport;
	
	/**
	 * Copy constructor
	 * @param view to be copied
	 */
	public GameView(View view) {
		this.view = view;
	}
}
