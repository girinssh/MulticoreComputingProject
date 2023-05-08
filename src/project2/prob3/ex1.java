package project2.prob3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;


class CustomerThread extends Thread{
    private final int remainOrderCount;
    private final BlockingQueue<String> orderQueue;

    CustomerThread(int orderCount, BlockingQueue<String> orderQueue){
        this.remainOrderCount = orderCount;
        this.orderQueue = orderQueue;
    }
    @Override
    public void run() {
        for(int i = 0; i < remainOrderCount; i++){
            String order = this.getName() + " 주문 #" + i;
            try {
                orderQueue.put(order);
                System.out.println("손님: " + order + "을(를) 주문했습니다.");
                sleep((int)(Math.random() * 1000)); // 일부러 주문 간격을 둠
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
class BaristaThread extends Thread {
    private final BlockingQueue<String> orderQueue;

    BaristaThread(BlockingQueue<String> orderQueue){
        this.orderQueue = orderQueue;
    }
    @Override
    public void run() {
         do{
            try {
                String order = orderQueue.take();
                System.out.println("바리스타: " + order + "를 제작 중입니다.");
                sleep((int)(Math.random() * 2000)); // 제작 시간
                System.out.println("바리스타: " + order + "를 제작했습니다.");

                // 주문이 없다면 손님을 기다림
                if(orderQueue.isEmpty()){
                    sleep(1000);
                }

            } catch (InterruptedException ignore) {
//                throw new RuntimeException(e);
            }
        }while (!orderQueue.isEmpty());
    }
}
public class ex1 {
    private static final int QUEUE_CAPACITY = 5;
    private static final int CUSTOMER_NUM = 10;
    public static void main(String[] args) {
        BlockingQueue<String> orderQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        CustomerThread[] customerThreads = new CustomerThread[CUSTOMER_NUM];
        BaristaThread baristaThread = new BaristaThread(orderQueue);
        for (int i = 0; i < CUSTOMER_NUM; i++) {
            customerThreads[i] = new CustomerThread((int)(Math.random() * 5), orderQueue);
            customerThreads[i].start();
        }
        baristaThread.start();

        try {
            for (int i = 0; i < CUSTOMER_NUM; i++) {
                customerThreads[i].join();
            }

            baristaThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}