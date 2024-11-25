package com.java.threading.threads;

public class PrintNumbers {

  int currentNumber = 0;
  int maxNumber = 100;
  Object lock = new Object();

  public void printNumbers(int reminder) {

    while(currentNumber < maxNumber) {
      synchronized (lock) {
        while (currentNumber < maxNumber && currentNumber % 4 != reminder) {
          try {
            lock.wait();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
        System.out.println(currentNumber);
        lock.notifyAll();

        if(currentNumber < maxNumber) {
          System.out.println("Current Number: " + currentNumber + " " + Thread.currentThread()
              .getName());
          currentNumber++;
        }
      }

    }
  }

  private void printTwos() {

  }

  private void printThrees() {

  }


}
