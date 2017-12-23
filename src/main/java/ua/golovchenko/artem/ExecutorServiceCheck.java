package ua.golovchenko.artem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceCheck {
    private static ExecutorService es = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        // first task
        es.execute(new RunnableTask());

        // second task
        Future future = es.submit(new RunnableTask());

        System.out.println("Second task finished: " + future.isDone());
        System.out.println("Second task finished: " + future.isDone());

        es.shutdown();
    }

    private static class RunnableTask implements Runnable {
        public void run() {
            System.out.println("Runnable task done");
        }
    }
}
