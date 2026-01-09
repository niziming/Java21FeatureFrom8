package com.javaevolution.jdk21.concurrency;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Virtual Threads (虚拟线程) 完整演示
 * JEP 444: Virtual Threads (JDK 21 - Final)
 * JEP 436: Virtual Threads (JDK 19 - Preview)
 * Project Loom 的核心特性
 */
public class VirtualThreadsDemo {

    /**
     * 虚拟线程基础创建方式
     */
    public static class BasicCreation {
        
        public void createVirtualThread() throws InterruptedException {
            // 方式1: Thread.ofVirtual()
            Thread vThread1 = Thread.ofVirtual().start(() -> {
                System.out.println("Virtual Thread 1: " + Thread.currentThread());
            });
            vThread1.join();
            
            // 方式2: Thread.startVirtualThread()
            Thread vThread2 = Thread.startVirtualThread(() -> {
                System.out.println("Virtual Thread 2: " + Thread.currentThread());
            });
            vThread2.join();
            
            // 方式3: Thread.Builder
            Thread vThread3 = Thread.ofVirtual()
                .name("my-virtual-thread")
                .start(() -> {
                    System.out.println("Virtual Thread 3: " + Thread.currentThread().getName());
                });
            vThread3.join();
        }
        
        public void platformVsVirtual() throws InterruptedException {
            // 平台线程 (传统线程)
            Thread platformThread = Thread.ofPlatform().start(() -> {
                System.out.println("Platform: " + Thread.currentThread());
            });
            
            // 虚拟线程
            Thread virtualThread = Thread.ofVirtual().start(() -> {
                System.out.println("Virtual: " + Thread.currentThread());
            });
            
            platformThread.join();
            virtualThread.join();
            
            // 检查是否为虚拟线程
            System.out.println("Is virtual: " + virtualThread.isVirtual()); // true
            System.out.println("Is virtual: " + platformThread.isVirtual()); // false
        }
    }

    /**
     * 使用 Executor 创建虚拟线程
     */
    public static class ExecutorUsage {
        
        public void virtualThreadExecutor() throws InterruptedException {
            // 虚拟线程执行器: 每个任务一个新虚拟线程
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                
                // 提交100个任务
                for (int i = 0; i < 100; i++) {
                    int taskId = i;
                    executor.submit(() -> {
                        System.out.println("Task " + taskId + " on " + Thread.currentThread());
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }
                
                executor.shutdown();
                executor.awaitTermination(10, TimeUnit.SECONDS);
            }
        }
        
        public void customThreadFactory() {
            // 自定义虚拟线程工厂
            ThreadFactory factory = Thread.ofVirtual()
                .name("worker-", 0)
                .factory();
            
            ExecutorService executor = Executors.newThreadPerTaskExecutor(factory);
            
            executor.submit(() -> {
                System.out.println("Running on: " + Thread.currentThread().getName());
            });
            
            executor.shutdown();
        }
    }

    /**
     * 性能压测: 虚拟线程 vs 平台线程
     */
    public static class PerformanceBenchmark {
        
        public void millionVirtualThreads() {
            long start = System.currentTimeMillis();
            
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                // 创建 100 万个虚拟线程!
                IntStream.range(0, 1_000_000).forEach(i -> {
                    executor.submit(() -> {
                        try {
                            Thread.sleep(Duration.ofSeconds(1));
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                });
                
                executor.shutdown();
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            long end = System.currentTimeMillis();
            System.out.println("Million virtual threads took: " + (end - start) + "ms");
        }
        
        public void platformThreadComparison() {
            // ❌ 平台线程: 创建10000个就会耗尽资源
            long start = System.currentTimeMillis();
            
            try (ExecutorService executor = Executors.newFixedThreadPool(1000)) {
                IntStream.range(0, 10_000).forEach(i -> {
                    executor.submit(() -> {
                        try {
                            Thread.sleep(Duration.ofMillis(100));
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                });
                
                executor.shutdown();
                executor.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            long end = System.currentTimeMillis();
            System.out.println("Platform threads took: " + (end - start) + "ms");
        }
    }

    /**
     * 虚拟线程的工作原理
     */
    public static class HowItWorks {
        
        public void carrierThreadDemo() {
            // 虚拟线程运行在"载体线程"(carrier thread)上
            Thread.ofVirtual().start(() -> {
                System.out.println("Virtual thread: " + Thread.currentThread());
                
                // 当虚拟线程阻塞时,会从载体线程上"卸载"
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // 醒来后,可能在不同的载体线程上
                System.out.println("After sleep: " + Thread.currentThread());
            });
        }
        
        public void pinningDemo() {
            // Pinning (钉住): 某些情况下虚拟线程不能卸载
            
            // 1. synchronized 块会导致 pinning
            Object lock = new Object();
            Thread.ofVirtual().start(() -> {
                synchronized (lock) {  // ❌ 会导致 pinning
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            
            // 2. JNI 调用会导致 pinning
            
            // ✅ 解决方案: 使用 ReentrantLock
            var reentrantLock = new java.util.concurrent.locks.ReentrantLock();
            Thread.ofVirtual().start(() -> {
                reentrantLock.lock();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    reentrantLock.unlock();
                }
            });
        }
    }

    /**
     * 实战应用场景
     */
    public static class RealWorldUsage {
        
        // 场景1: Web 服务器处理请求
        public void webServerSimulation() throws InterruptedException {
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                
                // 模拟10000个并发请求
                for (int i = 0; i < 10_000; i++) {
                    int requestId = i;
                    executor.submit(() -> handleRequest(requestId));
                }
                
                executor.shutdown();
                executor.awaitTermination(5, TimeUnit.SECONDS);
            }
            
            System.out.println("All requests handled!");
        }
        
        private void handleRequest(int requestId) {
            // 模拟数据库查询
            try {
                Thread.sleep(Duration.ofMillis(50));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // 模拟业务处理
            String result = processBusinessLogic(requestId);
            
            // 模拟外部 API 调用
            try {
                Thread.sleep(Duration.ofMillis(30));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // System.out.println("Request " + requestId + " completed");
        }
        
        private String processBusinessLogic(int id) {
            return "Result-" + id;
        }
        
        // 场景2: 并行数据处理
        public void parallelDataProcessing() throws InterruptedException, ExecutionException {
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                
                // 并行处理大量数据
                var futures = IntStream.range(0, 1000)
                    .mapToObj(i -> executor.submit(() -> processData(i)))
                    .toList();
                
                // 等待所有任务完成
                for (var future : futures) {
                    future.get();
                }
            }
            
            System.out.println("All data processed!");
        }
        
        private String processData(int id) {
            try {
                Thread.sleep(Duration.ofMillis(10));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Data-" + id;
        }
        
        // 场景3: 爬虫/批量下载
        public void webCrawler() {
            String[] urls = IntStream.range(0, 100)
                .mapToObj(i -> "https://example.com/page" + i)
                .toArray(String[]::new);
            
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (String url : urls) {
                    executor.submit(() -> downloadPage(url));
                }
                
                executor.shutdown();
                executor.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        private void downloadPage(String url) {
            try {
                Thread.sleep(Duration.ofMillis(100));
                // System.out.println("Downloaded: " + url);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 虚拟线程的限制与注意事项
     */
    public static class LimitationsAndCaveats {
        
        public void limitations() {
            // 1. ❌ 不要池化虚拟线程 (它们本身就是轻量级的)
            // ExecutorService pool = Executors.newFixedThreadPool(100, 
            //     Thread.ofVirtual().factory()); // 不推荐!
            
            // ✅ 正确: 每个任务一个虚拟线程
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
            
            // 2. ❌ 避免在虚拟线程中使用 synchronized
            //    改用 ReentrantLock
            
            // 3. ❌ ThreadLocal 在虚拟线程中开销大
            //    改用 ScopedValues (JDK 21 Preview)
            
            // 4. ⚠️ CPU 密集型任务不适合虚拟线程
            //    虚拟线程适合 I/O 密集型任务
        }
    }

    /**
     * 与 StructuredConcurrency 结合使用
     */
    public static class StructuredConcurrencyIntegration {
        
        // 结构化并发: 确保所有子任务完成或取消
        // (需要 --enable-preview)
        
        // public void structuredTaskExample() throws Exception {
        //     try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        //         Future<String> user = scope.fork(() -> fetchUser());
        //         Future<String> order = scope.fork(() -> fetchOrder());
        //         
        //         scope.join();           // 等待所有任务
        //         scope.throwIfFailed();  // 如果有失败则抛出异常
        //         
        //         String result = user.resultNow() + " " + order.resultNow();
        //         System.out.println(result);
        //     }
        // }
        
        private String fetchUser() throws InterruptedException {
            Thread.sleep(100);
            return "User";
        }
        
        private String fetchOrder() throws InterruptedException {
            Thread.sleep(100);
            return "Order";
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Virtual Threads Demo ===");
        
        BasicCreation bc = new BasicCreation();
        bc.createVirtualThread();
        bc.platformVsVirtual();
        
        ExecutorUsage eu = new ExecutorUsage();
        eu.virtualThreadExecutor();
        
        PerformanceBenchmark pb = new PerformanceBenchmark();
        System.out.println("\n=== Performance Test ===");
        pb.millionVirtualThreads();
        
        RealWorldUsage rwu = new RealWorldUsage();
        rwu.webServerSimulation();
    }
}
