package com.demo.text;

import com.demo.annotations.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文本处理增强演示
 * 展示Java 8到Java 21的文本处理相关新特性和改进
 */
public class TextProcessingDemo {

    /**
     * String类增强演示 (Java 8特性)
     * String.join()和其他便利方法
     */
    @Java8Feature(value = "String类增强", desc = "String.join()等便利方法，简化字符串操作")
    public void demonstrateStringEnhancements() {
        System.out.println("=== String类增强演示 ===");
        
        // String.join() - Java 8新增
        List<String> words = Arrays.asList("Java", "编程", "语言", "特性", "演示");
        String joined = String.join(" -> ", words);
        System.out.println("字符串连接: " + joined);
        
        // 数组连接
        String[] languages = {"Java", "Python", "JavaScript", "Go"};
        String languageList = String.join(", ", languages);
        System.out.println("语言列表: " + languageList);
        
        // 复杂连接示例
        Map<String, String> features = new LinkedHashMap<>();
        features.put("Java 8", "Lambda表达式");
        features.put("Java 11", "var关键字");
        features.put("Java 17", "Records");
        features.put("Java 21", "虚拟线程");
        
        String featuredesc = features.entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(java.util.stream.Collectors.joining(" | "));
        
        System.out.println("特性描述: " + featuredesc);
        
        // 字符串分割和处理
        String csvData = "张三,25,工程师;李四,30,设计师;王五,28,产品经理";
        System.out.println("\nCSV数据处理:");
        Arrays.stream(csvData.split(";"))
            .map(person -> person.split(","))
            .forEach(fields -> {
                if (fields.length >= 3) {
                    System.out.printf("  姓名: %s, 年龄: %s, 职位: %s%n", 
                        fields[0], fields[1], fields[2]);
                }
            });
    }

    /**
     * 文本块演示 (Java 17特性)
     * 多行字符串的简洁表示
     */
    @Java17Feature(value = "文本块", desc = "使用三重引号定义多行字符串，保持格式和缩进")
    public void demonstrateTextBlocks() {
        System.out.println("\n=== 文本块演示 ===");
        
        // 传统多行字符串
        String traditionalJson = "{\n" +
            "  \"name\": \"张三\",\n" +
            "  \"age\": 25,\n" +
            "  \"skills\": [\n" +
            "    \"Java\",\n" +
            "    \"Spring Boot\",\n" +
            "    \"微服务\"\n" +
            "  ]\n" +
            "}";
        
        // Java 17文本块
        String textBlockJson = """
            {
              "name": "张三",
              "age": 25,
              "skills": [
                "Java",
                "Spring Boot",
                "微服务"
              ]
            }
            """;
        
        System.out.println("传统方式JSON:");
        System.out.println(traditionalJson);
        
        System.out.println("\n文本块方式JSON:");
        System.out.println(textBlockJson);
        
        // SQL查询文本块
        String sqlQuery = """
            SELECT u.name, u.age, d.department_name
            FROM users u
            INNER JOIN departments d ON u.dept_id = d.id
            WHERE u.age > 25
              AND u.status = 'ACTIVE'
            ORDER BY u.name
            """;
        
        System.out.println("SQL查询文本块:");
        System.out.println(sqlQuery);
        
        // HTML模板文本块
        String name = "李四";
        int age = 30;
        String htmlTemplate = """
            <html>
            <head>
                <title>用户信息</title>
            </head>
            <body>
                <h1>欢迎, %s!</h1>
                <p>年龄: %d 岁</p>
                <p>系统时间: %s</p>
            </body>
            </html>
            """.formatted(name, age, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        System.out.println("HTML模板:");
        System.out.println(htmlTemplate);
    }

    /**
     * String新增方法演示 (Java 11特性)
     * isBlank(), lines(), strip()等新方法
     */
    @Java11Feature(value = "String新增方法", desc = "isBlank(), lines(), strip(), repeat()等便利方法")
    public void demonstrateStringNewMethods() {
        System.out.println("\n=== String新增方法演示 ===");
        
        // isBlank() - 检查字符串是否为空或只包含空白字符
        String emptyString = "";
        String whitespaceString = "   \t\n  ";
        String contentString = "  Java编程  ";
        
        System.out.println("isBlank()方法测试:");
        System.out.println("  空字符串.isBlank(): " + emptyString.isBlank());
        System.out.println("  空白字符串.isBlank(): " + whitespaceString.isBlank());
        System.out.println("  有内容字符串.isBlank(): " + contentString.isBlank());
        
        // lines() - 将字符串按行分割
        String multilineText = "第一行\n第二行\r\n第三行\n\n第五行";
        System.out.println("\nlines()方法测试:");
        System.out.println("原始文本: " + multilineText.replace("\n", "\\n").replace("\r", "\\r"));
        multilineText.lines()
            .map(line -> line.isEmpty() ? "<空行>" : line)
            .forEach(line -> System.out.println("  行: " + line));
        
        // strip() vs trim() - 更好的空白字符处理
        String textWithWhitespace = " \t Java编程语言 \u2000\u2001 ";
        System.out.println("\nstrip() vs trim()比较:");
        System.out.println("  原始: '" + textWithWhitespace + "'");
        System.out.println("  trim(): '" + textWithWhitespace.trim() + "'");
        System.out.println("  strip(): '" + textWithWhitespace.strip() + "'");
        System.out.println("  stripLeading(): '" + textWithWhitespace.stripLeading() + "'");
        System.out.println("  stripTrailing(): '" + textWithWhitespace.stripTrailing() + "'");
        
        // repeat() - 重复字符串
        String star = "*";
        String dash = "-";
        System.out.println("\nrepeat()方法演示:");
        System.out.println("  " + star.repeat(20));
        System.out.println("  " + "Java".repeat(3));
        System.out.println("  " + dash.repeat(15));
        
        // 组合使用创建格式化输出
        String title = "Java特性演示";
        int width = 30;
        String border = "=".repeat(width);
        String padding = " ".repeat((width - title.length()) / 2);
        
        System.out.println("\n格式化标题:");
        System.out.println(border);
        System.out.println(padding + title);
        System.out.println(border);
    }

    /**
     * 正则表达式增强演示 (Java 8特性)
     * Pattern类的新方法和Stream支持
     */
    @Java8Feature(value = "正则表达式增强", desc = "Pattern类支持Stream操作，更好的文本处理能力")
    public void demonstrateRegexEnhancements() {
        System.out.println("\n=== 正则表达式增强演示 ===");
        
        String sampleText = """
            联系方式信息：
            张三的邮箱是 zhangsan@company.com，电话 138-0013-8000
            李四的邮箱是 lisi@example.org，电话 139-0013-9000  
            王五的邮箱是 wangwu@demo.net，电话 137-0013-7000
            无效邮箱：test@，不完整电话：138-001
            """;
        
        // 邮箱匹配
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        System.out.println("提取的邮箱地址:");
        emailPattern.matcher(sampleText)
            .results()
            .map(java.util.regex.MatchResult::group)
            .forEach(email -> System.out.println("  " + email));
        
        // 电话号码匹配
        Pattern phonePattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");
        System.out.println("\n提取的电话号码:");
        phonePattern.matcher(sampleText)
            .results()
            .map(java.util.regex.MatchResult::group)
            .forEach(phone -> System.out.println("  " + phone));
        
        // 使用Stream处理匹配结果
        System.out.println("\n邮箱域名统计:");
        Map<String, Long> domainCount = emailPattern.matcher(sampleText)
            .results()
            .map(java.util.regex.MatchResult::group)
            .map(email -> email.substring(email.indexOf("@") + 1))
            .collect(java.util.stream.Collectors.groupingBy(
                domain -> domain,
                java.util.stream.Collectors.counting()
            ));
        
        domainCount.forEach((domain, count) -> 
            System.out.println("  " + domain + ": " + count + "个"));
        
        // 复杂替换操作
        Pattern nameEmailPattern = Pattern.compile("(\\w+)的邮箱是\\s+([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})");
        String formattedContacts = nameEmailPattern.matcher(sampleText)
            .replaceAll(matchResult -> {
                String name = matchResult.group(1);
                String email = matchResult.group(2);
                return String.format("联系人: %s <%s>", name, email);
            });
        
        System.out.println("\n格式化后的联系信息:");
        System.out.println(formattedContacts);
    }

    /**
     * 数字格式化演示 (Java 8特性)
     * NumberFormat和DecimalFormat的使用
     */
    @Java8Feature(value = "数字格式化", desc = "NumberFormat和DecimalFormat提供灵活的数字格式化")
    public void demonstrateNumberFormatting() {
        System.out.println("\n=== 数字格式化演示 ===");
        
        double[] numbers = { 1234567.89, 0.1234, 98.5, 0.0056, 1000000 };
        
        // 默认数字格式
        NumberFormat defaultFormat = NumberFormat.getInstance(Locale.CHINA);
        System.out.println("默认数字格式:");
        for (double num : numbers) {
            System.out.println("  " + num + " -> " + defaultFormat.format(num));
        }
        
        // 货币格式
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        System.out.println("\n货币格式:");
        for (double num : numbers) {
            System.out.println("  " + num + " -> " + currencyFormat.format(num));
        }
        
        // 百分比格式
        NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.CHINA);
        double[] percentages = { 0.1234, 0.05, 0.985, 1.25 };
        System.out.println("\n百分比格式:");
        for (double pct : percentages) {
            System.out.println("  " + pct + " -> " + percentFormat.format(pct));
        }
        
        // 自定义格式
        DecimalFormat customFormat = new DecimalFormat("#,##0.00");
        System.out.println("\n自定义格式 (#,##0.00):");
        for (double num : numbers) {
            System.out.println("  " + num + " -> " + customFormat.format(num));
        }
        
        // 科学计数法
        DecimalFormat scientificFormat = new DecimalFormat("0.###E0");
        System.out.println("\n科学计数法:");
        for (double num : numbers) {
            System.out.println("  " + num + " -> " + scientificFormat.format(num));
        }
        
        // 条件格式化
        DecimalFormat conditionalFormat = new DecimalFormat("#,##0.##;(#,##0.##);'零'");
        double[] testNumbers = { 1234.56, -1234.56, 0 };
        System.out.println("\n条件格式化 (正数;负数;零):");
        for (double num : testNumbers) {
            System.out.println("  " + num + " -> " + conditionalFormat.format(num));
        }
    }

    /**
     * 文本分析和统计演示
     * 综合运用字符串和正则表达式进行文本分析
     */
    @Java8Feature(value = "文本分析统计", desc = "综合运用字符串处理功能进行文本分析")
    public void demonstrateTextAnalysis() {
        System.out.println("\n=== 文本分析和统计演示 ===");
        
        String article = """
            Java是一种面向对象的编程语言，由Sun公司开发。Java具有跨平台特性，
            一次编写，到处运行。Java广泛应用于企业级应用开发、Web开发、
            Android移动应用开发等领域。Java的核心特性包括面向对象、
            跨平台、安全性、简单性等。随着版本的不断更新，Java引入了
            许多新特性，如Lambda表达式、Stream API、模块系统等，
            使得Java编程更加高效和现代化。Java社区活跃，拥有丰富的
            第三方库和框架，如Spring、Hibernate等，大大提升了开发效率。
            """;
        
        // 基本统计信息
        System.out.println("文本基本统计:");
        System.out.println("  总字符数: " + article.length());
        System.out.println("  总行数: " + article.lines().count());
        
        // 词频统计
        Map<String, Long> wordFrequency = Arrays.stream(article.split("[\\s，。、]+"))
            .filter(word -> !word.trim().isEmpty())
            .collect(java.util.stream.Collectors.groupingBy(
                word -> word,
                java.util.stream.Collectors.counting()
            ));
        
        System.out.println("\n词频统计 (出现2次以上的词):");
        wordFrequency.entrySet().stream()
            .filter(entry -> entry.getValue() >= 2)
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue() + "次"));
        
        // 句子分析
        String[] sentences = article.split("[。！？]");
        System.out.println("\n句子分析:");
        System.out.println("  句子总数: " + sentences.length);
        
        OptionalDouble avgLength = Arrays.stream(sentences)
            .mapToInt(String::length)
            .average();
        avgLength.ifPresent(avg -> 
            System.out.println("  平均句子长度: " + String.format("%.1f", avg) + "个字符"));
        
        // 关键词提取
        Set<String> keywords = Set.of("Java", "编程", "开发", "特性", "应用");
        System.out.println("\n关键词出现情况:");
        keywords.forEach(keyword -> {
            long count = Arrays.stream(article.split("\\s+"))
                .filter(word -> word.contains(keyword))
                .count();
            if (count > 0) {
                System.out.println("  " + keyword + ": " + count + "次");
            }
        });
        
        // 文本复杂度分析
        double uniqueWordRatio = (double) wordFrequency.size() / wordFrequency.values().stream().mapToLong(Long::longValue).sum();
        System.out.println("\n文本复杂度:");
        System.out.println("  独特词汇比例: " + String.format("%.2f%%", uniqueWordRatio * 100));
        
        // 最长和最短的句子
        Optional<String> longestSentence = Arrays.stream(sentences)
            .max(Comparator.comparing(String::length));
        Optional<String> shortestSentence = Arrays.stream(sentences)
            .min(Comparator.comparing(String::length));
        
        longestSentence.ifPresent(sentence -> 
            System.out.println("  最长句子: " + sentence.trim() + " (" + sentence.length() + "字符)"));
        shortestSentence.ifPresent(sentence -> 
            System.out.println("  最短句子: " + sentence.trim() + " (" + sentence.length() + "字符)"));
    }

    /**
     * 字符串模板演示概念 (Java 21预览特性)
     * 展示字符串插值的概念
     */
    @Java21Feature(value = "字符串模板概念", desc = "字符串模板提供类似其他语言的字符串插值功能")
    public void demonstrateStringTemplatesConcept() {
        System.out.println("\n=== 字符串模板概念演示 ===");
        
        // 注意：字符串模板是Java 21的预览特性
        System.out.println("Java 21字符串模板将提供以下特性:");
        System.out.println("1. 字符串插值语法");
        System.out.println("2. 类型安全的字符串构建");
        System.out.println("3. 更好的性能");
        
        // 使用传统方式展示对比
        String name = "张三";
        int age = 25;
        double salary = 15000.50;
        String department = "技术部";
        
        System.out.println("\n传统字符串格式化方式:");
        String traditional1 = String.format("员工信息: 姓名=%s, 年龄=%d, 薪资=%.2f, 部门=%s", 
            name, age, salary, department);
        System.out.println("  " + traditional1);
        
        String traditional2 = "员工信息: 姓名=" + name + ", 年龄=" + age + 
            ", 薪资=" + String.format("%.2f", salary) + ", 部门=" + department;
        System.out.println("  " + traditional2);
        
        System.out.println("\nJava 21字符串模板预期语法 (概念展示):");
        System.out.println("  STR.\"员工信息: 姓名=\\{name}, 年龄=\\{age}, 薪资=\\{salary}, 部门=\\{department}\"");
        System.out.println("  实际输出将是: " + traditional1);
        
        // 复杂表达式插值概念
        System.out.println("\n复杂表达式插值概念:");
        System.out.println("  传统方式计算: 年薪 = " + (salary * 12) + "元");
        System.out.println("  模板语法概念: STR.\"年薪 = \\{salary * 12}元\"");
        
        // JSON构建示例
        System.out.println("\nJSON构建对比:");
        String jsonTraditional = String.format("""
            {
              "name": "%s",
              "age": %d,
              "salary": %.2f,
              "department": "%s"
            }""", name, age, salary, department);
        System.out.println("传统方式:");
        System.out.println(jsonTraditional);
        
        System.out.println("\n字符串模板概念:");
        System.out.println("JSON.\"\"\"");
        System.out.println("{");
        System.out.println("  \"name\": \"\\{name}\",");
        System.out.println("  \"age\": \\{age},");
        System.out.println("  \"salary\": \\{salary},");
        System.out.println("  \"department\": \"\\{department}\"");
        System.out.println("}\"\"\"");
    }

    /**
     * 运行所有文本处理演示
     */
    public void runAllDemos() {
        System.out.println("开始文本处理增强演示...\n");
        
        demonstrateStringEnhancements();
        demonstrateTextBlocks();
        demonstrateStringNewMethods();
        demonstrateRegexEnhancements();
        demonstrateNumberFormatting();
        demonstrateTextAnalysis();
        demonstrateStringTemplatesConcept();
        
        System.out.println("\n文本处理增强演示完成！");
    }
}