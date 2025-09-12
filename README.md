# Java 版本对比演示项目

> **Java 8 vs Java 21 特性全面对比与演示**

本项目通过实际代码演示Java 8到Java 21的重要特性变化，帮助开发者了解和学习Java语言的演进。

## 📋 项目概述

### 主要目标
- 🔍 **对比展示**：直观对比Java 8与Java 21的特性差异
- 🎯 **实用演示**：通过实际可运行的代码展示各种特性
- 📚 **学习资源**：为Java开发者提供版本升级参考
- 🏷️ **版本标注**：使用自定义注解清晰标识各特性的Java版本

### 技术栈
- **Java版本**：Java 21 (兼容展示Java 8特性)
- **构建工具**：Maven 3.8+
- **测试框架**：JUnit 5
- **开发环境**：支持Java 21的IDE (推荐IDEA 2023.2+)

## 🚀 快速开始

### 环境要求
- ✅ Java 21 或更高版本
- ✅ Maven 3.8 或更高版本
- ✅ 支持Java 21的IDE

### 安装运行

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd dida-ai
   ```

2. **编译项目**
   ```bash
   mvn clean compile
   ```

3. **运行所有演示**
   ```bash
   mvn exec:java -Dexec.mainClass="com.demo.utils.DemoRunner"
   ```

4. **运行特定演示**
   ```bash
   # 运行语言特性演示
   mvn exec:java -Dexec.mainClass="com.demo.utils.DemoRunner" -Dexec.args="language"
   
   # 运行并发特性演示
   mvn exec:java -Dexec.mainClass="com.demo.utils.DemoRunner" -Dexec.args="concurrent"
   ```

## 📂 项目结构

```
src/main/java/com/demo/
├── annotations/          # 版本特性标注注解
│   ├── Java8Feature.java
│   ├── Java9Feature.java
│   ├── Java11Feature.java
│   ├── Java17Feature.java
│   └── Java21Feature.java
├── language/            # 语言特性增强
│   └── LanguageEnhancementsDemo.java
├── collections/         # 集合框架增强
│   └── CollectionsDemo.java
├── streams/            # Stream API增强
│   └── StreamsDemo.java
├── concurrent/         # 并发编程增强
│   └── ConcurrencyDemo.java
├── io/                 # I/O操作增强
│   └── IODemo.java
├── text/               # 文本处理增强
│   └── TextProcessingDemo.java
├── records/            # Records特性
│   └── RecordsDemo.java
├── pattern/            # 模式匹配
│   └── PatternMatchingDemo.java
├── sealed/             # 密封类
│   └── SealedClassesDemo.java
├── preview/            # 预览特性
│   └── PreviewFeaturesDemo.java
└── utils/              # 工具类
    └── DemoRunner.java
```

## 🎯 特性演示模块

### 1. 语言特性增强 (`language`)

**Java 8 引入的核心特性：**
- 🔥 **Lambda表达式** - 函数式编程支持
- 🔗 **方法引用** - Lambda的简化形式
- 📦 **Optional类** - 空值处理
- 🎛️ **接口默认方法** - 接口演进支持

**Java 11+ 增强：**
- 🆚 **var关键字** - 类型推断
- 🔍 **instanceof模式匹配** (Java 17)
- 🎯 **switch表达式** (Java 17)

```java
// Java 8: Lambda表达式
list.forEach(item -> System.out.println(item));

// Java 11: var关键字
var message = "Hello Java";

// Java 17: instanceof模式匹配
if (obj instanceof String str) {
    System.out.println(str.toUpperCase());
}
```

### 2. 集合框架增强 (`collections`)

**Java 8 特性：**
- 🎭 **Stream集成** - 函数式数据处理
- 🛠️ **便利方法** - forEach, removeIf等
- ⚡ **ConcurrentHashMap增强** - 并发安全操作

**Java 9+ 特性：**
- 🏭 **不可变集合工厂** - List.of(), Set.of(), Map.of()
- 🔄 **toArray()增强** (Java 11)

```java
// Java 9: 不可变集合
List<String> languages = List.of("Java", "Python", "JavaScript");

// Java 8: Stream操作
Map<String, List<Employee>> byDept = employees.stream()
    .collect(groupingBy(Employee::getDepartment));
```

### 3. Stream API增强 (`streams`)

**Java 8 核心功能：**
- 🌊 **基础Stream操作** - filter, map, reduce
- 📊 **收集器** - Collectors的丰富功能
- ⚡ **并行Stream** - 多核处理能力

**Java 9+ 增强：**
- 🎯 **takeWhile/dropWhile** - 条件操作
- 📝 **Stream.toList()** (Java 16) - 简化收集

```java
// Java 8: 基础操作
List<String> result = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Java 16: 简化收集
List<String> result = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .toList();
```

### 4. 并发编程增强 (`concurrent`)

**Java 8 特性：**
- 🔮 **CompletableFuture** - 异步编程框架
- 🍴 **ForkJoinPool** - 工作窃取算法
- ⚛️ **原子操作增强** - updateAndGet等

**Java 21 重大突破：**
- 🚀 **虚拟线程** - Project Loom的核心特性
- 🏗️ **结构化并发** (预览) - 任务组管理

```java
// Java 8: CompletableFuture
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> processData())
    .thenApply(String::toUpperCase);

// Java 21: 虚拟线程
Thread virtualThread = Thread.ofVirtual().start(() -> {
    // 可以安全地阻塞，成本极低
    processLongRunningTask();
});
```

### 5. Records特性 (`records`)

**Java 17 重要特性：**
- 📋 **数据载体类** - 简洁的不可变数据结构
- 🔧 **自动生成方法** - 构造器、getter、equals、hashCode、toString
- ✅ **紧凑构造器** - 数据验证
- 🎯 **泛型支持** - 类型安全

```java
// 传统类需要20+行代码
public record Person(String name, int age, String city) {}

// 自动获得所有标准方法
Person person = new Person("张三", 25, "北京");
System.out.println(person.name()); // 访问字段
```

### 6. 模式匹配 (`pattern`)

**Java 17 特性：**
- 🎯 **instanceof模式匹配** - 自动类型转换
- 🔄 **switch表达式增强** - 更灵活的分支

**Java 21 预览特性：**
- 📋 **Record模式匹配** - Record解构
- 🏗️ **嵌套模式匹配** - 复杂结构处理
- 🛡️ **守卫条件** - when子句

```java
// Java 17: instanceof模式匹配
if (obj instanceof String str && str.length() > 10) {
    System.out.println("长字符串: " + str.toUpperCase());
}

// Java 21 预览: Record模式匹配 (概念)
switch (person) {
    case Person(var name, var age, "工程师") -> handleEngineer(name, age);
    case Person(var name, var age, var job) when age > 30 -> handleSenior(name, job);
}
```

### 7. 密封类 (`sealed`)

**Java 17 特性：**
- 🔒 **继承控制** - 限制类的继承层次
- 🎯 **完备性检查** - 编译器保证覆盖所有情况
- 🏗️ **类型层次设计** - 更好的API设计
- 📋 **与Record结合** - 类型安全的数据结构

```java
// 密封类定义
public sealed abstract class Shape 
    permits Circle, Rectangle, Triangle {
}

// switch表达式无需default分支
return switch (shape) {
    case Circle c -> Math.PI * c.radius() * c.radius();
    case Rectangle r -> r.length() * r.width();
    case Triangle t -> calculateTriangleArea(t);
};
```

## 🎮 演示命令

### 运行全部演示
```bash
mvn exec:java
```

### 运行特定模块
```bash
# 语言特性
mvn exec:java -Dexec.args="language"

# 集合框架
mvn exec:java -Dexec.args="collections"

# Stream API
mvn exec:java -Dexec.args="streams"

# 并发编程 (包括虚拟线程)
mvn exec:java -Dexec.args="concurrent"

# Records特性
mvn exec:java -Dexec.args="records"

# 模式匹配
mvn exec:java -Dexec.args="pattern"

# 密封类
mvn exec:java -Dexec.args="sealed"
```

## 📊 特性版本对比表

| 特性分类 | Java 8 | Java 9-11 | Java 17 LTS | Java 21 LTS |
|---------|--------|-----------|-------------|-------------|
| **语言语法** | Lambda表达式<br>方法引用<br>接口默认方法 | var关键字 | instanceof模式匹配<br>switch表达式<br>文本块 | 字符串模板(预览)<br>未命名变量(预览) |
| **数据结构** | Optional<br>Stream API | 不可变集合工厂 | **Records**<br>**密封类** | Record模式匹配(预览) |
| **并发编程** | CompletableFuture<br>并行Stream | CompletableFuture增强 | - | **虚拟线程**<br>结构化并发(预览) |
| **集合框架** | Stream集成<br>便利方法 | takeWhile/dropWhile | - | - |
| **模式匹配** | - | - | instanceof<br>switch增强 | Record解构(预览)<br>守卫条件(预览) |

## 🏗️ 版本标注系统

项目使用自定义注解标识特性的Java版本：

```java
@Java8Feature(value = "Lambda表达式", description = "函数式编程支持")
@Java17Feature(value = "Records", description = "简洁的数据载体类")
@Java21Feature(value = "虚拟线程", description = "轻量级线程，支持数百万并发")
```

## 🔧 开发配置

### Maven配置要点
- **Java版本**: 21
- **编译器参数**: `--enable-preview` (预览特性)
- **编码**: UTF-8
- **测试框架**: JUnit 5

### IDE配置建议
- **语言级别**: Java 21
- **预览特性**: 启用
- **编码**: UTF-8
- **代码风格**: 使用现代Java语法

## 📈 性能对比

### 虚拟线程性能提升
```java
// 传统线程池: 100个线程处理1000个任务
ExecutorService traditional = Executors.newFixedThreadPool(100);
// 耗时: ~500ms

// 虚拟线程: 1000个虚拟线程处理1000个任务  
ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();
// 耗时: ~50ms (10倍性能提升)
```

### 代码简洁性对比
```java
// 传统类: ~25行代码
public class TraditionalPoint {
    private final int x, y;
    // 构造器、getter、equals、hashCode、toString...
}

// Record: 1行代码
public record Point(int x, int y) {}
```

## 🚨 注意事项

### Java 21预览特性
以下特性需要使用 `--enable-preview` 编译选项：
- 字符串模板
- 结构化并发
- Record模式匹配
- 未命名变量和模式

### 兼容性说明
- 项目需要Java 21运行环境
- 展示Java 8特性时保持向后兼容
- 预览特性可能在未来版本中发生变化

## 🤝 贡献指南

### 添加新特性演示
1. 在相应模块中添加演示方法
2. 使用正确的版本注解标注
3. 添加详细的中文注释
4. 更新README文档
5. 添加单元测试

### 代码规范
- 使用中文注释和文档
- 方法名使用英文，注释使用中文
- 保持代码简洁易懂
- 添加版本特性注解

## 📚 学习资源

### 官方文档
- [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [OpenJDK Java 21](https://openjdk.org/projects/jdk/21/)

### 重要JEP (Java Enhancement Proposal)
- **JEP 444**: 虚拟线程
- **JEP 440**: Record模式匹配 (预览)
- **JEP 430**: 字符串模板 (预览)
- **JEP 409**: 密封类
- **JEP 395**: Records

## 📄 许可证

本项目仅用于学习和演示目的。

---

**🎯 开始探索Java的演进之路，体验从Java 8到Java 21的精彩特性！**