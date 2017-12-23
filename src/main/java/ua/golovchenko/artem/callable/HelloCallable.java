package ua.golovchenko.artem.callable;

import java.util.concurrent.*;

public class HelloCallable {
    private static ExecutorService es = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        Future<Integer> future = es.submit(new HelloCallable1());

        try {
            System.out.println("Is finished:" + future.isDone());
            System.out.println("Result:" + future.get());
            System.out.println("Is finished:" + future.isDone());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        es.shutdown();
    }

    private static class HelloCallable1 implements  Callable<Integer>{
        public Integer call() throws Exception {
            Thread.sleep(System.currentTimeMillis() % 3 * 1000);
            return  2 + 2;
        }
    }

}
