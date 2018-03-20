package com.google.common.eventbus;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import datameer.health.backend.events.DelayedEvent;

public class DelayedDispatcher extends Dispatcher {
	Dispatcher defaultDispatcher = Dispatcher.perThreadDispatchQueue();
	ScheduledExecutorService service;

	DelayedDispatcher() {
		service = Executors.newScheduledThreadPool(0);
	}

	public void clear() {
		service.shutdownNow();
		service = Executors.newScheduledThreadPool(0);
	}

	@Override
	void dispatch(Object event, Iterator<Subscriber> subscribers) {
		checkNotNull(event);
		if (event instanceof DelayedEvent) {
			DelayedEvent delayedEvent = (DelayedEvent) event;
			Runnable dispatcher = new Runnable() {
				@Override
				public void run() {
					while (subscribers.hasNext()) {
						subscribers.next().dispatchEvent(event);
					}
				}
			};
			service.scheduleAtFixedRate(dispatcher, delayedEvent.delay(), 1, TimeUnit.SECONDS);
		} else {
			defaultDispatcher.dispatch(event, subscribers);
		}
	}
}
