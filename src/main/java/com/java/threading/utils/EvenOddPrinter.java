package com.java.threading.utils;



public class EvenOddPrinter {
  public static void main(String[] args) {
    NumberPrinter printer = new NumberPrinter(10);

    // Create threads
    Thread evenThread = new Thread(printer::printEven);
    Thread oddThread = new Thread(printer::printOdd);

    // Start threads
    oddThread.start();
    evenThread.start();

    // Wait for both threads to complete
    try {
      evenThread.join();
      oddThread.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.out.println("Main thread interrupted");
    }
  }
}