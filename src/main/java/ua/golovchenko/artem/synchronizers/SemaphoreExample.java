package ua.golovchenko.artem.synchronizers;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {
    private static final int SEMAPOHORES = 1;

    private static Semaphore semaphore = new Semaphore(SEMAPOHORES);

    public static void main(String[] args) {
        Thread t1 = new Thread(new RunnableTask());
        Thread t2 = new Thread(new RunnableTask());

        t1.start();
        t2.start();

    }

    private static class RunnableTask implements Runnable {
        public void run() {
            System.out.println(Thread.currentThread().getName() + " start and lock semaphore");
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " does something");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " releasing semaphore");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
