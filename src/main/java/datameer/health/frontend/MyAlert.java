package datameer.health.frontend;

import java.util.Optional;

import datameer.health.backend.events.DelayedEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MyAlert extends Alert {

	public MyAlert(DelayedEvent event) {
		super(AlertType.INFORMATION);
		setTitle("Notification for you!");
		setHeaderText(event.message());

		getDialogPane().getScene().setRoot(new Group());

		Image image = new Image(event.image());
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(512);
		imageView.setPreserveRatio(true);
		setGraphic(imageView);

		ButtonType buttonTypeOk = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
		ButtonType buttonTypeSnooze = new ButtonType("Snooze", ButtonData.NEXT_FORWARD);
		getButtonTypes().setAll(buttonTypeOk, buttonTypeSnooze);
	}

	public Optional<ButtonType> showInFrontAndWait() {
		Optional<ButtonType> showAndWait = super.showAndWait();
		DialogPane dialogPane = getDialogPane();
		System.out.println(dialogPane);
		Scene scene = dialogPane.getScene();
		System.out.println(scene);
		Stage stage = (Stage) scene.getWindow();
		System.out.println(stage);
		stage.setAlwaysOnTop(true);
		stage.toFront();
		return showAndWait;
	}
}
