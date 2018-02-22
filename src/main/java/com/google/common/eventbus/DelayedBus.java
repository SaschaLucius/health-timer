package com.google.common.eventbus;

import com.google.common.util.concurrent.MoreExecutors;

public class DelayedBus extends EventBus {
	private static DelayedBus instance;
	private static DelayedDispatcher dispatcher;

	private DelayedBus(Dispatcher dispatcher) {
		super("delayedBus", MoreExecutors.directExecutor(), dispatcher, LoggingHandler.INSTANCE);
	}

	public static DelayedBus getInstance() {
		if (instance == null) {
			dispatcher = new DelayedDispatcher();
			instance = new DelayedBus(dispatcher);
		}
		return instance;
	}

	public void clear() {
		dispatcher.clear();
	}
}
