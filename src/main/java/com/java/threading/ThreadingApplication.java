package com.java.threading;

import com.java.threading.utils.EvenOddPrinter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThreadingApplication {

  public static void main(String[] args) {
    EvenOddPrinter printer = new EvenOddPrinter();
    SpringApplication.run(ThreadingApplication.class, args);

  }

}
