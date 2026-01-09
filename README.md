# Java LTS Encyclopedia (Java LTS 全书)

> 🚀 全量覆盖 Java 8 到 Java 21 各个 LTS 版本所有新特性的参考手册级项目

[![Java](https://img.shields.io/badge/Java-8%20to%2021-orange.svg)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 📋 项目概述

这是一个**百科全书式的技术演示项目**，旨在为Java开发者提供从JDK 8到JDK 21所有LTS版本特性的完整参考实现。

### 🎯 核心目标

- ✅ **全量覆盖**: 不仅包含语法糖，还包含API增强、底层工具、并发改进等所有细节
- ✅ **JEP标注**: 每个特性都标注对应的JEP编号，便于追溯官方文档
- ✅ **实战导向**: 提供真实场景的使用示例，而非简单的Hello World
- ✅ **最佳实践**: 展示推荐用法和常见陷阱
- ✅ **性能对比**: 包含关键特性的性能基准测试

## 🏗️ 项目结构

```
java-lts-evolution/
├── pom.xml                          # 父工程POM
├── README.md                        # 项目主文档
│
├── jdk8-baseline/                   # Java 8 基准版本
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/javaevolution/jdk8/
│       ├── syntax/                  # Lambda表达式、方法引用
│       ├── api/                     # Stream API、Optional
│       ├── time/                    # Date-Time API (JSR 310)
│       ├── concurrency/             # CompletableFuture、StampedLock、LongAdder
│       ├── io/                      # Files、Paths
│       └── jvm/                     # Metaspace
│
├── jdk11-lts-comprehensive/         # Java 11 LTS (含JDK 9/10/11)
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/javaevolution/jdk11/
│       ├── module/                  # JPMS (模块系统)
│       ├── syntax/                  # var关键字、接口私有方法
│       ├── api/                     # Collection Factory、Stream增强
│       ├── net/                     # HTTP Client、WebSocket
│       ├── process/                 # Process API、StackWalker
│       └── lang/                    # String增强、Files增强
│
├── jdk17-lts-comprehensive/         # Java 17 LTS (含JDK 12-17)
│   ├── pom.xml
│   ├── README.md
│   └── src/main/java/com/javaevolution/jdk17/
│       ├── syntax/                  # Records、Text Blocks、Sealed Classes
│       ├── pattern/                 # Pattern Matching (instanceof)
│       ├── switch/                  # Switch Expressions
│       ├── lang/                    # NPE增强、Stream.toList()
│       ├── net/                     # Unix Domain Socket
│       └── util/                    # HexFormat、RandomGenerator
│
└── jdk21-lts-comprehensive/         # Java 21 LTS (含JDK 18-21)
    ├── pom.xml
    ├── README.md
    └── src/main/java/com/javaevolution/jdk21/
        ├── concurrency/             # Virtual Threads、Structured Concurrency
        ├── syntax/                  # Pattern Matching (Switch)、Record Patterns
        ├── api/                     # Sequenced Collections
        ├── ffi/                     # Foreign Function & Memory API
        ├── scope/                   # Scoped Values
        └── incubator/               # Vector API
```

## 📚 模块详解

### 🟢 JDK 8 Baseline (基准版本)

<details>
<summary><strong>核心特性清单</strong> (点击展开)</summary>

#### Lambda & Functional
- ✅ 所有函数式接口: `Predicate`, `Function`, `Consumer`, `Supplier`, `BiFunction`, `UnaryOperator`, `BinaryOperator`
- ✅ 4种方法引用: 静态、实例、类型、构造器
- ✅ 闭包与Effectively Final
- ✅ 柯里化、递归Lambda

#### Stream API (JEP 107)
- ✅ 中间操作: `filter`, `map`, `flatMap`, `distinct`, `sorted`, `limit`, `skip`, `peek`
- ✅ 终端操作: `collect`, `reduce`, `forEach`, `findFirst`, `anyMatch`, `allMatch`, `noneMatch`
- ✅ Collectors: `toList`, `toSet`, `toMap`, `groupingBy`, `partitioningBy`, `joining`, `summarizing`
- ✅ 并行流陷阱与最佳实践

#### Optional (JEP 126)
- ✅ `map`, `flatMap`, `filter`, `orElse`, `orElseGet`, `orElseThrow`
- ✅ 级联调用避免NPE

#### Date-Time API (JSR 310)
- ✅ `LocalDate`, `LocalTime`, `LocalDateTime`, `Instant`, `ZonedDateTime`
- ✅ `Duration` vs `Period`
- ✅ `TemporalAdjusters` 日期调整器
- ✅ 时区处理与格式化

#### CompletableFuture (JEP 155)
- ✅ 创建: `supplyAsync`, `runAsync`
- ✅ 链式: `thenApply`, `thenAccept`, `thenRun`, `thenCompose`, `thenCombine`
- ✅ Either: `applyToEither`, `acceptEither`
- ✅ 异常: `exceptionally`, `handle`, `whenComplete`
- ✅ 批量: `allOf`, `anyOf`

#### 并发增强
- ✅ **StampedLock**: 乐观读锁、锁升级
- ✅ **LongAdder**: 高性能计数器
- ✅ **ConcurrentHashMap**: `forEach`, `search`, `reduce`, `compute`, `merge`

</details>

**运行示例:**
```bash
cd jdk8-baseline
mvn clean compile
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk8.syntax.LambdaExpressions"
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk8.api.StreamAPIDemo"
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk8.concurrency.CompletableFutureDemo"
```

---

### 🟡 JDK 11 LTS Comprehensive (含 JDK 9/10/11)

<details>
<summary><strong>核心特性清单</strong> (点击展开)</summary>

#### 模块系统 (JEP 261)
- ✅ `module-info.java`: `exports`, `requires`, `opens`, `provides`, `uses`
- ✅ 模块化JDK
- ✅ jlink创建自定义运行时镜像

#### 语法增强
- ✅ **var** (JEP 286): 局部变量类型推断
- ✅ **Lambda var** (JEP 323): Lambda参数使用var
- ✅ 接口私有方法 (JEP 213)
- ✅ Try-with-resources增强 (JEP 213)

#### Collection Factory (JEP 269)
- ✅ `List.of`, `Set.of`, `Map.of`, `Map.ofEntries`
- ✅ 不可变集合特性

#### Stream API 增强 (JEP 271)
- ✅ `takeWhile`, `dropWhile`
- ✅ `iterate` 重载 (带条件)
- ✅ `ofNullable`

#### HTTP Client (JEP 321)
- ✅ 同步/异步请求
- ✅ HTTP/2支持
- ✅ WebSocket
- ✅ Server Push

#### 其他API
- ✅ **Process API** (JEP 102): `ProcessHandle`
- ✅ **StackWalker** (JEP 259)
- ✅ **String增强**: `lines()`, `strip()`, `isBlank()`, `repeat()`
- ✅ **Files增强**: `readString()`, `writeString()`
- ✅ **Optional增强**: `ifPresentOrElse()`, `or()`, `stream()`, `isEmpty()`

</details>

**运行示例:**
```bash
cd jdk11-lts-comprehensive
mvn clean compile
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk11.syntax.VarKeywordDemo"
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk11.net.HTTPClientDemo"
```

---

### 🟠 JDK 17 LTS Comprehensive (含 JDK 12-17)

<details>
<summary><strong>核心特性清单</strong> (点击展开)</summary>

#### Records (JEP 395)
- ✅ 基础Record定义
- ✅ 紧凑构造器 (Compact Constructor)
- ✅ 自定义构造器与方法
- ✅ Record实现接口
- ✅ 嵌套Record、泛型Record

#### Text Blocks (JEP 378)
- ✅ 多行字符串
- ✅ 缩进控制
- ✅ 转义字符: `\`, `\s`
- ✅ `formatted()`, `stripIndent()`, `translateEscapes()`
- ✅ JSON、SQL、HTML示例

#### Sealed Classes (JEP 409)
- ✅ `sealed`, `non-sealed`, `final`
- ✅ Sealed Interface
- ✅ 穷举性检查
- ✅ 表达式问题解决方案

#### Pattern Matching
- ✅ `instanceof` Pattern Matching (JEP 394)
- ✅ Switch Expressions (JEP 361)
- ✅ `yield` 关键字

#### 其他增强
- ✅ **NPE增强** (JEP 358): Helpful NullPointerExceptions
- ✅ **Stream.toList()** (JEP 333)
- ✅ **Unix Domain Socket** (JEP 380)
- ✅ **HexFormat** (JEP 412)
- ✅ **RandomGenerator** (JEP 356)

</details>

**运行示例:**
```bash
cd jdk17-lts-comprehensive
mvn clean compile
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk17.syntax.RecordsDemo"
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk17.syntax.TextBlocksDemo"
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk17.syntax.SealedClassesDemo"
```

---

### 🔴 JDK 21 LTS Comprehensive (含 JDK 18-21)

<details>
<summary><strong>核心特性清单</strong> (点击展开)</summary>

#### Virtual Threads (JEP 444) - Project Loom
- ✅ `Thread.ofVirtual()`, `Thread.startVirtualThread()`
- ✅ `Executors.newVirtualThreadPerTaskExecutor()`
- ✅ 性能压测: 100万虚拟线程
- ✅ Carrier Thread、Pinning问题
- ✅ 实战: Web服务器、数据处理、爬虫

#### Pattern Matching (JEP 441)
- ✅ **Switch Pattern Matching**: 类型模式、Guarded Patterns
- ✅ **Record Patterns** (JEP 440): 解构、嵌套解构
- ✅ **null** 处理
- ✅ 穷举性检查
- ✅ 实战: 表达式求值、JSON解析、状态机

#### Sequenced Collections (JEP 431)
- ✅ `SequencedCollection`: `addFirst`, `addLast`, `getFirst`, `getLast`, `reversed()`
- ✅ `SequencedSet`: LinkedHashSet, TreeSet
- ✅ `SequencedMap`: LinkedHashMap, TreeMap
- ✅ 实战: LRU Cache、访问历史

#### Foreign Function & Memory API (JEP 442)
- ✅ `Arena`, `Linker`, `MemorySegment`
- ✅ 调用C函数 (替代JNI)
- ✅ 堆外内存管理

#### Structured Concurrency (JEP 453 - Preview)
- ✅ `StructuredTaskScope`
- ✅ 任务分发与合并
- ✅ 错误处理

#### Scoped Values (JEP 446 - Preview)
- ✅ 替代ThreadLocal
- ✅ 不可变共享数据

#### Vector API (JEP 448 - Incubator)
- ✅ SIMD计算
- ✅ 性能优化

#### Simple Web Server (JEP 408)
- ✅ `SimpleFileServer`
- ✅ 快速启动HTTP服务器

</details>

**运行示例:**
```bash
cd jdk21-lts-comprehensive
mvn clean compile
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk21.concurrency.VirtualThreadsDemo"
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk21.syntax.PatternMatchingDemo"
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk21.api.SequencedCollectionsDemo"
```

## 🚀 快速开始

### 环境要求

- **JDK 21** (推荐使用最新版本)
- **Maven 3.8+**
- **IDE**: IntelliJ IDEA 2023+ 或 Eclipse 2023+

### 克隆项目

```bash
git clone https://github.com/yourusername/java-lts-encyclopedia.git
cd java-lts-encyclopedia
```

### 编译所有模块

```bash
mvn clean install
```

### 运行特定模块

```bash
# JDK 8
cd jdk8-baseline
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk8.syntax.LambdaExpressions"

# JDK 11
cd jdk11-lts-comprehensive
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk11.net.HTTPClientDemo"

# JDK 17
cd jdk17-lts-comprehensive
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk17.syntax.RecordsDemo"

# JDK 21
cd jdk21-lts-comprehensive
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk21.concurrency.VirtualThreadsDemo"
```

## 📖 学习路径

### 初学者路径 (Java 8 → 11)

1. **Lambda & Stream** (jdk8-baseline)
   - `LambdaExpressions.java` - 掌握函数式编程
   - `StreamAPIDemo.java` - 流式数据处理
   
2. **Optional & Date-Time** (jdk8-baseline)
   - `OptionalDemo.java` - 优雅处理null
   - `DateTimeAPIDemo.java` - 现代日期时间API

3. **var & HTTP Client** (jdk11-lts-comprehensive)
   - `VarKeywordDemo.java` - 类型推断
   - `HTTPClientDemo.java` - 现代HTTP客户端

### 进阶路径 (Java 11 → 17)

1. **Records & Text Blocks** (jdk17-lts-comprehensive)
   - `RecordsDemo.java` - 数据类简化
   - `TextBlocksDemo.java` - 多行字符串

2. **Sealed Classes** (jdk17-lts-comprehensive)
   - `SealedClassesDemo.java` - 受限继承体系

### 高级路径 (Java 17 → 21)

1. **Virtual Threads** (jdk21-lts-comprehensive)
   - `VirtualThreadsDemo.java` - 百万级并发

2. **Pattern Matching** (jdk21-lts-comprehensive)
   - `PatternMatchingDemo.java` - 现代控制流

3. **Sequenced Collections** (jdk21-lts-comprehensive)
   - `SequencedCollectionsDemo.java` - 有序集合

## 🎓 特性对比表

| 特性 | JDK 8 | JDK 11 | JDK 17 | JDK 21 |
|-----|-------|--------|--------|--------|
| Lambda表达式 | ✅ | ✅ | ✅ | ✅ |
| Stream API | ✅ | 增强✅ | 增强✅ | 增强✅ |
| var关键字 | ❌ | ✅ | ✅ | ✅ |
| HTTP Client | ❌ | ✅ | ✅ | ✅ |
| Records | ❌ | ❌ | ✅ | ✅ |
| Text Blocks | ❌ | ❌ | ✅ | ✅ |
| Sealed Classes | ❌ | ❌ | ✅ | ✅ |
| Pattern Matching | ❌ | ❌ | instanceof | switch✅ |
| Virtual Threads | ❌ | ❌ | ❌ | ✅ |
| Sequenced Collections | ❌ | ❌ | ❌ | ✅ |

## 💡 最佳实践

### 代码规范

- ✅ 所有特性都有JEP标注
- ✅ 注释清晰，中英文对照
- ✅ 包含正例和反例
- ✅ 提供性能对比

### 学习建议

1. **按顺序学习**: 从JDK 8开始，逐步升级
2. **动手实践**: 运行所有示例代码
3. **对比差异**: 理解每个版本的改进
4. **查阅JEP**: 深入理解设计动机

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

### 贡献类型

- 🐛 Bug修复
- ✨ 新特性补充
- 📝 文档改进
- 🎨 代码优化

## 📄 License

MIT License - 详见 [LICENSE](LICENSE) 文件

## 🙏 致谢

- OpenJDK团队
- Java社区所有贡献者
- 所有提供反馈的开发者

## 📞 联系方式

- **作者**: Your Name
- **Email**: your.email@example.com
- **GitHub**: [@yourusername](https://github.com/yourusername)

---

<div align="center">
  <strong>⭐ 如果这个项目对你有帮助，请给一个Star！ ⭐</strong>
</div>
