package ua.golovchenko.artem;

import java.util.concurrent.*;

public class ScheduledExecutorServiceExample {

    private static ScheduledExecutorService scheduledExecutorService =  Executors.newScheduledThreadPool(5);
    public static void main(String[] args) throws ExecutionException, InterruptedException {


        ScheduledFuture scheduledFuture =
                scheduledExecutorService.schedule(new Callable() {
                                                      public Object call() throws Exception {
                                                          System.out.println("Executed!");
                                                          return "Called!";
                                                      }
                                                  },
                        2,
                        TimeUnit.SECONDS);

        System.out.println(scheduledFuture.get());

        scheduledExecutorService.shutdown();
    }

}
