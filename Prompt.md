# Role
你是一位对 OpenJDK 源码了如指掌的 Java 技术专家，擅长编写百科全书式的技术演示项目。

# Task
请生成一个名为 `java-lts-encyclopedia` (Java LTS 全书) 的 Maven 多模块项目结构及核心源码。
**核心目标**：打造一个**全量覆盖** Java 8 到 Java 21 各个 LTS 版本所有新特性的参考手册级项目。不仅包含语法糖，还要包含 API 增强、底层工具、并发改进等所有细节。

# Project Structure (项目结构)
项目包含父工程和 4 个子模块。
**关键要求**：每个模块内部必须采用**包名分类**（如 `syntax`, `api`, `io`, `lang`, `util`）来组织代码，因为特性非常多。

1. `java-lts-evolution` (Root)
2. `jdk8-baseline` (基准版本)
3. `jdk11-lts-comprehensive` (涵盖 JDK 9, 10, 11 所有特性)
4. `jdk17-lts-comprehensive` (涵盖 JDK 12-17 所有特性)
5. `jdk21-lts-comprehensive` (涵盖 JDK 18-21 所有特性)

# Detailed Requirements (详细要求)

## 1. Maven 配置
- 父工程管理所有依赖（JUnit 5, JMH 用于基准测试, Lombok）。
- 子模块 `maven-compiler-plugin` 严格对应版本 (1.8, 11, 17, 21)。
- **JDK 21 模块必须开启 `--enable-preview` 和 `--add-modules jdk.incubator.vector` (如果涉及向量API)。**

## 2. 模块内容与特性覆盖列表 (必须全量)

请为每个模块生成详细的代码演示，代码注释中**必须标注对应的 JEP 编号** (例如 `// JEP 395: Records`)。

### 🟢 Module: `jdk8-baseline`
- **Lambda & Functional**: 演示所有内置函数式接口，方法引用，构造器引用。
- **Stream API**: 覆盖 `collect`, `reduce`, `flatMap`, 并行流陷阱。
- **Optional**: 级联调用演示。
- **Date-Time (JSR 310)**: `Instant` vs `LocalDateTime`, `Duration` vs `Period`, 时区处理。
- **Concurrency**: `CompletableFuture` 详解, `StampedLock`, `LongAdder`。
- **JVM**: 展示 Metaspace 替代 PermGen 的验证代码 (模拟 OOM)。

### 🟡 Module: `jdk11-lts-comprehensive` (含 JDK 9/10)
- **Module System (JPMS)**: 提供 `module-info.java` 示例，演示 `exports`, `requires`, `opens`。
- **Syntax**: `var` (局部变量推断), 接口私有方法, Try-with-resources 增强。
- **Collection Factory**: `List.of`, `Map.of`, `Set.of` (不可变性演示).
- **Stream Enhancements**: `takeWhile`, `dropWhile`, `iterate` 重载, `ofNullable`.
- **HTTP Client**: 同步/异步请求, HTTP/2 推送, WebSocket 演示.
- **Process API**: `ProcessHandle` 获取 PID, 销毁进程树.
- **StackWalker**: 演示高效获取栈帧信息.
- **String/Files**: `lines()`, `strip()`, `readString()`, `writeString()`.

### 🟠 Module: `jdk17-lts-comprehensive` (含 JDK 12-16)
- **Records**: 构造器定制, 紧凑构造器, Record 实现接口.
- **Text Blocks**: 缩进控制, 转义字符, SQL/JSON 演示.
- **Switch Expressions**: 箭头语法, `yield`, 穷举性检查.
- **Pattern Matching**: `instanceof` 模式匹配.
- **Sealed Classes**: `sealed`, `non-sealed`, `final` 继承体系演示.
- **NPE Enhancements**: 演示 Helpful NullPointerExceptions 的报错信息.
- **Stream**: `toList()` vs `collect(toList())`.
- **Unix Domain Socket**: `UnixDomainSocketAddress` 演示 (Mock).
- **Math/Random**: `HexFormat`, 新的伪随机数生成器接口 `RandomGenerator`.

### 🔴 Module: `jdk21-lts-comprehensive` (含 JDK 18-20)
- **Virtual Threads (Project Loom)**:
    - 基础用法: `Thread.ofVirtual().start()`.
    - 性能压测: 100万虚拟线程 vs 平台线程.
    - 结合 `Executors.newVirtualThreadPerTaskExecutor()`.
- **Sequenced Collections**: `SequencedMap`, `SequencedSet`, `getFirst`, `addLast` 演示.
- **Pattern Matching (Switch & Record)**:
    - Switch 匹配 null.
    - Switch 匹配类型 (`case Integer i`).
    - Record 解构 (`case Point(int x, int y)`).
    - Guarded Patterns (`when` 关键字).
- **FFM API (Foreign Function & Memory)**: 演示使用 `Arena` 和 `Linker` 调用 C 语言 `strlen` 函数 (替代 JNI).
- **Scoped Values (Preview)**: 演示替代 `ThreadLocal` 的用法.
- **Structured Concurrency (Preview)**: `StructuredTaskScope` 演示任务分发与合并.
- **Vector API (Incubator)**: 简单的 SIMD 计算演示.
- **Simple Web Server**: 代码启动 `SimpleFileServer`.

# Output Format
请直接输出：
1. 父工程 `pom.xml`。
2. **每个模块的包结构树状图** (Tree view)。
3. 每个模块中 **最具代表性的 3 个复杂特性** 的完整 Java 代码 (包含 JEP 注释)。
4. 其他特性的简要代码片段或说明。