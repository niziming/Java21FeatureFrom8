package com.demo.collections;

import com.demo.annotations.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 集合框架增强演示
 * 展示Java 8到Java 21集合框架的新特性和改进
 */
public class CollectionsDemo {

    public static void main(String[] args) {
        demonstrateConvenienceMethods();
    }

    /**
     * 不可变集合演示 (Java 9特性)
     * List.of(), Set.of(), Map.of()等工厂方法
     */
    @Java9Feature(value = "不可变集合工厂方法", desc = "List.of(), Set.of(), Map.of()等便捷的不可变集合创建方法")
    public static void demonstrateImmutableCollections() {
        System.out.println("=== 可变集合演示 ===");
        ArrayList<String> arrs = new ArrayList<>();
        arrs.add("Java");
        arrs.add("Python");
        arrs.add("JavaScript");
        arrs.add("Go");
        System.out.println("arrs = " + arrs);


        System.out.println("=== 不可变集合演示 ===");
        
        // Java 9之前的方式
        List<String> oldWayList = Arrays.asList("Java", "Python", "JavaScript");
        System.out.println("传统方式创建列表: " + oldWayList);
        
        // Java 9的不可变集合
        List<String> immutableList = List.of("Java", "Python", "JavaScript", "Go");
        Set<String> immutableSet = Set.of("苹果", "香蕉", "橙子");
        Map<String, Integer> immutableMap = Map.of(
            "Java", 1995,
            "Python", 1991,
            "JavaScript", 1995
        );


        try {
            System.out.println("传统方式添加元素: " + oldWayList);
            oldWayList.add("Kotlin");
        } catch (UnsupportedOperationException e) {
            System.out.println("传统方式创建的列表不能修改: " + e.getMessage());
        }

        // 尝试修改不可变集合（会抛出异常）

        System.out.println("不可变列表: " + immutableList);
        System.out.println("不可变集合: " + immutableSet);
        System.out.println("不可变映射: " + immutableMap);
        System.out.println("传统方式创建列表: " + oldWayList);

        try {
            immutableList.add("Kotlin");
            immutableMap.put("Kotlin", 2011);
            immutableSet.add("Kotlin");
        } catch (UnsupportedOperationException e) {
            System.out.println("不可变集合不能修改: " + e.getMessage());
        }
        
        // 更大的不可变映射
        Map<String, String> largerMap = Map.of(
            "北京", "中国首都",
            "上海", "经济中心",
            "深圳", "科技中心",
            "杭州", "电商中心",
            "成都", "西南中心"
        );
        System.out.println("城市映射: " + largerMap);
    }

    /**
     * 集合便利方法演示 (Java 8特性)
     * forEach, removeIf, replaceAll等方法
     */
    @Java8Feature(value = "集合便利方法", desc = "forEach, removeIf, replaceAll等便利的集合操作方法")
    public static void demonstrateConvenienceMethods() {
        System.out.println("\n=== 集合便利方法演示 ===");
        
        // 创建可变集合
        List<String> fruits = new ArrayList<>(Arrays.asList("苹果", "香蕉", "橙子", "葡萄", "西瓜"));
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        
        // forEach方法
        System.out.println("使用forEach遍历水果:");
        fruits.forEach(fruit -> System.out.print(fruit + " "));
        // :: 方法引用方式
        fruits.forEach(System.out::print);
        System.out.println();

        // removeIf方法
        numbers.removeIf(n -> n % 2 == 0); // 移除偶数
        System.out.println("移除偶数后的数字: " + numbers);
        
        // replaceAll方法
        List<String> words = new ArrayList<>(Arrays.asList("java", "python", "javascript"));
        words.replaceAll(String::toUpperCase);
        System.out.println("转换为大写: " + words);
        words.replaceAll(String::toLowerCase);
        System.out.println("转换为小写: " + words);

        // sort方法
        List<String> languages = new ArrayList<>(Arrays.asList("Java", "Python", "C++", "JavaScript", "Go"));
        languages.sort(String::compareTo);
        System.out.println("排序后的语言: " + languages);
        
        // Map的便利方法
        Map<String, Integer> scoreMap = new HashMap<>();
        scoreMap.put("张三", 85);
        scoreMap.put("李四", 92);
        scoreMap.put("王五", 78);
        
        // forEach遍历Map
        System.out.println("学生成绩:");
        scoreMap.forEach((name, score) -> 
            System.out.println(name + ": " + score + "分"));
        
        // merge方法
        scoreMap.merge("张三", 5, Integer::sum); // 给张三加5分
        System.out.println("张三加分后: " + scoreMap.get("张三"));
        
        // compute方法
        scoreMap.compute("李四", (name, score) -> score != null ? score + 3 : 0);
        System.out.println("李四调整后: " + scoreMap.get("李四"));
    }

    /**
     * ConcurrentHashMap增强演示 (Java 8特性)
     * 新的并发安全方法
     */
    @Java8Feature(value = "ConcurrentHashMap增强", desc = "新增并发安全的批量操作方法")
    public void demonstrateConcurrentHashMapEnhancements() {
        System.out.println("\n=== ConcurrentHashMap增强演示 ===");
        
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put("商品A", 100);
        concurrentMap.put("商品B", 200);
        concurrentMap.put("商品C", 150);
        concurrentMap.put("商品D", 300);
        
        // forEach并行处理
        System.out.println("并行遍历商品库存:");
        concurrentMap.forEach(1, (product, stock) -> 
            System.out.println(product + " 库存: " + stock));
        
        // search方法
        String expensiveProduct = concurrentMap.search(1, (key, value) -> 
            value > 250 ? key : null);
        System.out.println("找到高价商品: " + expensiveProduct);
        
        // reduce方法
        Integer totalStock = concurrentMap.reduce(1,
            (key, value) -> value,
            (v1, v2) -> v1 + v2);
        System.out.println("总库存: " + totalStock);
        
        // reduceKeys
        String allProducts = concurrentMap.reduceKeys(1, 
            (k1, k2) -> k1 + ", " + k2);
        System.out.println("所有商品: " + allProducts);
    }

    /**
     * 集合工厂方法增强演示 (Java 11特性)
     * toArray()方法的改进
     */
    @Java11Feature(value = "toArray方法增强", desc = "Collection接口新增toArray(IntFunction)方法，简化数组转换")
    public void demonstrateToArrayEnhancement() {
        System.out.println("\n=== toArray方法增强演示 ===");
        
        List<String> programmingLanguages = List.of("Java", "Python", "JavaScript", "C++", "Go");
        
        // Java 11之前的方式
        String[] oldWay = programmingLanguages.toArray(new String[0]);
        System.out.println("传统方式转数组: " + Arrays.toString(oldWay));
        
        // Java 11的新方式
        String[] newWay = programmingLanguages.toArray(String[]::new);
        System.out.println("新方式转数组: " + Arrays.toString(newWay));
        
        // 数字列表示例
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Integer[] numberArray = numbers.toArray(Integer[]::new);
        System.out.println("数字数组: " + Arrays.toString(numberArray));
    }

    /**
     * Stream集合操作演示 (Java 8特性)
     * 展示集合与Stream的结合使用
     */
    @Java8Feature(value = "Stream集合操作", desc = "集合与Stream API的结合，提供函数式数据处理能力")
    public void demonstrateStreamCollectionOperations() {
        System.out.println("\n=== Stream集合操作演示 ===");
        
        // 示例数据
        List<Employee> employees = Arrays.asList(
            new Employee("张三", 28, "开发部", 8000),
            new Employee("李四", 32, "测试部", 7000),
            new Employee("王五", 25, "开发部", 9000),
            new Employee("赵六", 30, "产品部", 7500),
            new Employee("钱七", 35, "开发部", 12000)
        );
        
        // 按部门分组
        Map<String, List<Employee>> byDepartment = employees.stream()
            .collect(java.util.stream.Collectors.groupingBy(Employee::getDepartment));
        
        System.out.println("按部门分组:");
        byDepartment.forEach((dept, empList) -> {
            System.out.println(dept + ": " + empList.size() + "人");
            empList.forEach(emp -> System.out.println("  - " + emp.getName()));
        });
        
        // 计算各部门平均工资
        Map<String, Double> avgSalaryByDept = employees.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Employee::getDepartment,
                java.util.stream.Collectors.averagingDouble(Employee::getSalary)
            ));
        
        System.out.println("\n各部门平均工资:");
        avgSalaryByDept.forEach((dept, avgSalary) -> 
            System.out.printf("%s: %.2f元\n", dept, avgSalary));
        
        // 找出高薪员工
        List<String> highSalaryEmployees = employees.stream()
            .filter(emp -> emp.getSalary() > 8000)
            .map(Employee::getName)
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        
        System.out.println("\n高薪员工(>8000): " + highSalaryEmployees);
    }

    /**
     * 集合比较和排序演示 (Java 8特性)
     * Comparator接口的增强
     */
    @Java8Feature(value = "Comparator增强", desc = "Comparator接口新增多种便利的比较和排序方法")
    public void demonstrateComparatorEnhancements() {
        System.out.println("\n=== Comparator增强演示 ===");
        
        List<Employee> employees = Arrays.asList(
            new Employee("张三", 28, "开发部", 8000),
            new Employee("李四", 32, "测试部", 7000),
            new Employee("王五", 25, "开发部", 9000),
            new Employee("赵六", 30, "产品部", 7500)
        );
        
        // 按年龄排序
        List<Employee> sortedByAge = new ArrayList<>(employees);
        sortedByAge.sort(Comparator.comparing(Employee::getAge));
        System.out.println("按年龄排序:");
        sortedByAge.forEach(emp -> System.out.println("  " + emp.getName() + " - " + emp.getAge() + "岁"));
        
        // 按工资倒序排序
        List<Employee> sortedBySalaryDesc = new ArrayList<>(employees);
        sortedBySalaryDesc.sort(Comparator.comparing(Employee::getSalary).reversed());
        System.out.println("\n按工资倒序排序:");
        sortedBySalaryDesc.forEach(emp -> System.out.println("  " + emp.getName() + " - " + emp.getSalary() + "元"));
        
        // 多条件排序：先按部门，再按工资
        List<Employee> multiSort = new ArrayList<>(employees);
        multiSort.sort(Comparator.comparing(Employee::getDepartment)
                .thenComparing(Employee::getSalary, Comparator.reverseOrder()));
        System.out.println("\n按部门和工资排序:");
        multiSort.forEach(emp -> System.out.println("  " + emp.getDepartment() + " - " + emp.getName() + " - " + emp.getSalary() + "元"));
        
        // 空值处理
        List<String> namesWithNull = Arrays.asList("张三", null, "李四", "王五", null);
        List<String> sortedNames = new ArrayList<>(namesWithNull);
        sortedNames.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println("\n处理空值的排序: " + sortedNames);
    }

    /**
     * 员工类，用于演示
     */
    public static class Employee {
        private String name;
        private int age;
        private String department;
        private double salary;
        
        public Employee(String name, int age, String department, double salary) {
            this.name = name;
            this.age = age;
            this.department = department;
            this.salary = salary;
        }
        
        // Getters
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getDepartment() { return department; }
        public double getSalary() { return salary; }
        
        @Override
        public String toString() {
            return String.format("Employee{name='%s', age=%d, department='%s', salary=%.2f}", 
                               name, age, department, salary);
        }
    }

    /**
     * 运行所有集合框架演示
     */
    public void runAllDemos() {
        System.out.println("开始集合框架增强演示...\n");
        
        demonstrateImmutableCollections();
        demonstrateConvenienceMethods();
        demonstrateConcurrentHashMapEnhancements();
        demonstrateToArrayEnhancement();
        demonstrateStreamCollectionOperations();
        demonstrateComparatorEnhancements();
        
        System.out.println("\n集合框架增强演示完成！");
    }
}