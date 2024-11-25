package com.java.threading.threads;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DelayedScheduler {

  private final PriorityBlockingQueue<ScheduledTask> taskQueue;
  private final AtomicBoolean isRunning;
  private final Thread schedulerThread;

  public DelayedScheduler() {
    this.taskQueue = new PriorityBlockingQueue<>();
    this.isRunning = new AtomicBoolean(true);

    // Scheduler thread to execute tasks
    this.schedulerThread = new Thread(this::processTasks);
    this.schedulerThread.start();
  }

  // Schedules a task to run after a specified delay
  public void schedule(Runnable task, long delayInMillis) {
    long executionTime = System.currentTimeMillis() + delayInMillis;
    ScheduledTask scheduledTask = new ScheduledTask(task, executionTime);
    taskQueue.add(scheduledTask);
  }

  // Stops the scheduler gracefully
  public void stop() {
    isRunning.set(false);
    schedulerThread.interrupt();
  }

  // Processes tasks in the queue
  private void processTasks() {
    while (isRunning.get()) {
      try {
        ScheduledTask nextTask = taskQueue.take();

        long currentTime = System.currentTimeMillis();
        if (nextTask.executionTime > currentTime) {
          // Re-add the task and sleep until it's ready
          taskQueue.add(nextTask);
          Thread.sleep(nextTask.executionTime - currentTime);
        } else {
          // Execute the task
          nextTask.task.run();
        }
      } catch (InterruptedException e) {
        // Handle interruption gracefully
        Thread.currentThread().interrupt();
      }
    }
  }

  // Represents a task to be scheduled
  private static class ScheduledTask implements Comparable<ScheduledTask> {

    private final Runnable task;
    private final long executionTime;

    public ScheduledTask(Runnable task, long executionTime) {
      this.task = task;
      this.executionTime = executionTime;
    }

    @Override
    public int compareTo(ScheduledTask other) {
      return Long.compare(this.executionTime, other.executionTime);
    }
  }
}

