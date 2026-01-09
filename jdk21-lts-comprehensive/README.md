# JDK 21 LTS Comprehensive Module

## 📦 模块包结构树状图

```
jdk21-lts-comprehensive/
└── src/main/java/com/javaevolution/jdk21/
    ├── concurrency/
    │   ├── VirtualThreadsDemo.java                # JEP 444: Virtual Threads (JDK 21)
    │   ├── StructuredConcurrencyDemo.java         # JEP 453: Structured Concurrency (Preview)
    │   └── ScopedValuesDemo.java                  # JEP 446: Scoped Values (Preview)
    │
    ├── syntax/
    │   ├── PatternMatchingDemo.java               # JEP 441: Switch模式匹配 (JDK 21)
    │   │                                           # JEP 440: Record Patterns (JDK 21)
    │   └── UnnamedPatternsDemo.java               # JEP 443: Unnamed Patterns (Preview)
    │
    ├── api/
    │   ├── SequencedCollectionsDemo.java          # JEP 431: Sequenced Collections (JDK 21)
    │   └── StringTemplatesDemo.java               # JEP 430: String Templates (Preview)
    │
    ├── ffi/
    │   └── ForeignFunctionMemoryDemo.java         # JEP 442: FFM API (JDK 21)
    │
    ├── incubator/
    │   └── VectorAPIDemo.java                     # JEP 448: Vector API (Incubator)
    │
    └── tools/
        └── SimpleWebServerDemo.java               # JEP 408: Simple Web Server (JDK 18)
```

## 🎯 最具代表性的3个复杂特性

### 1️⃣ Virtual Threads (JEP 444) - `VirtualThreadsDemo.java`

**特性说明**:
- **Project Loom**的核心成果
- 轻量级线程，由JVM管理
- 突破传统线程池限制，支持**百万级并发**
- 非常适合**I/O密集型**任务

**核心代码**:

```java
// 创建虚拟线程
Thread vThread = Thread.ofVirtual().start(() -> {
    System.out.println("Virtual Thread: " + Thread.currentThread());
});

// 简化方式
Thread.startVirtualThread(() -> {
    System.out.println("Hello from virtual thread!");
});

// Executor: 每个任务一个虚拟线程
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> {
            try {
                Thread.sleep(Duration.ofSeconds(1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}

// 检查是否为虚拟线程
Thread thread = Thread.currentThread();
boolean isVirtual = thread.isVirtual(); // true/false
```

**性能压测: 100万虚拟线程**:

```java
public void millionVirtualThreads() {
    long start = System.currentTimeMillis();
    
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
        // 创建 100 万个虚拟线程!
        IntStream.range(0, 1_000_000).forEach(i -> {
            executor.submit(() -> {
                try {
                    Thread.sleep(Duration.ofSeconds(1));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });
    }
    
    long end = System.currentTimeMillis();
    System.out.println("Time: " + (end - start) + "ms"); // ~1500ms
}
```

**实战应用: Web服务器**:

```java
public void webServerSimulation() {
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
        // 模拟10000个并发请求
        for (int i = 0; i < 10_000; i++) {
            int requestId = i;
            executor.submit(() -> handleRequest(requestId));
        }
    }
}

private void handleRequest(int requestId) {
    // 模拟数据库查询 (50ms)
    Thread.sleep(Duration.ofMillis(50));
    
    // 模拟业务处理
    String result = processBusinessLogic(requestId);
    
    // 模拟外部API调用 (30ms)
    Thread.sleep(Duration.ofMillis(30));
}
```

**关键概念**:

```java
// 1. Carrier Thread (载体线程)
// 虚拟线程运行在平台线程上

// 2. Pinning (钉住)
// ❌ synchronized会导致pinning
synchronized (lock) {
    Thread.sleep(100);  // 虚拟线程无法卸载
}

// ✅ 使用ReentrantLock
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    Thread.sleep(100);  // 虚拟线程可以卸载
} finally {
    lock.unlock();
}
```

**虚拟线程 vs 平台线程**:

| 特性 | 平台线程 | 虚拟线程 |
|------|---------|---------|
| 创建成本 | 高 (~1MB栈) | 低 (~1KB栈) |
| 调度 | OS调度 | JVM调度 |
| 数量限制 | 数千 | 数百万 |
| 适用场景 | CPU密集型 | I/O密集型 |
| 阻塞影响 | 阻塞OS线程 | 只阻塞虚拟线程 |

**最佳实践**:

```java
// ❌ 不要池化虚拟线程
ExecutorService pool = Executors.newFixedThreadPool(100, 
    Thread.ofVirtual().factory()); // 不推荐!

// ✅ 每个任务一个虚拟线程
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

// ❌ 避免使用synchronized
// ✅ 使用ReentrantLock

// ❌ ThreadLocal开销大
// ✅ 使用ScopedValues (JDK 21 Preview)
```

---

### 2️⃣ Pattern Matching for switch (JEP 441) + Record Patterns (JEP 440)

**特性说明**:
- JDK 21正式版的**强大模式匹配**
- 支持类型模式、守卫模式、Record解构
- 配合Sealed Classes实现穷举性检查

**核心代码**:

```java
// 1. 类型模式
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

// 2. Guarded Patterns (守卫模式)
public String classify(Object obj) {
    return switch (obj) {
        case String s when s.isEmpty() -> "Empty string";
        case String s when s.length() < 5 -> "Short string";
        case String s -> "Long string";
        case Integer i when i < 0 -> "Negative";
        case Integer i when i == 0 -> "Zero";
        case Integer i -> "Positive";
        default -> "Unknown";
    };
}

// 3. null处理
public String handleNull(String s) {
    return switch (s) {
        case null -> "null value";
        case "SPECIAL" -> "special";
        default -> "normal: " + s;
    };
}
```

**Record Patterns (解构)**:

```java
record Point(int x, int y) {}
record Circle(Point center, int radius) {}

// Record解构
public void printPoint(Object obj) {
    if (obj instanceof Point(int x, int y)) {
        System.out.println("Point: x=" + x + ", y=" + y);
    }
}

// 嵌套Record解构
public void printCircle(Object obj) {
    if (obj instanceof Circle(Point(int x, int y), int r)) {
        System.out.println("Circle at (" + x + "," + y + ") radius " + r);
    }
}

// Switch中的Record Patterns
public String describe(Object shape) {
    return switch (shape) {
        case Point(int x, int y) -> 
            "Point at (" + x + "," + y + ")";
        case Circle(Point(int x, int y), int r) -> 
            "Circle at (" + x + "," + y + ") radius " + r;
        default -> "Unknown";
    };
}

// Guarded Record Patterns
public String analyzePoint(Object obj) {
    return switch (obj) {
        case Point(int x, int y) when x == 0 && y == 0 -> "Origin";
        case Point(int x, int y) when x == y -> "On diagonal";
        case Point(int x, int y) when x > 0 && y > 0 -> "Quadrant I";
        case Point(int x, int y) -> "Other";
        default -> "Not a point";
    };
}
```

**实战应用: 表达式求值**:

```java
sealed interface Expr {}
record Constant(int value) implements Expr {}
record Negate(Expr expr) implements Expr {}
record Add(Expr left, Expr right) implements Expr {}
record Multiply(Expr left, Expr right) implements Expr {}

// 求值
public int eval(Expr expr) {
    return switch (expr) {
        case Constant(int value) -> value;
        case Negate(Expr e) -> -eval(e);
        case Add(Expr left, Expr right) -> eval(left) + eval(right);
        case Multiply(Expr left, Expr right) -> eval(left) * eval(right);
        // 不需要default,已穷举!
    };
}

// 优化: 常量折叠
public Expr simplify(Expr expr) {
    return switch (expr) {
        case Add(Constant(int a), Constant(int b)) -> new Constant(a + b);
        case Multiply(Constant(0), Expr e) -> new Constant(0);
        case Multiply(Expr e, Constant(0)) -> new Constant(0);
        case Multiply(Constant(1), Expr e) -> simplify(e);
        default -> expr;
    };
}
```

**实战应用: 状态机**:

```java
sealed interface State {}
record Idle() implements State {}
record Running(String taskId) implements State {}
record Paused(String taskId, long pausedAt) implements State {}

sealed interface Event {}
record Start(String taskId) implements Event {}
record Pause() implements Event {}
record Resume() implements Event {}

// 状态转换
public State transition(State currentState, Event event) {
    return switch (currentState) {
        case Idle() -> switch (event) {
            case Start(String taskId) -> new Running(taskId);
            default -> currentState;
        };
        case Running(String taskId) -> switch (event) {
            case Pause() -> new Paused(taskId, System.currentTimeMillis());
            default -> currentState;
        };
        case Paused(String taskId, long pausedAt) -> switch (event) {
            case Resume() -> new Running(taskId);
            default -> currentState;
        };
    };
}
```

---

### 3️⃣ Sequenced Collections (JEP 431) - `SequencedCollectionsDemo.java`

**特性说明**:
- 为集合框架引入**有序集合**的统一接口
- 新增`SequencedCollection`, `SequencedSet`, `SequencedMap`
- 提供`getFirst/getLast`, `addFirst/addLast`, `reversed()`方法

**核心代码**:

```java
// SequencedCollection
List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));

// 新方法
list.addFirst("FIRST");
list.addLast("LAST");
System.out.println(list); // [FIRST, A, B, C, D, LAST]

String first = list.getFirst();
String last = list.getLast();

list.removeFirst();
list.removeLast();

// reversed() - 反向视图
List<String> reversed = list.reversed();
System.out.println(reversed); // [D, C, B, A]

// 反向视图是可修改的
reversed.addFirst("Z");
System.out.println(list); // [A, B, C, D, Z]
```

**SequencedSet**:

```java
SequencedSet<String> set = new LinkedHashSet<>();
set.add("Java");
set.add("Python");
set.add("Go");

set.addFirst("First");
set.addLast("Last");

String first = set.getFirst();
String last = set.getLast();

// 反向视图
SequencedSet<String> reversed = set.reversed();

// TreeSet也实现了SequencedSet
SequencedSet<Integer> sortedSet = new TreeSet<>(List.of(5, 2, 8, 1));
int min = sortedSet.getFirst(); // 1
int max = sortedSet.getLast();  // 8
```

**SequencedMap**:

```java
SequencedMap<String, Integer> map = new LinkedHashMap<>();
map.put("Java", 1995);
map.put("Python", 1991);
map.put("Go", 2009);

// 新方法
Map.Entry<String, Integer> firstEntry = map.firstEntry();
Map.Entry<String, Integer> lastEntry = map.lastEntry();

Map.Entry<String, Integer> polled = map.pollFirstEntry();

map.putFirst("C++", 1985);
map.putLast("Swift", 2014);

// 反向视图
SequencedMap<String, Integer> reversed = map.reversed();

// 有序视图
SequencedSet<String> keys = map.sequencedKeySet();
SequencedCollection<Integer> values = map.sequencedValues();
SequencedSet<Map.Entry<String, Integer>> entries = map.sequencedEntrySet();
```

**实战应用: LRU Cache**:

```java
public class LRUCache<K, V> {
    private final int capacity;
    private final SequencedMap<K, V> cache;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>();
    }
    
    public V get(K key) {
        V value = cache.remove(key);
        if (value != null) {
            cache.putLast(key, value); // 移到最后(最近使用)
        }
        return value;
    }
    
    public void put(K key, V value) {
        cache.remove(key);
        cache.putLast(key, value);
        
        if (cache.size() > capacity) {
            cache.pollFirstEntry(); // 移除最久未使用的
        }
    }
}
```

**与旧API对比**:

```java
// JDK 21之前
List<String> list = new ArrayList<>();
String first = list.get(0);                    // ❌ 可能IndexOutOfBounds
String last = list.get(list.size() - 1);       // ❌ 繁琐
list.add(0, "FIRST");                          // ❌ 不直观

// JDK 21
String first = list.getFirst();                // ✅ 语义清晰
String last = list.getLast();                  // ✅ 简洁
list.addFirst("FIRST");                        // ✅ 直观
```

---

## 🚀 其他重要特性

### Structured Concurrency (Preview)
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user = scope.fork(() -> fetchUser());
    Future<String> order = scope.fork(() -> fetchOrder());
    
    scope.join();
    scope.throwIfFailed();
    
    String result = user.resultNow() + " " + order.resultNow();
}
```

### Scoped Values (Preview)
```java
// 替代ThreadLocal
final static ScopedValue<String> USER = ScopedValue.newInstance();

ScopedValue.where(USER, "Alice").run(() -> {
    System.out.println(USER.get()); // Alice
});
```

### Vector API (Incubator)
```java
// SIMD计算
VectorSpecies<Float> SPECIES = FloatVector.SPECIES_256;
FloatVector a = FloatVector.fromArray(SPECIES, array1, 0);
FloatVector b = FloatVector.fromArray(SPECIES, array2, 0);
FloatVector c = a.add(b);
```

## 📊 JEP清单

| JEP | 标题 | 版本 | 状态 |
|-----|------|------|------|
| JEP 444 | Virtual Threads | JDK 21 | Final |
| JEP 431 | Sequenced Collections | JDK 21 | Final |
| JEP 441 | Pattern Matching for switch | JDK 21 | Final |
| JEP 440 | Record Patterns | JDK 21 | Final |
| JEP 442 | Foreign Function & Memory API | JDK 21 | Final |
| JEP 453 | Structured Concurrency | JDK 21 | Preview |
| JEP 446 | Scoped Values | JDK 21 | Preview |
| JEP 430 | String Templates | JDK 21 | Preview |
| JEP 448 | Vector API (Sixth Incubator) | JDK 21 | Incubator |

## 🎓 学习建议

1. **必须掌握Virtual Threads**: 革命性的并发编程范式
2. **熟练使用Pattern Matching**: 简化代码逻辑
3. **了解Sequenced Collections**: 改进集合操作
4. **关注Preview特性**: Structured Concurrency等

## 🔗 参考资料

- [JDK 21 Release Notes](https://www.oracle.com/java/technologies/javase/21-relnote.html)
- [Project Loom](https://openjdk.org/projects/loom/)
