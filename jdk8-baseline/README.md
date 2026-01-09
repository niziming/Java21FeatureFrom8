# JDK 8 Baseline Module

## 📦 模块包结构

```
jdk8-baseline/
└── src/main/java/com/javaevolution/jdk8/
    ├── syntax/
    │   └── LambdaExpressions.java          # Lambda 表达式与函数式接口
    ├── api/
    │   ├── StreamAPIDemo.java               # Stream API 完整演示
    │   └── OptionalDemo.java                # Optional 使用演示
    ├── time/
    │   └── DateTimeAPIDemo.java             # JSR 310 Date-Time API
    ├── concurrency/
    │   ├── CompletableFutureDemo.java       # 异步编程
    │   └── ConcurrencyEnhancements.java     # StampedLock, LongAdder
    ├── io/
    │   └── (待补充: Files, Paths)
    └── jvm/
        └── (待补充: Metaspace 演示)
```

## 🎯 核心特性覆盖

### 1. Lambda 表达式与函数式接口 (JEP 126)
- ✅ 所有内置函数式接口: `Predicate`, `Function`, `Consumer`, `Supplier`, `BiFunction`
- ✅ 4 种方法引用: 静态、实例、类型、构造器
- ✅ 闭包与 Effectively Final
- ✅ 柯里化、递归 Lambda

### 2. Stream API (JEP 107)
- ✅ 中间操作: `filter`, `map`, `flatMap`, `distinct`, `sorted`, `limit`, `skip`, `peek`
- ✅ 终端操作: `collect`, `reduce`, `forEach`, `findFirst`, `anyMatch`, `allMatch`
- ✅ Collectors 工具: `toList`, `toSet`, `toMap`, `groupingBy`, `partitioningBy`, `joining`
- ✅ 并行流陷阱与最佳实践

### 3. Optional (JEP 126)
- ✅ 创建方式: `of`, `ofNullable`, `empty`
- ✅ 转换操作: `map`, `flatMap`, `filter`
- ✅ 终端操作: `orElse`, `orElseGet`, `orElseThrow`, `ifPresent`
- ✅ 级联调用避免 NPE

### 4. Date-Time API (JSR 310)
- ✅ `LocalDate`, `LocalTime`, `LocalDateTime`
- ✅ `Instant` vs `LocalDateTime` 区别
- ✅ `ZonedDateTime` 时区处理
- ✅ `Duration` vs `Period` 区别
- ✅ `TemporalAdjusters` 日期调整器
- ✅ 格式化与解析

### 5. CompletableFuture (JEP 155)
- ✅ 创建方式: `supplyAsync`, `runAsync`
- ✅ 链式操作: `thenApply`, `thenAccept`, `thenRun`
- ✅ 组合操作: `thenCompose`, `thenCombine`, `thenAcceptBoth`
- ✅ Either 操作: `applyToEither`, `acceptEither`
- ✅ 异常处理: `exceptionally`, `handle`, `whenComplete`
- ✅ 批量操作: `allOf`, `anyOf`

### 6. 并发增强 (JEP 155)
- ✅ **StampedLock**: 乐观读锁、锁升级
- ✅ **LongAdder/LongAccumulator**: 高性能计数器
- ✅ **ConcurrentHashMap 增强**: `forEach`, `search`, `reduce`, `compute`, `merge`

## 🚀 运行示例

```bash
cd jdk8-baseline
mvn clean compile
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk8.syntax.LambdaExpressions"
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk8.api.StreamAPIDemo"
mvn exec:java -Dexec.mainClass="com.javaevolution.jdk8.concurrency.CompletableFutureDemo"
```

## 📚 其他待补充特性
- Interface Default Methods
- Method Parameter Reflection
- Base64 Encoding
- Nashorn JavaScript Engine
- Metaspace 替代 PermGen 演示
