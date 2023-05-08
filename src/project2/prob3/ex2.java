package project2.prob3;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ex2 {
    private static final int NUM_VISITOR = 3;
    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();

        Thread[] visitorThreads = new Thread[NUM_VISITOR];
        for(int i = 0; i < NUM_VISITOR; i++){
            visitorThreads[i] = new Thread(()->{
                for(int j = 0; j < 10; j++){
                    lock.readLock().lock();
                    System.out.println(Thread.currentThread().getName() + " comes into the museum. (" + (j+1) + "/10)");
                    try {
                        Thread.sleep((int)(Math.random()*1000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }finally{
                        System.out.println(Thread.currentThread().getName() + " leaves the museum.");
                        lock.readLock().unlock();
                    }
                }
            });
            visitorThreads[i].start();
        }
        Thread museumThread = new Thread(()->{
            for(int i = 0; i < 3; i++){
                try {
                    Thread.sleep((int)(Math.random()*3000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lock.writeLock().lock();
                System.out.println("Repairs inside the museum");
                try {
                    Thread.sleep((int)(Math.random()*1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    System.out.println("Repair End");
                    lock.writeLock().unlock();
                }
            }
        });
        museumThread.start();

        try {
            for(int i = 0; i < NUM_VISITOR; i++){
                visitorThreads[i].join();
            }
            museumThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
