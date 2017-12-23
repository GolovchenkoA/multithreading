package ua.golovchenko.artem;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
    source: Jeff Friesen - Java Threads and the Concurrency Utilities (The Expert's Voice in Java) - 2015
    Listing 5-1. Calculating Euler’s Number
 */
public class CalculatingEulersNumber
{
    final static int LASTITER = 17;
    private static ExecutorService executor = Executors.newFixedThreadPool(2);
    private static Callable<BigDecimal>  callable;

    public static void main(String[] args)
    {

        callable = new Callable<BigDecimal>(){

            public BigDecimal call() {
                MathContext mc = new MathContext(100, RoundingMode.HALF_UP);
                BigDecimal result = BigDecimal.ZERO;
                for (int i = 0; i <= LASTITER; i++) {
                    BigDecimal factorial = factorial(new BigDecimal(i));
                    BigDecimal res = BigDecimal.ONE.divide(factorial, mc);
                    result = result.add(res);
                }

                return result;
            }

            public BigDecimal factorial(BigDecimal n) {
                if (n.equals(BigDecimal.ZERO))
                    return BigDecimal.ONE;
                else
                    return n.multiply(factorial(n.
                            subtract(BigDecimal.ONE)));
            }
        };


        Future<BigDecimal> taskFuture = executor.submit(callable);

        try {
            while (!taskFuture.isDone())
                System.out.println("waiting task");
            System.out.println(taskFuture.get());


        } catch(ExecutionException ee){
            System.err.println("task threw an exception");
            System.err.println(ee);
        }
        catch(InterruptedException ie){
            System.err.println("interrupted while waiting");
        }

        executor.shutdownNow();
    }
}
