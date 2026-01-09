package com.javaevolution.jdk8.concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

/**
 * JDK 8 并发增强演示
 * JEP 155: Concurrency Updates
 */
public class ConcurrencyEnhancements {

    /**
     * StampedLock: 读写锁的改进版
     * 比 ReentrantReadWriteLock 性能更好
     */
    public static class StampedLockDemo {
        private double x, y;
        private final StampedLock sl = new StampedLock();
        
        // 写锁 (独占锁)
        void move(double deltaX, double deltaY) {
            long stamp = sl.writeLock();
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                sl.unlockWrite(stamp);
            }
        }
        
        // 乐观读锁
        double distanceFromOrigin() {
            long stamp = sl.tryOptimisticRead(); // 乐观读
            double currentX = x, currentY = y;
            
            if (!sl.validate(stamp)) { // 验证是否被写入
                stamp = sl.readLock(); // 升级为悲观读锁
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    sl.unlockRead(stamp);
                }
            }
            
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }
        
        // 悲观读锁
        double getX() {
            long stamp = sl.readLock();
            try {
                return x;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        
        // 锁升级: 读锁 -> 写锁
        void moveIfAtOrigin(double newX, double newY) {
            long stamp = sl.readLock();
            try {
                while (x == 0.0 && y == 0.0) {
                    long ws = sl.tryConvertToWriteLock(stamp);
                    if (ws != 0L) { // 升级成功
                        stamp = ws;
                        x = newX;
                        y = newY;
                        break;
                    } else { // 升级失败,释放读锁,获取写锁
                        sl.unlockRead(stamp);
                        stamp = sl.writeLock();
                    }
                }
            } finally {
                sl.unlock(stamp);
            }
        }
        
        public void demo() {
            StampedLockDemo point = new StampedLockDemo();
            
            // 写操作
            point.move(10, 20);
            
            // 读操作
            double distance = point.distanceFromOrigin();
            System.out.println("Distance: " + distance);
        }
    }

    /**
     * LongAdder & LongAccumulator: 高性能计数器
     * 比 AtomicLong 在高并发下性能更好
     */
    public static class LongAdderDemo {
        
        public void longAdderDemo() throws InterruptedException {
            LongAdder adder = new LongAdder();
            
            // 高并发累加
            int threadCount = 100;
            CountDownLatch latch = new CountDownLatch(threadCount);
            
            for (int i = 0; i < threadCount; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 10000; j++) {
                        adder.increment(); // 等价于 add(1)
                    }
                    latch.countDown();
                }).start();
            }
            
            latch.await();
            System.out.println("LongAdder result: " + adder.sum()); // 1000000
        }
        
        public void longAccumulatorDemo() {
            // LongAccumulator: 更通用的累加器
            LongAccumulator accumulator = new LongAccumulator(Long::max, Long.MIN_VALUE);
            
            accumulator.accumulate(100);
            accumulator.accumulate(50);
            accumulator.accumulate(200);
            accumulator.accumulate(150);
            
            System.out.println("Max value: " + accumulator.get()); // 200
        }
        
        // AtomicLong vs LongAdder 性能对比
        public void performanceComparison() throws InterruptedException {
            int threadCount = 100;
            int iterations = 100000;
            
            // AtomicLong
            AtomicLong atomicLong = new AtomicLong();
            long start1 = System.currentTimeMillis();
            CountDownLatch latch1 = new CountDownLatch(threadCount);
            
            for (int i = 0; i < threadCount; i++) {
                new Thread(() -> {
                    for (int j = 0; j < iterations; j++) {
                        atomicLong.incrementAndGet();
                    }
                    latch1.countDown();
                }).start();
            }
            latch1.await();
            long end1 = System.currentTimeMillis();
            
            // LongAdder
            LongAdder longAdder = new LongAdder();
            long start2 = System.currentTimeMillis();
            CountDownLatch latch2 = new CountDownLatch(threadCount);
            
            for (int i = 0; i < threadCount; i++) {
                new Thread(() -> {
                    for (int j = 0; j < iterations; j++) {
                        longAdder.increment();
                    }
                    latch2.countDown();
                }).start();
            }
            latch2.await();
            long end2 = System.currentTimeMillis();
            
            System.out.println("AtomicLong time: " + (end1 - start1) + "ms");
            System.out.println("LongAdder time: " + (end2 - start2) + "ms");
        }
    }

    /**
     * ConcurrentHashMap 增强
     */
    public static class ConcurrentHashMapEnhancements {
        
        public void newMethods() {
            ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
            
            // forEach
            map.put("A", 1);
            map.put("B", 2);
            map.forEach((k, v) -> System.out.println(k + " = " + v));
            
            // search - 找到第一个匹配的
            String result = map.search(1, (k, v) -> v > 1 ? k : null);
            System.out.println("First key with value > 1: " + result); // B
            
            // reduce - 归约操作
            Integer sum = map.reduce(1, 
                (k, v) -> v,                    // transformer
                (v1, v2) -> v1 + v2);           // reducer
            System.out.println("Sum: " + sum); // 3
            
            // compute - 原子计算
            map.compute("A", (k, v) -> v == null ? 1 : v + 1);
            System.out.println("A = " + map.get("A")); // 2
            
            // computeIfAbsent - key 不存在时计算
            map.computeIfAbsent("C", k -> 10);
            System.out.println("C = " + map.get("C")); // 10
            
            // computeIfPresent - key 存在时计算
            map.computeIfPresent("A", (k, v) -> v * 2);
            System.out.println("A = " + map.get("A")); // 4
            
            // merge - 合并值
            map.merge("A", 5, Integer::sum);
            System.out.println("A = " + map.get("A")); // 9
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Concurrency Enhancements ===");
        
        StampedLockDemo sld = new StampedLockDemo();
        sld.demo();
        
        LongAdderDemo lad = new LongAdderDemo();
        lad.longAdderDemo();
        lad.longAccumulatorDemo();
        
        ConcurrentHashMapEnhancements che = new ConcurrentHashMapEnhancements();
        che.newMethods();
    }
}
