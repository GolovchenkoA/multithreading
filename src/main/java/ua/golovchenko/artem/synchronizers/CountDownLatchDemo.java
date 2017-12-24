package ua.golovchenko.artem.synchronizers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo
{
    private static final int NTHREADS = 3;
    private static ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
    private static CountDownLatch doneSignal = new CountDownLatch(NTHREADS);
    private static CountDownLatch startSignal = new CountDownLatch(1);

    public static void main(String[] args){

        Runnable r = new Runnable(){
            public void run(){
                try{
                    report("entered run()");
                    startSignal.await();  // wait until told to ...
                    report("doing work"); // ... proceed
                    Thread.sleep((int) (Math.random() * 1000));
                    doneSignal.countDown(); // reduce count on which
                    // main thread is ...
                } catch (InterruptedException ie){
                    System.err.println(ie);
                }
            }

            void report(String s){
                System.out.println(System.currentTimeMillis() +
                        ": " + Thread.currentThread() +
                        ": " + s);
            }
        };

        for (int i = 0; i < NTHREADS; i++)
            executor.execute(r);

        try{
            System.out.println("main thread doing something");
            Thread.sleep(1000); // sleep for 1 second

            startSignal.countDown(); // let all threads proceed
            System.out.println("main thread doing something else");

            doneSignal.await(); // wait for all threads to finish
            executor.shutdownNow();
        } catch (InterruptedException ie){
            System.err.println(ie);
        }
    }
}
