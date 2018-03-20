package datameer.health.backend.listener;

import java.text.SimpleDateFormat;
import java.util.Optional;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.DelayedBus;
import com.google.common.eventbus.Subscribe;

import datameer.health.backend.events.DelayedEvent;
import datameer.health.frontend.MyAlert;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class EventListener {

	private int _eventsHandled;

	@Subscribe
	public void someCustomEvent(DelayedEvent customEvent) {

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + "/ DelayedEvent: "
				+ customEvent.message());
		// CountDownLatch blub = new CountDownLatch(1);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				MyAlert myAlert = new MyAlert(customEvent);
				// https://stackoverflow.com/questions/38799220
				((Stage) myAlert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);

				Optional<ButtonType> showAndWait = myAlert.showAndWait();

				if (showAndWait.isPresent()) {
					ButtonType buttonType = showAndWait.get();
					switch (buttonType.getText()) {
					case MyAlert.CLOSE:
						myAlert.stop();
						System.out.println(customEvent.message() + ": Close");
						setEventsHandled(getEventsHandled() + 1);
						if (customEvent.next().isPresent()) {
							DelayedBus.getInstance().post(customEvent.next().get());
						}
						break;
					default:
						myAlert.stop();
						System.out.println(buttonType.toString());
						throw new IllegalStateException();
					}
				}
			}
		});
	}

	@Subscribe
	public void handleDeadEvent(DeadEvent deadEvent) {
		System.out.println("deadEvent: " + deadEvent.getEvent().getClass());
		setEventsHandled(getEventsHandled() + 1);
	}

	public int getEventsHandled() {
		return _eventsHandled;
	}

	public void setEventsHandled(int eventsHandled) {
		_eventsHandled = eventsHandled;
	}
}
