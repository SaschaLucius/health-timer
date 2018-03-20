package datameer.health.backend;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.util.Duration;

public class CountDown {
	private final ReadOnlyIntegerWrapper timeLeft;
	private final ReadOnlyDoubleWrapper timeLeftDouble;
	private final ReadOnlyStringWrapper timeLeftSting;
	private final Timeline timeline;

	public ReadOnlyIntegerProperty timeLeftProperty() {
		return timeLeft.getReadOnlyProperty();
	}

	public ReadOnlyStringProperty asString() {
		return timeLeftSting.getReadOnlyProperty();
	}

	public CountDown(final int time) {
		timeLeft = new ReadOnlyIntegerWrapper(time);
		timeLeftDouble = new ReadOnlyDoubleWrapper(time);
		timeLeftSting = new ReadOnlyStringWrapper();
		if (time == 0) {
			timeLeftSting.set("Close");
		} else {
			timeLeftSting.set("Start");
		}

		timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(timeLeftDouble, time)),
				new KeyFrame(Duration.seconds(time), new KeyValue(timeLeftDouble, 0)));

		timeLeftDouble.addListener(new InvalidationListener() {
			private boolean played = false;
			@Override
			public void invalidated(Observable o) {
				timeLeft.set((int) Math.ceil(timeLeftDouble.get()));
				if (isRunning()) {
					timeLeftSting.set(String.format("%3d", timeLeftProperty().get()));
				}
				if (timeLeft.get() == 0) {
					timeLeftSting.set("Close");
					if (!played) {
						played = true;
						Sound.playSound();
					}
				}
			}
		});
	}

	public boolean isRunning() {
		return timeline.getStatus() != javafx.animation.Animation.Status.PAUSED
				&& timeline.getStatus() != javafx.animation.Animation.Status.STOPPED;
	}

	public void stop() {
		timeline.stop();
	}

	public void start() {
		timeline.playFromStart();
	}
}