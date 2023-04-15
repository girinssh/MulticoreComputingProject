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
        long startTime= System.currentTimeMillis();
        dynamic_manager_thread dmthread = new dynamic_manager_thread(numOfThreads, NUM_END);
        dmthread.start();
        try {
            dmthread.join();
        } catch (InterruptedException e) {
//                throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("Program Execution Time: " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END) + " prime# counter=" + dmthread.getCounter());
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
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
            }
        }
    }
    private int counter = 0;
    public int getCounter(){
        return counter;
    }
    private synchronized void count(){
        counter++;
    }
    class dynamic_thread extends Thread{

        dynamic_thread(){}

        public void run(){
            while(idx < NUM_END){
                int s = getNext();
                for(int i = s; i < s+10; i++){
                    if(isPrime(i)){
                        count();
                    }
                }
            }
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
