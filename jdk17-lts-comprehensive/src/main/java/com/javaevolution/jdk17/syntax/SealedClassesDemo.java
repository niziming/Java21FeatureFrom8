package com.javaevolution.jdk17.syntax;

/**
 * Sealed Classes 完整演示
 * JEP 409: Sealed Classes (JDK 17 - Final)
 * JEP 397: Sealed Classes (JDK 15 - Preview)
 */
public class SealedClassesDemo {

    /**
     * 基础 Sealed Class
     */
    // sealed 关键字: 限制谁可以继承/实现
    public sealed class Shape
        permits Circle, Rectangle, Triangle {
        
        public abstract double area();
    }
    
    // final: 不能再被继承
    public final class Circle extends Shape {
        private final double radius;
        
        public Circle(double radius) {
            this.radius = radius;
        }
        
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }
    
    // sealed: 可以继承,但必须指定 permits
    public sealed class Rectangle extends Shape
        permits Square {
        
        protected final double width;
        protected final double height;
        
        public Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }
        
        @Override
        public double area() {
            return width * height;
        }
    }
    
    // non-sealed: 打开继承,任何类都可以继承
    public non-sealed class Triangle extends Shape {
        private final double base;
        private final double height;
        
        public Triangle(double base, double height) {
            this.base = base;
            this.height = height;
        }
        
        @Override
        public double area() {
            return 0.5 * base * height;
        }
    }
    
    // Square 继承自 sealed Rectangle
    public final class Square extends Rectangle {
        public Square(double side) {
            super(side, side);
        }
    }
    
    // ❌ 编译错误: Pentagon 不在 permits 列表中
    // public class Pentagon extends Shape {}

    /**
     * Sealed Interface
     */
    public sealed interface Vehicle
        permits Car, Truck, Motorcycle {
        void start();
    }
    
    public final class Car implements Vehicle {
        @Override
        public void start() {
            System.out.println("Car starting...");
        }
    }
    
    public final class Truck implements Vehicle {
        @Override
        public void start() {
            System.out.println("Truck starting...");
        }
    }
    
    public non-sealed class Motorcycle implements Vehicle {
        @Override
        public void start() {
            System.out.println("Motorcycle starting...");
        }
    }
    
    // 可以继承 non-sealed 类
    public class SportMotorcycle extends Motorcycle {
        @Override
        public void start() {
            System.out.println("Sport motorcycle starting...");
        }
    }

    /**
     * 与 Pattern Matching 结合使用
     */
    public static class PatternMatchingIntegration {
        
        public sealed interface Result
            permits Success, Failure {}
        
        public record Success(String data) implements Result {}
        public record Failure(String error) implements Result {}
        
        public void processResult(Result result) {
            // 穷举性检查: 编译器知道所有可能的子类
            String message = switch (result) {
                case Success(String data) -> "Success: " + data;
                case Failure(String error) -> "Failure: " + error;
                // 不需要 default,因为已经穷举所有情况
            };
            
            System.out.println(message);
        }
    }

    /**
     * 表达式问题 (Expression Problem) 的解决
     */
    public static class ExpressionProblemSolution {
        
        // 表达式类型
        public sealed interface Expr
            permits Constant, Add, Multiply {}
        
        public record Constant(int value) implements Expr {}
        public record Add(Expr left, Expr right) implements Expr {}
        public record Multiply(Expr left, Expr right) implements Expr {}
        
        // 求值
        public int eval(Expr expr) {
            return switch (expr) {
                case Constant(int value) -> value;
                case Add(Expr left, Expr right) -> eval(left) + eval(right);
                case Multiply(Expr left, Expr right) -> eval(left) * eval(right);
            };
        }
        
        // 转为字符串
        public String toString(Expr expr) {
            return switch (expr) {
                case Constant(int value) -> String.valueOf(value);
                case Add(Expr left, Expr right) -> 
                    "(" + toString(left) + " + " + toString(right) + ")";
                case Multiply(Expr left, Expr right) -> 
                    "(" + toString(left) + " * " + toString(right) + ")";
            };
        }
        
        public void demo() {
            // (2 + 3) * 4
            Expr expr = new Multiply(
                new Add(new Constant(2), new Constant(3)),
                new Constant(4)
            );
            
            System.out.println("Expression: " + toString(expr));
            System.out.println("Result: " + eval(expr));
        }
    }

    /**
     * API 设计最佳实践
     */
    public static class APIDesignBestPractices {
        
        // 状态机建模
        public sealed interface ConnectionState
            permits Disconnected, Connecting, Connected, Disconnecting {}
        
        public record Disconnected() implements ConnectionState {}
        public record Connecting() implements ConnectionState {}
        public record Connected(String sessionId) implements ConnectionState {}
        public record Disconnecting() implements ConnectionState {}
        
        public void handleState(ConnectionState state) {
            switch (state) {
                case Disconnected() -> System.out.println("Not connected");
                case Connecting() -> System.out.println("Connecting...");
                case Connected(String sessionId) -> 
                    System.out.println("Connected: " + sessionId);
                case Disconnecting() -> System.out.println("Disconnecting...");
            }
        }
        
        // 错误处理
        public sealed interface ApiError
            permits ValidationError, AuthError, NetworkError {}
        
        public record ValidationError(String field, String message) 
            implements ApiError {}
        public record AuthError(String reason) 
            implements ApiError {}
        public record NetworkError(int code, String message) 
            implements ApiError {}
        
        public void handleError(ApiError error) {
            String message = switch (error) {
                case ValidationError(String field, String msg) -> 
                    "Validation failed on " + field + ": " + msg;
                case AuthError(String reason) -> 
                    "Authentication failed: " + reason;
                case NetworkError(int code, String msg) -> 
                    "Network error " + code + ": " + msg;
            };
            
            System.err.println(message);
        }
    }

    /**
     * Sealed Classes 的优势
     */
    public static class Advantages {
        
        // 1. 类型安全的枚举
        public sealed interface PaymentMethod
            permits CreditCard, DebitCard, PayPal, BankTransfer {}
        
        public record CreditCard(String number, String cvv) 
            implements PaymentMethod {}
        public record DebitCard(String number, String pin) 
            implements PaymentMethod {}
        public record PayPal(String email) 
            implements PaymentMethod {}
        public record BankTransfer(String accountNumber, String routingNumber) 
            implements PaymentMethod {}
        
        // 穷举性检查: 编译时保证不会遗漏
        public void processPayment(PaymentMethod method, double amount) {
            switch (method) {
                case CreditCard(String num, String cvv) -> 
                    System.out.println("Processing credit card: " + num);
                case DebitCard(String num, String pin) -> 
                    System.out.println("Processing debit card: " + num);
                case PayPal(String email) -> 
                    System.out.println("Processing PayPal: " + email);
                case BankTransfer(String acc, String routing) -> 
                    System.out.println("Processing bank transfer: " + acc);
                // 不需要 default!
            }
        }
    }

    /**
     * 限制与规则
     */
    public static class RestrictionsAndRules {
        
        // 1. sealed class 必须在同一模块或同一包中定义 permits 的类
        
        // 2. permits 子句可以省略 (如果在同一文件中)
        public sealed class Animal {}
        final class Dog extends Animal {}
        final class Cat extends Animal {}
        
        // 3. 允许的修饰符:
        // - final: 不能再被继承
        // - sealed: 可以继承,但必须指定 permits
        // - non-sealed: 打开继承
        
        // 4. 不能同时是 sealed 和 final
        // public sealed final class Bad {} // 编译错误
        
        // 5. 抽象类也可以是 sealed
        public sealed abstract class AbstractShape
            permits ConcreteCircle {
            public abstract double area();
        }
        
        public final class ConcreteCircle extends AbstractShape {
            private final double radius;
            
            public ConcreteCircle(double radius) {
                this.radius = radius;
            }
            
            @Override
            public double area() {
                return Math.PI * radius * radius;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Sealed Classes Demo ===");
        
        PatternMatchingIntegration pmi = new PatternMatchingIntegration();
        pmi.processResult(new PatternMatchingIntegration.Success("OK"));
        pmi.processResult(new PatternMatchingIntegration.Failure("Error"));
        
        ExpressionProblemSolution eps = new ExpressionProblemSolution();
        eps.demo();
        
        APIDesignBestPractices api = new APIDesignBestPractices();
        api.handleState(new APIDesignBestPractices.Connected("sess-123"));
    }
}
