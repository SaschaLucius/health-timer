package datameer.health.backend.listener;

import java.util.Optional;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.DelayedBus;
import com.google.common.eventbus.Subscribe;

import datameer.health.backend.events.DelayedEvent;
import datameer.health.frontend.MyAlert;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class DelayedListener {
	@Subscribe
	public void someCustomEvent(DelayedEvent customEvent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				MyAlert myAlert = new MyAlert(customEvent);
				// https://stackoverflow.com/questions/38799220
				((Stage) myAlert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);

				Optional<ButtonType> alertResult = myAlert.showAndWait();
				if (alertResult.isPresent()) {
					ButtonType buttonType = alertResult.get();
					switch (buttonType.getText()) {
					case MyAlert.CLOSE:
						myAlert.stop();
						System.out.println(customEvent.message() + ": Close");
						if (customEvent.next().isPresent()) {
							DelayedBus.getInstance().post(customEvent.next().get());
						}
						break;
					default:
						myAlert.stop();
						throw new IllegalStateException("Unknown Button pressed.");
					}
				}
			}
		});
	}

	@Subscribe
	public void handleDeadEvent(DeadEvent deadEvent) {
		throw new IllegalStateException("Dead event rekognized.");
	}
}
