package com.javaevolution.jdk8.concurrency;

import java.util.*;
import java.util.concurrent.*;

/**
 * CompletableFuture 完整演示
 * JEP 155: Concurrency Updates (JDK 8)
 * 提供了非阻塞、函数式风格的异步编程API
 */
public class CompletableFutureDemo {

    /**
     * 创建 CompletableFuture 的各种方式
     */
    public static class CreationMethods {
        
        // 1. 创建已完成的 Future
        public void completedFuture() {
            CompletableFuture<String> future = CompletableFuture.completedFuture("Hello");
            System.out.println(future.join()); // Hello
        }
        
        // 2. supplyAsync: 异步执行并返回结果
        public void supplyAsync() {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "Result from supplyAsync";
            });
            
            System.out.println(future.join());
        }
        
        // 3. runAsync: 异步执行无返回值
        public void runAsync() {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("Running in: " + Thread.currentThread().getName());
            });
            
            future.join();
        }
        
        // 4. 使用自定义线程池
        public void customExecutor() {
            ExecutorService executor = Executors.newFixedThreadPool(3);
            
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                System.out.println("Thread: " + Thread.currentThread().getName());
                return "Custom pool result";
            }, executor);
            
            System.out.println(future.join());
            executor.shutdown();
        }
    }

    /**
     * 链式操作: thenApply, thenAccept, thenRun
     */
    public static class ChainingOperations {
        
        // thenApply: 转换结果 (有返回值)
        public void thenApplyDemo() {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10)
                .thenApply(n -> n * 2)
                .thenApply(n -> n + 5);
            
            System.out.println(future.join()); // 25
        }
        
        // thenAccept: 消费结果 (无返回值)
        public void thenAcceptDemo() {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenAccept(s -> System.out.println("Result: " + s));
            
            future.join();
        }
        
        // thenRun: 执行后续动作 (不关心上游结果)
        public void thenRunDemo() {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenRun(() -> System.out.println("Task completed!"));
            
            future.join();
        }
        
        // Async 版本: 异步执行链式操作
        public void asyncChaining() {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                System.out.println("Supply: " + Thread.currentThread().getName());
                return "Hello";
            }).thenApplyAsync(s -> {
                System.out.println("Apply: " + Thread.currentThread().getName());
                return s + " World";
            }).thenApplyAsync(s -> {
                System.out.println("Apply2: " + Thread.currentThread().getName());
                return s.toUpperCase();
            });
            
            System.out.println(future.join());
        }
    }

    /**
     * 组合多个 Future: thenCompose, thenCombine
     */
    public static class CombiningFutures {
        
        // thenCompose: 依赖关系 (flatMap)
        public void thenComposeDemo() {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "User123")
                .thenCompose(userId -> getUserDetails(userId))
                .thenCompose(userDetails -> getOrders(userDetails));
            
            System.out.println(future.join());
        }
        
        private CompletableFuture<String> getUserDetails(String userId) {
            return CompletableFuture.supplyAsync(() -> {
                System.out.println("Fetching user: " + userId);
                return "UserDetails-" + userId;
            });
        }
        
        private CompletableFuture<String> getOrders(String userDetails) {
            return CompletableFuture.supplyAsync(() -> {
                System.out.println("Fetching orders for: " + userDetails);
                return "Orders-" + userDetails;
            });
        }
        
        // thenCombine: 独立关系,合并结果
        public void thenCombineDemo() {
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                sleep(1000);
                return "Result1";
            });
            
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                sleep(1500);
                return "Result2";
            });
            
            CompletableFuture<String> combined = future1.thenCombine(future2, 
                (r1, r2) -> r1 + " + " + r2);
            
            System.out.println(combined.join()); // Result1 + Result2
        }
        
        // thenAcceptBoth: 消费两个结果
        public void thenAcceptBothDemo() {
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");
            
            CompletableFuture<Void> combined = future1.thenAcceptBoth(future2,
                (r1, r2) -> System.out.println(r1 + " " + r2));
            
            combined.join();
        }
        
        // runAfterBoth: 两个都完成后执行
        public void runAfterBothDemo() {
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Task1");
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Task2");
            
            CompletableFuture<Void> both = future1.runAfterBoth(future2,
                () -> System.out.println("Both completed!"));
            
            both.join();
        }
    }

    /**
     * Either 操作: 任一完成即触发
     */
    public static class EitherOperations {
        
        // applyToEither: 任一完成,转换结果
        public void applyToEitherDemo() {
            CompletableFuture<String> fast = CompletableFuture.supplyAsync(() -> {
                sleep(500);
                return "Fast";
            });
            
            CompletableFuture<String> slow = CompletableFuture.supplyAsync(() -> {
                sleep(2000);
                return "Slow";
            });
            
            CompletableFuture<String> result = fast.applyToEither(slow, 
                s -> "Winner: " + s);
            
            System.out.println(result.join()); // Winner: Fast
        }
        
        // acceptEither: 任一完成,消费结果
        public void acceptEitherDemo() {
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                sleep(500);
                return "First";
            });
            
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                sleep(1000);
                return "Second";
            });
            
            CompletableFuture<Void> result = future1.acceptEither(future2,
                s -> System.out.println("First to complete: " + s));
            
            result.join();
        }
    }

    /**
     * 异常处理
     */
    public static class ExceptionHandling {
        
        // exceptionally: 异常恢复
        public void exceptionallyDemo() {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                if (Math.random() > 0.5) {
                    throw new RuntimeException("Random error!");
                }
                return "Success";
            }).exceptionally(ex -> {
                System.out.println("Error: " + ex.getMessage());
                return "Default Value";
            });
            
            System.out.println(future.join());
        }
        
        // handle: 处理正常结果和异常
        public void handleDemo() {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                if (Math.random() > 0.5) {
                    throw new RuntimeException("Error!");
                }
                return "Success";
            }).handle((result, ex) -> {
                if (ex != null) {
                    return "Handled error: " + ex.getMessage();
                }
                return result;
            });
            
            System.out.println(future.join());
        }
        
        // whenComplete: 类似 finally,不影响结果
        public void whenCompleteDemo() {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Result")
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.out.println("Error occurred: " + ex);
                    } else {
                        System.out.println("Success: " + result);
                    }
                });
            
            System.out.println(future.join());
        }
    }

    /**
     * 批量操作: allOf, anyOf
     */
    public static class BatchOperations {
        
        // allOf: 等待所有完成
        public void allOfDemo() {
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                sleep(1000);
                return "Task1";
            });
            
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                sleep(1500);
                return "Task2";
            });
            
            CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
                sleep(800);
                return "Task3";
            });
            
            CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2, future3);
            
            allOf.thenRun(() -> {
                System.out.println("All tasks completed!");
                System.out.println(future1.join());
                System.out.println(future2.join());
                System.out.println(future3.join());
            }).join();
        }
        
        // 获取所有结果
        public void allOfWithResults() {
            List<CompletableFuture<String>> futures = Arrays.asList(
                CompletableFuture.supplyAsync(() -> "Task1"),
                CompletableFuture.supplyAsync(() -> "Task2"),
                CompletableFuture.supplyAsync(() -> "Task3")
            );
            
            CompletableFuture<List<String>> allResults = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .collect(java.util.stream.Collectors.toList()));
            
            System.out.println(allResults.join());
        }
        
        // anyOf: 任一完成即返回
        public void anyOfDemo() {
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                sleep(2000);
                return "Slow";
            });
            
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                sleep(500);
                return "Fast";
            });
            
            CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2);
            
            System.out.println("First to complete: " + anyOf.join()); // Fast
        }
    }

    /**
     * 实战案例: 并行调用多个服务
     */
    public static class RealWorldExample {
        
        public void parallelServiceCalls() {
            long start = System.currentTimeMillis();
            
            // 并行调用3个服务
            CompletableFuture<String> userService = CompletableFuture.supplyAsync(() -> {
                sleep(1000);
                return "User: Alice";
            });
            
            CompletableFuture<String> orderService = CompletableFuture.supplyAsync(() -> {
                sleep(1500);
                return "Orders: 5";
            });
            
            CompletableFuture<String> inventoryService = CompletableFuture.supplyAsync(() -> {
                sleep(800);
                return "Inventory: 100";
            });
            
            // 合并所有结果
            CompletableFuture<String> combined = userService
                .thenCombine(orderService, (user, orders) -> user + ", " + orders)
                .thenCombine(inventoryService, (partial, inventory) -> partial + ", " + inventory);
            
            String result = combined.join();
            long end = System.currentTimeMillis();
            
            System.out.println("Result: " + result);
            System.out.println("Time: " + (end - start) + "ms"); // ~1500ms (并行)
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== CompletableFuture Demo ===");
        
        CombiningFutures cf = new CombiningFutures();
        cf.thenCombineDemo();
        
        BatchOperations bo = new BatchOperations();
        bo.allOfDemo();
        
        RealWorldExample rwe = new RealWorldExample();
        rwe.parallelServiceCalls();
    }
}
