package com.java.threading.threads.blockingqueue;

public interface BlockingQueue<T> {

  void put(T item) throws InterruptedException;
  T take() throws InterruptedException;

}
