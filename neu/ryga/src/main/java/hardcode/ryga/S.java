package hardcode.ryga;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class S {
	public static String APP_NAME = "Codename Ryga";
	
	public static Image getFlagImage(String language) {
		switch(language) {
		case "DE":
			return new Image(S.class.getResourceAsStream("DE.png"));
		case "PL":
			return new Image(S.class.getResourceAsStream("PL.png"));
		default:
			return null;
		}
	}
	
	public static ImageView getFlagImageView(String language) {
		return new ImageView(getFlagImage(language));
	}
	
	public static ImageView getPlusImageView(){
		return new ImageView(loadImage("plus.png"));
	}
	
	public static Image getEditImage(){
		return loadImage("edit.png");
	}
	
	public static Image getAddConnectionImage(){
		return loadImage("connection.png");
	}
	
	public static Image getAddTranslationImage(){
		return loadImage("translation.png");
	}
	
	public static Image getCloseImage(){
		return loadImage("close.png");
	}
	
	private static Image loadImage(String filename) {
		return new Image(S.class.getResourceAsStream(filename));
	}
	

	
}
