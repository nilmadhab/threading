package com.java.threading.threads;

import com.java.threading.threads.AsyncEventDispatcher.Event;
import java.util.function.Consumer;
import org.junit.jupiter.api.*;
import java.util.concurrent.atomic.AtomicInteger;

class AsyncEventDispatcherTest {

  private AsyncEventDispatcher dispatcher;

  @BeforeEach
  void setUp() {
    dispatcher = new AsyncEventDispatcher(4);
  }

  @AfterEach
  void tearDown() {
    dispatcher.shutdown();
  }

  @Test
  void testWorking() throws InterruptedException {
    AsyncEventDispatcher dispatcher = new AsyncEventDispatcher(5);

    // Register listeners
    dispatcher.registerListener("TYPE_A", new Consumer<Event>() {
      @Override
      public void accept(Event event) {
        System.out.println("Listener 1 received: " + event.getData());
      }
    });

    dispatcher.registerListener("TYPE_A", event -> {
      System.out.println("Listener 2 received: " + event.getData());
    });

    dispatcher.registerListener("TYPE_B", event -> {
      System.out.println("Listener 3 received: " + event.getData());
    });

    // Dispatch events
    dispatcher.dispatchEvent(new Event("TYPE_A", "Event A1"));
    dispatcher.dispatchEvent(new Event("TYPE_A", "Event A2"));
    dispatcher.dispatchEvent(new Event("TYPE_B", "Event B1"));

    // Give some time for events to process
    Thread.sleep(2000);

    // Shutdown dispatcher
    dispatcher.shutdown();
  }

  @Test
  void testRegisterListener() {
    AtomicInteger counter = new AtomicInteger(0);
    dispatcher.registerListener("TYPE_A", event -> counter.incrementAndGet());
    dispatcher.dispatchEvent(new AsyncEventDispatcher.Event("TYPE_A", "Test Data"));

    // Wait for async execution
    sleep(500);

    Assertions.assertEquals(1, counter.get());
  }

  @Test
  void testDeregisterListener() {
    AtomicInteger counter = new AtomicInteger(0);
    Consumer<Event> listener = event -> counter.incrementAndGet();

    dispatcher.registerListener("TYPE_A", listener);
    dispatcher.deregisterListener("TYPE_A", listener);
    dispatcher.dispatchEvent(new AsyncEventDispatcher.Event("TYPE_A", "Test Data"));

    // Wait for async execution
    sleep(500);

    Assertions.assertEquals(0, counter.get());
  }

  @Test
  void testAsynchronousExecution() {
    AtomicInteger counter = new AtomicInteger(0);
    dispatcher.registerListener("TYPE_A", event -> {
      sleep(2000); // Simulate long processing
      counter.incrementAndGet();
    });

    long startTime = System.currentTimeMillis();
    dispatcher.dispatchEvent(new AsyncEventDispatcher.Event("TYPE_A", "Test Data"));
    long dispatchTime = System.currentTimeMillis() - startTime;

    // Ensure dispatch does not block
    Assertions.assertTrue(dispatchTime < 500);
  }

  @Test
  void testMultipleEventTypes() {
    AtomicInteger typeACounter = new AtomicInteger(0);
    AtomicInteger typeBCounter = new AtomicInteger(0);

    dispatcher.registerListener("TYPE_A", event -> typeACounter.incrementAndGet());
    dispatcher.registerListener("TYPE_B", event -> typeBCounter.incrementAndGet());

    dispatcher.dispatchEvent(new AsyncEventDispatcher.Event("TYPE_A", "Test A"));
    dispatcher.dispatchEvent(new AsyncEventDispatcher.Event("TYPE_B", "Test B"));

    // Wait for async execution
    sleep(500);

    Assertions.assertEquals(1, typeACounter.get());
    Assertions.assertEquals(1, typeBCounter.get());
  }

  @Test
  void testListenerExceptionHandling() {
    AtomicInteger counter = new AtomicInteger(0);

    dispatcher.registerListener("TYPE_A", event -> {
      throw new RuntimeException("Listener exception");
    });
    dispatcher.registerListener("TYPE_A", event -> counter.incrementAndGet());

    dispatcher.dispatchEvent(new AsyncEventDispatcher.Event("TYPE_A", "Test Data"));

    // Wait for async execution
    sleep(500);

    Assertions.assertEquals(1, counter.get());
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}