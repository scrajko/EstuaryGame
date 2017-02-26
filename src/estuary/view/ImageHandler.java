package estuary.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


/**
 * Responsible for holding all of the image files and animation frames.
 * 
 * @author Sean Rajkowski
 *
 */
public class ImageHandler {

	//private int numImages = 1;
	
	public static final int DEFAULT_FRAME       = 0;
	
	public static final int Spotlight           = 0;
	
	public static final int MazeCrab_Stationary = 1;
	public static final int MazeCrab_Moving     = 2;
	
	public static final int RockWall            = 3;
	public static final int HorseshoeCrab_North = 4;
	public static final int HorseshoeCrab_West  = 5;
	public static final int HorseshoeCrab_South = 6;
	public static final int HorseshoeCrab_East  = 7;
	public static final int Rock1               = 8;
	
	public static final int MazeFloor           = 9;
	public static final int SandFloor           = 10;
	
	public static final int ShoreWater          = 11;
	
	public static final int Crack5              = 12;
	public static final int Crack4              = 13;
	public static final int Crack3              = 14;
	public static final int Crack2              = 15;
	public static final int Crack1              = 16;

	public static final int HorseshoeCrab_East_Found = 17;
	public static final int HorseshoeCrab_West_Found = 18;
	
	public static final int Boat_Right               = 19;
	
	public static final int Kelp			         = 20;
	
	public static final int Oyster			         = 21;
	
	public static final int SeaWall 		         = 22;
	public static final int Wave			         = 23;
	
	public static final int Predator_Left	 	     = 24;
	public static final int Predator_Right			 = 25;
	
	// Must match order of non-hurt sequence:
	public static final int MazeCrab_Stationary_Hurt = 26;
	public static final int MazeCrab_Moving_Hurt     = 27;
	
	public static final int Heart_Full       = 28;
	public static final int Heart_Empty      = 29;
	
	public static final int Denrec			 = 30;
	
	public static final int Gabion		     = 31;
	public static final int Boat_Left		 = 32;
	
	public static final int Shark_Up         = 33;
	public static final int Shark_Down       = 34;
	
	public static final int SplashScreen     = 35;
	
	public static final int Storm			 = 36;
	
	public static final int Sailboat_Left	 = 37;
	public static final int Sailboat_Right   = 38;
	
	public static final int TextMazeMovement = 39;
	public static final int TextMazeSalinity = 40;
	public static final int TextMazePredator = 41;
	public static final int TextMazeKelp	 = 42;
	public static final int TextMazeEnd		 = 43;
	
	public static final int TextCountingCount   = 44;
	public static final int TextCountingGlow    = 45;
	public static final int TextCountingBattery = 46;
	
	public static final int TextShoreMovement 	 = 47;
	public static final int TextShoreSeawall     = 48;
	public static final int TextShoreShorethreat = 49;
	public static final int TextShoreOysters     = 50;
	public static final int TextShoreGabion      = 51;
	public static final int TextShoreEnd         = 52;
	
	public static final int Battery   = 53;
	public static final int Hammer    = 54;
	                                    // hammer has 2x images with it,
	                                    // so EndScreen is now off by 1
	public static final int EndScreen = 55;
	
	/////////////////////////////////////////////////
	public static final int HurtOffset          = MazeCrab_Moving_Hurt - MazeCrab_Moving;
	/////////////////////////////////////////////////
	public static final int CountingGame_Darkness_Red   = 0;
	public static final int CountingGame_Darkness_Green = 0;
	public static final int CountingGame_Darkness_Blue  = 30;
	
	private List<List<BufferedImage>> images;
	
	/**
	 * Constructor used to give the option of whether or not to load the image files
	 * @param loadImages
	 */
	private ImageHandler(boolean loadImages) {
		if (loadImages)
			loadResources_nonstatic();
	}
	
	/**
	 * Edits the image file of the light used in the CountingGame to match the color of the surrounding darkness
	 * @param img Image to be modified
	 */
	private void setupLight(BufferedImage img) {
		
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				
				// http://stackoverflow.com/questions/1599963/how-to-read-pixel-color-in-a-java-bufferedimage-with-transparency?rq=1
				// http://www.javamex.com/tutorials/graphics/bufferedimage_setrgb.shtml
				int color = img.getRGB(x, y);
				
				int  red   = CountingGame_Darkness_Red;  // 0
				int  green = CountingGame_Darkness_Green; // 0
				int  blue  = CountingGame_Darkness_Blue;  // 30;
				int alpha = (color >> 24) & 0xff;
	
				alpha = (int)Math.round((View.ModelCountingGame_DARKNESS/255.0)*alpha);
				
				int col = (alpha << 24) | (red << 16) | (green << 8) | blue;
				img.setRGB(x, y, col);
			}
		}
	}
	
	/**
	 * Sets up shore water images
	 * @param filepath
	 */
	private void setupShoreWater(String filepath) {
		BufferedImage img = createImage(filepath);
		images.get(ShoreWater).add(img);
	}
	
	/**
	 * Sets up the animation of the Shore Crab to handle moving and being stationary
	 * @param filepath
	 * @param MovingIndex
	 * @param StationaryIndex
	 */
	private void setupMazeCrab(String filepath, int MovingIndex, int StationaryIndex) {

		int StationaryFrameX = 3; // 4th column
		int StationaryFrameY = 1; // 2nd row
		
    	BufferedImage img = createImage(filepath);
		int imgWidth  = img.getWidth()  / 6;
		int imgHeight = img.getHeight() / 4;
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				images.get(MovingIndex).add(
						img.getSubimage(imgWidth*i, imgHeight*j, imgWidth, imgHeight)
				);
			}
		}
		
		images.get(StationaryIndex).add(
			img.getSubimage(imgWidth*StationaryFrameX,
					imgHeight*StationaryFrameY,
					imgWidth, imgHeight)
		);

	}
	
	/**
	 * Sets up Hammer animation for when shoreCrab is building a wall
	 * @param hammer1_path first frame of animation
	 * @param hammer2_path second frame of animation
	 * @param HammerIndex
	 */
	private void setupHammer(String hammer1_path, String hammer2_path, int HammerIndex) {
		
		int animation_length = 5;
		BufferedImage img;
		
		img = createImage(hammer1_path);
		for (int i = 0; i < animation_length; i++) {
			images.get(HammerIndex).add(img);
		}
		
		img = createImage(hammer2_path);
		for (int i = 0; i < animation_length; i++) {
			images.get(HammerIndex).add(img);
		}
	}
	
	/**
	 * Singleton Design Pattern designed to let other classes access the only instance of the image handler
	 *
	 */
	private static class ImageHandlerHolder {
		private static ImageHandler instance = new ImageHandler(true);
	}
	
	/**
	 * Initializes all needed image resources for later use
	 */
	private void loadResources_nonstatic() {
		
		String[] filepaths = {
		    "images/counting_spotlight.png",  // 0
		    
		    "images/MazeCrab_Stationary.png", // 1
		    "images/MazeCrab_Moving_00.png",  // 2
		    "images/MazeCrab_Moving_01.png",  // 3
		    "images/MazeCrab_Moving_02.png",  // 4
		    
		    "images/hcrab/HorseshoeCrab_E.png",   // 5
		    "images/hcrab/HorseshoeCrab_W.png",   // 6
		    "images/rockwall.jpg",                // 7
		    "images/rock.png",                    // 8
		    "images/crab/blue_crab_walk.png",     // 9

		    "images/dirt_texture.jpg",            // 10
		    "images/sand.jpg",                    // 11
		    "images/water/Wave0001.png",          // 12
		    
		    "images/crack/crack5.png",                 // 13
		    "images/crack/crack4.png",                 // 14 
		    "images/crack/crack3.png",                 // 15
		    "images/crack/crack2.png",                 // 16
		    "images/crack/crack1.png",                 // 17
		    "images/hcrab/HorseshoeCrab_E_found.png",  // 18
		    "images/hcrab/HorseshoeCrab_W_found.png",  // 19
		    
		    "images/boat_right.png",                // 20
		    "images/kelp.png",					    // 21
		    "images/oyster.png",				    // 22
		    
		    "images/wall.jpeg",					    // 23
		    "images/wave.png",					    // 24
		    "images/predator/predator_left.png",	// 25
		    "images/predator/predator_right.png",   // 26
		    
		    "images/crab/blue_crab_walk_hurt.png",  // 27
		    
		    "images/heart_full.png",                // 28
		    "images/heart_empty.png",               // 29
		    
		    "images/denrec.png",					// 30
		    "images/gabion.png",					// 31
		    "images/boat_left.png",					// 32
		    
		    "images/predator/shark_up.png",         // 33
		    "images/predator/shark_down.png",       // 34
		    
		    "images/splash.jpg",                    // 35
		    
		    "images/storm/Storm_Cloud_Lightning.png", // 36
		    
		    "images/sailboat/sailboat_left.png",		// 37
		    "images/sailboat/sailboat_right.png",		// 38
		    
		    "images/text_bubbles/TextMazeMovement.png", // 39
		    "images/text_bubbles/TextMazeSalinity.png", // 40
		    "images/text_bubbles/TextMazePredator.png", // 41
		    "images/text_bubbles/TextMazeKelp.png", 	// 42
		    "images/text_bubbles/TextMazeEnd.png", 		// 43
		    
		    "images/text_bubbles/TextCountingCount.png",   // 44
		    "images/text_bubbles/TextCountingGlow.png",    // 45
		    "images/text_bubbles/TextCountingBattery.png", // 46
		    
		    "images/text_bubbles/TextShoreMovement.png",    // 47
		    "images/text_bubbles/TextShoreSeawall.png",     // 48
		    "images/text_bubbles/TextShoreShorethreat.png", // 49
		    "images/text_bubbles/TextShoreOysters.png",     // 50
		    "images/text_bubbles/TextShoreGabion.png",      // 51
		    "images/text_bubbles/TextShoreEnd.png",         // 52
		    
		    "images/battery.png",				//53
		    "images/hammer/hammer1.png",        //54
		    "images/hammer/hammer2.png",		//55
		    "images/endscreen.jpg"				//56
		    
		};
		
		images = new ArrayList<List<BufferedImage>>();
		images.add(new ArrayList<BufferedImage>()); // First image's list of frames
		for (int i = 0; i < 60; i++) {
			images.add(new ArrayList<BufferedImage>());
		}
		
		BufferedImage img = createImage(filepaths[Spotlight]);
		setupLight(img);
		images.get(Spotlight).add(img); // Add first frame of first image

		img = createImage(filepaths[5]);
    	images.get(HorseshoeCrab_East).add(img);
    	
    	img = createImage(filepaths[6]);
    	images.get(HorseshoeCrab_West).add(img);
    	
    	img = createImage(filepaths[7]);
    	images.get(RockWall).add(img);
    	
    	img = createImage(filepaths[8]);
    	images.get(Rock1).add(img);
		
    	setupMazeCrab(filepaths[9],  MazeCrab_Moving,      MazeCrab_Stationary);
    	setupMazeCrab(filepaths[MazeCrab_Moving_Hurt], MazeCrab_Moving_Hurt, MazeCrab_Stationary_Hurt);
    	
    	img = createImage(filepaths[10]);
    	images.get(MazeFloor).add(img);
    	
    	img = createImage(filepaths[11]);
    	images.get(SandFloor).add(img);
		
    	setupShoreWater(filepaths[12]);
    	
    	img = createImage(filepaths[13]);
    	images.get(Crack5).add(img);
    	img = createImage(filepaths[14]);
    	images.get(Crack4).add(img);
    	img = createImage(filepaths[15]);
    	images.get(Crack3).add(img);
    	img = createImage(filepaths[16]);
    	images.get(Crack2).add(img);
    	img = createImage(filepaths[17]);
    	images.get(Crack1).add(img);
    	
    	img = createImage(filepaths[18]);
    	images.get(HorseshoeCrab_East_Found).add(img);
    	img = createImage(filepaths[19]);
    	images.get(HorseshoeCrab_West_Found).add(img);
    	
    	img = createImage(filepaths[20]);
    	images.get(Boat_Right).add(img);
    	
    	img = createImage(filepaths[21]);
    	images.get(Kelp).add(img);
    	
    	img = createImage(filepaths[22]);
    	images.get(Oyster).add(img);
    	
    	img = createImage(filepaths[23]);
    	images.get(SeaWall).add(img);
    	
    	img = createImage(filepaths[24]);
    	images.get(Wave).add(img);
    	
    	img = createImage(filepaths[25]);
    	images.get(Predator_Left).add(img);
    	img = createImage(filepaths[26]);
    	images.get(Predator_Right).add(img);
    	
    	img = createImage(filepaths[28]);
    	images.get(Heart_Full).add(img);
    	img = createImage(filepaths[29]);
    	images.get(Heart_Empty).add(img);
    	
    	img = createImage(filepaths[30]);
    	images.get(Denrec).add(img);
    	
    	img = createImage(filepaths[31]);
    	images.get(Gabion).add(img);
    	
    	img = createImage(filepaths[32]);
    	images.get(Boat_Left).add(img);
    	
    	img = createImage(filepaths[33]);
    	images.get(Shark_Up).add(img);
    	img = createImage(filepaths[34]);
    	images.get(Shark_Down).add(img);
    	
    	img = createImage(filepaths[35]);
    	images.get(SplashScreen).add(img);
    	
    	img = createImage(filepaths[36]);
    	images.get(Storm).add(img);
    	
    	img = createImage(filepaths[37]);
    	images.get(Sailboat_Left).add(img);
    	img = createImage(filepaths[38]);
    	images.get(Sailboat_Right).add(img);
    	
    	
    	img = createImage(filepaths[39]);
    	images.get(TextMazeMovement).add(img);
    	img = createImage(filepaths[40]);
    	images.get(TextMazeSalinity).add(img);
    	img = createImage(filepaths[41]);
    	images.get(TextMazePredator).add(img);
    	img = createImage(filepaths[42]);
    	images.get(TextMazeKelp).add(img);
    	img = createImage(filepaths[43]);
    	images.get(TextMazeEnd).add(img);
    	
    	img = createImage(filepaths[44]);
    	images.get(TextCountingCount).add(img);
    	img = createImage(filepaths[45]);
    	images.get(TextCountingGlow).add(img);
    	img = createImage(filepaths[46]);
    	images.get(TextCountingBattery).add(img);
    	
    	img = createImage(filepaths[47]);
    	images.get(TextShoreMovement).add(img);
    	img = createImage(filepaths[48]);
    	images.get(TextShoreSeawall).add(img);
    	img = createImage(filepaths[49]);
    	images.get(TextShoreShorethreat).add(img);
    	img = createImage(filepaths[50]);
    	images.get(TextShoreOysters).add(img);
    	img = createImage(filepaths[51]);
    	images.get(TextShoreGabion).add(img);
    	img = createImage(filepaths[52]);
    	images.get(TextShoreEnd).add(img);
    	
    	img = createImage(filepaths[53]);
    	images.get(Battery).add(img);
    	
    	setupHammer(filepaths[54], filepaths[55], Hammer);
    	
    	img = createImage(filepaths[56]);
    	images.get(EndScreen).add(img);
    	
		
	}
	
	/**
	 * Loads nonstatic resources
	 */
	public static void loadResources() {
		ImageHandlerHolder.instance.loadResources_nonstatic();
	}
	
	/**
	 * Gets a BufferedImage from the internal list of images
	 * Only functionality end-user cares about.
	 * 
	 * @param imageNum
	 * @param frameNum
	 * @return BuffereImage to be drawn by the View
	 */
	public static BufferedImage getImageFrame(int imageNum, int frameNum) {
		return ImageHandlerHolder.instance.images.get(imageNum).get(frameNum);
	}
	
	/**
	 * Gets the list of frames for an individual entity
	 * @param imageNum
	 * @return
	 */
	public static List<BufferedImage> getImageSet(int imageNum) {
		return ImageHandlerHolder.instance.images.get(imageNum);
	}
	
    /**
     * Read image from file and return
     * @param path Path of image
     * @return image to be used
     */
    private BufferedImage createImage(String path){
    	
    	BufferedImage bufferedImage;
    	try {
    		bufferedImage = ImageIO.read(new File(path));
    		return bufferedImage;
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
}
