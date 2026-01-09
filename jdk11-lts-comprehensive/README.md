# JDK 11 LTS Comprehensive Module

## 📦 模块包结构树状图

```
jdk11-lts-comprehensive/
└── src/main/java/com/javaevolution/jdk11/
    ├── syntax/
    │   ├── VarKeywordDemo.java                    # JEP 286: var关键字 (JDK 10)
    │   │                                           # JEP 323: Lambda参数var (JDK 11)
    │   ├── InterfacePrivateMethodsDemo.java       # JEP 213: 接口私有方法 (JDK 9)
    │   └── TryWithResourcesEnhancementDemo.java   # JEP 213: Try增强 (JDK 9)
    │
    ├── api/
    │   ├── CollectionAndStreamDemo.java           # JEP 269: Collection Factory (JDK 9)
    │   │                                           # JEP 271: Stream增强 (JDK 9)
    │   ├── OptionalEnhancementsDemo.java          # Optional增强 (JDK 9/11)
    │   └── CollectorsEnhancementsDemo.java        # Collectors增强 (JDK 9)
    │
    ├── net/
    │   └── HTTPClientDemo.java                    # JEP 321: HTTP Client (JDK 11)
    │                                               # 同步/异步请求、HTTP/2、WebSocket
    │
    ├── process/
    │   ├── ProcessAPIDemo.java                    # JEP 102: Process API (JDK 9)
    │   └── StackWalkerDemo.java                   # JEP 259: StackWalker (JDK 9)
    │
    ├── lang/
    │   ├── StringEnhancementsDemo.java            # String新方法 (JDK 11)
    │   └── FilesEnhancementsDemo.java             # Files新方法 (JDK 11)
    │
    └── module/
        ├── module-info.java                       # JEP 261: 模块系统 (JDK 9)
        └── ModuleSystemDemo.java                  # JPMS完整演示
```

## 🎯 最具代表性的3个复杂特性

### 1️⃣ HTTP Client API (JEP 321) - `HTTPClientDemo.java`

**特性说明**: 
- JDK 11引入的标准化HTTP客户端，替代旧的`HttpURLConnection`
- 支持HTTP/1.1和HTTP/2
- 提供同步和异步API
- 支持WebSocket

**核心代码**:

```java
// 同步请求
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.github.com/users/octocat"))
    .build();
HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

// 异步请求
CompletableFuture<HttpResponse<String>> futureResponse = 
    client.sendAsync(request, BodyHandlers.ofString());

futureResponse
    .thenApply(HttpResponse::body)
    .thenAccept(System.out::println)
    .join();

// WebSocket
WebSocket ws = client.newWebSocketBuilder()
    .buildAsync(URI.create("wss://echo.websocket.org"), listener)
    .join();
```

**实战价值**:
- 替代Apache HttpClient等第三方库
- 更好的HTTP/2支持
- 与CompletableFuture无缝集成

---

### 2️⃣ Collection Factory Methods (JEP 269) - `CollectionAndStreamDemo.java`

**特性说明**:
- JDK 9引入的便捷工厂方法创建不可变集合
- 比`Collections.unmodifiableXXX()`更简洁高效
- 不允许null元素，编译时检查

**核心代码**:

```java
// List.of - 创建不可变List
List<String> list = List.of("Java", "Python", "Go");

// Set.of - 创建不可变Set (不允许重复)
Set<Integer> set = Set.of(1, 2, 3, 4, 5);

// Map.of - 创建不可变Map (最多10个键值对)
Map<String, Integer> map = Map.of(
    "Java", 11,
    "Python", 3,
    "Go", 1
);

// Map.ofEntries - 超过10个键值对
Map<String, Integer> largeMap = Map.ofEntries(
    Map.entry("k1", 1),
    Map.entry("k2", 2),
    Map.entry("k3", 3)
);

// List.copyOf - 防御性复制
List<String> mutableList = new ArrayList<>(Arrays.asList("A", "B"));
List<String> immutableCopy = List.copyOf(mutableList);
```

**Stream API 增强** (JEP 271):

```java
// takeWhile - 从头开始取，直到条件不满足
List<Integer> result = List.of(1, 2, 3, 4, 5, 3, 2, 1).stream()
    .takeWhile(n -> n < 5)
    .collect(Collectors.toList()); // [1, 2, 3, 4]

// dropWhile - 丢弃元素，直到条件不满足
List<Integer> result2 = List.of(1, 2, 3, 4, 5, 3, 2, 1).stream()
    .dropWhile(n -> n < 5)
    .collect(Collectors.toList()); // [5, 3, 2, 1]

// iterate带条件
Stream.iterate(0, n -> n < 10, n -> n + 1)
    .forEach(System.out::println); // 等价于 for(int i=0; i<10; i++)

// ofNullable - 处理可能为null的单个元素
List<String> list = Arrays.asList("A", null, "B", null, "C");
list.stream()
    .flatMap(Stream::ofNullable)
    .forEach(System.out::println); // A, B, C
```

**实战价值**:
- 简化不可变集合创建
- 提高代码可读性
- 减少样板代码

---

### 3️⃣ var 关键字 (JEP 286 & JEP 323) - `VarKeywordDemo.java`

**特性说明**:
- JDK 10引入局部变量类型推断
- JDK 11允许在Lambda参数中使用var
- 编译时特性，无运行时开销

**核心代码**:

```java
// 基础用法
var message = "Hello, Java 10!";  // String
var number = 42;                   // int
var list = new ArrayList<String>();
var map = new HashMap<String, Integer>();

// 复杂泛型类型
var complexMap = new HashMap<String, List<Map<String, Object>>>();

// Stream中使用
var numbers = List.of(1, 2, 3, 4, 5);
var doubled = numbers.stream()
    .map(n -> n * 2)
    .collect(Collectors.toList());

// JDK 11: Lambda参数中使用var
Function<String, Integer> f = (var s) -> s.length();

// 主要用途: 添加注解
Function<String, String> toUpper = (@NonNull var s) -> s.toUpperCase();

// for循环
for (var item : list) {
    System.out.println(item);
}

for (var i = 0; i < 10; i++) {
    System.out.println(i);
}
```

**限制**:
```java
// ❌ 不能用于字段
private var name = "test";  // 编译错误

// ❌ 不能用于方法参数 (JDK 10)
public void test(var param) {}  // JDK 10编译错误

// ❌ 不能没有初始化器
var x;  // 编译错误

// ❌ 不能初始化为null
var x = null;  // 编译错误

// ❌ 不能用于Lambda表达式
var f = x -> x * 2;  // 编译错误
```

**最佳实践**:

```java
// ✅ 好的用法: 类型明显
var name = "John";
var list = new ArrayList<String>();
var builder = new StringBuilder();

// ❌ 不好: 类型不明显
var result = calculate();  // 返回什么类型?

// ❌ 不好: Diamond operator
var list = new ArrayList<>();  // ArrayList<Object>

// ✅ 改进: 明确类型
var list = new ArrayList<String>();
```

**实战价值**:
- 减少冗余代码
- 提高可读性（复杂泛型）
- 配合Stream API更简洁

---

## 🚀 其他重要特性简要说明

### String 增强
```java
// lines() - 按行分割
"A\nB\nC".lines().forEach(System.out::println);

// strip() - 去除Unicode空白
" hello ".strip(); // "hello"

// isBlank() - 判断是否为空白
"   ".isBlank(); // true

// repeat() - 重复字符串
"AB".repeat(3); // "ABABAB"
```

### Files 增强
```java
// readString / writeString
String content = Files.readString(Path.of("file.txt"));
Files.writeString(Path.of("file.txt"), "Hello");
```

### Optional 增强
```java
// ifPresentOrElse (JDK 9)
optional.ifPresentOrElse(
    value -> System.out.println(value),
    () -> System.out.println("No value")
);

// or (JDK 9)
Optional<String> result = optional.or(() -> Optional.of("Default"));

// stream (JDK 9)
optional.stream().forEach(System.out::println);

// isEmpty (JDK 11)
if (optional.isEmpty()) { ... }
```

## 📊 JEP清单

| JEP | 标题 | 版本 |
|-----|------|------|
| JEP 261 | Module System | JDK 9 |
| JEP 269 | Convenience Factory Methods for Collections | JDK 9 |
| JEP 271 | Stream Enhancements | JDK 9 |
| JEP 102 | Process API Updates | JDK 9 |
| JEP 259 | Stack-Walking API | JDK 9 |
| JEP 213 | Milling Project Coin | JDK 9 |
| JEP 286 | Local-Variable Type Inference | JDK 10 |
| JEP 321 | HTTP Client (Standard) | JDK 11 |
| JEP 323 | Local-Variable Syntax for Lambda Parameters | JDK 11 |
| JEP 330 | Launch Single-File Source-Code Programs | JDK 11 |

## 🎓 学习建议

1. **先掌握var**: 提高日常开发效率
2. **熟悉Collection Factory**: 替代Collections工具类
3. **学习HTTP Client**: 替代第三方HTTP库
4. **理解Stream增强**: `takeWhile`/`dropWhile`很实用
5. **了解模块系统**: 大型项目必备

## 🔗 参考资料

- [JDK 9 Release Notes](https://www.oracle.com/java/technologies/javase/9-relnotes.html)
- [JDK 10 Release Notes](https://www.oracle.com/java/technologies/javase/10-relnote.html)
- [JDK 11 Release Notes](https://www.oracle.com/java/technologies/javase/11-relnote.html)
