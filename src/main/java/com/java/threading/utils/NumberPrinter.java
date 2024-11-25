package com.java.threading.utils;

public class NumberPrinter {
  private final int maxNumber;
  private volatile int currentNumber = 1;
  private final Object lock = new Object();

  public NumberPrinter(int maxNumber) {
    this.maxNumber = maxNumber;
  }

  public void printEven() {
    while (currentNumber <= maxNumber) {
      synchronized (lock) {
        // Wait while number is odd
        while (currentNumber % 2 != 0 && currentNumber <= maxNumber) {
          try {
            lock.wait();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
          }
        }

        // Print even number if we haven't exceeded maxNumber
        if (currentNumber <= maxNumber) {
          System.out.println("Even Thread: " + currentNumber);
          currentNumber++;
          lock.notify();
        }
      }
    }
  }

  public void printOdd() {
    while (currentNumber <= maxNumber) {
      synchronized (lock) {
        // Wait while number is even
        while (currentNumber % 2 == 0 && currentNumber <= maxNumber) {
          try {
            lock.wait();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
          }
        }

        // Print odd number if we haven't exceeded maxNumber
        if (currentNumber <= maxNumber) {
          System.out.println("Odd Thread: " + currentNumber);
          currentNumber++;
          lock.notify();
        }
      }
    }
  }
}
