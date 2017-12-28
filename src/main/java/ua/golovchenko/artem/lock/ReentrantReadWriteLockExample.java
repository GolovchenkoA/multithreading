package ua.golovchenko.artem.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
    ReadWriteLock interface

    ReadWriteLock is implemented by the ReentrantReadWriteLock class

    Situations arise where data structures are read more often than they’re modified.
    ReadWriteLock maintains a pair of locks: one lock for read-only operations and one
    lock for write operations


    ReentrantReadWriteLock description

    Read: A thread that tries to acquire a fair read lock (non-reentrantly) will block when the write lock
    is held or when there’s a waiting writer thread. The thread will not acquire the read lock until
    after the oldest currently waiting writer thread has acquired and released the write lock. If a
    waiting writer abandons its wait, leaving one or more reader threads as the longest waiters
    in the queue with the write lock free, those readers will be assigned the read lock.

    Write: A thread that tries to acquire a fair write lock (non-reentrantly) will block unless both the
    read lock and write lock are free (which implies no waiting threads). (The nonblocking
    tryLock() methods don’t honor this fair setting and will immediately acquire the lock if
    possible, regardless of waiting threads.)

 */


public class ReentrantReadWriteLockExample {

    private static final int ATTEMPS_TO_READ_COUNT = 7;
    private static final Map<String, String> dictionary = new HashMap<String, String>();
    private static final String[] words = {"hypocalcemia", "prolixity", "assiduous", "indefatigable","castellan" };
    private static final String[] definitions =
            {
                    "a deficiency of calcium in the blood",
                    "unduly prolonged or drawn out",
                    "showing great care, attention, and effort",
                    "able to work or continue for a lengthy time without tiring",
                    "the govenor or warden of a castle or fort"
            };


    public static void main(String[] args) {

        ReadWriteLock rwl = new ReentrantReadWriteLock(true);
        final Lock rlock = rwl.readLock();
        final Lock wlock = rwl.writeLock();

        Runnable writer = () ->
        {
            for (int i = 0; i < words.length; i++) {
                wlock.lock();
                try {
                    dictionary.put(words[i],
                            definitions[i]);
                    System.out.println("writer storing " +
                            words[i] + " entry");
                } finally {
                    wlock.unlock();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    System.err.println("writer " +
                            "interrupted");
                }
            }
        };
        ExecutorService readExecutorService = Executors.newFixedThreadPool(1);
        readExecutorService.submit(writer);
        readExecutorService.shutdown();

        Runnable reader = () ->
        {
            //while (true) {
            for(int a = 0; a < ATTEMPS_TO_READ_COUNT; a++){
                rlock.lock();
                try {
                    int i = (int) (Math.random() *
                            words.length);
                    System.out.println("reader accessing " +
                            words[i] + ": " +
                            dictionary.get(words[i])
                            + " entry");


                } finally {
                    rlock.unlock();
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService writeExecutorService = Executors.newFixedThreadPool(1);
        writeExecutorService.submit(reader);

        writeExecutorService.shutdown();
    }
}
