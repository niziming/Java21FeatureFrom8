package com.demo.records;

import com.demo.annotations.*;
import java.util.*;
import java.time.LocalDate;

/**
 * Records特性演示
 * 展示Java 17正式引入的Record类特性
 */
public class RecordsDemo {

    /**
     * 基础Record演示 (Java 17特性)
     * Record提供简洁的数据载体类定义
     */
    @Java17Feature(value = "Record类", description = "简洁的不可变数据载体类，自动生成构造器、getter、equals、hashCode和toString")
    public void demonstrateBasicRecords() {
        System.out.println("=== 基础Record演示 ===");
        
        // 创建Person记录
        Person person1 = new Person("张三", 25, "北京");
        Person person2 = new Person("李四", 30, "上海");
        Person person3 = new Person("张三", 25, "北京"); // 与person1相同
        
        System.out.println("Person记录:");
        System.out.println("  " + person1);
        System.out.println("  " + person2);
        
        // 自动生成的getter方法
        System.out.println("\n访问Record字段:");
        System.out.println("  姓名: " + person1.name());
        System.out.println("  年龄: " + person1.age());
        System.out.println("  城市: " + person1.city());
        
        // 自动生成的equals和hashCode
        System.out.println("\n相等性比较:");
        System.out.println("  person1.equals(person2): " + person1.equals(person2));
        System.out.println("  person1.equals(person3): " + person1.equals(person3));
        System.out.println("  person1.hashCode(): " + person1.hashCode());
        System.out.println("  person3.hashCode(): " + person3.hashCode());
        
        // 在集合中使用
        Set<Person> personSet = new HashSet<>();
        personSet.add(person1);
        personSet.add(person2);
        personSet.add(person3); // 重复的person1，不会被添加
        
        System.out.println("\nSet中的Person数量: " + personSet.size());
        System.out.println("Set内容: " + personSet);
    }

    /**
     * 复杂Record演示
     * 展示Record的高级用法
     */
    @Java17Feature(value = "复杂Record用法", description = "Record支持泛型、嵌套、验证等高级特性")
    public void demonstrateComplexRecords() {
        System.out.println("\n=== 复杂Record演示 ===");
        
        // 创建带验证的学生记录
        try {
            Student student1 = new Student("王五", 20, 85.5, "计算机科学");
            System.out.println("学生信息: " + student1);
            
            // 尝试创建无效学生（年龄小于0）
            Student invalidStudent = new Student("无效", -1, 90.0, "数学");
        } catch (IllegalArgumentException e) {
            System.out.println("创建无效学生失败: " + e.getMessage());
        }
        
        // 使用泛型Record
        GenericContainer<String> stringContainer = new GenericContainer<>("Java编程", "这是一个字符串容器");
        GenericContainer<Integer> intContainer = new GenericContainer<>(42, "这是一个整数容器");
        
        System.out.println("\n泛型Record:");
        System.out.println("  字符串容器: " + stringContainer);
        System.out.println("  整数容器: " + intContainer);
        
        // 嵌套Record
        Address address = new Address("朝阳区", "北京", "100000");
        Employee employee = new Employee("赵六", address, LocalDate.of(2020, 1, 15));
        
        System.out.println("\n嵌套Record:");
        System.out.println("  员工信息: " + employee);
        System.out.println("  员工城市: " + employee.address().city());
        
        // Record在业务逻辑中的应用
        Order order = new Order("ORD001", List.of(
            new OrderItem("笔记本电脑", 5999.0, 1),
            new OrderItem("鼠标", 99.0, 2)
        ));
        
        double totalAmount = calculateOrderTotal(order);
        System.out.println("\n订单信息: " + order);
        System.out.println("订单总金额: " + totalAmount + "元");
    }

    /**
     * Record与传统类对比演示
     */
    @Java17Feature(value = "Record vs 传统类", description = "对比Record与传统类的代码简洁性和功能")
    public void demonstrateRecordVsTraditionalClass() {
        System.out.println("\n=== Record vs 传统类对比 ===");
        
        // 使用Record
        Point recordPoint = new Point(10, 20);
        System.out.println("Record Point: " + recordPoint);
        
        // 使用传统类
        TraditionalPoint traditionalPoint = new TraditionalPoint(10, 20);
        System.out.println("Traditional Point: " + traditionalPoint);
        
        // 比较代码行数和复杂度
        System.out.println("\n代码复杂度对比:");
        System.out.println("  Record Point: 1行代码定义完整功能");
        System.out.println("  Traditional Point: 需要约20-30行代码实现相同功能");
        
        // 性能比较（创建和比较）
        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            Point p = new Point(i, i);
            p.equals(recordPoint);
        }
        long recordTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            TraditionalPoint p = new TraditionalPoint(i, i);
            p.equals(traditionalPoint);
        }
        long traditionalTime = System.nanoTime() - startTime;
        
        System.out.println("\n性能对比 (100,000次操作):");
        System.out.println("  Record: " + recordTime / 1_000_000 + "ms");
        System.out.println("  Traditional: " + traditionalTime / 1_000_000 + "ms");
    }

    /**
     * Record在函数式编程中的应用
     */
    @Java17Feature(value = "Record函数式编程", description = "Record与Stream、Optional等函数式编程的结合")
    public void demonstrateRecordWithFunctionalProgramming() {
        System.out.println("\n=== Record与函数式编程结合 ===");
        
        List<Product> products = List.of(
            new Product("iPhone 15", "电子产品", 7999.0, true),
            new Product("MacBook Pro", "电子产品", 15999.0, true),
            new Product("AirPods", "电子产品", 1299.0, false),
            new Product("iPad", "电子产品", 3999.0, true),
            new Product("Apple Watch", "电子产品", 2999.0, false)
        );
        
        // 使用Stream处理Record
        System.out.println("所有产品:");
        products.forEach(System.out::println);
        
        // 筛选在售产品
        List<Product> availableProducts = products.stream()
            .filter(Product::available)
            .toList();
        System.out.println("\n在售产品: " + availableProducts.size() + "个");
        
        // 按价格分组
        Map<String, List<Product>> productsByPriceRange = products.stream()
            .collect(java.util.stream.Collectors.groupingBy(p -> 
                p.price() > 5000 ? "高端" : "中端"));
        
        System.out.println("\n按价格分组:");
        productsByPriceRange.forEach((range, productList) -> {
            System.out.println("  " + range + ": " + productList.size() + "个产品");
        });
        
        // 计算总价值
        double totalValue = products.stream()
            .filter(Product::available)
            .mapToDouble(Product::price)
            .sum();
        System.out.println("\n在售产品总价值: " + totalValue + "元");
        
        // 查找最贵的产品
        Optional<Product> mostExpensive = products.stream()
            .max(Comparator.comparing(Product::price));
        
        mostExpensive.ifPresent(product -> 
            System.out.println("最贵产品: " + product.name() + ", 价格: " + product.price() + "元"));
    }

    /**
     * 计算订单总金额
     */
    private double calculateOrderTotal(Order order) {
        return order.items().stream()
            .mapToDouble(item -> item.price() * item.quantity())
            .sum();
    }

    // =============== Record定义 ===============

    /**
     * 基础Person记录
     */
    public record Person(String name, int age, String city) {}

    /**
     * 带验证的学生记录
     */
    public record Student(String name, int age, double score, String major) {
        // 紧凑构造器 - 用于验证
        public Student {
            if (age < 0) {
                throw new IllegalArgumentException("年龄不能为负数");
            }
            if (score < 0 || score > 100) {
                throw new IllegalArgumentException("分数必须在0-100之间");
            }
            Objects.requireNonNull(name, "姓名不能为空");
            Objects.requireNonNull(major, "专业不能为空");
        }
        
        // 自定义方法
        public String getGrade() {
            if (score >= 90) return "优秀";
            if (score >= 80) return "良好";
            if (score >= 70) return "中等";
            if (score >= 60) return "及格";
            return "不及格";
        }
    }

    /**
     * 泛型Record
     */
    public record GenericContainer<T>(T value, String description) {}

    /**
     * 地址记录
     */
    public record Address(String district, String city, String zipCode) {}

    /**
     * 员工记录（嵌套Record）
     */
    public record Employee(String name, Address address, LocalDate hireDate) {}

    /**
     * 订单项记录
     */
    public record OrderItem(String name, double price, int quantity) {}

    /**
     * 订单记录
     */
    public record Order(String orderId, List<OrderItem> items) {}

    /**
     * 产品记录
     */
    public record Product(String name, String category, double price, boolean available) {}

    /**
     * Point记录（用于对比）
     */
    public record Point(int x, int y) {}

    /**
     * 传统Point类（用于对比）
     */
    public static class TraditionalPoint {
        private final int x;
        private final int y;
        
        public TraditionalPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TraditionalPoint that = (TraditionalPoint) o;
            return x == that.x && y == that.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
        
        @Override
        public String toString() {
            return "TraditionalPoint{x=" + x + ", y=" + y + '}';
        }
    }

    /**
     * 运行所有Record演示
     */
    public void runAllDemos() {
        System.out.println("开始Record特性演示...\n");
        
        demonstrateBasicRecords();
        demonstrateComplexRecords();
        demonstrateRecordVsTraditionalClass();
        demonstrateRecordWithFunctionalProgramming();
        
        System.out.println("\nRecord特性演示完成！");
    }
}