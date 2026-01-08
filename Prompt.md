# Role
你是一位拥有 10 年经验的 Java 首席架构师，精通 Java 语言演进历史，擅长编写高质量、教学性质的代码演示项目。

# Task
请为一个名为 `java-lts-evolution` 的 Maven 多模块项目生成完整的代码结构和核心源码。
该项目的目的是：**全面对比与演示 Java 8 到 Java 21 所有 LTS (长期支持) 版本的核心特性**。

# Project Structure (项目结构)
项目应包含一个父工程和 4 个子模块，每个子模块对应一个 LTS 版本：
1. `java-lts-evolution` (Root Parent POM)
2. `jdk8-foundation` (展示 Java 8 基石特性)
3. `jdk11-modernization` (展示 Java 11 模块化与 API 增强)
4. `jdk17-syntax-sugar` (展示 Java 17 语法糖与现代化特性)
5. `jdk21-concurrency` (展示 Java 21 虚拟线程与高并发新特性)

# Requirements (具体要求)

## 1. Maven 配置
- 请提供父工程的 `pom.xml`，统一管理依赖（如 JUnit 5, Lombok, Slf4j）。
- **关键点**：每个子模块的 `maven-compiler-plugin` 必须配置为对应的 Java 版本（source/target 分别为 1.8, 11, 17, 21）。
- JDK 21 模块如果使用了 Preview 特性，请在配置中开启 `--enable-preview`。

## 2. 模块内容与代码演示
请为每个模块生成至少 3-4 个核心特性的演示类 (`Demo.java`)。代码要求：
- **可运行**：包含 `main` 方法或 JUnit 测试用例。
- **对比视角**：在注释中或代码里展示 "Before (旧写法)" vs "After (新特性)"。
- **详细注释**：解释该特性解决了什么痛点。

### 模块详情：
*   **`jdk8-foundation`**:
    - Lambda 表达式与函数式接口 (Function, Consumer 等)。
    - Stream API 复杂操作 (Grouping, Partitioning)。
    - Optional 的正确用法 (避免 `isPresent()`)。
    - CompletableFuture 异步编排。

*   **`jdk11-modernization`**:
    - `var` 局部变量类型推断的各种场景。
    - 新 `HttpClient` (同步 vs 异步) 演示。
    - String 新增 API (`lines`, `strip`, `isBlank`)。
    - 集合工厂方法 (`List.of`) 与流增强 (`takeWhile`).

*   **`jdk17-syntax-sugar`**:
    - **Records**: 定义 DTO，对比传统 Class。
    - **Text Blocks**: 展示 JSON/SQL 拼接对比。
    - **Switch Expressions**: 箭头语法与返回值。
    - **Sealed Classes**: 定义一个密封接口及其实现类。
    - **Pattern Matching for instanceof**: 避免强制类型转换。

*   **`jdk21-concurrency`**:
    - **Virtual Threads**: 创建 10万个虚拟线程 vs 平台线程的性能/资源对比演示。
    - **Sequenced Collections**: 演示 `getFirst`, `addLast` 等统一 API。
    - **Switch Pattern Matching**: 在 Switch 中处理不同类型对象与 `null`。
    - **Record Patterns**: 解构 Record 对象。

# Output Format
请以 Markdown 代码块的形式输出：
1. 父工程 `pom.xml`。
2. 每个子模块的 `pom.xml` (关键部分)。
3. 每个模块的核心 Java 文件代码。
4. 简要的运行说明。