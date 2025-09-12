package com.demo.sealed;

import com.demo.annotations.*;

/**
 * 密封类演示
 * 展示Java 17正式版的密封类特性
 */
public class SealedClassesDemo {

    /**
     * 基础密封类演示 (Java 17特性)
     * 密封类限制继承层次结构
     */
    @Java17Feature(value = "密封类", desc = "限制类的继承层次，只允许指定的类进行继承")
    public void demonstrateBasicSealedClasses() {
        System.out.println("=== 基础密封类演示 ===");
        
        // 创建不同类型的形状
        Shape circle = new Circle(5.0);
        Shape rectangle = new Rectangle(4.0, 6.0);
        Shape triangle = new Triangle(3.0, 4.0, 5.0);
        
        // 使用密封类的优势 - 完备的模式匹配
        System.out.println("形状信息:");
        System.out.println("  " + getShapeInfo(circle));
        System.out.println("  " + getShapeInfo(rectangle));
        System.out.println("  " + getShapeInfo(triangle));
        
        // 计算面积
        System.out.println("\n形状面积:");
        System.out.println("  圆形面积: " + calculateArea(circle));
        System.out.println("  矩形面积: " + calculateArea(rectangle));
        System.out.println("  三角形面积: " + calculateArea(triangle));
    }

    /**
     * 获取形状信息（利用密封类的完备性）
     */
    private String getShapeInfo(Shape shape) {
        // 由于Shape是密封类，编译器知道所有可能的子类
        return switch (shape) {
            case Circle c -> String.format("圆形 - 半径: %.2f", c.radius());
            case Rectangle r -> String.format("矩形 - 长: %.2f, 宽: %.2f", r.length(), r.width());
            case Triangle t -> String.format("三角形 - 边长: %.2f, %.2f, %.2f", t.a(), t.b(), t.c());
            // 不需要default分支，因为所有情况都已覆盖
        };
    }

    /**
     * 计算形状面积
     */
    private double calculateArea(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.length() * r.width();
            case Triangle t -> {
                // 海伦公式计算三角形面积
                double s = (t.a() + t.b() + t.c()) / 2;
                yield Math.sqrt(s * (s - t.a()) * (s - t.b()) * (s - t.c()));
            }
        };
    }

    /**
     * 密封接口演示 (Java 17特性)
     * 密封接口限制实现类
     */
    @Java17Feature(value = "密封接口", desc = "限制接口的实现类，提供更好的类型安全")
    public void demonstrateSealedInterfaces() {
        System.out.println("\n=== 密封接口演示 ===");
        
        // 创建不同类型的表达式
        Expression number = new NumberExpression(42);
        Expression addition = new AdditionExpression(new NumberExpression(10), new NumberExpression(20));
        Expression multiplication = new MultiplicationExpression(new NumberExpression(6), new NumberExpression(7));
        
        // 评估表达式
        System.out.println("表达式评估:");
        System.out.println("  数字表达式: " + evaluate(number));
        System.out.println("  加法表达式: " + evaluate(addition));
        System.out.println("  乘法表达式: " + evaluate(multiplication));
        
        // 转换为字符串表示
        System.out.println("\n表达式字符串:");
        System.out.println("  数字表达式: " + toString(number));
        System.out.println("  加法表达式: " + toString(addition));
        System.out.println("  乘法表达式: " + toString(multiplication));
    }

    /**
     * 评估表达式
     */
    private int evaluate(Expression expr) {
        return switch (expr) {
            case NumberExpression n -> n.value();
            case AdditionExpression a -> evaluate(a.left()) + evaluate(a.right());
            case MultiplicationExpression m -> evaluate(m.left()) * evaluate(m.right());
        };
    }

    /**
     * 表达式转字符串
     */
    private String toString(Expression expr) {
        return switch (expr) {
            case NumberExpression n -> String.valueOf(n.value());
            case AdditionExpression a -> "(" + toString(a.left()) + " + " + toString(a.right()) + ")";
            case MultiplicationExpression m -> "(" + toString(m.left()) + " * " + toString(m.right()) + ")";
        };
    }

    /**
     * 复杂密封类层次演示
     * 展示多层密封类结构
     */
    @Java17Feature(value = "复杂密封类层次", desc = "展示多层密封类结构和组合使用")
    public void demonstrateComplexSealedHierarchy() {
        System.out.println("\n=== 复杂密封类层次演示 ===");
        
        // 创建不同类型的车辆
        Vehicle car = new Car("丰田", "凯美瑞", 4);
        Vehicle motorcycle = new Motorcycle("哈雷", "戴维森", true);
        Vehicle truck = new Truck("沃尔沃", "FH16", 25.0);
        Vehicle electricCar = new ElectricCar("特斯拉", "Model 3", 4, 75.0);
        
        Vehicle[] vehicles = {car, motorcycle, truck, electricCar};
        
        System.out.println("车辆信息:");
        for (Vehicle vehicle : vehicles) {
            System.out.println("  " + getVehicledesc(vehicle));
        }
        
        System.out.println("\n车辆分类:");
        for (Vehicle vehicle : vehicles) {
            System.out.println("  " + classifyVehicle(vehicle));
        }
    }

    /**
     * 获取车辆描述
     */
    private String getVehicledesc(Vehicle vehicle) {
        return switch (vehicle) {
            case Car c -> String.format("汽车: %s %s，%d个座位", c.brand(), c.model(), c.seats());
            case Motorcycle m -> String.format("摩托车: %s %s，%s边车", 
                m.brand(), m.model(), m.hasSidecar() ? "有" : "无");
            case Truck t -> String.format("卡车: %s %s，载重: %.1f吨", t.brand(), t.model(), t.capacity());
            case ElectricCar e -> String.format("电动汽车: %s %s，%d个座位，电池: %.1fkWh", 
                e.brand(), e.model(), e.seats(), e.batteryCapacity());
        };
    }

    /**
     * 车辆分类
     */
    private String classifyVehicle(Vehicle vehicle) {
        return switch (vehicle) {
            case PassengerVehicle p -> "客运车辆 - " + getPassengerVehicleType(p);
            case Truck t -> "货运车辆 - 载重: " + t.capacity() + "吨";
        };
    }

    /**
     * 获取客运车辆类型
     */
    private String getPassengerVehicleType(PassengerVehicle vehicle) {
        return switch (vehicle) {
            case Car c -> "汽车，座位数: " + c.seats();
            case Motorcycle m -> "摩托车" + (m.hasSidecar() ? "（带边车）" : "");
            case ElectricCar e -> "电动汽车，座位数: " + e.seats() + "，电池: " + e.batteryCapacity() + "kWh";
        };
    }

    /**
     * 密封类与Record结合演示
     */
    @Java17Feature(value = "密封类与Record结合", desc = "密封类与Record的结合使用，创建类型安全的数据结构")
    public void demonstrateSealedWithRecords() {
        System.out.println("\n=== 密封类与Record结合演示 ===");
        
        // 创建不同类型的响应
        ApiResponse success = new SuccessResponse("操作成功", new User("张三", "zhang@example.com"));
        ApiResponse error = new ErrorResponse("用户不存在", 404);
        ApiResponse loading = new LoadingResponse("正在处理请求...");
        
        ApiResponse[] responses = {success, error, loading};
        
        System.out.println("API响应处理:");
        for (ApiResponse response : responses) {
            handleResponse(response);
        }
    }

    /**
     * 处理API响应
     */
    private void handleResponse(ApiResponse response) {
        switch (response) {
            case SuccessResponse s -> {
                System.out.println("  成功: " + s.message());
                System.out.println("    用户: " + s.data().name() + " (" + s.data().email() + ")");
            }
            case ErrorResponse e -> {
                System.out.println("  错误: " + e.message() + " (状态码: " + e.statusCode() + ")");
            }
            case LoadingResponse l -> {
                System.out.println("  加载中: " + l.message());
            }
        }
    }

    // =============== 密封类定义 ===============

    /**
     * 密封类 - 形状
     */
    public static sealed abstract class Shape
            permits Circle, Rectangle, Triangle {
        public abstract double area();
    }

    /**
     * 圆形
     */
    public static final class Circle extends Shape {
        private final double radius;
        
        public Circle(double radius) {
            this.radius = radius;
        }
        
        public double radius() { return radius; }
        
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    /**
     * 矩形
     */
    public static final class Rectangle extends Shape {
        private final double length;
        private final double width;
        
        public Rectangle(double length, double width) {
            this.length = length;
            this.width = width;
        }
        
        public double length() { return length; }
        public double width() { return width; }
        
        @Override
        public double area() {
            return length * width;
        }
    }

    /**
     * 三角形
     */
    public static final class Triangle extends Shape {
        private final double a, b, c;
        
        public Triangle(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        
        public double a() { return a; }
        public double b() { return b; }
        public double c() { return c; }
        
        @Override
        public double area() {
            double s = (a + b + c) / 2;
            return Math.sqrt(s * (s - a) * (s - b) * (s - c));
        }
    }

    // =============== 密封接口定义 ===============

    /**
     * 密封接口 - 表达式
     */
    public sealed interface Expression
            permits NumberExpression, AdditionExpression, MultiplicationExpression {}

    /**
     * 数字表达式
     */
    public record NumberExpression(int value) implements Expression {}

    /**
     * 加法表达式
     */
    public record AdditionExpression(Expression left, Expression right) implements Expression {}

    /**
     * 乘法表达式
     */
    public record MultiplicationExpression(Expression left, Expression right) implements Expression {}

    // =============== 复杂密封类层次 ===============

    /**
     * 车辆密封类
     */
    public static sealed abstract class Vehicle
            permits PassengerVehicle, Truck {
        protected final String brand;
        protected final String model;
        
        public Vehicle(String brand, String model) {
            this.brand = brand;
            this.model = model;
        }
        
        public String brand() { return brand; }
        public String model() { return model; }
    }

    /**
     * 客运车辆密封类
     */
    public static sealed abstract class PassengerVehicle extends Vehicle
            permits Car, Motorcycle, ElectricCar {
        
        public PassengerVehicle(String brand, String model) {
            super(brand, model);
        }
    }

    /**
     * 汽车
     */
    public static final class Car extends PassengerVehicle {
        private final int seats;
        
        public Car(String brand, String model, int seats) {
            super(brand, model);
            this.seats = seats;
        }
        
        public int seats() { return seats; }
    }

    /**
     * 摩托车
     */
    public static final class Motorcycle extends PassengerVehicle {
        private final boolean hasSidecar;
        
        public Motorcycle(String brand, String model, boolean hasSidecar) {
            super(brand, model);
            this.hasSidecar = hasSidecar;
        }
        
        public boolean hasSidecar() { return hasSidecar; }
    }

    /**
     * 电动汽车
     */
    public static final class ElectricCar extends PassengerVehicle {
        private final int seats;
        private final double batteryCapacity;
        
        public ElectricCar(String brand, String model, int seats, double batteryCapacity) {
            super(brand, model);
            this.seats = seats;
            this.batteryCapacity = batteryCapacity;
        }
        
        public int seats() { return seats; }
        public double batteryCapacity() { return batteryCapacity; }
    }

    /**
     * 卡车
     */
    public static final class Truck extends Vehicle {
        private final double capacity;
        
        public Truck(String brand, String model, double capacity) {
            super(brand, model);
            this.capacity = capacity;
        }
        
        public double capacity() { return capacity; }
    }

    // =============== 密封类与Record结合 ===============

    /**
     * API响应密封接口
     */
    public sealed interface ApiResponse
            permits SuccessResponse, ErrorResponse, LoadingResponse {}

    /**
     * 成功响应
     */
    public record SuccessResponse(String message, User data) implements ApiResponse {}

    /**
     * 错误响应
     */
    public record ErrorResponse(String message, int statusCode) implements ApiResponse {}

    /**
     * 加载响应
     */
    public record LoadingResponse(String message) implements ApiResponse {}

    /**
     * 用户记录
     */
    public record User(String name, String email) {}

    /**
     * 运行所有密封类演示
     */
    public void runAllDemos() {
        System.out.println("开始密封类演示...\n");
        
        demonstrateBasicSealedClasses();
        demonstrateSealedInterfaces();
        demonstrateComplexSealedHierarchy();
        demonstrateSealedWithRecords();
        
        System.out.println("\n密封类演示完成！");
    }
}