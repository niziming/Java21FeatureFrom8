package com.demo.utils;

import com.demo.language.LanguageEnhancementsDemo;
import com.demo.collections.CollectionsDemo;
import com.demo.streams.StreamsDemo;
import com.demo.concurrent.ConcurrencyDemo;
import com.demo.io.IODemo;
import com.demo.text.TextProcessingDemo;
import com.demo.records.RecordsDemo;
import com.demo.pattern.PatternMatchingDemo;
import com.demo.sealed.SealedClassesDemo;
import com.demo.preview.PreviewFeaturesDemo;

/**
 * 演示运行器
 * 统一运行所有Java特性演示
 */
public class DemoRunner {

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   Java 8 vs Java 21 特性对比演示");
        System.out.println("====================================");
        System.out.println();
        
        if (args.length > 0) {
            // 根据参数运行特定演示
            String demoType = args[0].toLowerCase();
            runSpecificDemo(demoType);
        } else {
            // 运行所有演示
            runAllDemos();
        }
        
        System.out.println("\n====================================");
        System.out.println("        所有演示运行完成！");
        System.out.println("====================================");
    }

    /**
     * 运行所有演示
     */
    private static void runAllDemos() {
        System.out.println("运行所有Java特性演示...\n");
        // 1. 语言特性增强
        printSectionHeader("语言特性增强");
        new LanguageEnhancementsDemo().runAllDemos();
        
        waitForContinue();
        
        // 2. 集合框架增强
        printSectionHeader("集合框架增强");
        new CollectionsDemo().runAllDemos();
        
        waitForContinue();
        
        // 3. Stream API演示
        printSectionHeader("Stream API增强");
        new StreamsDemo().runAllDemos();
        
        waitForContinue();
        
        // 4. 并发编程增强
        printSectionHeader("并发编程增强");
        new ConcurrencyDemo().runAllDemos();
        
        waitForContinue();
        
        // 5. I/O操作增强
        printSectionHeader("I/O操作增强");
        new IODemo().runAllDemos();
        
        waitForContinue();
        
        // 6. 文本处理增强
        printSectionHeader("文本处理增强");
        new TextProcessingDemo().runAllDemos();
        
        waitForContinue();
        
        // 7. Records特性
        printSectionHeader("Records特性");
        new RecordsDemo().runAllDemos();
        
        waitForContinue();
        
        // 8. 模式匹配
        printSectionHeader("模式匹配");
        new PatternMatchingDemo().runAllDemos();
        
        waitForContinue();
        
        // 9. 密封类
        printSectionHeader("密封类");
        new SealedClassesDemo().runAllDemos();
        
        waitForContinue();
        
        // 10. 预览特性
        printSectionHeader("预览特性");
        new PreviewFeaturesDemo().runAllDemos();
    }

    /**
     * 运行特定演示
     */
    private static void runSpecificDemo(String demoType) {
        System.out.println("运行特定演示: " + demoType + "\n");
        
        switch (demoType) {
            case "language" -> {
                printSectionHeader("语言特性增强");
                new LanguageEnhancementsDemo().runAllDemos();
            }
            case "collections" -> {
                printSectionHeader("集合框架增强");
                new CollectionsDemo().runAllDemos();
            }
            case "streams" -> {
                printSectionHeader("Stream API增强");
                new StreamsDemo().runAllDemos();
            }
            case "concurrent" -> {
                printSectionHeader("并发编程增强");
                new ConcurrencyDemo().runAllDemos();
            }
            case "io" -> {
                printSectionHeader("I/O操作增强");
                new IODemo().runAllDemos();
            }
            case "text" -> {
                printSectionHeader("文本处理增强");
                new TextProcessingDemo().runAllDemos();
            }
            case "records" -> {
                printSectionHeader("Records特性");
                new RecordsDemo().runAllDemos();
            }
            case "pattern" -> {
                printSectionHeader("模式匹配");
                new PatternMatchingDemo().runAllDemos();
            }
            case "sealed" -> {
                printSectionHeader("密封类");
                new SealedClassesDemo().runAllDemos();
            }
            case "preview" -> {
                printSectionHeader("预览特性");
                new PreviewFeaturesDemo().runAllDemos();
            }
            default -> {
                System.err.println("未知的演示类型: " + demoType);
                printUsage();
            }
        }
    }

    /**
     * 打印使用说明
     */
    private static void printUsage() {
        System.out.println("\n使用说明:");
        System.out.println("  java com.demo.utils.DemoRunner [演示类型]");
        System.out.println("\n可用的演示类型:");
        System.out.println("  language    - 语言特性增强演示");
        System.out.println("  collections - 集合框架增强演示");
        System.out.println("  streams     - Stream API增强演示");
        System.out.println("  concurrent  - 并发编程增强演示");
        System.out.println("  io          - I/O操作增强演示");
        System.out.println("  text        - 文本处理增强演示");
        System.out.println("  records     - Records特性演示");
        System.out.println("  pattern     - 模式匹配演示");
        System.out.println("  sealed      - 密封类演示");
        System.out.println("  preview     - 预览特性演示");
        System.out.println("\n不指定参数则运行所有演示");
    }

    /**
     * 打印章节标题
     */
    private static void printSectionHeader(String title) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  " + title);
        System.out.println("=".repeat(50));
    }

    /**
     * 等待用户继续（在实际演示中可以启用）
     */
    private static void waitForContinue() {
        // 在演示环境中，为了避免阻塞，这里不等待用户输入
        // 实际使用时可以取消注释下面的代码来等待用户确认
        /*
        System.out.println("\n按Enter键继续下一个演示...");
        try {
            System.in.read();
        } catch (Exception e) {
            // 忽略异常
        }
        */
        
        // 添加短暂延迟来分隔不同的演示
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 显示Java版本信息
     */
    public static void showJavaVersionInfo() {
        System.out.println("Java版本信息:");
        System.out.println("  版本: " + System.getProperty("java.version"));
        System.out.println("  供应商: " + System.getProperty("java.vendor"));
        System.out.println("  虚拟机: " + System.getProperty("java.vm.name"));
        System.out.println("  虚拟机版本: " + System.getProperty("java.vm.version"));
        System.out.println();
    }

    /**
     * 显示特性对比总结
     */
    public static void showFeatureComparison() {
        System.out.println("Java 8 vs Java 21 主要特性对比:");
        System.out.println();
        
        System.out.println("Java 8 引入的重要特性:");
        System.out.println("  • Lambda表达式和方法引用");
        System.out.println("  • Stream API");
        System.out.println("  • Optional类");
        System.out.println("  • 接口默认方法");
        System.out.println("  • CompletableFuture");
        System.out.println("  • 新的日期时间API");
        System.out.println();
        
        System.out.println("Java 9-11 重要增强:");
        System.out.println("  • 模块系统 (Java 9)");
        System.out.println("  • 不可变集合工厂方法 (Java 9)");
        System.out.println("  • var关键字 (Java 11)");
        System.out.println("  • HTTP Client (Java 11)");
        System.out.println();
        
        System.out.println("Java 17 LTS 重要特性:");
        System.out.println("  • Records (数据载体类)");
        System.out.println("  • 密封类 (Sealed Classes)");
        System.out.println("  • instanceof模式匹配");
        System.out.println("  • switch表达式增强");
        System.out.println();
        
        System.out.println("Java 21 LTS 重要特性:");
        System.out.println("  • 虚拟线程 (Virtual Threads) - Project Loom");
        System.out.println("  • 结构化并发 (预览)");
        System.out.println("  • 字符串模板 (预览)");
        System.out.println("  • Record模式匹配 (预览)");
        System.out.println("  • Switch模式匹配增强");
        System.out.println();
        
        System.out.println("性能和生产力提升:");
        System.out.println("  • 更简洁的代码语法");
        System.out.println("  • 更好的类型安全");
        System.out.println("  • 更强的并发处理能力");
        System.out.println("  • 更好的内存管理");
        System.out.println("  • 更快的启动时间");
        System.out.println();
    }

    static {
        // 静态初始化块，显示运行环境信息
        showJavaVersionInfo();
        showFeatureComparison();
    }
}