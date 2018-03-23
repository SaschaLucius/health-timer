package datameer.health.backend.events;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;

public class DelayedEvent {
	private final long delay;
	private DelayedEvent nextEvent;
	private final String message;
	private final String imagePath;
	private boolean soundAtEnd;
	private long duration;

	private DelayedEvent(Builder eventBuilder) {
		this.delay = eventBuilder.delay;
		this.nextEvent = eventBuilder.nextEvent;
		this.message = eventBuilder.message;
		this.imagePath = eventBuilder.imagePath;
		if (eventBuilder.repeat) {
			this.nextEvent = this;
		}
		this.soundAtEnd = eventBuilder.soundAtEnd;
		this.duration = eventBuilder.duration;

	}

	public static DelayedEvent chain(boolean repeat, DelayedEvent... events) {
		for (int i = 0; i < events.length - 1; i++) {
			events[i].nextEvent = events[i + 1];
		}
		if (repeat) {
			events[events.length - 1].nextEvent = events[0];
		}
		return events[0];
	}

	/**
	 * @return delay in Seconds
	 */
	public long delayInSeconds() {
		return delay;
	}

	public String image() {
		return imagePath;
	}

	/**
	 * @return duration in Seconds
	 */
	public long duration() {
		return duration;
	}

	public boolean playSound() {
		return soundAtEnd;
	}

	public Optional<DelayedEvent> next() {
		return Optional.fromNullable(nextEvent);
	}

	public String message() {
		return message;
	}

	public static class Builder {
		private long delay = 0;
		private DelayedEvent nextEvent = null;
		private String message = "";
		private boolean repeat = false;
		private String imagePath = "";
		private boolean soundAtEnd = false;
		private long duration = 0;

		/**
		 * @param delayInMilliseconds
		 */
		public Builder delay(int delayInMilliseconds) {
			this.delay = delayInMilliseconds;
			return this;
		}

		public Builder playSound() {
			this.soundAtEnd = true;
			return this;
		}

		public Builder duration(int duration, TimeUnit timeUnit) {
			this.duration = TimeUnit.SECONDS.convert(duration, timeUnit);
			return this;
		}

		public Builder delay(int delay, TimeUnit timeUnit) {
			this.delay = TimeUnit.SECONDS.convert(delay, timeUnit);
			return this;
		}

		public Builder image(String imagePath) {
			this.imagePath = imagePath;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder repeat() {
			this.repeat = true;
			return this;
		}

		public Builder repeat(boolean repeat) {
			this.repeat = repeat;
			return this;
		}

		public Builder next(DelayedEvent nextEvent) {
			this.nextEvent = nextEvent;
			return this;
		}

		public DelayedEvent build() {
			return new DelayedEvent(this);
		}
	}
}
