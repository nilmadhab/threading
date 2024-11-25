package com.java.threading.controller;

import com.java.threading.threads.DelayedScheduler;
import com.java.threading.threads.PrintNumbers;
import com.java.threading.utils.EvenOddPrinter;
import com.java.threading.utils.NumberPrinter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/threading")
public class ThreadingController {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final EvenOddPrinter evenOddPrinter = new EvenOddPrinter();


    @GetMapping("/status")
    public ResponseEntity<String> getThreadStatus() throws InterruptedException {

//      NumberPrinter printer = new NumberPrinter(10);
//
//      // Create threads
//      Thread evenThread = new Thread(printer::printEven);
//      Thread oddThread = new Thread(printer::printOdd);
//
//      // Start threads
//      oddThread.start();
//      evenThread.start();
//
//      // Wait for both threads to complete
//      try {
//        evenThread.join();
//        oddThread.join();
//      } catch (InterruptedException e) {
//        Thread.currentThread().interrupt();
//        System.out.println("Main thread interrupted");
//      }

      /*
      PrintNumbers printNumbers = new PrintNumbers();
      Thread thread1 = new Thread(() -> printNumbers.printNumbers(0));
      Thread thread2 = new Thread(() -> printNumbers.printNumbers(1));
      Thread thread3 = new Thread(() -> printNumbers.printNumbers(2));
      Thread thread4 = new Thread(() -> printNumbers.printNumbers(3));
      thread1.setName("Thread 1");
      thread2.setName("Thread 2");
      thread3.setName("Thread 3");
      thread4.setName("Thread 4");
      thread1.start();
      thread2.start();
      thread3.start();
      thread4.start();

      try {
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Main thread interrupted");
      }

       */

      DelayedScheduler scheduler = new DelayedScheduler();

      // Schedule tasks
      scheduler.schedule(() -> System.out.println("Task 1 executed at " + System.currentTimeMillis()), 2000);
      scheduler.schedule(() -> System.out.println("Task 2 executed at " + System.currentTimeMillis()), 1000);
      scheduler.schedule(() -> System.out.println("Task 3 executed at " + System.currentTimeMillis()), 3000);

      // Let tasks run
      Thread.sleep(5000);

      // Stop the scheduler
      scheduler.stop();


        return ResponseEntity.ok("Active threads: " + Thread.activeCount() +
                               "\nThread pool status: " + !executorService.isShutdown());
    }
}
