package project1;

import java.util.ArrayList;

public class pc_static_cyclic {

    private static int NUM_END = 200000;
    private static int NUM_THREADS = 32;
    private static int counter = 0;
    public static void main(String[] args){
        if(args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }

        for(NUM_THREADS = 1; NUM_THREADS <= 32; NUM_THREADS++){
            counter = 0;
            int i;
            pc_cyclic_thread[] threads = new pc_cyclic_thread[NUM_THREADS];

            for(i = 0; i < NUM_THREADS; i++){
                threads[i] = new pc_cyclic_thread(i * 10, NUM_THREADS * 10);
            }
            long startTime= System.currentTimeMillis();
            for(i = 0; i < NUM_THREADS; i++){
                threads[i].start();
            }
            for(i = 0; i < NUM_THREADS; i++){
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;
            System.out.println("# NUM_THREAD: " + NUM_THREADS);
            System.out.println("Program Execution Time: " + timeDiff + "ms");
            System.out.println("1..." + (NUM_END-1) + " prime# counter=" + counter);
        }
    }

    static synchronized void count(){
        counter++;
    }

    static class pc_cyclic_thread extends Thread{
        private ArrayList<Integer[]> pairs;

        pc_cyclic_thread(int start, int bias){
            super();
            pairs = new ArrayList<>();
            for(int i = start; i < NUM_END; i+=bias){
                pairs.add(new Integer[]{i, i+10-1});
            }
//            System.out.println("" + start + "\t" + pairs.get(pairs.size()-1)[1]);
        }

        @Override
        public void run() {
            for(Integer[] pair : pairs){
                for(int i = pair[0]; i <= pair[1]; i++){
                    if(isPrime(i)) {
                        count();
                    }
                }
            }
        }
        private boolean isPrime(int x){
            int i;
            if(x <= 1) return false;
            for(i = 2; i < x; i++){
                if(x % i == 0) return false;
            }
            return true;
        }
    }
}
