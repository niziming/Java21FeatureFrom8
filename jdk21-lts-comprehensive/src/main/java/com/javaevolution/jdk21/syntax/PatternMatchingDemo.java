package com.javaevolution.jdk21.syntax;

import java.util.*;

/**
 * Pattern Matching 完整演示
 * JEP 441: Pattern Matching for switch (JDK 21 - Final)
 * JEP 440: Record Patterns (JDK 21 - Final)
 * JEP 427: Pattern Matching for switch (JDK 19 - Preview)
 */
public class PatternMatchingDemo {

    /**
     * instanceof Pattern Matching (JDK 16+)
     */
    public static class InstanceOfPatterns {
        
        public void traditionalInstanceOf(Object obj) {
            // 传统方式: 繁琐
            if (obj instanceof String) {
                String s = (String) obj;
                System.out.println(s.toUpperCase());
            }
        }
        
        public void patternInstanceOf(Object obj) {
            // Pattern Matching: 简洁
            if (obj instanceof String s) {
                System.out.println(s.toUpperCase());
            }
            
            // 可以在表达式中使用
            if (obj instanceof String s && s.length() > 5) {
                System.out.println("Long string: " + s);
            }
        }
    }

    /**
     * Switch Pattern Matching (JDK 21)
     */
    public static class SwitchPatterns {
        
        // 类型模式
        public String formatValue(Object obj) {
            return switch (obj) {
                case Integer i -> String.format("int %d", i);
                case Long l -> String.format("long %d", l);
                case Double d -> String.format("double %f", d);
                case String s -> String.format("String %s", s);
                case null -> "null";
                default -> obj.toString();
            };
        }
        
        // Guarded Patterns (守卫模式)
        public String classify(Object obj) {
            return switch (obj) {
                case String s when s.isEmpty() -> "Empty string";
                case String s when s.length() < 5 -> "Short string: " + s;
                case String s -> "Long string: " + s;
                case Integer i when i < 0 -> "Negative number";
                case Integer i when i == 0 -> "Zero";
                case Integer i -> "Positive number";
                case null -> "null";
                default -> "Unknown";
            };
        }
        
        // null 处理
        public String handleNull(String s) {
            return switch (s) {
                case null -> "null value";
                case "SPECIAL" -> "special value";
                default -> "normal value: " + s;
            };
        }
    }

    /**
     * Record Patterns (JDK 21)
     */
    public static class RecordPatterns {
        
        record Point(int x, int y) {}
        record Circle(Point center, int radius) {}
        record Rectangle(Point topLeft, Point bottomRight) {}
        
        // Record 解构
        public void printPoint(Object obj) {
            if (obj instanceof Point(int x, int y)) {
                System.out.println("Point: x=" + x + ", y=" + y);
            }
        }
        
        // 嵌套 Record 解构
        public void printCircle(Object obj) {
            if (obj instanceof Circle(Point(int x, int y), int r)) {
                System.out.println("Circle at (" + x + "," + y + ") with radius " + r);
            }
        }
        
        // Switch 中的 Record Patterns
        public String describe(Object shape) {
            return switch (shape) {
                case Point(int x, int y) -> 
                    "Point at (" + x + "," + y + ")";
                case Circle(Point(int x, int y), int r) -> 
                    "Circle at (" + x + "," + y + ") radius " + r;
                case Rectangle(Point(int x1, int y1), Point(int x2, int y2)) -> 
                    "Rectangle from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")";
                case null -> "null";
                default -> "Unknown shape";
            };
        }
        
        // Guarded Record Patterns
        public String analyzePoint(Object obj) {
            return switch (obj) {
                case Point(int x, int y) when x == 0 && y == 0 -> "Origin";
                case Point(int x, int y) when x == y -> "On diagonal";
                case Point(int x, int y) when x > 0 && y > 0 -> "Quadrant I";
                case Point(int x, int y) when x < 0 && y > 0 -> "Quadrant II";
                case Point(int x, int y) when x < 0 && y < 0 -> "Quadrant III";
                case Point(int x, int y) when x > 0 && y < 0 -> "Quadrant IV";
                case Point(int x, int y) -> "On axis";
                default -> "Not a point";
            };
        }
    }

    /**
     * 实际应用: 表达式求值
     */
    public static class ExpressionEvaluator {
        
        sealed interface Expr {}
        record Constant(int value) implements Expr {}
        record Negate(Expr expr) implements Expr {}
        record Add(Expr left, Expr right) implements Expr {}
        record Multiply(Expr left, Expr right) implements Expr {}
        
        // 使用 Pattern Matching 求值
        public int eval(Expr expr) {
            return switch (expr) {
                case Constant(int value) -> value;
                case Negate(Expr e) -> -eval(e);
                case Add(Expr left, Expr right) -> eval(left) + eval(right);
                case Multiply(Expr left, Expr right) -> eval(left) * eval(right);
            };
        }
        
        // 优化: 常量折叠
        public Expr simplify(Expr expr) {
            return switch (expr) {
                case Add(Constant(int a), Constant(int b)) -> 
                    new Constant(a + b);
                case Multiply(Constant(int a), Constant(int b)) -> 
                    new Constant(a * b);
                case Multiply(Constant(0), Expr e) -> 
                    new Constant(0);
                case Multiply(Expr e, Constant(0)) -> 
                    new Constant(0);
                case Multiply(Constant(1), Expr e) -> 
                    simplify(e);
                case Multiply(Expr e, Constant(1)) -> 
                    simplify(e);
                default -> expr;
            };
        }
        
        public void demo() {
            // (2 + 3) * 4
            Expr expr = new Multiply(
                new Add(new Constant(2), new Constant(3)),
                new Constant(4)
            );
            
            System.out.println("Result: " + eval(expr)); // 20
            
            // 优化: 0 * x -> 0
            Expr zeroExpr = new Multiply(new Constant(0), new Constant(999));
            Expr simplified = simplify(zeroExpr);
            System.out.println("Simplified: " + eval(simplified)); // 0
        }
    }

    /**
     * 实际应用: JSON 解析
     */
    public static class JSONParser {
        
        sealed interface JSON {}
        record JSONObject(Map<String, JSON> fields) implements JSON {}
        record JSONArray(List<JSON> elements) implements JSON {}
        record JSONString(String value) implements JSON {}
        record JSONNumber(double value) implements JSON {}
        record JSONBoolean(boolean value) implements JSON {}
        record JSONNull() implements JSON {}
        
        // 提取值
        public Object extract(JSON json) {
            return switch (json) {
                case JSONString(String s) -> s;
                case JSONNumber(double d) -> d;
                case JSONBoolean(boolean b) -> b;
                case JSONNull() -> null;
                case JSONArray(List<JSON> elements) -> 
                    elements.stream().map(this::extract).toList();
                case JSONObject(Map<String, JSON> fields) -> 
                    fields.entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                            Map.Entry::getKey,
                            e -> extract(e.getValue())
                        ));
            };
        }
        
        // 查找字段
        public JSON findField(JSON json, String fieldName) {
            return switch (json) {
                case JSONObject(Map<String, JSON> fields) when fields.containsKey(fieldName) -> 
                    fields.get(fieldName);
                case JSONObject(Map<String, JSON> fields) -> 
                    new JSONNull();
                default -> new JSONNull();
            };
        }
    }

    /**
     * 实际应用: 状态机
     */
    public static class StateMachine {
        
        sealed interface State {}
        record Idle() implements State {}
        record Running(String taskId) implements State {}
        record Paused(String taskId, long pausedAt) implements State {}
        record Failed(String taskId, String error) implements State {}
        
        sealed interface Event {}
        record Start(String taskId) implements Event {}
        record Pause() implements Event {}
        record Resume() implements Event {}
        record Fail(String error) implements Event {}
        record Reset() implements Event {}
        
        // 状态转换
        public State transition(State currentState, Event event) {
            return switch (currentState) {
                case Idle() -> switch (event) {
                    case Start(String taskId) -> new Running(taskId);
                    default -> currentState;
                };
                
                case Running(String taskId) -> switch (event) {
                    case Pause() -> new Paused(taskId, System.currentTimeMillis());
                    case Fail(String error) -> new Failed(taskId, error);
                    case Reset() -> new Idle();
                    default -> currentState;
                };
                
                case Paused(String taskId, long pausedAt) -> switch (event) {
                    case Resume() -> new Running(taskId);
                    case Fail(String error) -> new Failed(taskId, error);
                    case Reset() -> new Idle();
                    default -> currentState;
                };
                
                case Failed(String taskId, String error) -> switch (event) {
                    case Reset() -> new Idle();
                    default -> currentState;
                };
            };
        }
        
        public void demo() {
            State state = new Idle();
            
            state = transition(state, new Start("task-001"));
            System.out.println("State: " + state); // Running
            
            state = transition(state, new Pause());
            System.out.println("State: " + state); // Paused
            
            state = transition(state, new Resume());
            System.out.println("State: " + state); // Running
            
            state = transition(state, new Fail("Out of memory"));
            System.out.println("State: " + state); // Failed
        }
    }

    /**
     * Pattern Matching 最佳实践
     */
    public static class BestPractices {
        
        // 1. 穷举性检查: 使用 sealed 类型
        sealed interface Result {}
        record Success(String data) implements Result {}
        record Failure(String error) implements Result {}
        
        public void exhaustiveSwitch(Result result) {
            // 不需要 default,编译器保证穷举
            String message = switch (result) {
                case Success(String data) -> "OK: " + data;
                case Failure(String error) -> "Error: " + error;
            };
            
            System.out.println(message);
        }
        
        // 2. Guard 条件简化复杂逻辑
        public String classifyNumber(Object obj) {
            return switch (obj) {
                case Integer i when i < 0 -> "negative";
                case Integer i when i == 0 -> "zero";
                case Integer i when i > 0 && i < 10 -> "small positive";
                case Integer i -> "large positive";
                default -> "not an integer";
            };
        }
        
        // 3. Record Patterns 简化数据提取
        record User(String name, int age, String email) {}
        
        public boolean isAdult(Object obj) {
            return obj instanceof User(String name, int age, String email) && age >= 18;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Pattern Matching Demo ===");
        
        SwitchPatterns sp = new SwitchPatterns();
        System.out.println(sp.formatValue(42));
        System.out.println(sp.classify("Hi"));
        
        RecordPatterns rp = new RecordPatterns();
        rp.printPoint(new RecordPatterns.Point(10, 20));
        System.out.println(rp.describe(new RecordPatterns.Circle(
            new RecordPatterns.Point(0, 0), 5)));
        
        ExpressionEvaluator ee = new ExpressionEvaluator();
        ee.demo();
        
        StateMachine sm = new StateMachine();
        sm.demo();
    }
}
