package project2.prob3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class RacingRunnable implements Runnable{
    private static int point = 1;
    @Override
    public void run() {
        System.out.println("\nNow " + point++ + " Attempt");
    }
}

class UserThread extends Thread{
    private final CyclicBarrier barrier;

    UserThread(CyclicBarrier barrier){
        this.barrier = barrier;
    }

    @Override
    public void run() {
        for(int j = 0; j < 3; j++){
            try {
                sleep((int)(Math.random()*1000));
                System.out.print(Thread.currentThread().getName() + "\t");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
public class ex4 {
    private static final int NUM_USERS = 3;

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(NUM_USERS, new RacingRunnable()); // +1은 바리스타 스레드를 위한 추가

        Thread[] userThreads = new Thread[NUM_USERS];

        for(int i = 0; i < NUM_USERS; i++) {
            userThreads[i] = new UserThread(barrier);
            userThreads[i].start();
        }
    }
}
