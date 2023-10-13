package CAS;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * CAS全程为Compare And Swap，即比较与交换。这是CAS机制的两大核心。
 */
public class CompareAndSwap {
    public static void main(String[] args) throws InterruptedException {
        // 多线程不安全的实例
        UnsafeOrder unsafeOrder = new UnsafeOrder();        // 不安全的订单
        for (int i = 0; i < 10; i++) {                      // 创建10个线程清理库存
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {    // 每个线程负责1000个库存的清理
                    unsafeOrder.order();
                }
            }).start();
        }
        Thread.sleep(3000);     // 休眠确保任务执行完成
        unsafeOrder.checkCnt();
        // CAS线程安全
        CASSafeOrder safeOrder = new CASSafeOrder();
        for (int i = 0; i < 10; i++) {                      // 创建10个线程清理库存
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {    // 每个线程负责1000个库存的清理
                    safeOrder.order();
                }
            }).start();
        }
        Thread.sleep(3000);     // 休眠确保任务执行完成
        safeOrder.checkCnt();
        // synchronized线程安全
        SynSafeOrder synSafeOrder = new SynSafeOrder();
        for (int i = 0; i < 10; i++) {                      // 创建10个线程清理库存
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {    // 每个线程负责1000个库存的清理
                    synSafeOrder.synOrder();
                }
            }).start();
        }
        Thread.sleep(3000);     // 休眠确保任务执行完成
        synSafeOrder.checkCnt();
    }
}

class UnsafeOrder {
    private int unsafeCnt = 10000;

    public void order() {
        unsafeCnt--;
    }

    public void checkCnt() {
        System.out.println("unsafeCnt: " + unsafeCnt);
    }
}

class CASSafeOrder {
    private AtomicInteger safeCnt = new AtomicInteger(10000);

    public void order() {
        safeCnt.decrementAndGet();
    }

    public void checkCnt() {
        System.out.println("SafeCnt:  " + safeCnt);
    }
}

class SynSafeOrder {
    private int unsafeCnt = 10000;

    public void synOrder() {
        synchronized (this) {
            unsafeCnt--;
        }
    }

    public void checkCnt() {
        System.out.println("SynSafeCnt: " + unsafeCnt);
    }
}
