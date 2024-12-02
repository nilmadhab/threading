package com.java.threading.threads.blockingqueue;

import java.util.ArrayList;
import java.util.List;

public class BlockingQueueSynchronised<T> implements BlockingQueue<T> {

  private final int capacity;
  private List<T> queue;

  public BlockingQueueSynchronised(int capacity) {
    this.capacity = capacity;
    this.queue = new ArrayList<>(capacity);
  }

  @Override
  public synchronized void put(T item) throws InterruptedException {
    if(queue.size() == capacity) {
      wait();
    }
    this.queue.add(item);
    notifyAll();
  }

  @Override
  public synchronized T take() throws InterruptedException {
    if(queue.isEmpty()) {
      wait();
    }
    T item = queue.remove(0);
    notifyAll();
    return item;
  }

  // Optional: Get the current size of the queue
  public synchronized int size() {
    return queue.size();
  }


}
