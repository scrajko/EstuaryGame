package estuary.model;

import estuary.view.ImageHandler;

/**
 * Used to handle the images for entities
 * @author Sean Rajkowski
 *
 */
public class Animation implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public int imageIndex;
	public int frameIndex;
	
	/**
	 * default constructor
	 */
	public Animation() {

	}
	
	/**
	 * Constructor for animation that takes in the current imageIndex
	 * @param imageIndex current index in image
	 */
	public Animation(int imageIndex) {
		this.imageIndex = imageIndex;
		this.frameIndex = 0;
	}
	
	/**
	 * Setter used to set an animation's image
	 * @param index current index of image
	 * @param resetFrame sets the index back to 0
	 */
	public void setImage(int index, boolean resetFrame) {
		imageIndex = index;
		if (resetFrame) 
			frameIndex = 0;
	}
	
	/**
	 * Increases the frame index by 1
	 */
	public void incrementFrame() {
		frameIndex = (frameIndex + 1) % ImageHandler.getImageSet(imageIndex).size();
	}
}
