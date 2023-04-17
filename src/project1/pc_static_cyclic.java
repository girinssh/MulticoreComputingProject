package project1;

import java.util.ArrayList;

public class pc_static_cyclic {

    private static int NUM_END = 200000;
//    private static int NUM_THREADS = 32;
    public static void main(String[] args){
        if(args.length == 2) {
            int NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
            tryTest(NUM_THREADS);
        }
        else{
            int[] nths = new int[]{1, 2, 4, 6, 8, 10, 12, 14, 16, 32};
            for(int nth : nths){
                tryTest(nth);
            }
        }

    }
    static void tryTest(int NUM_THREADS){

        int NUM_TRY = 1;
        long sumTimeDiff = 0;
        long sumCount = 0;
        for(int j = 0; j < NUM_TRY; j++){
            int count = 0;
            int i;
            long startTime= System.currentTimeMillis();
            pc_cyclic_thread[] threads = new pc_cyclic_thread[NUM_THREADS];
            for(i = 0; i < NUM_THREADS; i++){
                threads[i] = new pc_cyclic_thread(i * 10, NUM_THREADS * 10);
                threads[i].start();
            }
            for(i = 0; i < NUM_THREADS; i++){
                try {
                    threads[i].join();
                    count += threads[i].count;
                    System.out.println("Thread_" + NUM_THREADS + "_" + i + "_ExecTime: " + threads[i].getTimeDiff());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            long endTime = System.currentTimeMillis();
            sumTimeDiff += endTime - startTime;
            sumCount += count;
        }
        System.out.println("# NUM_THREAD: " + NUM_THREADS);
        System.out.println("Program Execution Time: " + sumTimeDiff/NUM_TRY + "ms");
        System.out.println("1..." + (NUM_END-1) + " prime# counter=" + sumCount/NUM_TRY);
    }

    static class pc_cyclic_thread extends Thread{
        int id;
        int bias;
        int count = 0;
        private long startTime = 0;
        private long endTime = 0;
        private long getTimeDiff(){
            return endTime - startTime;
        }

        pc_cyclic_thread(int _id, int _bias){
            super();
            id = _id;
            bias = _bias;
//            System.out.println("" + start + "\t" + pairs.get(pairs.size()-1)[1]);
        }

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            for(int j = id; j < NUM_END; j += bias){
                for(int i = j; i < j+10; i++){
                    if(isPrime(i)) {
                        count++;
                    }
                }
            }
            endTime = System.currentTimeMillis();
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
