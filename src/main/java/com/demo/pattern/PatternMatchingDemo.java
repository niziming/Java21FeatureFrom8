package com.demo.pattern;

import com.demo.annotations.*;

/**
 * 模式匹配演示
 * 展示Java 17正式版和Java 21预览版的模式匹配特性
 */
public class PatternMatchingDemo {

    /**
     * instanceof模式匹配演示 (Java 17特性)
     * instanceof操作符支持模式匹配和类型转换
     */
    @Java17Feature(value = "instanceof模式匹配", description = "instanceof支持模式匹配，自动进行类型转换")
    public void demonstrateInstanceofPatternMatching() {
        System.out.println("=== instanceof模式匹配演示 ===");
        
        Object[] testObjects = {
            "Java编程语言",
            42,
            3.14159,
            new java.util.ArrayList<String>(),
            new java.util.HashMap<String, Integer>(),
            null
        };
        
        for (Object obj : testObjects) {
            System.out.println("处理对象: " + obj);
            processObject(obj);
            System.out.println();
        }
    }

    /**
     * 使用模式匹配处理不同类型的对象
     */
    private void processObject(Object obj) {
        // Java 17的instanceof模式匹配
        if (obj instanceof String str) {
            System.out.println("  字符串处理:");
            System.out.println("    长度: " + str.length());
            System.out.println("    大写: " + str.toUpperCase());
            System.out.println("    是否包含'Java': " + str.contains("Java"));
        } else if (obj instanceof Integer num) {
            System.out.println("  整数处理:");
            System.out.println("    值: " + num);
            System.out.println("    平方: " + (num * num));
            System.out.println("    是否偶数: " + (num % 2 == 0));
        } else if (obj instanceof Double dbl) {
            System.out.println("  双精度浮点数处理:");
            System.out.println("    值: " + dbl);
            System.out.println("    四舍五入: " + Math.round(dbl));
            System.out.println("    整数部分: " + dbl.intValue());
        } else if (obj instanceof java.util.List<?> list) {
            System.out.println("  列表处理:");
            System.out.println("    大小: " + list.size());
            System.out.println("    是否为空: " + list.isEmpty());
            System.out.println("    类型: " + list.getClass().getSimpleName());
        } else if (obj instanceof java.util.Map<?, ?> map) {
            System.out.println("  映射处理:");
            System.out.println("    大小: " + map.size());
            System.out.println("    是否为空: " + map.isEmpty());
            System.out.println("    类型: " + map.getClass().getSimpleName());
        } else if (obj == null) {
            System.out.println("  空值处理: 对象为null");
        } else {
            System.out.println("  未知类型: " + obj.getClass().getSimpleName());
        }
    }

    /**
     * switch表达式增强演示 (Java 17特性)
     * switch支持更复杂的模式匹配
     */
    @Java17Feature(value = "switch表达式增强", description = "switch表达式支持更灵活的模式匹配")
    public void demonstrateSwitchExpressions() {
        System.out.println("\n=== switch表达式增强演示 ===");
        
        Object[] testData = {
            "Java",
            42,
            3.14,
            true,
            new int[]{1, 2, 3},
            new String[]{"a", "b"},
            null
        };
        
        for (Object data : testData) {
            String result = analyzeData(data);
            System.out.println("数据: " + data + " -> " + result);
        }
    }

    /**
     * 使用switch表达式分析数据
     */
    private String analyzeData(Object data) {
        return switch (data) {
            case null -> "空值";
            case String s -> "字符串，长度: " + s.length();
            case Integer i -> "整数，值: " + i + (i % 2 == 0 ? " (偶数)" : " (奇数)");
            case Double d -> "双精度浮点数，值: " + d;
            case Boolean b -> "布尔值: " + (b ? "真" : "假");
            case int[] arr -> "整数数组，长度: " + arr.length;
            case String[] arr -> "字符串数组，长度: " + arr.length;
            default -> "未知类型: " + data.getClass().getSimpleName();
        };
    }

    /**
     * Record模式匹配演示 (Java 21预览特性)
     * 对Record进行解构和匹配
     */
    @Java21Feature(value = "Record模式匹配", description = "对Record类型进行解构和模式匹配")
    public void demonstrateRecordPatternMatching() {
        System.out.println("\n=== Record模式匹配演示 (概念展示) ===");
        
        // 注意：这是Java 21的预览特性，需要特殊编译选项
        System.out.println("Record模式匹配将支持以下特性:");
        System.out.println("1. Record解构");
        System.out.println("2. 嵌套模式匹配");
        System.out.println("3. 守卫条件");
        
        // 使用传统方式展示概念
        Person[] persons = {
            new Person("张三", 25, "工程师"),
            new Person("李四", 30, "设计师"),
            new Person("王五", 35, "经理"),
            new Person("赵六", 22, "实习生")
        };
        
        System.out.println("\n使用传统方式处理Person记录:");
        for (Person person : persons) {
            processPersonTraditional(person);
        }
        
        System.out.println("\nJava 21 Record模式匹配预期语法（概念展示）:");
        System.out.println("switch (person) {");
        System.out.println("  case Person(var name, var age, \"工程师\") -> ...");
        System.out.println("  case Person(var name, var age, var job) when age > 30 -> ...");
        System.out.println("  default -> ...");
        System.out.println("}");
    }

    /**
     * 传统方式处理Person（展示未来模式匹配的对比）
     */
    private void processPersonTraditional(Person person) {
        String category;
        if ("工程师".equals(person.job())) {
            category = "技术人员: " + person.name();
        } else if ("设计师".equals(person.job())) {
            category = "创意人员: " + person.name();
        } else if ("经理".equals(person.job()) && person.age() > 30) {
            category = "资深管理者: " + person.name();
        } else if (person.age() < 25) {
            category = "年轻员工: " + person.name() + " (" + person.job() + ")";
        } else {
            category = "普通员工: " + person.name() + " (" + person.job() + ")";
        }
        System.out.println("  " + category);
    }

    /**
     * 嵌套模式匹配演示 (Java 21预览特性)
     * 展示复杂的嵌套结构模式匹配
     */
    @Java21Feature(value = "嵌套模式匹配", description = "支持对复杂嵌套结构进行模式匹配")
    public void demonstrateNestedPatternMatching() {
        System.out.println("\n=== 嵌套模式匹配演示 (概念展示) ===");
        
        Employee[] employees = {
            new Employee("张三", new Address("朝阳区", "北京", "100000"), "高级工程师"),
            new Employee("李四", new Address("浦东区", "上海", "200000"), "设计师"),
            new Employee("王五", new Address("南山区", "深圳", "518000"), "产品经理")
        };
        
        System.out.println("传统方式处理嵌套结构:");
        for (Employee emp : employees) {
            processEmployeeTraditional(emp);
        }
        
        System.out.println("\nJava 21嵌套模式匹配预期语法（概念展示）:");
        System.out.println("switch (employee) {");
        System.out.println("  case Employee(var name, Address(var district, \"北京\", var zip), var job) -> ...");
        System.out.println("  case Employee(var name, Address(_, \"上海\", _), var job) -> ...");
        System.out.println("  default -> ...");
        System.out.println("}");
    }

    /**
     * 传统方式处理Employee（展示未来嵌套模式匹配的对比）
     */
    private void processEmployeeTraditional(Employee emp) {
        String info;
        if ("北京".equals(emp.address().city())) {
            info = String.format("北京员工: %s，工作在%s，职位: %s", 
                emp.name(), emp.address().district(), emp.job());
        } else if ("上海".equals(emp.address().city())) {
            info = String.format("上海员工: %s，工作在%s，职位: %s", 
                emp.name(), emp.address().district(), emp.job());
        } else if ("深圳".equals(emp.address().city())) {
            info = String.format("深圳员工: %s，工作在%s，职位: %s", 
                emp.name(), emp.address().district(), emp.job());
        } else {
            info = String.format("其他地区员工: %s，职位: %s", emp.name(), emp.job());
        }
        System.out.println("  " + info);
    }

    /**
     * 守卫条件演示 (Java 21预览特性)
     * 模式匹配中的条件约束
     */
    @Java21Feature(value = "守卫条件", description = "模式匹配中支持when子句进行条件约束")
    public void demonstrateGuardConditions() {
        System.out.println("\n=== 守卫条件演示 (概念展示) ===");
        
        System.out.println("守卫条件允许在模式匹配中添加额外的条件约束");
        System.out.println("预期语法示例:");
        System.out.println("switch (obj) {");
        System.out.println("  case String s when s.length() > 10 -> \"长字符串\";");
        System.out.println("  case Integer i when i > 0 -> \"正整数\";");
        System.out.println("  case Integer i when i < 0 -> \"负整数\";");
        System.out.println("  case Integer i when i == 0 -> \"零\";");
        System.out.println("  default -> \"其他\";");
        System.out.println("}");
        
        // 使用传统方式模拟守卫条件
        Object[] testData = {"Java编程语言参考手册", "Java", 42, -10, 0, 3.14};
        
        System.out.println("\n使用传统方式模拟守卫条件:");
        for (Object obj : testData) {
            String result = classifyWithConditions(obj);
            System.out.println(obj + " -> " + result);
        }
    }

    /**
     * 传统方式模拟守卫条件
     */
    private String classifyWithConditions(Object obj) {
        if (obj instanceof String s) {
            if (s.length() > 10) {
                return "长字符串";
            } else if (s.length() > 5) {
                return "中等字符串";
            } else {
                return "短字符串";
            }
        } else if (obj instanceof Integer i) {
            if (i > 0) {
                return "正整数";
            } else if (i < 0) {
                return "负整数";
            } else {
                return "零";
            }
        } else {
            return "其他类型";
        }
    }

    // =============== Record定义 ===============

    /**
     * Person记录
     */
    public record Person(String name, int age, String job) {}

    /**
     * Address记录
     */
    public record Address(String district, String city, String zipCode) {}

    /**
     * Employee记录
     */
    public record Employee(String name, Address address, String job) {}

    /**
     * 运行所有模式匹配演示
     */
    public void runAllDemos() {
        System.out.println("开始模式匹配演示...\n");
        
        demonstrateInstanceofPatternMatching();
        demonstrateSwitchExpressions();
        demonstrateRecordPatternMatching();
        demonstrateNestedPatternMatching();
        demonstrateGuardConditions();
        
        System.out.println("\n模式匹配演示完成！");
    }
}