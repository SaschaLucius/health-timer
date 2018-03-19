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
				// final Button btOk = (Button)
				// myAlert.getDialogPane().lookupButton(MyAlert.buttonTypeClose);
				
				// customEvent.duration() / 1000;
				
				// Timeline fiveSecondsWonder = new Timeline(new
				// KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>()
				// {
				// @Override
				// public void handle(ActionEvent event) {
				// btOk.setText("");
				// System.out.println("this is called every 5 seconds on UI
				// thread");
				// }
				// }));
				// fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
				// fiveSecondsWonder.play();
				
				
				// btOk.addEventFilter(ActionEvent.ACTION, event -> {
				//
				// Runnable runnable = new Runnable() {
				// @Override
				// public void run() {
				// for (long i = customEvent.duration() / 1000; i > 0; i--) {
				// btOk.setText(String.valueOf(i));
				// try {
				// Thread.sleep(1000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// }
				// }
				// };
				//
				// Thread t1 = new Thread(runnable);
				// t1.start();
				//
				// event.consume();
				// });

				Optional<ButtonType> showAndWait = myAlert.showAndWait();

				if (showAndWait.isPresent()) {
					ButtonType buttonType = showAndWait.get();
					switch (buttonType.getText()) {
					case MyAlert.CLOSE:
						System.out.println(customEvent.message() + ": Close");
						// customEvent.duration()

						setEventsHandled(getEventsHandled() + 1);
						if (customEvent.next().isPresent()) {
							DelayedBus.getInstance().post(customEvent.next().get());
						}
						// blub.countDown();
						break;
					// case MyAlert.SNOOZE:
					// System.out.println(customEvent.message() + ": snooze");
					// break;
					default:
						System.out.println(buttonType.toString());
						throw new IllegalStateException();
					}
				}
			}
		});

		// try {
		// blub.await();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// setEventsHandled(getEventsHandled() + 1);
		// if (customEvent.next().isPresent()) {
		// DelayedBus.getInstance().post(customEvent.next().get());
		// }

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
