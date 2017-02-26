package estuary.view;

import estuary.model.Vector2;

/**
 * Allows for mapping game coordinates to screen coordinates and vice-versa
 * @author Sean Rajkowski
 *
 */
public class Viewport {
	
	Vector2 screenBegin;
	Vector2 screenEnd;

	double  sightWidth;
	double  sightHeight;
	
	Vector2 center;

	/**
	 * Constructor to create viewport based on the screen dimensions and the visible world coordinates
	 * @param screenBeginX
	 * @param screenBeginY
	 * @param screenEndX
	 * @param screenEndY
	 * @param sightWidth
	 * @param sightHeight
	 * @param center Middle of game coordinate's view
	 */
	public Viewport(int screenBeginX, int screenBeginY, int screenEndX, int screenEndY,
			double sightWidth, double sightHeight, Vector2 center) {
		
		this.screenBegin = new Vector2(screenBeginX, screenBeginY);
		this.screenEnd   = new Vector2(screenEndX,   screenEndY);
		
		this.sightWidth  = sightWidth;
		this.sightHeight = sightHeight;
		
		this.center      = new Vector2(center);
	}
	
	/**
	 * Moves the center of the viewport to a given position
	 * @param position Position for center to be moved to
	 */
	public void move(Vector2 position) {
		this.center.x = position.x;
		this.center.y = position.y;
	}
	
	/**
	 * Maps the game coordinate to a screen pixel
	 * @param coord Game coordinate
	 * @return Vector2 Pixel coordinate
	 */
	public Vector2 mapCoordToPixel(Vector2 coord) {
		
        //
        // X part
        // 
        double left  = center.x - sightWidth / 2.0;
        double right = center.x + sightWidth / 2.0;
        double width = right - left;
        
        // shift and normalize
        double coord_x_shifted    = coord.x - left;
        double coord_x_normalized = coord_x_shifted / width;
        
        int screenWidth = (int) (screenEnd.x - screenBegin.x);
        int pixel_x = (int) (screenBegin.x + coord_x_normalized * screenWidth + 0.5);
        
        //
        // Y part
        //
        double top    = center.y - sightHeight / 2.0;
        double bottom = center.y + sightHeight / 2.0;
        double height = bottom - top;
        
        // shift and normalize
        double coord_y_shifted = coord.y - top;
        double coord_y_normalized = coord_y_shifted / height;
        
        int screenHeight = (int) (screenEnd.y - screenBegin.y);
        int pixel_y = (int) (screenBegin.y + coord_y_normalized * screenHeight + 0.5);
        
        return new Vector2(pixel_x, pixel_y);
	}
	
	/**
	 * Maps screen pixel to a world coordinate
	 * @param pixel Pixel coordinate
	 * @return World coordinate
	 */
	public Vector2 mapPixelToCoord(Vector2 pixel) {
		
        //
        // X part
        //
        int left = (int)(pixel.x - screenBegin.x);
        int width = (int)(screenEnd.x - screenBegin.x);
        double ratio_x = ((double)left) / width;
        double coord_x = (center.x - sightWidth/2.0) + ratio_x * sightWidth;
        
        //
        // Y part
        // 
        int top = (int)(pixel.y - screenBegin.y);
        int height = (int)(screenEnd.y - screenBegin.y);
        double ratio_y = ((double)top) / height;
        double coord_y = (center.y - sightHeight/2.0) + ratio_y * sightHeight;
        
        return new Vector2(coord_x, coord_y);
	}
}
