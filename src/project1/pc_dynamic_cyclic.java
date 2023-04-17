package project1;

public class pc_dynamic_cyclic {

    private static int NUM_THREADS = 32;
    private static int NUM_END = 200000;
    private static int counter = 0;
    public static void main(String[] args){
        if(args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
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

    public static void tryTest(int numOfThreads){
        int NUM_TRY = 1;
        long sumTimeDiff = 0;
        long sumCount = 0;
        for(int j = 0; j < NUM_TRY; j++){
            long startTime= System.currentTimeMillis();
            dynamic_manager_thread dmthread = new dynamic_manager_thread(numOfThreads, NUM_END);
            dmthread.start();
            try {
                dmthread.join();
            } catch (InterruptedException e) {
    //                throw new RuntimeException(e);
            }
            long endTime = System.currentTimeMillis();
            sumTimeDiff += endTime - startTime;
            sumCount += dmthread.count;
        }
        System.out.println("# NUM_THREAD: " + numOfThreads);
        System.out.println("Program Execution Time: " + sumTimeDiff/NUM_TRY + "ms");
        System.out.println("1..." + (NUM_END-1) + " prime# counter=" + sumCount/NUM_TRY);
    }
}

class dynamic_manager_thread extends Thread{

    private final int NUM_END ;
    private final int NUM_THREADS;
    dynamic_manager_thread(int nths, int ne){
        NUM_THREADS = nths;
        NUM_END = ne;
    }

    private int idx = 0;
    int count = 0;
    protected synchronized int getNext(){
        int ret = idx;
        if(idx < NUM_END){
            idx+=10;
        }
        return ret;
    }
    @Override
    public void run() {
        super.run();
        dynamic_thread[] dthreads = new dynamic_thread[NUM_THREADS];
        for(int i = 0; i < NUM_THREADS; i++){
            dthreads[i] = new dynamic_thread();
            dthreads[i].start();
        }
        for(int i = 0; i < NUM_THREADS; i++){
            try {
                dthreads[i].join();
                count += dthreads[i].count;
                System.out.println("Thread_" + NUM_THREADS + "_" + i + "_ExecTime: " + dthreads[i].getTimeDiff());
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
            }
        }
    }
    class dynamic_thread extends Thread{

        private long startTime = 0;
        private long endTime = 0;
        private long getTimeDiff(){
            return endTime - startTime;
        }
        dynamic_thread(){}
        int count=0;
        public void run(){
            startTime = System.currentTimeMillis();
            while(idx < NUM_END){
                int s = getNext();
                for(int i = s; i < s+10; i++){
                    if(isPrime(i)){
                        this.count++;
                    }
                }
            }
            endTime = System.currentTimeMillis();
        }
        private boolean isPrime(int x) {
            int i;
            if (x <= 1) return false;
            for (i = 2; i < x; i++) {
                if (x % i == 0) return false;
            }
            return true;
        }


    }
}
