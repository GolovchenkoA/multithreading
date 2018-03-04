package ua.golovchenko.artem.queue;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @see https://www.concretepage.com/java/jdk7/transferqueue-java-example
 */

public class LinkedTransferQueueExample {

    private static Boolean inprocess = true;

    static LinkedTransferQueue<String> lnkTransQueue = new LinkedTransferQueue<String>();

    public static void main(String[] args) {

        ExecutorService exService = Executors.newFixedThreadPool(2);
        Producer producer = new LinkedTransferQueueExample().new Producer();
        Consumer consumer = new LinkedTransferQueueExample().new Consumer();
        exService.execute(producer);
        exService.execute(consumer);
        exService.shutdown();
    }

    class Producer implements Runnable{



        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            for(int i=0;i<1000;i++)
                try {
                    System.out.println(threadName + " Producer is waiting to transfer...");
                    lnkTransQueue.tryTransfer("A" + i);
                    System.out.println(threadName + " producer transfered element: A" + i);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            inprocess = false;
        }
    }
    class Consumer implements Runnable{


        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();

            while (inprocess){
                try {
                    System.out.println(threadName + " Consumer is waiting to take element...");
                    Thread.sleep(3000);
                    if (!inprocess) {
                        break;
                    }
                    String s= lnkTransQueue.take();
                    System.out.println(threadName + " Consumer received Element: "+s);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
