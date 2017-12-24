package ua.golovchenko.artem.synchronizers;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangerExample {

    private static Exchanger exchanger = new Exchanger();

    public static void main(String[] args) {

        ExchangerRunnable exchangerRunnable1 = new ExchangerRunnable(exchanger, "A");
        ExchangerRunnable exchangerRunnable2 = new ExchangerRunnable(exchanger, "B");
        ExchangerRunnable exchangerRunnable3 = new ExchangerRunnable(exchanger, "C");
        ExchangerRunnable exchangerRunnable4 = new ExchangerRunnable(exchanger, "D");

        new Thread(exchangerRunnable1).start();
        new Thread(exchangerRunnable2).start();

        // Working only if the thread has pair
        new Thread(exchangerRunnable3).start();
        //new Thread(exchangerRunnable4).start();
    }
}

class ExchangerRunnable implements Runnable{

    Exchanger exchanger = null;
    Object    object    = null;

    public ExchangerRunnable(Exchanger exchanger, Object object) {
        this.exchanger = exchanger;
        this.object = object;
    }

    public void run() {
        try {
            Object previous = this.object;

            this.object = this.exchanger.exchange(this.object, 2, TimeUnit.SECONDS);
            //Thread.sleep((long)(Math.random() * 1000));
            System.out.println( Thread.currentThread().getName() +
                                " exchanged " + previous + " for " + this.object
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("Timeout Exception");
            e.printStackTrace();
        }
    }
}
