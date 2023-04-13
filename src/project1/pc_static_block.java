package project1;

public class pc_static_block {
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

            pc_block_thread[] threads = new pc_block_thread[NUM_THREADS];
            {
                int r = NUM_END % NUM_THREADS;
                int s = 0;
                int d = NUM_END / NUM_THREADS;
                int e = d - 1;

//                System.out.println("property: " + r + "\t" + d );

                for(i = 0; i < NUM_THREADS; i++) {
                    e += (r > 0 ? 1 : 0);
//                    System.out.println("" + s + '\t' + e);

                    threads[i] = new pc_block_thread(s, e);
                    s = e+1;
                    e = s + d - 1;
                    r-=1;
                }
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

    static class pc_block_thread extends Thread{
        private int start;
        private int end;

        pc_block_thread(int start, int end){
            super();
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for(int i = start; i <= end; i++){
                if(isPrime(i)) {
                    count();
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