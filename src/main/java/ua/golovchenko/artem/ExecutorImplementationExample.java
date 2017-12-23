package ua.golovchenko.artem;

import java.util.concurrent.Executor;

public class ExecutorImplementationExample {

    private static DirectExecutor directExecutor = new DirectExecutor();

    public static void main(String[] args) {

        directExecutor.execute(new Runnable() {
            public void run() {
                System.out.println("Own implementation of Executor");
            }
        });

    }

    private static class DirectExecutor implements Executor {
        public void execute(Runnable r) {
            r.run();
        }
    }
}
