package com.javaevolution.jdk11.api;

import java.util.*;
import java.util.stream.*;

/**
 * Collection Factory Methods & Stream Enhancements
 * JEP 269: Convenience Factory Methods for Collections (JDK 9)
 * JEP 321: Stream API Improvements (JDK 9/10)
 */
public class CollectionAndStreamDemo {

    /**
     * 不可变集合工厂方法
     * JEP 269: Convenience Factory Methods for Collections
     */
    public static class CollectionFactories {
        
        public void listOf() {
            // List.of - 创建不可变 List
            List<String> list1 = List.of("A", "B", "C");
            List<Integer> list2 = List.of(1, 2, 3, 4, 5);
            List<String> emptyList = List.of();
            
            System.out.println(list1); // [A, B, C]
            
            // 特性: 不可变
            try {
                list1.add("D");  // UnsupportedOperationException
            } catch (UnsupportedOperationException e) {
                System.out.println("Cannot modify immutable list");
            }
            
            // 特性: 不允许 null
            try {
                List<String> listWithNull = List.of("A", null, "C");
            } catch (NullPointerException e) {
                System.out.println("Cannot contain null");
            }
            
            // 特性: 紧凑实现,节省内存
            System.out.println(list1.getClass()); // ImmutableCollections$ListN
        }
        
        public void setOf() {
            // Set.of - 创建不可变 Set
            Set<String> set1 = Set.of("A", "B", "C");
            Set<Integer> set2 = Set.of(1, 2, 3, 4, 5);
            Set<String> emptySet = Set.of();
            
            System.out.println(set1); // [A, B, C] (无序)
            
            // 特性: 不允许重复元素
            try {
                Set<String> setWithDup = Set.of("A", "B", "A");
            } catch (IllegalArgumentException e) {
                System.out.println("Cannot contain duplicates");
            }
        }
        
        public void mapOf() {
            // Map.of - 创建不可变 Map (最多10个键值对)
            Map<String, Integer> map1 = Map.of(
                "Java", 11,
                "Python", 3,
                "Go", 1
            );
            
            System.out.println(map1); // {Java=11, Python=3, Go=1}
            
            // Map.ofEntries - 超过10个键值对时使用
            Map<String, Integer> map2 = Map.ofEntries(
                Map.entry("k1", 1),
                Map.entry("k2", 2),
                Map.entry("k3", 3),
                Map.entry("k4", 4),
                Map.entry("k5", 5)
            );
            
            // 特性: 不可变
            try {
                map1.put("Rust", 1);  // UnsupportedOperationException
            } catch (UnsupportedOperationException e) {
                System.out.println("Cannot modify immutable map");
            }
        }
        
        public void copyOf() {
            // List.copyOf - 复制为不可变集合
            List<String> mutableList = new ArrayList<>(Arrays.asList("A", "B", "C"));
            List<String> immutableList = List.copyOf(mutableList);
            
            mutableList.add("D");
            System.out.println("Mutable: " + mutableList);      // [A, B, C, D]
            System.out.println("Immutable: " + immutableList);  // [A, B, C]
            
            // Set.copyOf & Map.copyOf 同理
            Set<String> immutableSet = Set.copyOf(new HashSet<>(Arrays.asList("X", "Y")));
            Map<String, Integer> immutableMap = Map.copyOf(new HashMap<>());
        }
    }

    /**
     * Stream API 增强
     * JEP 271: Stream Enhancements (JDK 9)
     */
    public static class StreamEnhancements {
        
        // takeWhile - 从头开始取,直到条件不满足
        public void takeWhileDemo() {
            List<Integer> numbers = List.of(1, 2, 3, 4, 5, 3, 2, 1);
            
            List<Integer> result = numbers.stream()
                .takeWhile(n -> n < 5)
                .collect(Collectors.toList());
            
            System.out.println(result); // [1, 2, 3, 4] (遇到5就停止)
            
            // 有序流中很有用
            List<Integer> sorted = List.of(1, 2, 3, 4, 5, 6, 7, 8);
            List<Integer> lessThan5 = sorted.stream()
                .takeWhile(n -> n < 5)
                .collect(Collectors.toList());
            
            System.out.println(lessThan5); // [1, 2, 3, 4]
        }
        
        // dropWhile - 丢弃元素,直到条件不满足
        public void dropWhileDemo() {
            List<Integer> numbers = List.of(1, 2, 3, 4, 5, 3, 2, 1);
            
            List<Integer> result = numbers.stream()
                .dropWhile(n -> n < 5)
                .collect(Collectors.toList());
            
            System.out.println(result); // [5, 3, 2, 1] (丢弃1,2,3,4后开始保留)
            
            // 实用场景: 跳过文件头
            List<String> lines = List.of("# Header", "# Comment", "Data1", "Data2");
            List<String> data = lines.stream()
                .dropWhile(line -> line.startsWith("#"))
                .collect(Collectors.toList());
            
            System.out.println(data); // [Data1, Data2]
        }
        
        // iterate 重载 - 带条件的无限流
        public void iterateDemo() {
            // JDK 8: 无限流,需要 limit
            Stream.iterate(0, n -> n + 1)
                .limit(10)
                .forEach(System.out::println);
            
            // JDK 9: 带条件的有限流
            Stream.iterate(0, n -> n < 10, n -> n + 1)
                .forEach(System.out::println);
            
            // 等价于传统 for 循环
            // for (int i = 0; i < 10; i++) { ... }
            
            // 实用示例: 斐波那契数列
            Stream.iterate(new int[]{0, 1}, 
                          arr -> arr[0] + arr[1] < 100,
                          arr -> new int[]{arr[1], arr[0] + arr[1]})
                .map(arr -> arr[0])
                .forEach(System.out::println);
        }
        
        // ofNullable - 处理可能为 null 的单个元素
        public void ofNullableDemo() {
            String value = "Java";
            
            // JDK 8: 繁琐的 null 检查
            Stream<String> stream1 = value == null ? Stream.empty() : Stream.of(value);
            
            // JDK 9: 简洁的 ofNullable
            Stream<String> stream2 = Stream.ofNullable(value);
            
            stream2.forEach(System.out::println); // Java
            
            // null 值会返回空流
            Stream.ofNullable(null).forEach(System.out::println); // 不输出
            
            // 实用场景: flatMap 中处理可能为 null 的值
            List<String> list = Arrays.asList("A", null, "B", "C", null);
            list.stream()
                .flatMap(Stream::ofNullable)
                .forEach(System.out::println); // A, B, C
        }
    }

    /**
     * Optional 增强
     * JEP 269: Optional improvements
     */
    public static class OptionalEnhancements {
        
        // ifPresentOrElse (JDK 9)
        public void ifPresentOrElseDemo() {
            Optional<String> opt1 = Optional.of("Java");
            Optional<String> opt2 = Optional.empty();
            
            opt1.ifPresentOrElse(
                value -> System.out.println("Value: " + value),  // present action
                () -> System.out.println("No value")              // empty action
            );
            
            opt2.ifPresentOrElse(
                value -> System.out.println("Value: " + value),
                () -> System.out.println("No value")              // 输出: No value
            );
        }
        
        // or (JDK 9)
        public void orDemo() {
            Optional<String> opt1 = Optional.of("Java");
            Optional<String> opt2 = Optional.empty();
            
            // 提供备选的 Optional
            Optional<String> result1 = opt1.or(() -> Optional.of("Default"));
            System.out.println(result1.get()); // Java
            
            Optional<String> result2 = opt2.or(() -> Optional.of("Default"));
            System.out.println(result2.get()); // Default
            
            // 与 orElse 的区别
            String value1 = opt2.orElse("Default");           // 直接返回值
            Optional<String> value2 = opt2.or(() -> Optional.of("Default")); // 返回 Optional
        }
        
        // stream (JDK 9)
        public void streamDemo() {
            Optional<String> opt1 = Optional.of("Java");
            Optional<String> opt2 = Optional.empty();
            
            // Optional 转 Stream
            Stream<String> stream1 = opt1.stream();
            stream1.forEach(System.out::println); // Java
            
            Stream<String> stream2 = opt2.stream();
            stream2.forEach(System.out::println); // 不输出
            
            // 实用场景: 过滤列表中的 Optional
            List<Optional<String>> list = Arrays.asList(
                Optional.of("A"),
                Optional.empty(),
                Optional.of("B"),
                Optional.empty(),
                Optional.of("C")
            );
            
            List<String> result = list.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
            
            System.out.println(result); // [A, B, C]
        }
        
        // isEmpty (JDK 11)
        public void isEmptyDemo() {
            Optional<String> opt = Optional.empty();
            
            // JDK 8
            boolean empty1 = !opt.isPresent();
            
            // JDK 11
            boolean empty2 = opt.isEmpty();
            
            System.out.println("Is empty: " + empty2); // true
        }
    }

    /**
     * Collectors 增强
     */
    public static class CollectorsEnhancements {
        
        // filtering (JDK 9)
        public void filteringDemo() {
            List<String> words = List.of("java", "python", "go", "rust", "kotlin");
            
            // 分组后过滤
            Map<Integer, List<String>> grouped = words.stream()
                .collect(Collectors.groupingBy(
                    String::length,
                    Collectors.filtering(s -> s.startsWith("j") || s.startsWith("k"), 
                                         Collectors.toList())
                ));
            
            System.out.println(grouped); // {4=[java], 6=[kotlin]}
        }
        
        // flatMapping (JDK 9)
        public void flatMappingDemo() {
            Map<String, List<String>> map = Map.of(
                "Java", List.of("JVM", "JDK"),
                "Python", List.of("CPython", "PyPy")
            );
            
            // 扁平化映射
            Map<String, List<Character>> result = map.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().stream()
                        .flatMap(s -> s.chars().mapToObj(c -> (char) c))
                        .collect(Collectors.toList())
                ));
            
            System.out.println(result);
        }
        
        // teeing (JDK 12)
        public void teeingDemo() {
            List<Integer> numbers = List.of(1, 2, 3, 4, 5);
            
            // 同时计算最大值和最小值
            // String result = numbers.stream()
            //     .collect(Collectors.teeing(
            //         Collectors.maxBy(Integer::compare),
            //         Collectors.minBy(Integer::compare),
            //         (max, min) -> "Max: " + max.orElse(0) + ", Min: " + min.orElse(0)
            //     ));
            
            // System.out.println(result);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Collection and Stream Demo ===");
        
        CollectionFactories cf = new CollectionFactories();
        cf.listOf();
        cf.mapOf();
        
        StreamEnhancements se = new StreamEnhancements();
        se.takeWhileDemo();
        se.dropWhileDemo();
        se.iterateDemo();
        
        OptionalEnhancements oe = new OptionalEnhancements();
        oe.streamDemo();
    }
}
