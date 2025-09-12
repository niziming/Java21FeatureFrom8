package com.demo.concurrent;

import com.demo.annotations.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

/**
 * 并发编程增强演示
 * 展示Java 8到Java 21并发相关的新特性，重点展示Java 21的虚拟线程
 */
public class ConcurrencyDemo {

    /**
     * CompletableFuture基础演示 (Java 8特性)
     * 异步编程和链式操作
     */
    @Java8Feature(value = "CompletableFuture", desc = "异步编程框架，支持链式操作和组合")
    public void demonstrateCompletableFuture() {
        System.out.println("=== CompletableFuture演示 ===");
        
        // 基础异步操作
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("异步任务1执行中，线程: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000); // 模拟耗时操作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "任务1完成";
        });
        
        // 链式操作
        CompletableFuture<String> future2 = future1.thenApply(result -> {
            System.out.println("处理任务1结果: " + result);
            return result + " -> 任务2完成";
        });
        
        // 异步链式操作
        CompletableFuture<String> future3 = future2.thenApplyAsync(result -> {
            System.out.println("异步处理任务2结果，线程: " + Thread.currentThread().getName());
            return result + " -> 任务3完成";
        });
        
        // 等待结果
        try {
            String finalResult = future3.get(3, TimeUnit.SECONDS);
            System.out.println("最终结果: " + finalResult);
        } catch (Exception e) {
            System.err.println("获取结果失败: " + e.getMessage());
        }
        
        // 组合多个CompletableFuture
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(800); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "数据A";
        });
        
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(600); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "数据B";
        });
        
        // 等待所有任务完成
        CompletableFuture<String> combined = task1.thenCombine(task2, 
            (dataA, dataB) -> "组合结果: " + dataA + " + " + dataB);
        
        try {
            System.out.println(combined.get());
        } catch (Exception e) {
            System.err.println("组合失败: " + e.getMessage());
        }
    }

    /**
     * CompletableFuture增强演示 (Java 9特性)
     * 超时和延迟执行等新特性
     */
    @Java9Feature(value = "CompletableFuture增强", desc = "新增超时处理、延迟执行等增强功能")
    public void demonstrateCompletableFutureEnhancements() {
        System.out.println("\n=== CompletableFuture增强演示 ===");
        
        // 带超时的异步操作
        CompletableFuture<String> timeoutFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000); // 模拟长时间操作
                return "操作完成";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "操作被中断";
            }
        }).orTimeout(1, TimeUnit.SECONDS) // 设置1秒超时
          .handle((result, throwable) -> {
              if (throwable != null) {
                  return "操作超时: " + throwable.getMessage();
              }
              return result;
          });
        
        try {
            System.out.println("超时测试结果: " + timeoutFuture.get());
        } catch (Exception e) {
            System.err.println("超时操作失败: " + e.getMessage());
        }
        
        // 延迟执行
        CompletableFuture<Void> delayedFuture = CompletableFuture
            .runAsync(() -> System.out.println("延迟500ms后执行"), 
                     CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS));
        
        // 默认值处理
        CompletableFuture<String> futureWithDefault = CompletableFuture
            .supplyAsync(() -> {
                if (Math.random() > 0.5) {
                    throw new RuntimeException("随机失败");
                }
                return "成功结果";
            })
            .completeOnTimeout("默认值", 100, TimeUnit.MILLISECONDS);
        
        try {
            System.out.println("默认值测试结果: " + futureWithDefault.get());
        } catch (Exception e) {
            System.err.println("默认值操作失败: " + e.getMessage());
        }
    }

    /**
     * 虚拟线程演示 (Java 21重要特性)
     * Project Loom的核心功能，轻量级线程
     */
    @Java21Feature(value = "虚拟线程", desc = "Project Loom的轻量级线程，可以创建数百万个线程")
    public void demonstrateVirtualThreads() {
        System.out.println("\n=== 虚拟线程演示 ===");
        
        // 创建虚拟线程
        Thread virtualThread = Thread.ofVirtual().name("虚拟线程-1").start(() -> {
            System.out.println("虚拟线程运行中: " + Thread.currentThread());
            try {
                Thread.sleep(1000); // 虚拟线程可以安全地阻塞
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("虚拟线程完成");
        });
        
        // 等待虚拟线程完成
        try {
            virtualThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 使用虚拟线程执行器
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            System.out.println("使用虚拟线程执行器处理大量任务...");
            
            Instant start = Instant.now();
            List<Future<String>> futures = new ArrayList<>();
            
            // 创建大量虚拟线程任务
            for (int i = 0; i < 10000; i++) {
                int taskId = i;
                Future<String> future = virtualExecutor.submit(() -> {
                    try {
                        Thread.sleep(10); // 短暂阻塞
                        return "任务" + taskId + "完成";
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "任务" + taskId + "被中断";
                    }
                });
                futures.add(future);
            }
            
            // 等待所有任务完成
            int completedTasks = 0;
            for (Future<String> future : futures) {
                try {
                    future.get();
                    completedTasks++;
                } catch (Exception e) {
                    System.err.println("任务执行失败: " + e.getMessage());
                }
            }
            
            Duration duration = Duration.between(start, Instant.now());
            System.out.println("完成 " + completedTasks + " 个虚拟线程任务，耗时: " + duration.toMillis() + "ms");
        }
        
        // 虚拟线程与传统线程对比
        demonstrateVirtualVsTraditionalThreads();
    }

    /**
     * 虚拟线程与传统线程性能对比
     */
    private void demonstrateVirtualVsTraditionalThreads() {
        System.out.println("\n--- 虚拟线程 vs 传统线程性能对比 ---");
        
        int taskCount = 1000;
        
        // 传统线程池
        Instant start = Instant.now();
        try (ExecutorService traditionalExecutor = Executors.newFixedThreadPool(100)) {
            List<Future<Void>> futures = new ArrayList<>();
            
            for (int i = 0; i < taskCount; i++) {
                Future<Void> future = traditionalExecutor.submit(() -> {
                    try {
                        Thread.sleep(50); // 模拟I/O操作
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                });
                futures.add(future);
            }
            
            // 等待所有任务完成
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    System.err.println("传统线程任务失败: " + e.getMessage());
                }
            }
        }
        Duration traditionalDuration = Duration.between(start, Instant.now());
        System.out.println("传统线程池完成 " + taskCount + " 个任务，耗时: " + traditionalDuration.toMillis() + "ms");
        
        // 虚拟线程
        start = Instant.now();
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Void>> futures = new ArrayList<>();
            
            for (int i = 0; i < taskCount; i++) {
                Future<Void> future = virtualExecutor.submit(() -> {
                    try {
                        Thread.sleep(50); // 模拟I/O操作
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                });
                futures.add(future);
            }
            
            // 等待所有任务完成
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    System.err.println("虚拟线程任务失败: " + e.getMessage());
                }
            }
        }
        Duration virtualDuration = Duration.between(start, Instant.now());
        System.out.println("虚拟线程完成 " + taskCount + " 个任务，耗时: " + virtualDuration.toMillis() + "ms");
        
        if (traditionalDuration.toMillis() > 0) {
            double improvement = (double) traditionalDuration.toMillis() / virtualDuration.toMillis();
            System.out.printf("虚拟线程性能提升: %.2f倍\n", improvement);
        }
    }

    /**
     * 结构化并发演示 (Java 21预览特性)
     * 管理相关任务的生命周期
     */
    @Java21Feature(value = "结构化并发", desc = "管理相关任务组的生命周期，提供更好的错误处理和资源管理")
    public void demonstrateStructuredConcurrency() {
        System.out.println("\n=== 结构化并发演示 (概念演示) ===");
        
        // 注意：结构化并发是Java 21的预览特性，需要特殊编译选项
        // 这里展示概念和预期的API使用方式
        
        System.out.println("结构化并发将提供以下特性:");
        System.out.println("1. 任务组生命周期管理");
        System.out.println("2. 自动错误传播");
        System.out.println("3. 资源自动清理");
        System.out.println("4. 更好的调试和监控支持");
        
        // 模拟结构化并发的概念
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            System.out.println("模拟结构化并发任务组...");
            
            List<Future<String>> tasks = new ArrayList<>();
            
            // 提交相关联的任务
            for (int i = 0; i < 3; i++) {
                int taskId = i;
                Future<String> task = executor.submit(() -> {
                    try {
                        Thread.sleep(100 * (taskId + 1));
                        if (taskId == 1 && Math.random() > 0.7) {
                            throw new RuntimeException("任务" + taskId + "失败");
                        }
                        return "任务" + taskId + "成功";
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "任务" + taskId + "被中断";
                    }
                });
                tasks.add(task);
            }
            
            // 等待所有任务完成或第一个失败
            boolean allSuccess = true;
            for (int i = 0; i < tasks.size(); i++) {
                try {
                    String result = tasks.get(i).get();
                    System.out.println("任务组结果: " + result);
                } catch (Exception e) {
                    System.err.println("任务组中的任务失败: " + e.getMessage());
                    allSuccess = false;
                    // 在真正的结构化并发中，会自动取消其他任务
                    break;
                }
            }
            
            if (!allSuccess) {
                System.out.println("任务组失败，取消其他任务...");
                for (Future<String> task : tasks) {
                    task.cancel(true);
                }
            }
        }
    }

    /**
     * ForkJoinPool增强演示 (Java 8特性)
     * 工作窃取算法和并行计算
     */
    @Java8Feature(value = "ForkJoinPool", desc = "基于工作窃取算法的并行计算框架")
    public void demonstrateForkJoinPool() {
        System.out.println("\n=== ForkJoinPool演示 ===");
        
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        
        try {
            // 计算斐波那契数列
            FibonacciTask fibTask = new FibonacciTask(35);
            long startTime = System.currentTimeMillis();
            Long result = forkJoinPool.invoke(fibTask);
            long endTime = System.currentTimeMillis();
            
            System.out.println("斐波那契(35) = " + result + ", 耗时: " + (endTime - startTime) + "ms");
            
            // 并行求和
            int[] array = new int[10_000_000];
            for (int i = 0; i < array.length; i++) {
                array[i] = i + 1;
            }
            
            SumTask sumTask = new SumTask(array, 0, array.length);
            startTime = System.currentTimeMillis();
            Long sum = forkJoinPool.invoke(sumTask);
            endTime = System.currentTimeMillis();
            
            System.out.println("并行求和结果: " + sum + ", 耗时: " + (endTime - startTime) + "ms");
            
        } finally {
            forkJoinPool.shutdown();
        }
    }

    /**
     * 斐波那契数列计算任务
     */
    static class FibonacciTask extends RecursiveTask<Long> {
        private final int n;
        
        public FibonacciTask(int n) {
            this.n = n;
        }
        
        @Override
        protected Long compute() {
            if (n <= 1) {
                return (long) n;
            }
            
            FibonacciTask f1 = new FibonacciTask(n - 1);
            FibonacciTask f2 = new FibonacciTask(n - 2);
            
            f1.fork(); // 异步执行
            return f2.compute() + f1.join(); // 同步等待结果
        }
    }

    /**
     * 并行求和任务
     */
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10000;
        private final int[] array;
        private final int start;
        private final int end;
        
        public SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }
        
        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                // 直接计算
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else {
                // 分割任务
                int mid = (start + end) / 2;
                SumTask leftTask = new SumTask(array, start, mid);
                SumTask rightTask = new SumTask(array, mid, end);
                
                leftTask.fork();
                return rightTask.compute() + leftTask.join();
            }
        }
    }

    /**
     * 原子操作演示 (Java 8特性)
     * 原子类的增强方法
     */
    @Java8Feature(value = "原子操作增强", desc = "原子类新增updateAndGet、accumulateAndGet等方法")
    public void demonstrateAtomicOperations() {
        System.out.println("\n=== 原子操作增强演示 ===");
        
        AtomicInteger counter = new AtomicInteger(0);
        
        // 使用虚拟线程进行并发测试
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Void>> futures = new ArrayList<>();
            
            // 启动多个并发任务
            for (int i = 0; i < 1000; i++) {
                Future<Void> future = executor.submit(() -> {
                    // 原子增加
                    counter.incrementAndGet();
                    
                    // 条件更新
                    counter.updateAndGet(current -> current % 2 == 0 ? current + 1 : current);
                    
                    // 累积操作
                    counter.accumulateAndGet(1, Integer::sum);
                    
                    return null;
                });
                futures.add(future);
            }
            
            // 等待所有任务完成
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    System.err.println("原子操作任务失败: " + e.getMessage());
                }
            }
            
            System.out.println("并发操作后的计数器值: " + counter.get());
        }
    }

    /**
     * 运行所有并发演示
     */
    public void runAllDemos() {
        System.out.println("开始并发编程增强演示...\n");
        
        demonstrateCompletableFuture();
        demonstrateCompletableFutureEnhancements();
        demonstrateVirtualThreads();
        demonstrateStructuredConcurrency();
        demonstrateForkJoinPool();
        demonstrateAtomicOperations();
        
        System.out.println("\n并发编程增强演示完成！");
    }
}