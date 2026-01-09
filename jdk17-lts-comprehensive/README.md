# JDK 17 LTS Comprehensive Module

## 📦 模块包结构树状图

```
jdk17-lts-comprehensive/
└── src/main/java/com/javaevolution/jdk17/
    ├── syntax/
    │   ├── RecordsDemo.java                       # JEP 395: Records (JDK 16)
    │   ├── TextBlocksDemo.java                    # JEP 378: Text Blocks (JDK 15)
    │   ├── SealedClassesDemo.java                 # JEP 409: Sealed Classes (JDK 17)
    │   └── PatternMatchingDemo.java               # JEP 394: instanceof模式匹配 (JDK 16)
    │
    ├── switch/
    │   └── SwitchExpressionsDemo.java             # JEP 361: Switch表达式 (JDK 14)
    │
    ├── lang/
    │   ├── HelpfulNPEDemo.java                    # JEP 358: NPE增强 (JDK 14)
    │   └── StreamToListDemo.java                  # JEP 333: toList() (JDK 16)
    │
    ├── net/
    │   └── UnixDomainSocketDemo.java              # JEP 380: Unix Domain Socket (JDK 16)
    │
    └── util/
        ├── HexFormatDemo.java                     # JEP 412: HexFormat (JDK 17)
        └── RandomGeneratorDemo.java               # JEP 356: RandomGenerator (JDK 17)
```

## 🎯 最具代表性的3个复杂特性

### 1️⃣ Records (JEP 395) - `RecordsDemo.java`

**特性说明**:
- JDK 16正式引入的数据载体类
- 自动生成构造器、getter、equals、hashCode、toString
- 不可变、紧凑、线程安全

**核心代码**:

```java
// 基础Record
public record Point(int x, int y) {}

// 使用
Point p = new Point(10, 20);
System.out.println(p.x());        // 10
System.out.println(p.y());        // 20
System.out.println(p);            // Point[x=10, y=20]

// 紧凑构造器 (Compact Constructor)
public record Range(int min, int max) {
    public Range {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        // 自动分配: this.min = min; this.max = max;
    }
}

// 自定义方法
public record Employee(String name, int age, double salary) {
    
    // 紧凑构造器: 数据验证和规范化
    public Employee {
        if (age < 18) {
            throw new IllegalArgumentException("Age must be >= 18");
        }
        name = name.trim().toUpperCase();
    }
    
    // 额外构造器
    public Employee(String name) {
        this(name, 18, 0.0);
    }
    
    // 自定义方法
    public double annualSalary() {
        return salary * 12;
    }
    
    // 静态方法
    public static Employee createIntern(String name) {
        return new Employee(name, 18, 3000.0);
    }
}

// Record实现接口
public record Circle(double radius) implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing circle with radius: " + radius);
    }
}

// 泛型Record
public record Pair<T, U>(T first, U second) {}

// 嵌套Record
public record Address(String street, String city) {}
public record Customer(String name, Address address) {}
```

**与Class对比**:

```java
// 传统Class: 60+行代码
public final class PointClass {
    private final int x;
    private final int y;
    
    public PointClass(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    @Override
    public boolean equals(Object obj) { /*...*/ }
    
    @Override
    public int hashCode() { /*...*/ }
    
    @Override
    public String toString() { /*...*/ }
}

// Record: 1行代码!
public record Point(int x, int y) {}
```

**实战应用**:

```java
// 1. DTO (Data Transfer Object)
public record UserDTO(Long id, String username, String email) {}

// 2. API响应
public record ApiResponse(int code, String message, Object data) {}

// 3. 配置类
public record DatabaseConfig(
    String host,
    int port,
    String username,
    String password
) {}

// 4. 值对象 (Value Object)
public record Money(double amount, String currency) {
    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(amount + other.amount, currency);
    }
}

// 5. 多返回值
public record SearchResult(int totalCount, List<String> items) {}
```

---

### 2️⃣ Text Blocks (JEP 378) - `TextBlocksDemo.java`

**特性说明**:
- JDK 15正式引入的多行字符串字面量
- 自动处理缩进
- 无需转义引号
- 非常适合JSON、SQL、HTML等

**核心代码**:

```java
// 传统多行字符串: 繁琐
String traditional = "{\n" +
    "  \"name\": \"John\",\n" +
    "  \"age\": 30\n" +
    "}";

// Text Block: 简洁直观
String textBlock = """
    {
      "name": "John",
      "age": 30
    }
    """;

// JSON示例
String json = """
    {
      "users": [
        {
          "id": 1,
          "name": "Alice",
          "email": "alice@example.com"
        },
        {
          "id": 2,
          "name": "Bob",
          "email": "bob@example.com"
        }
      ]
    }
    """;

// SQL示例
String sql = """
    SELECT u.id, u.name, o.order_id, o.total
    FROM users u
    INNER JOIN orders o ON u.id = o.user_id
    WHERE u.status = 'active'
      AND o.total > 100
    ORDER BY o.created_at DESC
    LIMIT 10
    """;

// HTML示例
String name = "John";
int age = 30;
String html = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>User Profile</title>
    </head>
    <body>
        <h1>%s</h1>
        <p>Age: %d</p>
    </body>
    </html>
    """.formatted(name, age);

// 转义字符
String oneLine = """
    This is a \
    single line
    """;  // "This is a single line"

// 保留尾随空格
String withSpaces = """
    Line 1\s
    Line 2\s\s
    """;
```

**缩进控制**:

```java
// 自动缩进: 基于最左边的非空白字符
String sql = """
        SELECT * FROM users
        WHERE age > 18
        ORDER BY name
        """;

// 显式缩进
String text = """
    Line 1
    """.indent(4);
```

**实战价值**:
- 大幅提升多行文本可读性
- 减少转义字符
- 配合`formatted()`方法构建动态内容

---

### 3️⃣ Sealed Classes (JEP 409) - `SealedClassesDemo.java`

**特性说明**:
- JDK 17正式引入的受限继承体系
- 显式控制谁可以继承/实现
- 配合Pattern Matching实现穷举性检查
- 解决表达式问题 (Expression Problem)

**核心代码**:

```java
// 基础Sealed Class
public sealed class Shape
    permits Circle, Rectangle, Triangle {
    public abstract double area();
}

// final: 不能再被继承
public final class Circle extends Shape {
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

// sealed: 可以继承，但必须指定permits
public sealed class Rectangle extends Shape
    permits Square {
    
    protected final double width;
    protected final double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
}

// non-sealed: 打开继承
public non-sealed class Triangle extends Shape {
    @Override
    public double area() { /*...*/ }
}

// Square继承自sealed Rectangle
public final class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }
}

// ❌ 编译错误: Pentagon不在permits列表中
// public class Pentagon extends Shape {}
```

**Sealed Interface**:

```java
public sealed interface Vehicle
    permits Car, Truck, Motorcycle {
    void start();
}

public final class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Car starting...");
    }
}

public non-sealed class Motorcycle implements Vehicle {
    @Override
    public void start() {
        System.out.println("Motorcycle starting...");
    }
}

// 可以继承non-sealed类
public class SportMotorcycle extends Motorcycle {}
```

**与Pattern Matching结合**:

```java
public sealed interface Result
    permits Success, Failure {}

public record Success(String data) implements Result {}
public record Failure(String error) implements Result {}

public void processResult(Result result) {
    // 穷举性检查: 不需要default
    String message = switch (result) {
        case Success(String data) -> "Success: " + data;
        case Failure(String error) -> "Failure: " + error;
    };
}
```

**实战应用**:

```java
// 1. 状态机建模
public sealed interface ConnectionState
    permits Disconnected, Connecting, Connected {}

public record Disconnected() implements ConnectionState {}
public record Connecting() implements ConnectionState {}
public record Connected(String sessionId) implements ConnectionState {}

// 2. 错误处理
public sealed interface ApiError
    permits ValidationError, AuthError, NetworkError {}

public record ValidationError(String field, String message) 
    implements ApiError {}
public record AuthError(String reason) 
    implements ApiError {}
public record NetworkError(int code, String message) 
    implements ApiError {}

// 3. 表达式求值
public sealed interface Expr
    permits Constant, Add, Multiply {}

public record Constant(int value) implements Expr {}
public record Add(Expr left, Expr right) implements Expr {}
public record Multiply(Expr left, Expr right) implements Expr {}

public int eval(Expr expr) {
    return switch (expr) {
        case Constant(int value) -> value;
        case Add(Expr left, Expr right) -> eval(left) + eval(right);
        case Multiply(Expr left, Expr right) -> eval(left) * eval(right);
    };
}
```

**优势**:
- 类型安全的枚举
- 穷举性检查 (编译时保证不会遗漏)
- 更好的API设计
- 解决表达式问题

---

## 🚀 其他重要特性

### Switch Expressions (JEP 361)
```java
// Switch表达式 (有返回值)
String result = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> "6 hours";
    case TUESDAY -> "7 hours";
    case THURSDAY, SATURDAY -> "8 hours";
    case WEDNESDAY -> "9 hours";
};

// yield关键字
int result = switch (value) {
    case 1 -> 10;
    case 2 -> {
        System.out.println("Complex logic");
        yield 20;
    }
    default -> 0;
};
```

### instanceof Pattern Matching (JEP 394)
```java
// 传统方式
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.toUpperCase());
}

// Pattern Matching
if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
}

// 在表达式中使用
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long string: " + s);
}
```

### Stream.toList() (JEP 333)
```java
// JDK 16之前
List<String> list1 = stream.collect(Collectors.toList());

// JDK 16+: 更简洁
List<String> list2 = stream.toList();
```

### HexFormat (JEP 412)
```java
// 字节数组转十六进制
byte[] bytes = {10, 20, 30};
String hex = HexFormat.of().formatHex(bytes); // "0a141e"

// 十六进制转字节数组
byte[] decoded = HexFormat.of().parseHex("0a141e");
```

## 📊 JEP清单

| JEP | 标题 | 版本 |
|-----|------|------|
| JEP 361 | Switch Expressions | JDK 14 |
| JEP 378 | Text Blocks | JDK 15 |
| JEP 394 | Pattern Matching for instanceof | JDK 16 |
| JEP 395 | Records | JDK 16 |
| JEP 409 | Sealed Classes | JDK 17 |
| JEP 358 | Helpful NullPointerExceptions | JDK 14 |
| JEP 333 | ZGC: Scalable Low-Latency GC | JDK 11/15 |
| JEP 356 | Enhanced Pseudo-Random Number Generators | JDK 17 |
| JEP 412 | Foreign Function & Memory API (Incubator) | JDK 17 |

## 🎓 学习建议

1. **优先学习Records**: 日常开发最实用
2. **掌握Text Blocks**: 大幅提升代码可读性
3. **理解Sealed Classes**: 设计优雅的API
4. **熟悉Pattern Matching**: 简化类型判断

## 🔗 参考资料

- [JDK 17 Release Notes](https://www.oracle.com/java/technologies/javase/17-relnote.html)
