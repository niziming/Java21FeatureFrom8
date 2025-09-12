package com.demo.streams;

import com.demo.annotations.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Stream API演示
 * 展示Java 8到Java 21 Stream API的特性和增强
 */
public class StreamsDemo {

    /**
     * 基础Stream操作演示 (Java 8特性)
     * Stream的创建、中间操作和终端操作
     */
    @Java8Feature(value = "Stream API基础", desc = "函数式风格的数据处理管道，支持顺序和并行处理")
    public void demonstrateBasicStreamOperations() {
        System.out.println("=== Stream API基础操作演示 ===");
        
        // 创建Stream的不同方式
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // 1. 从集合创建
        Stream<Integer> fromCollection = numbers.stream();
        
        // 2. 使用Stream.of()
        Stream<String> fromValues = Stream.of("Java", "Python", "JavaScript", "C++");
        
        // 3. 使用Stream.generate()
        Stream<Double> randomNumbers = Stream.generate(Math::random).limit(5);
        
        // 4. 使用Stream.iterate()
        Stream<Integer> sequence = Stream.iterate(0, n -> n + 2).limit(10);
        
        System.out.println("随机数: " + randomNumbers.collect(Collectors.toList()));
        System.out.println("等差数列: " + sequence.collect(Collectors.toList()));
        
        // 基础操作链式调用
        List<Integer> result = numbers.stream()
            .filter(n -> n % 2 == 0)        // 过滤偶数
            .map(n -> n * n)                // 平方
            .sorted(Comparator.reverseOrder()) // 倒序排序
            .collect(Collectors.toList());   // 收集结果
        
        System.out.println("偶数平方倒序: " + result);
        
        // 字符串处理示例
        List<String> words = Arrays.asList("Java编程", "Stream处理", "函数式编程", "Lambda表达式");
        List<String> processed = words.stream()
            .filter(word -> word.length() > 3)
            .map(String::toUpperCase)
            .sorted()
            .collect(Collectors.toList());
        
        System.out.println("处理后的词汇: " + processed);
    }

    /**
     * Stream收集器演示 (Java 8特性)
     * Collectors类的各种收集操作
     */
    @Java8Feature(value = "Stream收集器", desc = "Collectors提供各种数据收集和聚合操作")
    public void demonstrateStreamCollectors() {
        System.out.println("\n=== Stream收集器演示 ===");
        
        List<Product> products = Arrays.asList(
            new Product("笔记本电脑", "电子产品", 5999.0, 50),
            new Product("智能手机", "电子产品", 3999.0, 120),
            new Product("耳机", "电子产品", 299.0, 200),
            new Product("书桌", "家具", 899.0, 30),
            new Product("椅子", "家具", 599.0, 45),
            new Product("台灯", "家具", 199.0, 80)
        );
        
        // 基本收集
        List<String> productNames = products.stream()
            .map(Product::getName)
            .collect(Collectors.toList());
        System.out.println("商品名称: " + productNames);
        
        // 分组收集
        Map<String, List<Product>> productsByCategory = products.stream()
            .collect(Collectors.groupingBy(Product::getCategory));
        
        System.out.println("按类别分组:");
        productsByCategory.forEach((category, productList) -> {
            System.out.println("  " + category + ": " + productList.size() + "个商品");
        });
        
        // 统计信息收集
        DoubleSummaryStatistics priceStats = products.stream()
            .collect(Collectors.summarizingDouble(Product::getPrice));
        
        System.out.println("价格统计信息:");
        System.out.printf("  数量: %d, 总价: %.2f, 平均价: %.2f, 最低价: %.2f, 最高价: %.2f\n",
            priceStats.getCount(), priceStats.getSum(), priceStats.getAverage(),
            priceStats.getMin(), priceStats.getMax());
        
        // 分组统计
        Map<String, Double> avgPriceByCategory = products.stream()
            .collect(Collectors.groupingBy(
                Product::getCategory,
                Collectors.averagingDouble(Product::getPrice)
            ));
        
        System.out.println("各类别平均价格:");
        avgPriceByCategory.forEach((category, avgPrice) ->
            System.out.printf("  %s: %.2f元\n", category, avgPrice));
        
        // 连接字符串
        String expensiveProducts = products.stream()
            .filter(p -> p.getPrice() > 1000)
            .map(Product::getName)
            .collect(Collectors.joining(", ", "昂贵商品: [", "]"));
        
        System.out.println(expensiveProducts);
        
        // 分区收集
        Map<Boolean, List<Product>> partitionedProducts = products.stream()
            .collect(Collectors.partitioningBy(p -> p.getPrice() > 1000));
        
        System.out.println("高价商品数量: " + partitionedProducts.get(true).size());
        System.out.println("普通商品数量: " + partitionedProducts.get(false).size());
    }

    /**
     * 并行Stream演示 (Java 8特性)
     * 展示并行处理的能力和注意事项
     */
    @Java8Feature(value = "并行Stream", desc = "利用多核处理器进行并行数据处理")
    public void demonstrateParallelStreams() {
        System.out.println("\n=== 并行Stream演示 ===");
        
        // 创建大量数据用于演示
        List<Integer> largeList = IntStream.rangeClosed(1, 10_000_000)
            .boxed()
            .collect(Collectors.toList());
        
        // 顺序处理
        long startTime = System.currentTimeMillis();
        long sequentialSum = largeList.stream()
            .mapToLong(Integer::longValue)
            .sum();
        long sequentialTime = System.currentTimeMillis() - startTime;
        
        // 并行处理
        startTime = System.currentTimeMillis();
        long parallelSum = largeList.parallelStream()
            .mapToLong(Integer::longValue)
            .sum();
        long parallelTime = System.currentTimeMillis() - startTime;
        
        System.out.println("数据量: " + largeList.size());
        System.out.println("顺序处理结果: " + sequentialSum + ", 耗时: " + sequentialTime + "ms");
        System.out.println("并行处理结果: " + parallelSum + ", 耗时: " + parallelTime + "ms");
        System.out.println("性能提升: " + (sequentialTime > 0 ? (double)sequentialTime / parallelTime : "N/A") + "倍");
        
        // 并行处理示例 - 查找质数
        List<Integer> numbers = IntStream.rangeClosed(2, 1000).boxed().collect(Collectors.toList());
        
        startTime = System.currentTimeMillis();
        List<Integer> primes = numbers.parallelStream()
            .filter(this::isPrime)
            .collect(Collectors.toList());
        long primeTime = System.currentTimeMillis() - startTime;
        
        System.out.println("找到质数数量: " + primes.size() + ", 耗时: " + primeTime + "ms");
        System.out.println("前10个质数: " + primes.stream().limit(10).collect(Collectors.toList()));
    }

    /**
     * 判断是否为质数
     */
    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * Stream新增方法演示 (Java 9特性)
     * takeWhile, dropWhile, ofNullable等新方法
     */
    @Java9Feature(value = "Stream新增方法", desc = "takeWhile, dropWhile, ofNullable等新的Stream操作方法")
    public void demonstrateJava9StreamEnhancements() {
        System.out.println("\n=== Java 9 Stream增强演示 ===");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // takeWhile - 从开头开始取元素直到条件不满足
        List<Integer> takeWhileResult = numbers.stream()
            .takeWhile(n -> n < 6)
            .collect(Collectors.toList());
        System.out.println("takeWhile(< 6): " + takeWhileResult);
        
        // dropWhile - 从开头开始丢弃元素直到条件不满足
        List<Integer> dropWhileResult = numbers.stream()
            .dropWhile(n -> n < 6)
            .collect(Collectors.toList());
        System.out.println("dropWhile(< 6): " + dropWhileResult);
        
        // ofNullable - 创建可能为null的Stream
        Stream<String> nullableStream1 = Stream.ofNullable("存在的值");
        Stream<String> nullableStream2 = Stream.ofNullable(null);
        
        System.out.println("非空Stream: " + nullableStream1.collect(Collectors.toList()));
        System.out.println("空Stream: " + nullableStream2.collect(Collectors.toList()));
        
        // iterate with predicate - 带条件的迭代
        List<Integer> iterateWithPredicate = Stream.iterate(1, n -> n <= 100, n -> n * 2)
            .collect(Collectors.toList());
        System.out.println("2的幂次(<=100): " + iterateWithPredicate);
        
        // 实际应用示例 - 处理日志数据
        List<String> logLines = Arrays.asList(
            "INFO: 系统启动",
            "DEBUG: 连接数据库",
            "INFO: 用户登录",
            "ERROR: 数据库连接失败",
            "DEBUG: 重试连接",
            "INFO: 连接成功",
            "WARN: 内存使用率过高"
        );
        
        // 获取第一个ERROR之前的所有日志
        List<String> beforeError = logLines.stream()
            .takeWhile(line -> !line.startsWith("ERROR"))
            .collect(Collectors.toList());
        System.out.println("ERROR前的日志: " + beforeError);
        
        // 跳过所有DEBUG日志后的内容
        List<String> afterDebug = logLines.stream()
            .dropWhile(line -> line.startsWith("DEBUG") || line.startsWith("INFO"))
            .collect(Collectors.toList());
        System.out.println("跳过INFO和DEBUG后: " + afterDebug);
    }

    /**
     * Collectors新增方法演示 (Java 9-11特性)
     * filtering, flatMapping等新的收集器
     */
    @Java9Feature(value = "Collectors增强", desc = "filtering, flatMapping等新的收集器方法")
    public void demonstrateCollectorsEnhancements() {
        System.out.println("\n=== Collectors增强演示 ===");
        
        List<Product> products = Arrays.asList(
            new Product("笔记本电脑", "电子产品", 5999.0, 50),
            new Product("智能手机", "电子产品", 3999.0, 120),
            new Product("耳机", "电子产品", 299.0, 200),
            new Product("书桌", "家具", 899.0, 30),
            new Product("椅子", "家具", 599.0, 45)
        );
        
        // filtering - 在分组时进行过滤
        Map<String, List<Product>> expensiveProductsByCategory = products.stream()
            .collect(Collectors.groupingBy(
                Product::getCategory,
                Collectors.filtering(p -> p.getPrice() > 500, Collectors.toList())
            ));
        
        System.out.println("各类别高价商品:");
        expensiveProductsByCategory.forEach((category, productList) -> {
            System.out.println("  " + category + ": " + 
                productList.stream().map(Product::getName).collect(Collectors.joining(", ")));
        });
        
        // flatMapping - 在分组时进行扁平化映射
        Map<String, List<String>> productTagsByCategory = products.stream()
            .collect(Collectors.groupingBy(
                Product::getCategory,
                Collectors.flatMapping(
                    p -> Stream.of(p.getName().split("\\s+")), 
                    Collectors.toList()
                )
            ));
        
        System.out.println("各类别商品标签:");
        productTagsByCategory.forEach((category, tags) -> {
            System.out.println("  " + category + ": " + String.join(", ", tags));
        });
    }

    /**
     * Stream.toList()演示 (Java 16特性)
     * 简化的列表收集方法
     */
    @Java17Feature(value = "Stream.toList()", desc = "简化的不可变列表收集方法，无需使用Collectors.toList()")
    public void demonstrateStreamToList() {
        System.out.println("\n=== Stream.toList()演示 ===");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Java 16之前的方式
        List<Integer> evenNumbersOld = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        
        // Java 16的新方式
        List<Integer> evenNumbersNew = numbers.stream()
            .filter(n -> n % 2 == 0)
            .toList();
        
        System.out.println("传统方式收集偶数: " + evenNumbersOld);
        System.out.println("新方式收集偶数: " + evenNumbersNew);
        
        // 字符串处理示例
        List<String> languages = Arrays.asList("Java", "Python", "JavaScript", "C++", "Go");
        List<String> longLanguages = languages.stream()
            .filter(lang -> lang.length() > 4)
            .map(String::toUpperCase)
            .toList();
        
        System.out.println("长语言名称: " + longLanguages);
        
        // 注意：toList()返回的是不可变列表
        try {
            longLanguages.add("Kotlin");
        } catch (UnsupportedOperationException e) {
            System.out.println("toList()返回不可变列表: " + e.getMessage());
        }
    }

    /**
     * 复杂Stream操作综合演示
     * 展示实际应用场景中的Stream处理
     */
    @Java8Feature(value = "复杂Stream操作", desc = "展示Stream在实际业务场景中的综合应用")
    public void demonstrateComplexStreamOperations() {
        System.out.println("\n=== 复杂Stream操作综合演示 ===");
        
        // 模拟订单数据
        List<Order> orders = Arrays.asList(
            new Order("ORD001", "张三", Arrays.asList(
                new OrderItem("笔记本电脑", 5999.0, 1),
                new OrderItem("鼠标", 99.0, 2)
            )),
            new Order("ORD002", "李四", Arrays.asList(
                new OrderItem("智能手机", 3999.0, 1),
                new OrderItem("手机壳", 29.0, 1),
                new OrderItem("充电器", 59.0, 1)
            )),
            new Order("ORD003", "王五", Arrays.asList(
                new OrderItem("书桌", 899.0, 1),
                new OrderItem("椅子", 599.0, 1),
                new OrderItem("台灯", 199.0, 1)
            ))
        );
        
        // 1. 计算每个订单的总金额
        Map<String, Double> orderTotals = orders.stream()
            .collect(Collectors.toMap(
                Order::getOrderId,
                order -> order.getItems().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum()
            ));
        
        System.out.println("订单总金额:");
        orderTotals.forEach((orderId, total) -> 
            System.out.printf("  %s: %.2f元\n", orderId, total));
        
        // 2. 找出最受欢迎的商品（按销量）
        Map<String, Integer> productSales = orders.stream()
            .flatMap(order -> order.getItems().stream())
            .collect(Collectors.groupingBy(
                OrderItem::getProductName,
                Collectors.summingInt(OrderItem::getQuantity)
            ));
        
        Optional<Map.Entry<String, Integer>> topProduct = productSales.entrySet().stream()
            .max(Map.Entry.comparingByValue());
        
        topProduct.ifPresent(entry -> 
            System.out.println("最受欢迎商品: " + entry.getKey() + " (销量: " + entry.getValue() + ")"));
        
        // 3. 按客户统计购买情况
        Map<String, CustomerSummary> customerSummaries = orders.stream()
            .collect(Collectors.groupingBy(
                Order::getCustomerName,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    orderList -> new CustomerSummary(
                        orderList.size(),
                        orderList.stream()
                            .flatMap(order -> order.getItems().stream())
                            .mapToDouble(item -> item.getPrice() * item.getQuantity())
                            .sum(),
                        orderList.stream()
                            .flatMap(order -> order.getItems().stream())
                            .mapToInt(OrderItem::getQuantity)
                            .sum()
                    )
                )
            ));
        
        System.out.println("客户购买统计:");
        customerSummaries.forEach((customer, summary) ->
            System.out.printf("  %s: %d个订单, %.2f元, %d件商品\n",
                customer, summary.getOrderCount(), summary.getTotalAmount(), summary.getTotalItems()));
        
        // 4. 查找高价值订单（总金额>3000）的客户
        Set<String> highValueCustomers = orders.stream()
            .filter(order -> order.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum() > 3000)
            .map(Order::getCustomerName)
            .collect(Collectors.toSet());
        
        System.out.println("高价值客户: " + highValueCustomers);
    }

    /**
     * 商品类
     */
    public static class Product {
        private String name;
        private String category;
        private double price;
        private int stock;
        
        public Product(String name, String category, double price, int stock) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.stock = stock;
        }
        
        // Getters
        public String getName() { return name; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }
    }

    /**
     * 订单类
     */
    public static class Order {
        private String orderId;
        private String customerName;
        private List<OrderItem> items;
        
        public Order(String orderId, String customerName, List<OrderItem> items) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.items = items;
        }
        
        // Getters
        public String getOrderId() { return orderId; }
        public String getCustomerName() { return customerName; }
        public List<OrderItem> getItems() { return items; }
    }

    /**
     * 订单项类
     */
    public static class OrderItem {
        private String productName;
        private double price;
        private int quantity;
        
        public OrderItem(String productName, double price, int quantity) {
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
        }
        
        // Getters
        public String getProductName() { return productName; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }

    /**
     * 客户统计类
     */
    public static class CustomerSummary {
        private int orderCount;
        private double totalAmount;
        private int totalItems;
        
        public CustomerSummary(int orderCount, double totalAmount, int totalItems) {
            this.orderCount = orderCount;
            this.totalAmount = totalAmount;
            this.totalItems = totalItems;
        }
        
        // Getters
        public int getOrderCount() { return orderCount; }
        public double getTotalAmount() { return totalAmount; }
        public int getTotalItems() { return totalItems; }
    }

    /**
     * 运行所有Stream演示
     */
    public void runAllDemos() {
        System.out.println("开始Stream API演示...\n");
        
        demonstrateBasicStreamOperations();
        demonstrateStreamCollectors();
        demonstrateParallelStreams();
        demonstrateJava9StreamEnhancements();
        demonstrateCollectorsEnhancements();
        demonstrateStreamToList();
        demonstrateComplexStreamOperations();
        
        System.out.println("\nStream API演示完成！");
    }
}