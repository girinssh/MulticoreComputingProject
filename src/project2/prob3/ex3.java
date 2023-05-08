package project2.prob3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;



public class ex3 {
    private static final int MAX_COUNTER = 5;
    public static void main(String[] args) {
        AtomicInteger atomic = new AtomicInteger(0);

        System.out.println("Atomicity Test");
        Runnable runnable = () -> {
            for (int i = 0; i < 3; i++) {
                int a = atomic.get();
                atomic.set(a + 1);
            }
        };
        Thread th1 = new Thread(runnable);
        Thread th2 = new Thread(runnable);

        th1.start();
        th2.start();
        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("0 + 6 = " + atomic.get());

        List<Integer> list = new ArrayList<>();

        Runnable run1 = () -> {
            atomic.set(0);
            for (int i = 0; i < 3; i++) {
                list.add(atomic.getAndAdd(1));
            }
            list.add(atomic.get());
        };
        Runnable run2 = () -> {
            atomic.set(0);
            for (int i = 0; i < 3; i++) {
                list.add(atomic.addAndGet(1));
            }
            list.add(atomic.get());
        };

        System.out.println("GET AND ADD test");
        th1 = new Thread(run1);
        th1.start();
        try {
            th1.join();
            for(int val : list){
                System.out.print(val + "\t");
            }
            System.out.println();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ADD AND GET test");
        list.clear();
        th2 = new Thread(run2);
        th2.start();
        try {
            th2.join();
            for(int val : list){
                System.out.print(val + "\t");
            }
            System.out.println();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
