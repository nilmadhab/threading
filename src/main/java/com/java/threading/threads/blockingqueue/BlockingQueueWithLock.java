package com.java.threading.threads.blockingqueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueWithLock<T> implements BlockingQueue<T>{

  private final Queue<T> queue;         // Internal storage for elements
  private final int capacity;          // Maximum capacity of the queue
  private final Lock lock;             // ReentrantLock for thread safety
  private final Condition notFull;     // Condition to wait for space in the queue
  private final Condition notEmpty;    // Condition to wait for elements in the queue

  public BlockingQueueWithLock(int capacity) {
    this.queue = new LinkedList<>();
    this.capacity = capacity;
    this.lock = new ReentrantLock();
    this.notFull = lock.newCondition();
    this.notEmpty = lock.newCondition();
  }


  @Override
  public void put(T item) throws InterruptedException {
    lock.lock();
    try {
      while(size() == capacity) {
        notFull.await();
      }
      queue.add(item);
      notEmpty.signal();
    } finally {
      lock.unlock();
    }

  }

  @Override
  public T take() throws InterruptedException {
    lock.lock();
    try {
      while (size() == 0) {
        notEmpty.await();
      }
      T item = queue.poll();
      notFull.signal();
      return item;
    } finally {
      lock.unlock();
    }
  }

  public int size() {
    lock.lock();
    try {
      return queue.size();
    } finally {
      lock.unlock();
    }
  }
}
