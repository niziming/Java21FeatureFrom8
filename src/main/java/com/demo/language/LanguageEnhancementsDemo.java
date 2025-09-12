package com.demo.language;

import com.demo.annotations.*;
import java.util.function.*;
import java.util.Optional;

/**
 * 语言特性增强演示
 * 展示Java 8到Java 21的语言层面特性变化
 */
public class LanguageEnhancementsDemo {

    /**
     * Lambda表达式和函数式接口演示 (Java 8特性)
     * Lambda表达式是Java 8引入的重要特性
     */
    @Java8Feature(value = "Lambda表达式", desc = "函数式编程支持，简化匿名函数的编写")
    public void demonstrateLambdaExpressions() {
        System.out.println("=== Lambda表达式演示 ===");
        
        // 传统匿名内部类写法
        Runnable oldWay = new Runnable() {
            @Override
            public void run() {
                System.out.println("传统匿名内部类方式");
            }
        };
        
        // Java 8 Lambda表达式写法
        Runnable newWay = () -> System.out.println("Java 8 Lambda表达式方式");
        
        oldWay.run();
        newWay.run();
        
        // 函数式接口示例
        Function<String, Integer> stringLength = String::length;
        System.out.println("字符串长度: " + stringLength.apply("Java编程"));
        
        // 复杂Lambda表达式
        BiFunction<Integer, Integer, String> calculator = (a, b) -> {
            int result = a + b;
            return String.format("计算结果: %d + %d = %d", a, b, result);
        };
        System.out.println(calculator.apply(10, 20));
    }

    /**
     * 方法引用演示 (Java 8特性)
     * 方法引用是Lambda表达式的简化形式
     */
    @Java8Feature(value = "方法引用", desc = "Lambda表达式的简化形式，直接引用已存在的方法")
    public void demonstrateMethodReferences() {
        System.out.println("\n=== 方法引用演示 ===");
        
        // 静态方法引用
        Function<String, Integer> parseInt = Integer::parseInt;
        System.out.println("解析数字: " + parseInt.apply("123"));
        
        // 实例方法引用
        String text = "Java编程语言";
        Supplier<String> toUpperCase = text::toUpperCase;
        System.out.println("转大写: " + toUpperCase.get());
        
        // 构造器引用
        Supplier<StringBuilder> sbSupplier = StringBuilder::new;
        StringBuilder sb = sbSupplier.get();
        sb.append("使用构造器引用创建");
        System.out.println(sb.toString());
    }

    /**
     * Optional类演示 (Java 8特性)
     * Optional用于避免空指针异常
     */
    @Java8Feature(value = "Optional类", desc = "避免空指针异常的容器类")
    public void demonstrateOptional() {
        System.out.println("\n=== Optional类演示 ===");
        
        // 创建Optional
        Optional<String> nonEmpty = Optional.of("有值的Optional");
        Optional<String> empty = Optional.empty();
        Optional<String> nullable = Optional.ofNullable(null);
        
        // 检查值是否存在
        System.out.println("非空Optional是否有值: " + nonEmpty.isPresent());
        System.out.println("空Optional是否有值: " + empty.isPresent());
        
        // 安全地获取值
        nonEmpty.ifPresent(value -> System.out.println("值: " + value));
        
        // 提供默认值
        String defaultValue = empty.orElse("默认值");
        System.out.println("空Optional的默认值: " + defaultValue);
        
        // 链式调用
        String result = nonEmpty
                .map(String::toUpperCase)
                .filter(s -> s.length() > 5)
                .orElse("过滤后无值");
        System.out.println("链式处理结果: " + result);
    }

    /**
     * 接口默认方法演示 (Java 8特性)
     * 接口可以包含默认实现的方法
     */
    @Java8Feature(value = "接口默认方法", desc = "接口中可以定义有默认实现的方法")
    public interface ProcessorInterface {
        // 抽象方法
        void process(String data);
        
        // 默认方法
        default void preProcess(String data) {
            System.out.println("默认预处理: " + data);
        }
        
        // 静态方法
        static void staticMethod() {
            System.out.println("接口中的静态方法");
        }
    }

    // 实现接口的类
    public static class DataProcessor implements ProcessorInterface {
        @Override
        public void process(String data) {
            System.out.println("处理数据: " + data);
        }
        
        // 可以选择重写默认方法
        @Override
        public void preProcess(String data) {
            System.out.println("自定义预处理: " + data);
        }
    }

    /**
     * 接口默认方法使用演示
     */
    public void demonstrateDefaultMethods() {
        System.out.println("\n=== 接口默认方法演示 ===");
        
        DataProcessor processor = new DataProcessor();
        processor.preProcess("测试数据");
        processor.process("测试数据");
        
        // 调用接口静态方法
        ProcessorInterface.staticMethod();
    }

    /**
     * var关键字演示 (Java 11特性)
     * 局部变量类型推断
     */
    @Java11Feature(value = "var关键字", desc = "局部变量类型推断，编译器自动推断变量类型")
    public void demonstrateVarKeyword() {
        System.out.println("\n=== var关键字演示 ===");
        
        // 编译器推断类型
        var message = "这是一个字符串"; // String类型
        var number = 42; // int类型
        var list = java.util.List.of("Java", "Python", "JavaScript"); // List<String>类型
        var map = java.util.Map.of("key1", "value1", "key2", "value2"); // Map<String, String>类型
        
        System.out.println("推断的字符串: " + message);
        System.out.println("推断的数字: " + number);
        System.out.println("推断的列表: " + list);
        System.out.println("推断的映射: " + map);
        
        // Lambda表达式中的var
        list.forEach((var item) -> System.out.println("列表项: " + item));
    }

    /**
     * instanceof模式匹配演示 (Java 17特性)
     * instanceof操作符支持模式匹配
     */
    @Java17Feature(value = "instanceof模式匹配", desc = "instanceof操作符支持模式匹配，自动类型转换")
    public void demonstrateInstanceofPatternMatching() {
        System.out.println("\n=== instanceof模式匹配演示 ===");
        
        Object[] objects = {
            "这是字符串",
            42,
            java.util.List.of("列表", "元素"),
            new java.util.Date()
        };
        
        for (Object obj : objects) {
            // Java 17的模式匹配
            if (obj instanceof String str) {
                System.out.println("字符串长度: " + str.length());
            } else if (obj instanceof Integer num) {
                System.out.println("整数值: " + num + ", 平方: " + (num * num));
            } else if (obj instanceof java.util.List<?> list) {
                System.out.println("列表大小: " + list.size());
            } else {
                System.out.println("其他类型: " + obj.getClass().getSimpleName());
            }
        }
    }

    /**
     * 字符串模板演示 (Java 21预览特性)
     * 字符串插值功能
     */
    @Java21Feature(value = "字符串模板", desc = "字符串插值功能，更方便的字符串格式化")
    public void demonstrateStringTemplates() {
        System.out.println("\n=== 字符串模板演示 (预览特性) ===");
        
        String name = "张三";
        int age = 25;
        double score = 95.5;
        
        // 使用传统方式
        String traditional = String.format("姓名: %s, 年龄: %d, 分数: %.2f", name, age, score);
        System.out.println("传统格式化: " + traditional);
        
        // 注意：字符串模板是预览特性，需要特殊编译选项
        // 这里展示概念，实际使用需要 --enable-preview
        System.out.println("字符串模板将在Java 21中提供更简洁的语法");
    }

    /**
     * switch表达式演示 (Java 17正式特性)
     * 增强的switch语句，支持表达式形式
     */
    @Java17Feature(value = "switch表达式", desc = "增强的switch语句，支持表达式形式和箭头语法")
    public void demonstrateSwitchExpressions() {
        System.out.println("\n=== switch表达式演示 ===");
        
        // 传统switch语句
        int dayNumber = 3;
        String dayName;
        switch (dayNumber) {
            case 1:
                dayName = "星期一";
                break;
            case 2:
                dayName = "星期二";
                break;
            case 3:
                dayName = "星期三";
                break;
            default:
                dayName = "未知";
                break;
        }
        System.out.println("传统switch: " + dayName);
        
        // Java 17 switch表达式
        String newDayName = switch (dayNumber) {
            case 1 -> "星期一";
            case 2 -> "星期二";
            case 3 -> "星期三";
            case 4 -> "星期四";
            case 5 -> "星期五";
            case 6, 7 -> "周末";
            default -> "未知";
        };
        System.out.println("新式switch表达式: " + newDayName);
        
        // 复杂switch表达式
        String seasondesc = switch (dayNumber % 4) {
            case 0 -> {
                String season = "冬季";
                yield season + " - 寒冷的季节";
            }
            case 1 -> {
                String season = "春季";
                yield season + " - 温暖的季节";
            }
            case 2 -> {
                String season = "夏季";
                yield season + " - 炎热的季节";
            }
            case 3 -> {
                String season = "秋季";
                yield season + " - 凉爽的季节";
            }
            default -> "未知季节";
        };
        System.out.println("季节描述: " + seasondesc);
    }

    /**
     * 运行所有语言特性演示
     */
    public void runAllDemos() {
        System.out.println("开始语言特性增强演示...\n");
        
        demonstrateLambdaExpressions();
        demonstrateMethodReferences();
        demonstrateOptional();
        demonstrateDefaultMethods();
        demonstrateVarKeyword();
        demonstrateInstanceofPatternMatching();
        demonstrateSwitchExpressions();
        demonstrateStringTemplates();
        
        System.out.println("\n语言特性增强演示完成！");
    }
}