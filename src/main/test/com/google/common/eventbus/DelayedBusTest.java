package com.google.common.eventbus;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Optional;

import datameer.health.backend.events.DelayedEvent;
import datameer.health.backend.events.DelayedEvent.Builder;
import datameer.health.backend.listener.EventListener;
import javafx.embed.swing.JFXPanel;

public class DelayedBusTest {

	DelayedBus eventBus = DelayedBus.getInstance();
	EventListener listener;

	@BeforeClass
	public static void beforeClass() {
		new JFXPanel();
	}

	@Before
	public void before() {
		eventBus.clear();
		listener = new EventListener();
		eventBus.register(listener);
	}

	@After
	public void after() {
		eventBus.unregister(listener);
		listener = null;
	}

	@Test
	public void givenStringEvent() throws InterruptedException {
		eventBus.post("String Event");
		Thread.sleep(10);
		assertEquals(1, listener.getEventsHandled());
	}

	@Test
	public void givenDelayedEvent() throws InterruptedException {
		eventBus.post(new DelayedEvent.Builder().delay(100).message("test").build());
		Thread.sleep(200);
		assertEquals(1, listener.getEventsHandled());
	}

	@Test
	public void givenCustomEvents() throws InterruptedException {
		eventBus.post(new DelayedEvent.Builder().delay(100).build());
		eventBus.post(new DelayedEvent.Builder().delay(200).build());
		eventBus.post(new DelayedEvent.Builder().delay(300).build());
		eventBus.post(new DelayedEvent.Builder().delay(400).build());
		eventBus.post(new DelayedEvent.Builder().delay(500).build());
		Thread.sleep(50);
		assertEquals(0, listener.getEventsHandled());
		Thread.sleep(100);
		assertEquals(1, listener.getEventsHandled());
		Thread.sleep(100);
		assertEquals(2, listener.getEventsHandled());
		Thread.sleep(100);
		assertEquals(3, listener.getEventsHandled());
		Thread.sleep(100);
		assertEquals(4, listener.getEventsHandled());
		Thread.sleep(100);
		assertEquals(5, listener.getEventsHandled());
	}

	@Test
	public void givenCustomEvents_clear() throws InterruptedException {
		eventBus.post(new DelayedEvent.Builder().delay(100).build());
		eventBus.post(new DelayedEvent.Builder().delay(200).build());
		eventBus.post(new DelayedEvent.Builder().delay(300).build());
		eventBus.post(new DelayedEvent.Builder().delay(400).build());
		eventBus.post(new DelayedEvent.Builder().delay(500).build());
		Thread.sleep(50);
		assertEquals(0, listener.getEventsHandled());
		Thread.sleep(100);
		assertEquals(1, listener.getEventsHandled());
		Thread.sleep(100);
		assertEquals(2, listener.getEventsHandled());
		eventBus.clear();
		Thread.sleep(100);
		assertEquals(2, listener.getEventsHandled());
		Thread.sleep(100);
		assertEquals(2, listener.getEventsHandled());
		Thread.sleep(100);
		assertEquals(2, listener.getEventsHandled());
	}

	@Test
	public void givenCustomEvent_dead() throws InterruptedException {
		eventBus.post(Optional.absent());
		Thread.sleep(10);
		assertEquals(1, listener.getEventsHandled());
	}

	@Test
	public void givenCustomEvent_nextEvent() throws InterruptedException {
		Builder builder = new DelayedEvent.Builder().delay(10);
		builder.next(new DelayedEvent.Builder().delay(10).build());
		eventBus.post(builder.build());
		Thread.sleep(50);
		assertEquals(2, listener.getEventsHandled());
	}

	@Test
	public void givenCustomEvent_nextEvent_this() throws InterruptedException {
		eventBus.post(new DelayedEvent.Builder().delay(10).repeat().build());
		Thread.sleep(45);
		assertEquals(4, listener.getEventsHandled());
	}

	@Test
	public void givenCustomEven() throws InterruptedException {
		File file = new File("img/");

		Random r = new Random();
		int Low = 0;
		int High = file.listFiles().length - 1;
		int Result = r.nextInt(High - Low) + Low;
		System.out.println(file.listFiles()[Result].getPath());
		System.out.println(file.listFiles()[Result].getAbsolutePath());
		try {
			System.out.println(file.listFiles()[Result].getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(file.listFiles()[Result].getName());
		System.out.println(file.listFiles()[Result].canRead());
	}
}
