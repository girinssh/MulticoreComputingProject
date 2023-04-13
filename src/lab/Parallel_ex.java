package lab;

public class Parallel_ex {
    private static int NUM_END = 10000;
    private static int NUM_THREAD=4;
    public static void main(String[] args){
        if(args.length == 2){
            NUM_THREAD = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        int[] int_arr = new int[NUM_END];
        int i, s;
        for(i = 0; i <NUM_END; i++){
            int_arr[i] = i+1;
        }

        s = sum(int_arr);
        System.out.println("sum=" + s);
    }
    static int sum(int[] arr){
//        int ans = 0;
//        SumThread[] threads = new SumThread[NUM_THREAD];
//        int d =NUM_END/NUM_THREAD;
//        int r = NUM_END % NUM_THREAD;
//
//        int s = 0;
//        int e = s + d - 1 + (r>0 ? 1 : 0);
//
//        for(int i = 0; i < NUM_THREAD; i++){
//            threads[i] = new SumThread(arr, s, e);
//            s = e + 1;
//            e = s + d - 1 + + (--r>0 ? 1 : 0);
//            threads[i].start();
//        }
            // This is a Naive Method.
//        for(int i = 0; i < NUM_THREAD; i++){
//            //
//            try{
//                // 스레드의 개수가 늘어나면 여길 덧셈하는데도 시간이 오래 걸리게 된다.
//                threads[i].join();
//                ans += threads[i].ans;
//            } catch (InterruptedException ex) {
//    //            throw new RuntimeException(ex);
//            }
//        }
        // This uses divide and conquer method
        SumThread thread = new SumThread(arr, 0, arr.length);
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        
        thread.run();

        return thread.ans;
    }
}

class SumThread extends Thread{
    private static final int SEQUENCIAL_CUTOFF = 500;
    int lo, hi;
    int [] arr;
    int ans = 0;
    SumThread(int[] a, int l, int h){
        lo = l; hi = h; arr = a;
    }
    public void run(){
        if(hi - lo < SEQUENCIAL_CUTOFF){
            for(int i = lo; i < hi; i++){
                ans += arr[i];
            }
        }else{
            SumThread left = new SumThread(arr, lo, (hi+lo)/2);
            SumThread right = new SumThread(arr, (hi+lo)/2, hi );
            left.start();
            right.start();
            try {
                left.join();
                right.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ans = left.ans + right.ans;
        }
    }
}
