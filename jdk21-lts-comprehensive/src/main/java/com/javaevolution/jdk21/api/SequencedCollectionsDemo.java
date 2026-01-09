package com.javaevolution.jdk21.api;

import java.util.*;

/**
 * Sequenced Collections 完整演示
 * JEP 431: Sequenced Collections (JDK 21)
 * 为集合框架引入有序集合的统一接口
 */
public class SequencedCollectionsDemo {

    /**
     * SequencedCollection 接口
     */
    public static class SequencedCollectionBasics {
        
        public void sequencedCollectionMethods() {
            // List 实现了 SequencedCollection
            List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));
            
            // 新方法: addFirst / addLast
            list.addFirst("FIRST");
            list.addLast("LAST");
            System.out.println(list); // [FIRST, A, B, C, D, LAST]
            
            // 新方法: getFirst / getLast
            String first = list.getFirst();
            String last = list.getLast();
            System.out.println("First: " + first); // FIRST
            System.out.println("Last: " + last);   // LAST
            
            // 新方法: removeFirst / removeLast
            list.removeFirst();
            list.removeLast();
            System.out.println(list); // [A, B, C, D]
            
            // 新方法: reversed() - 返回反向视图
            List<String> reversed = list.reversed();
            System.out.println("Reversed: " + reversed); // [D, C, B, A]
            
            // 反向视图是可修改的
            reversed.addFirst("Z");
            System.out.println("Original: " + list); // [A, B, C, D, Z]
            System.out.println("Reversed: " + reversed); // [Z, D, C, B, A]
        }
        
        public void dequeExample() {
            // Deque 也实现了 SequencedCollection
            Deque<Integer> deque = new ArrayDeque<>(List.of(1, 2, 3, 4, 5));
            
            deque.addFirst(0);
            deque.addLast(6);
            System.out.println(deque); // [0, 1, 2, 3, 4, 5, 6]
            
            int first = deque.getFirst();
            int last = deque.getLast();
            System.out.println("First: " + first + ", Last: " + last);
        }
    }

    /**
     * SequencedSet 接口
     */
    public static class SequencedSetDemo {
        
        public void linkedHashSetExample() {
            // LinkedHashSet 实现了 SequencedSet
            SequencedSet<String> set = new LinkedHashSet<>();
            
            set.add("Java");
            set.add("Python");
            set.add("Go");
            set.add("Rust");
            
            System.out.println(set); // [Java, Python, Go, Rust] (保持插入顺序)
            
            // SequencedSet 方法
            set.addFirst("First");
            set.addLast("Last");
            System.out.println(set); // [First, Java, Python, Go, Rust, Last]
            
            String first = set.getFirst();
            String last = set.getLast();
            System.out.println("First: " + first + ", Last: " + last);
            
            // reversed() - 反向视图
            SequencedSet<String> reversed = set.reversed();
            System.out.println("Reversed: " + reversed); // [Last, Rust, Go, Python, Java, First]
        }
        
        public void treeSetExample() {
            // TreeSet 也实现了 SequencedSet
            SequencedSet<Integer> sortedSet = new TreeSet<>(List.of(5, 2, 8, 1, 9, 3));
            
            System.out.println(sortedSet); // [1, 2, 3, 5, 8, 9] (自然排序)
            
            int first = sortedSet.getFirst(); // 最小值
            int last = sortedSet.getLast();   // 最大值
            System.out.println("Min: " + first + ", Max: " + last);
            
            // 反向视图: 降序
            SequencedSet<Integer> descending = sortedSet.reversed();
            System.out.println("Descending: " + descending); // [9, 8, 5, 3, 2, 1]
        }
    }

    /**
     * SequencedMap 接口
     */
    public static class SequencedMapDemo {
        
        public void linkedHashMapExample() {
            // LinkedHashMap 实现了 SequencedMap
            SequencedMap<String, Integer> map = new LinkedHashMap<>();
            
            map.put("Java", 1995);
            map.put("Python", 1991);
            map.put("Go", 2009);
            map.put("Rust", 2010);
            
            System.out.println(map);
            
            // 新方法: firstEntry / lastEntry
            Map.Entry<String, Integer> firstEntry = map.firstEntry();
            Map.Entry<String, Integer> lastEntry = map.lastEntry();
            System.out.println("First: " + firstEntry); // Java=1995
            System.out.println("Last: " + lastEntry);   // Rust=2010
            
            // 新方法: pollFirstEntry / pollLastEntry (移除并返回)
            Map.Entry<String, Integer> polledFirst = map.pollFirstEntry();
            System.out.println("Polled: " + polledFirst);
            System.out.println("After poll: " + map);
            
            // 新方法: putFirst / putLast
            map.putFirst("C++", 1985);
            map.putLast("Swift", 2014);
            System.out.println(map);
            
            // reversed() - 反向视图
            SequencedMap<String, Integer> reversed = map.reversed();
            System.out.println("Reversed: " + reversed);
        }
        
        public void treeMapExample() {
            // TreeMap 也实现了 SequencedMap
            SequencedMap<Integer, String> sortedMap = new TreeMap<>();
            
            sortedMap.put(5, "Five");
            sortedMap.put(2, "Two");
            sortedMap.put(8, "Eight");
            sortedMap.put(1, "One");
            
            System.out.println(sortedMap); // {1=One, 2=Two, 5=Five, 8=Eight}
            
            Map.Entry<Integer, String> first = sortedMap.firstEntry();
            Map.Entry<Integer, String> last = sortedMap.lastEntry();
            System.out.println("First: " + first + ", Last: " + last);
            
            // 降序视图
            SequencedMap<Integer, String> descending = sortedMap.reversed();
            System.out.println("Descending: " + descending);
        }
        
        public void sequencedKeysAndValues() {
            SequencedMap<String, Integer> map = new LinkedHashMap<>();
            map.put("A", 1);
            map.put("B", 2);
            map.put("C", 3);
            
            // sequencedKeySet() - 有序键集合
            SequencedSet<String> keys = map.sequencedKeySet();
            System.out.println("Keys: " + keys); // [A, B, C]
            System.out.println("First key: " + keys.getFirst()); // A
            
            // sequencedValues() - 有序值集合
            SequencedCollection<Integer> values = map.sequencedValues();
            System.out.println("Values: " + values); // [1, 2, 3]
            System.out.println("First value: " + values.getFirst()); // 1
            
            // sequencedEntrySet() - 有序键值对集合
            SequencedSet<Map.Entry<String, Integer>> entries = map.sequencedEntrySet();
            System.out.println("Entries: " + entries);
        }
    }

    /**
     * 与旧 API 对比
     */
    public static class ComparisonWithOldAPI {
        
        public void beforeJDK21() {
            List<String> list = new ArrayList<>(List.of("A", "B", "C"));
            
            // 获取第一个元素 (旧方式)
            String first = list.get(0);
            
            // 获取最后一个元素 (旧方式)
            String last = list.get(list.size() - 1);
            
            // 添加到开头 (旧方式)
            list.add(0, "FIRST");
            
            // 反向迭代 (旧方式)
            for (int i = list.size() - 1; i >= 0; i--) {
                System.out.println(list.get(i));
            }
        }
        
        public void afterJDK21() {
            List<String> list = new ArrayList<>(List.of("A", "B", "C"));
            
            // 获取第一个/最后一个 (新方式)
            String first = list.getFirst();
            String last = list.getLast();
            
            // 添加到开头 (新方式)
            list.addFirst("FIRST");
            
            // 反向迭代 (新方式)
            for (String item : list.reversed()) {
                System.out.println(item);
            }
        }
    }

    /**
     * 实际应用场景
     */
    public static class RealWorldUsage {
        
        // 场景1: LRU 缓存
        public static class LRUCache<K, V> {
            private final int capacity;
            private final SequencedMap<K, V> cache;
            
            public LRUCache(int capacity) {
                this.capacity = capacity;
                this.cache = new LinkedHashMap<>();
            }
            
            public V get(K key) {
                V value = cache.remove(key);
                if (value != null) {
                    cache.putLast(key, value); // 移到最后(最近使用)
                }
                return value;
            }
            
            public void put(K key, V value) {
                cache.remove(key);
                cache.putLast(key, value);
                
                if (cache.size() > capacity) {
                    cache.pollFirstEntry(); // 移除最久未使用的
                }
            }
            
            public void display() {
                System.out.println("Cache: " + cache);
            }
        }
        
        public void lruCacheDemo() {
            LRUCache<String, Integer> cache = new LRUCache<>(3);
            
            cache.put("A", 1);
            cache.put("B", 2);
            cache.put("C", 3);
            cache.display(); // {A=1, B=2, C=3}
            
            cache.get("A"); // 访问 A
            cache.display(); // {B=2, C=3, A=1}
            
            cache.put("D", 4); // 添加 D,移除 B
            cache.display(); // {C=3, A=1, D=4}
        }
        
        // 场景2: 最近访问记录
        public void recentAccessHistory() {
            SequencedSet<String> history = new LinkedHashSet<>();
            
            // 记录访问
            history.add("page1.html");
            history.add("page2.html");
            history.add("page3.html");
            history.add("page2.html"); // 重复访问,保持唯一
            
            System.out.println("History: " + history);
            
            // 获取最近访问的页面
            String mostRecent = history.getLast();
            System.out.println("Most recent: " + mostRecent);
            
            // 反向查看历史
            for (String page : history.reversed()) {
                System.out.println("Visited: " + page);
            }
        }
        
        // 场景3: 有序任务队列
        public void taskQueue() {
            SequencedMap<String, String> tasks = new LinkedHashMap<>();
            
            tasks.put("task-1", "Initialize database");
            tasks.put("task-2", "Load configuration");
            tasks.put("task-3", "Start server");
            
            // 执行任务(按顺序)
            while (!tasks.isEmpty()) {
                Map.Entry<String, String> task = tasks.pollFirstEntry();
                System.out.println("Executing: " + task.getKey() + " - " + task.getValue());
            }
        }
    }

    /**
     * 反向视图的高级用法
     */
    public static class ReversedViewAdvanced {
        
        public void modifyThroughReversedView() {
            List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
            List<Integer> reversed = numbers.reversed();
            
            System.out.println("Original: " + numbers);  // [1, 2, 3, 4, 5]
            System.out.println("Reversed: " + reversed); // [5, 4, 3, 2, 1]
            
            // 修改反向视图
            reversed.set(0, 99);
            System.out.println("After modify reversed:");
            System.out.println("Original: " + numbers);  // [1, 2, 3, 4, 99]
            System.out.println("Reversed: " + reversed); // [99, 4, 3, 2, 1]
            
            // 双重反向 = 原顺序
            List<Integer> doubleReversed = reversed.reversed();
            System.out.println("Double reversed: " + doubleReversed); // [1, 2, 3, 4, 99]
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Sequenced Collections Demo ===");
        
        SequencedCollectionBasics scb = new SequencedCollectionBasics();
        scb.sequencedCollectionMethods();
        
        SequencedSetDemo ssd = new SequencedSetDemo();
        ssd.linkedHashSetExample();
        ssd.treeSetExample();
        
        SequencedMapDemo smd = new SequencedMapDemo();
        smd.linkedHashMapExample();
        
        RealWorldUsage rwu = new RealWorldUsage();
        rwu.lruCacheDemo();
    }
}
