package datameer.health.backend;

import java.net.URISyntaxException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
	public static void playSound() {
		try {
			String resource = Sound.class.getClassLoader().getResource("Klangschale.mp3").toURI().toString();
			Media sound = new Media(resource);
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
		} catch (URISyntaxException | NullPointerException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
