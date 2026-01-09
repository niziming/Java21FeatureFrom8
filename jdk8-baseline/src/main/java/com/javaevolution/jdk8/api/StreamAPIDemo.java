package com.javaevolution.jdk8.api;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

/**
 * Stream API 完整演示
 * JEP 107: Bulk Data Operations for Collections
 */
public class StreamAPIDemo {

    /**
     * 中间操作 (Intermediate Operations) 完整演示
     */
    public static class IntermediateOperations {
        
        public void filterDemo() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            
            List<Integer> evenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
            
            System.out.println("Even numbers: " + evenNumbers); // [2, 4, 6, 8, 10]
        }
        
        public void mapDemo() {
            List<String> words = Arrays.asList("java", "python", "go");
            
            List<Integer> lengths = words.stream()
                .map(String::length)
                .collect(Collectors.toList());
            
            System.out.println("Lengths: " + lengths); // [4, 6, 2]
        }
        
        public void flatMapDemo() {
            List<List<Integer>> nested = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5, 6)
            );
            
            // flatMap 用于扁平化嵌套结构
            List<Integer> flat = nested.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
            
            System.out.println("Flattened: " + flat); // [1, 2, 3, 4, 5, 6]
            
            // 字符串拆分成字符
            List<String> words = Arrays.asList("Hello", "World");
            List<String> chars = words.stream()
                .flatMap(word -> Arrays.stream(word.split("")))
                .distinct()
                .collect(Collectors.toList());
            
            System.out.println("Unique chars: " + chars);
        }
        
        public void distinctSortedLimitSkip() {
            List<Integer> numbers = Arrays.asList(5, 2, 8, 2, 9, 1, 5, 3);
            
            List<Integer> result = numbers.stream()
                .distinct()          // 去重
                .sorted()            // 排序
                .skip(2)             // 跳过前2个
                .limit(3)            // 取3个
                .collect(Collectors.toList());
            
            System.out.println("Result: " + result); // [3, 5, 8]
        }
        
        public void peekDemo() {
            // peek 用于调试,不改变流
            List<String> result = Stream.of("one", "two", "three")
                .peek(s -> System.out.println("Original: " + s))
                .map(String::toUpperCase)
                .peek(s -> System.out.println("Uppercase: " + s))
                .collect(Collectors.toList());
        }
    }

    /**
     * 终端操作 (Terminal Operations) 完整演示
     */
    public static class TerminalOperations {
        
        public void collectDemo() {
            List<String> words = Arrays.asList("java", "python", "go", "java");
            
            // 1. toList
            List<String> list = words.stream().collect(Collectors.toList());
            
            // 2. toSet
            Set<String> set = words.stream().collect(Collectors.toSet());
            
            // 3. toMap
            Map<String, Integer> map = words.stream()
                .distinct()
                .collect(Collectors.toMap(
                    Function.identity(),  // key
                    String::length        // value
                ));
            
            // 4. joining
            String joined = words.stream()
                .collect(Collectors.joining(", ", "[", "]"));
            System.out.println(joined); // [java, python, go, java]
            
            // 5. groupingBy
            Map<Integer, List<String>> grouped = words.stream()
                .collect(Collectors.groupingBy(String::length));
            System.out.println(grouped); // {2=[go], 4=[java, java], 6=[python]}
            
            // 6. partitioningBy
            Map<Boolean, List<String>> partitioned = words.stream()
                .collect(Collectors.partitioningBy(s -> s.length() > 3));
            System.out.println(partitioned); // {false=[go], true=[java, python, java]}
            
            // 7. counting
            long count = words.stream()
                .collect(Collectors.counting());
            
            // 8. summarizingInt
            IntSummaryStatistics stats = words.stream()
                .collect(Collectors.summarizingInt(String::length));
            System.out.println("Max length: " + stats.getMax()); // 6
        }
        
        public void reduceDemo() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            
            // 1. reduce with identity
            int sum = numbers.stream()
                .reduce(0, Integer::sum);
            System.out.println("Sum: " + sum); // 15
            
            // 2. reduce without identity (returns Optional)
            Optional<Integer> product = numbers.stream()
                .reduce((a, b) -> a * b);
            product.ifPresent(p -> System.out.println("Product: " + p)); // 120
            
            // 3. reduce with combiner (for parallel streams)
            int sumParallel = numbers.parallelStream()
                .reduce(0, 
                    Integer::sum,           // accumulator
                    Integer::sum);          // combiner
            
            // 复杂 reduce: 连接字符串
            String concatenated = Stream.of("a", "b", "c", "d")
                .reduce("", (acc, s) -> acc + s);
            System.out.println(concatenated); // abcd
        }
        
        public void findMatchDemo() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            
            // findFirst
            Optional<Integer> first = numbers.stream()
                .filter(n -> n > 3)
                .findFirst();
            System.out.println(first.orElse(-1)); // 4
            
            // findAny (在并行流中更高效)
            Optional<Integer> any = numbers.parallelStream()
                .filter(n -> n > 3)
                .findAny();
            
            // anyMatch
            boolean hasEven = numbers.stream()
                .anyMatch(n -> n % 2 == 0);
            System.out.println("Has even: " + hasEven); // true
            
            // allMatch
            boolean allPositive = numbers.stream()
                .allMatch(n -> n > 0);
            System.out.println("All positive: " + allPositive); // true
            
            // noneMatch
            boolean noNegative = numbers.stream()
                .noneMatch(n -> n < 0);
            System.out.println("No negative: " + noNegative); // true
        }
        
        public void forEachDemo() {
            List<String> words = Arrays.asList("java", "python", "go");
            
            // forEach (无序,用于并行流)
            words.parallelStream().forEach(System.out::println);
            
            // forEachOrdered (保持顺序)
            words.parallelStream().forEachOrdered(System.out::println);
        }
    }

    /**
     * 并行流陷阱与最佳实践
     */
    public static class ParallelStreamPitfalls {
        
        // 陷阱1: 共享可变状态
        public void sharedMutableState() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            List<Integer> results = new ArrayList<>(); // 共享可变状态
            
            // ❌ 错误: 线程不安全
            // numbers.parallelStream().forEach(results::add);
            
            // ✅ 正确: 使用 collect
            List<Integer> correctResults = numbers.parallelStream()
                .collect(Collectors.toList());
        }
        
        // 陷阱2: 装箱开销
        public void boxingOverhead() {
            // ❌ 性能差: 自动装箱
            int sum1 = IntStream.range(0, 1000000)
                .boxed()
                .parallel()
                .reduce(0, Integer::sum);
            
            // ✅ 性能好: 使用原始类型流
            int sum2 = IntStream.range(0, 1000000)
                .parallel()
                .sum();
        }
        
        // 陷阱3: 任务太小不适合并行
        public void smallTasksPitfall() {
            List<Integer> smallList = Arrays.asList(1, 2, 3, 4, 5);
            
            // ❌ 不适合: 数据量太小,线程切换开销大于收益
            // int sum = smallList.parallelStream().mapToInt(i -> i).sum();
            
            // ✅ 适合: 顺序流
            int sum = smallList.stream().mapToInt(i -> i).sum();
        }
        
        // 最佳实践: 何时使用并行流
        public void whenToUseParallel() {
            // 1. 数据量大 (通常 > 10000)
            // 2. 计算密集型任务
            // 3. 无状态、无副作用的操作
            // 4. 易于拆分的数据结构 (ArrayList > LinkedList)
            
            List<Integer> largeList = IntStream.range(0, 1000000)
                .boxed()
                .collect(Collectors.toList());
            
            // 计算密集型: 适合并行
            double result = largeList.parallelStream()
                .mapToDouble(n -> Math.sqrt(n))
                .sum();
        }
    }

    /**
     * Stream 创建方式
     */
    public static class StreamCreation {
        public void createStreams() {
            // 1. 从集合
            List<String> list = Arrays.asList("a", "b", "c");
            Stream<String> stream1 = list.stream();
            
            // 2. 从数组
            String[] array = {"a", "b", "c"};
            Stream<String> stream2 = Arrays.stream(array);
            
            // 3. Stream.of
            Stream<String> stream3 = Stream.of("a", "b", "c");
            
            // 4. Stream.generate (无限流)
            Stream<Double> random = Stream.generate(Math::random).limit(10);
            
            // 5. Stream.iterate (无限流)
            Stream<Integer> natural = Stream.iterate(0, n -> n + 1).limit(10);
            
            // 6. IntStream/LongStream/DoubleStream
            IntStream intStream = IntStream.range(1, 10);
            LongStream longStream = LongStream.rangeClosed(1, 10);
            DoubleStream doubleStream = DoubleStream.of(1.0, 2.0, 3.0);
            
            // 7. Stream.builder
            Stream<String> stream4 = Stream.<String>builder()
                .add("a")
                .add("b")
                .add("c")
                .build();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Stream API Demo ===");
        
        IntermediateOperations io = new IntermediateOperations();
        io.flatMapDemo();
        
        TerminalOperations to = new TerminalOperations();
        to.collectDemo();
        to.reduceDemo();
    }
}
