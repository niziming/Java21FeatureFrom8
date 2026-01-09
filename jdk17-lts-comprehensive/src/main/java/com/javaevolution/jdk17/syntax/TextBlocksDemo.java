package com.javaevolution.jdk17.syntax;

/**
 * Text Blocks 完整演示
 * JEP 378: Text Blocks (JDK 15 - Final)
 * JEP 368: Text Blocks (JDK 13 - Preview)
 */
public class TextBlocksDemo {

    /**
     * 基础用法
     */
    public static class BasicUsage {
        
        public void traditionalStringVsTextBlock() {
            // 传统多行字符串: 繁琐,难以阅读
            String traditional = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"New York\"\n" +
                "}";
            
            // Text Block: 简洁,直观
            String textBlock = """
                {
                  "name": "John",
                  "age": 30,
                  "city": "New York"
                }
                """;
            
            System.out.println(traditional);
            System.out.println(textBlock);
            
            // 两者完全相同
            System.out.println(traditional.equals(textBlock)); // true
        }
        
        public void basicTextBlock() {
            // Text Block 以 """ 开始和结束
            String html = """
                <html>
                    <body>
                        <h1>Hello, World!</h1>
                    </body>
                </html>
                """;
            
            System.out.println(html);
        }
    }

    /**
     * 缩进控制
     */
    public static class IndentationControl {
        
        public void automaticIndentation() {
            // 自动缩进: 基于最左边的非空白字符
            String sql1 = """
                    SELECT * FROM users
                    WHERE age > 18
                    ORDER BY name
                    """;
            
            // 等价于 (没有前导空格)
            String sql2 = """
SELECT * FROM users
WHERE age > 18
ORDER BY name
""";
            
            System.out.println(sql1);
            
            // 带缩进的 Text Block
            String indented = """
                    Line 1
                        Line 2 (indented)
                    Line 3
                    """;
            
            System.out.println(indented);
        }
        
        public void explicitIndentation() {
            // 使用 \s 保留尾随空格
            String withSpaces = """
                Line 1\s
                Line 2\s\s
                """;
            
            // 使用 indent() 方法
            String text = """
                Line 1
                Line 2
                """;
            
            String indented = text.indent(4);
            System.out.println(indented);
        }
    }

    /**
     * 转义字符
     */
    public static class EscapeSequences {
        
        public void basicEscapes() {
            // 换行符
            String withNewline = """
                Line 1
                Line 2
                """;
            
            // \n 显式换行
            String explicitNewline = """
                Line 1\nLine 2
                """;
            
            // \" 不需要转义 (Text Block 内部)
            String quotes = """
                She said "Hello"
                """;
            
            System.out.println(quotes);
            
            // 三引号需要转义
            String tripleQuotes = """
                This is a triple quote: \"""
                """;
            
            System.out.println(tripleQuotes);
        }
        
        public void lineTerminatorEscape() {
            // \ 在行尾: 续行,不产生换行符
            String oneLine = """
                This is a \
                single line
                """;
            
            System.out.println(oneLine); // This is a single line
            
            // 对比: 不使用 \
            String twoLines = """
                This is
                two lines
                """;
            
            System.out.println(twoLines);
        }
    }

    /**
     * 实际应用场景
     */
    public static class RealWorldUsage {
        
        public void jsonExample() {
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
            
            System.out.println(json);
        }
        
        public void sqlExample() {
            String sql = """
                SELECT u.id, u.name, o.order_id, o.total
                FROM users u
                INNER JOIN orders o ON u.id = o.user_id
                WHERE u.status = 'active'
                  AND o.total > 100
                ORDER BY o.created_at DESC
                LIMIT 10
                """;
            
            System.out.println(sql);
        }
        
        public void htmlExample() {
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
            
            System.out.println(html);
        }
        
        public void regexExample() {
            // 复杂正则表达式
            String regex = """
                ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$
                """;
            
            // 多行正则 (带注释)
            String complexRegex = """
                (?x)              # 启用注释模式
                ^                 # 行首
                [a-zA-Z0-9._%+-]+ # 用户名部分
                @                 # @符号
                [a-zA-Z0-9.-]+    # 域名部分
                \\.               # 点号
                [a-zA-Z]{2,}      # 顶级域名
                $                 # 行尾
                """;
        }
        
        public void multilineString() {
            // 帮助文档
            String help = """
                Usage: myapp [OPTIONS] FILE
                
                Options:
                  -h, --help     Show this help message
                  -v, --verbose  Enable verbose mode
                  -o, --output   Specify output file
                
                Examples:
                  myapp input.txt
                  myapp -v -o result.txt input.txt
                """;
            
            System.out.println(help);
        }
    }

    /**
     * 字符串格式化方法
     */
    public static class FormattingMethods {
        
        public void formattedMethod() {
            String name = "Alice";
            int score = 95;
            
            // formatted() 方法 (JDK 15)
            String result = """
                Student: %s
                Score: %d
                Grade: %s
                """.formatted(name, score, score >= 90 ? "A" : "B");
            
            System.out.println(result);
        }
        
        public void stripIndent() {
            String text = """
                    Line 1
                    Line 2
                    """;
            
            // stripIndent() - 移除通用前导空白
            String stripped = text.stripIndent();
            System.out.println(stripped);
        }
        
        public void translateEscapes() {
            String text = """
                Line 1\\nLine 2\\tTabbed
                """;
            
            // translateEscapes() - 转换转义序列
            String translated = text.translateEscapes();
            System.out.println(translated);
        }
    }

    /**
     * Text Block 最佳实践
     */
    public static class BestPractices {
        
        public void goodUseCases() {
            // ✅ 适用场景: SQL, JSON, HTML, XML 等多行文本
            
            // ✅ 测试用例中的预期输出
            String expected = """
                {
                  "status": "success",
                  "code": 200
                }
                """;
            
            // ✅ 配置模板
            String config = """
                server:
                  port: 8080
                  host: localhost
                database:
                  url: jdbc:mysql://localhost:3306/mydb
                  username: root
                """;
        }
        
        public void avoidUseCases() {
            // ❌ 单行字符串: 不需要 Text Block
            // String simple = """
            //     Hello, World!
            //     """;
            
            // ✅ 使用普通字符串
            String simple = "Hello, World!";
            
            // ❌ 需要精确控制每个字符时
            // Text Block 会自动处理缩进
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Text Blocks Demo ===");
        
        BasicUsage bu = new BasicUsage();
        bu.traditionalStringVsTextBlock();
        
        RealWorldUsage rwu = new RealWorldUsage();
        rwu.jsonExample();
        rwu.sqlExample();
        
        FormattingMethods fm = new FormattingMethods();
        fm.formattedMethod();
    }
}
