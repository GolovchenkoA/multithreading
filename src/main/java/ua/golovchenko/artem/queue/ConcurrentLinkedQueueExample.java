package ua.golovchenko.artem.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLinkedQueueExample {
    private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue();
    private static List<Integer> sharedLog = new ArrayList<>();
    private static Boolean finished = false;
    private  static  final ReentrantLock logLock = new ReentrantLock();

    public static void main(String[] args) {


        ExecutorService exService = Executors.newFixedThreadPool(3);
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        Logger logger = new Logger();
        exService.execute(producer);
        exService.execute(consumer);
        exService.execute(logger);
        exService.shutdown();
    }

    private static class Producer implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < 200; i++) {
                try {
                    Thread.sleep(10);
                    System.out.println("Produce: " + i);
                    queue.add(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            finished = true;

        }
    }

    private static class Consumer implements Runnable {

        @Override
        public void run() {
            while (!finished) {
                if (!queue.isEmpty()) {
                    Integer i = queue.poll();
                    System.out.println("Consume: " + i);
                    if ( i % 2 == 0){

                       logLock.lock();
                           System.out.println("Add to log: " + i);
                           sharedLog.add(i);
                       logLock.unlock();

                    }
                }
            }

        }
    }

    private static class Logger implements Runnable {

        List<Integer> localCacheLog = new ArrayList<>();

        @Override
        public void run() {


            while (!finished) {
                try {
                    Thread.sleep(500);

                    logLock.lock();
                    localCacheLog = sharedLog;
                    sharedLog = new ArrayList<>(); // new array for shared log

                    //  if (localCacheLog.size() > 1) {
                    System.out.println("Saving log to disk: " + localCacheLog);
                    //System.out.println("Saved elements from " + localCacheLog.get(1) + " to " + localCacheLog.get(localCacheLog.size() - 1));
                    // serialization
                    // }

                    logLock.unlock();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
