import java.lang.Thread;
import java.lang.Runnable;
import java.util.concurrent.ThreadPoolExecutor;

class YieldThread extends Thread {
    public void run(){
        for(int count = 0; count < 4; count++){
            System.out.println(count + "From: " + getName());
            Thread.yield();
        }
    }
}

class MyThread1 extends Thread{
    public void run(){
        System.out.println(getName() + " is running....");
        for(int i = 0; i < 4; i++){
            try{
                sleep(500);
            }
            catch(InterruptedException e){
                System.out.println("Hellothere, from"+getName());
            }
        }
    }
}
class MyThread2 extends Thread{
    private Thread wait4me;
    MyThread2(Thread target){
        super();
        wait4me = target;
    }
    public void run(){
        System.out.println(getName() + " is waiting for " + wait4me.getName() + "...");
        try{
            wait4me.join();
        }catch(InterruptedException e){}
        System.out.println(wait4me.getName() + "has finished...");

        for(int i = 0; i < 4; i++){
            try{
                sleep(500);
            }catch(InterruptedException e){}
        }
    }
}

public class Main {
    public static void main(String[] args){
        YieldThread first = new YieldThread();
        YieldThread second = new YieldThread();
        first.start(); second.start();
        System.out.println("End");
    }
}
