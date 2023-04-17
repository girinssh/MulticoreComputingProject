package project1;

public class pc_static_block {
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

    public static void tryTest(int NUM_THREADS){
        int NUM_TRY = 1;
        long sumTimeDiff = 0;
        long sumCount = 0;
        // 20번 반복
        for(int j = 0; j < NUM_TRY; j++){
            int count = 0;
            int i;

            long startTime= System.currentTimeMillis();
            pc_block_thread[] threads = new pc_block_thread[NUM_THREADS];
            {
                int r = NUM_END % NUM_THREADS;
                int s = 0;
                int d = NUM_END / NUM_THREADS;
                int e = d - 1 + (r > 0 ? 1 : 0);
    //                System.out.println("property: " + r + "\t" + d );
                for(i = 0; i < NUM_THREADS; i++) {
    //                    System.out.println("" + s + '\t' + e);

                    threads[i] = new pc_block_thread(s, e);
                    threads[i].start();
                    s = e+1;
                    e = s + d - 1;
                    e += (r > 0 ? 1 : 0);
                    r-=1;
                }
            }

            for(i = 0; i < NUM_THREADS; i++){
                try {
                    threads[i].join();
                    count += threads[i].getCount();
                    System.out.println("Thread_" + NUM_THREADS + "_" + i + "_" + j + "_ExecTime: " + threads[i].getTimeDiff());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            long endTime = System.currentTimeMillis();
            sumTimeDiff += endTime - startTime;
            sumCount += count;
        }
        System.out.println("# NUM_THREAD: " + NUM_THREADS);
        System.out.println("Program Execution Time: " + sumTimeDiff/NUM_TRY + "ms"); // Mean Time Diff
        System.out.println("1..." + (NUM_END-1) + " prime# counter=" + sumCount/NUM_TRY);
    }

    static class pc_block_thread extends Thread{
        private final int start, end;
        private int count;

        int getCount(){
            return count;
        }
        private long startTime = 0;
        private long endTime = 0;
        private long getTimeDiff(){
            return endTime - startTime;
        }
        pc_block_thread(int start, int end){
            super();
            this.start = start;
            this.end = end;
            count = 0;
        }

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            for(int i = start; i <= end; i++){
                if(isPrime(i)) {
                    count++;
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