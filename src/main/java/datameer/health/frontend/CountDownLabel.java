package datameer.health.frontend;

import datameer.health.backend.CountDown;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;

class CountDownLabel extends Label {
	public CountDownLabel(final CountDown countdown) {
		textProperty().bind(Bindings.format("%3d", countdown.timeLeftProperty()));
	}
}