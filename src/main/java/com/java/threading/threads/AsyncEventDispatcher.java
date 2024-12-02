package com.java.threading.threads;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AsyncEventDispatcher {

  private final Map<String, List<Consumer<Event>>> listeners = new ConcurrentHashMap<>();
  private final ExecutorService executorService;

  // Constructor
  public AsyncEventDispatcher(int threadPoolSize) {
    this.executorService = Executors.newFixedThreadPool(threadPoolSize);
  }

  // Method to register a listener for a specific event type
  public void registerListener(String eventType, Consumer<Event> listener) {
    // TODO: Add listener to the appropriate data structure
    listeners.putIfAbsent(eventType, Collections.synchronizedList(new ArrayList<>()));
    listeners.get(eventType).add(listener);
  }

  // Method to deregister a listener for a specific event type
  public void deregisterListener(String eventType, Consumer<Event> listener) {
    // TODO: Remove listener from the appropriate data structure
    List<Consumer<Event>> eventListeners = listeners.get(eventType);
    if (eventListeners != null) {
      eventListeners.remove(listener);
    }
  }

  // Method to dispatch an event to all relevant listeners
  public void dispatchEvent(Event event) {
    // TODO: Dispatch the event asynchronously to all registered listeners
    List<Consumer<Event>> consumers = listeners.get(event.getType());
    for (Consumer<Event> consumer: consumers) {
      consumer.accept(event);
    }

  }

  // Method to gracefully shut down the dispatcher
  public void shutdown() {
    // TODO: Shut down the thread pool
    executorService.shutdown();
  }



  // Event class
  public static class Event {
    private final String type;
    private final String data;

    public Event(String type, String data) {
      this.type = type;
      this.data = data;
    }

    public String getType() {
      return type;
    }

    public String getData() {
      return data;
    }
  }
}
