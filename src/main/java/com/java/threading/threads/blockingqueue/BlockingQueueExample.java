package com.java.threading.threads.blockingqueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockingQueueExample {

  public static void main(String[] args) throws InterruptedException {

    BlockingQueue<Integer> blockingQueueSynchronised = new BlockingQueueWithLock<>(5);
        //new BlockingQueueSynchronised<>(5);

    ExecutorService executor = Executors.newFixedThreadPool(3);

    // Producer
    executor.execute(() -> {
      try {
        for (int i = 1; i <= 10; i++) {
          blockingQueueSynchronised.put(i);
          System.out.println("Produced: " + i);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    executor.execute(() -> {
      try {
        for (int i = 11; i <= 20; i++) {
          blockingQueueSynchronised.put(i);
          System.out.println("Produced: " + i);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    // Consumer
    executor.execute(() -> {
      try {
        for (int i = 1; i <= 20; i++) {
          int item = blockingQueueSynchronised.take();
          System.out.println("Consumed: " + item);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    executor.shutdown();
  }

}
