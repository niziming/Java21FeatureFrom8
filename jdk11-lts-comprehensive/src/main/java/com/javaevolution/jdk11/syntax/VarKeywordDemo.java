package com.javaevolution.jdk11.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 局部变量类型推断 (var 关键字)
 * JEP 286: Local-Variable Type Inference (JDK 10)
 */
public class VarKeywordDemo {

    /**
     * var 基础用法
     */
    public static class BasicUsage {
        
        public void basicVar() {
            // 1. 局部变量
            var message = "Hello, Java 10!";  // String
            var number = 42;                   // int
            var pi = 3.14;                     // double
            var flag = true;                   // boolean
            
            System.out.println(message.getClass()); // class java.lang.String
            System.out.println(number);
            
            // 2. 集合类型
            var list = new ArrayList<String>();
            list.add("Java");
            list.add("Python");
            
            var map = new java.util.HashMap<String, Integer>();
            map.put("Java", 11);
            map.put("Python", 3);
            
            // 3. 泛型类型
            var strings = List.of("A", "B", "C");  // List<String>
            
            // 4. 匿名类不能使用 var
            // var obj = new Object() { void test() {} };  // 编译错误
        }
        
        public void varWithStreamAndLambda() {
            // Stream 中使用 var
            var numbers = List.of(1, 2, 3, 4, 5);
            var doubled = numbers.stream()
                .map(n -> n * 2)
                .collect(java.util.stream.Collectors.toList());
            
            System.out.println(doubled);
            
            // Lambda 参数不能使用 var (JDK 10)
            // Function<String, Integer> f = (var s) -> s.length(); // JDK 10 不支持
            
            // JDK 11 开始支持 (JEP 323)
            Function<String, Integer> f = (var s) -> s.length();
            System.out.println(f.apply("Java")); // 4
        }
        
        public void varInLoops() {
            // for-each 循环
            var list = List.of("A", "B", "C");
            for (var item : list) {
                System.out.println(item);
            }
            
            // 传统 for 循环
            for (var i = 0; i < 10; i++) {
                System.out.println(i);
            }
            
            // Stream forEach
            list.forEach((var item) -> System.out.println(item));
        }
        
        public void varWithTryWithResources() {
            // try-with-resources
            var fileName = "test.txt";
            
            // var reader = new java.io.BufferedReader(
            //     new java.io.FileReader(fileName)
            // );
        }
    }

    /**
     * var 的限制
     */
    public static class Limitations {
        
        public void cannotUseVarCases() {
            // ❌ 1. 不能用于字段
            // private var name = "test";  // 编译错误
            
            // ❌ 2. 不能用于方法参数
            // public void test(var param) {}  // 编译错误 (JDK 10)
            
            // ❌ 3. 不能用于方法返回类型
            // public var getNumber() { return 42; }  // 编译错误
            
            // ❌ 4. 不能没有初始化器
            // var x;  // 编译错误
            
            // ❌ 5. 不能初始化为 null
            // var x = null;  // 编译错误
            
            // ❌ 6. 不能用于数组初始化器
            // var arr = {1, 2, 3};  // 编译错误
            
            // ✅ 正确的数组声明
            var arr = new int[]{1, 2, 3};
            
            // ❌ 7. 不能用于 Lambda 表达式
            // var f = x -> x * 2;  // 编译错误 (类型推断歧义)
            
            // ✅ 正确: 显式指定函数式接口
            Function<Integer, Integer> f = x -> x * 2;
        }
    }

    /**
     * JEP 323: Lambda 参数的局部变量语法 (JDK 11)
     */
    public static class LambdaVarParameters {
        
        public void varInLambda() {
            // 1. 允许在 Lambda 参数中使用 var
            Function<String, Integer> length = (var s) -> s.length();
            System.out.println(length.apply("Java")); // 4
            
            // 2. 可以添加注解
            java.util.function.BiFunction<String, String, String> concat = 
                (@Deprecated var s1, var s2) -> s1 + s2;
            
            // 3. 混合使用 var 和显式类型 (不允许)
            // BiFunction<String, String, String> f = (var s1, String s2) -> s1 + s2; // 编译错误
            
            // 4. 全部使用 var 或全部不使用
            java.util.function.BiFunction<Integer, Integer, Integer> add1 = (var a, var b) -> a + b;
            java.util.function.BiFunction<Integer, Integer, Integer> add2 = (a, b) -> a + b;
        }
        
        public void whyUseVarInLambda() {
            // 主要用途: 添加注解
            
            // 示例: 添加 @NonNull 注解 (假设有这样的注解)
            // Function<String, String> toUpper = (@NonNull var s) -> s.toUpperCase();
            
            // 不使用 var 时无法添加注解
            // Function<String, String> toUpper = (@NonNull s) -> s.toUpperCase(); // 编译错误
        }
    }

    /**
     * var 最佳实践
     */
    public static class BestPractices {
        
        public void goodUseCases() {
            // ✅ 好的用法: 类型明显
            var name = "John";
            var age = 30;
            var list = new ArrayList<String>();
            var map = new java.util.HashMap<String, Integer>();
            
            // ✅ 好的用法: 减少冗余
            var builder = new StringBuilder();
            var stream = list.stream();
            
            // ✅ 好的用法: 复杂泛型类型
            var complexMap = new java.util.HashMap<String, List<java.util.Map<String, Object>>>();
        }
        
        public void badUseCases() {
            // ❌ 不好: 类型不明显
            var result = calculate();  // 返回什么类型?
            
            // ❌ 不好: 使用字面量时
            var value = 0;  // int or long?
            
            // ✅ 改进: 显式指定
            int value2 = 0;
            long value3 = 0L;
            
            // ❌ 不好: Diamond operator
            var list = new ArrayList<>();  // ArrayList<Object>
            
            // ✅ 改进: 明确类型
            var list2 = new ArrayList<String>();
        }
        
        private Object calculate() {
            return 42;
        }
    }

    /**
     * var vs 传统类型声明的性能对比
     */
    public static class PerformanceComparison {
        
        public void noPerformanceDifference() {
            // var 是编译时特性,运行时性能完全相同
            
            long start1 = System.nanoTime();
            for (int i = 0; i < 1000000; i++) {
                String s = "test";
            }
            long end1 = System.nanoTime();
            
            long start2 = System.nanoTime();
            for (int i = 0; i < 1000000; i++) {
                var s = "test";
            }
            long end2 = System.nanoTime();
            
            System.out.println("Traditional: " + (end1 - start1) + " ns");
            System.out.println("Var: " + (end2 - start2) + " ns");
            // 结果几乎相同
        }
    }

    public static void main(String[] args) {
        System.out.println("=== var Keyword Demo ===");
        
        BasicUsage bu = new BasicUsage();
        bu.basicVar();
        bu.varWithStreamAndLambda();
        
        LambdaVarParameters lvp = new LambdaVarParameters();
        lvp.varInLambda();
        
        BestPractices bp = new BestPractices();
        bp.goodUseCases();
    }
}
