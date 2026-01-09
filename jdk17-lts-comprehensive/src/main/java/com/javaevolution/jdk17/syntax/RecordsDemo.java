package com.javaevolution.jdk17.syntax;

/**
 * Records 完整演示
 * JEP 395: Records (JDK 16 - Final)
 * JEP 384: Records (JDK 14 - Preview)
 * JEP 359: Records (JDK 14 - Preview)
 */
public class RecordsDemo {

    /**
     * 基础 Record 定义
     */
    // 简单 Record
    public record Point(int x, int y) {}
    
    // 带多个字段的 Record
    public record Person(String name, int age, String email) {}
    
    // Record 自动生成:
    // 1. private final fields
    // 2. public constructor
    // 3. public getters (x(), y())
    // 4. equals()
    // 5. hashCode()
    // 6. toString()
    
    public static class BasicUsage {
        public void basicRecord() {
            Point p = new Point(10, 20);
            
            System.out.println(p.x());      // 10
            System.out.println(p.y());      // 20
            System.out.println(p);          // Point[x=10, y=20]
            
            // 自动生成的 equals 和 hashCode
            Point p2 = new Point(10, 20);
            System.out.println(p.equals(p2)); // true
            System.out.println(p.hashCode() == p2.hashCode()); // true
        }
    }

    /**
     * 紧凑构造器 (Compact Constructor)
     */
    public record Range(int min, int max) {
        // 紧凑构造器: 不需要参数列表
        public Range {
            if (min > max) {
                throw new IllegalArgumentException("min must be <= max");
            }
            // 自动分配 this.min = min; this.max = max;
        }
    }
    
    // 传统构造器写法
    public record RangeTraditional(int min, int max) {
        public RangeTraditional(int min, int max) {
            if (min > max) {
                throw new IllegalArgumentException("min must be <= max");
            }
            this.min = min;
            this.max = max;
        }
    }

    /**
     * 自定义构造器与方法
     */
    public record Employee(String name, int age, double salary) {
        
        // 紧凑构造器: 数据验证
        public Employee {
            if (age < 18) {
                throw new IllegalArgumentException("Age must be >= 18");
            }
            if (salary < 0) {
                throw new IllegalArgumentException("Salary must be positive");
            }
            // 数据规范化
            name = name.trim().toUpperCase();
        }
        
        // 额外的构造器 (必须委托给主构造器)
        public Employee(String name) {
            this(name, 18, 0.0);
        }
        
        // 自定义方法
        public boolean isAdult() {
            return age >= 18;
        }
        
        public double annualSalary() {
            return salary * 12;
        }
        
        // 静态方法
        public static Employee createIntern(String name) {
            return new Employee(name, 18, 3000.0);
        }
    }

    /**
     * Record 实现接口
     */
    public interface Drawable {
        void draw();
    }
    
    public record Circle(double radius) implements Drawable {
        @Override
        public void draw() {
            System.out.println("Drawing circle with radius: " + radius);
        }
        
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    /**
     * 嵌套 Record
     */
    public record Address(String street, String city, String zipCode) {}
    
    public record Customer(String name, Address address) {
        // Record 可以包含其他 Record
    }

    /**
     * 泛型 Record
     */
    public record Pair<T, U>(T first, U second) {
        public void print() {
            System.out.println("(" + first + ", " + second + ")");
        }
    }

    /**
     * Record 与模式匹配 (JDK 21)
     */
    public static class RecordPatternMatching {
        
        public record Point3D(int x, int y, int z) {}
        
        public void patternMatching(Object obj) {
            // JDK 17+: instanceof pattern matching
            if (obj instanceof Point(int x, int y)) {
                System.out.println("Point: " + x + ", " + y);
            }
            
            // JDK 21+: Record patterns in switch (需要 JDK 21)
            // String result = switch (obj) {
            //     case Point(int x, int y) -> "2D Point: " + x + "," + y;
            //     case Point3D(int x, int y, int z) -> "3D Point: " + x + "," + y + "," + z;
            //     default -> "Unknown";
            // };
        }
    }

    /**
     * Record 的限制
     */
    public static class Limitations {
        
        // ❌ Record 不能继承其他类 (隐式继承 java.lang.Record)
        // public record BadRecord(int x) extends SomeClass {}
        
        // ❌ Record 不能声明实例字段 (只能有组件)
        // public record BadRecord(int x) {
        //     private int y; // 编译错误
        // }
        
        // ✅ 可以声明静态字段
        public record ValidRecord(int x) {
            private static int count = 0;
        }
        
        // ❌ Record 是隐式 final 的,不能被继承
        // public class BadClass extends Point {}
        
        // ✅ Record 可以有静态嵌套类
        public record OuterRecord(int value) {
            public static class NestedClass {}
        }
    }

    /**
     * Record vs Class 对比
     */
    public static class RecordVsClass {
        
        // 传统 Class 方式
        public static final class PointClass {
            private final int x;
            private final int y;
            
            public PointClass(int x, int y) {
                this.x = x;
                this.y = y;
            }
            
            public int getX() { return x; }
            public int getY() { return y; }
            
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof PointClass)) return false;
                PointClass other = (PointClass) obj;
                return x == other.x && y == other.y;
            }
            
            @Override
            public int hashCode() {
                return java.util.Objects.hash(x, y);
            }
            
            @Override
            public String toString() {
                return "PointClass[x=" + x + ", y=" + y + "]";
            }
        }
        
        // Record 方式: 简洁!
        public record PointRecord(int x, int y) {}
    }

    /**
     * 实际应用场景
     */
    public static class RealWorldUsage {
        
        // 1. DTO (Data Transfer Object)
        public record UserDTO(Long id, String username, String email) {}
        
        // 2. API 响应
        public record ApiResponse(int code, String message, Object data) {}
        
        // 3. 配置类
        public record DatabaseConfig(
            String host,
            int port,
            String username,
            String password,
            int maxConnections
        ) {}
        
        // 4. 值对象 (Value Object)
        public record Money(double amount, String currency) {
            public Money add(Money other) {
                if (!currency.equals(other.currency)) {
                    throw new IllegalArgumentException("Currency mismatch");
                }
                return new Money(amount + other.amount, currency);
            }
        }
        
        // 5. 多返回值
        public record SearchResult(int totalCount, java.util.List<String> items) {}
        
        public SearchResult search(String keyword) {
            var items = java.util.List.of("result1", "result2");
            return new SearchResult(items.size(), items);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Records Demo ===");
        
        BasicUsage bu = new BasicUsage();
        bu.basicRecord();
        
        Range range = new Range(1, 10);
        System.out.println(range);
        
        Employee emp = new Employee("john doe", 25, 5000);
        System.out.println(emp);
        System.out.println("Annual: " + emp.annualSalary());
        
        Pair<String, Integer> pair = new Pair<>("Java", 17);
        pair.print();
    }
}
