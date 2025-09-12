package com.demo.io;

import com.demo.annotations.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.stream.Stream;

/**
 * I/O操作增强演示
 * 展示Java 8到Java 21的I/O相关新特性和改进
 */
public class IODemo {

    /**
     * Files类增强演示 (Java 8特性)
     * Files类提供了丰富的文件操作方法
     */
    @Java8Feature(value = "Files类增强", desc = "Files类提供便捷的文件操作方法，支持Path API")
    public void demonstrateFilesEnhancements() {
        System.out.println("=== Files类增强演示 ===");
        
        try {
            // 创建测试目录和文件
            Path testDir = Paths.get("temp-demo");
            Path testFile = testDir.resolve("test.txt");
            
            // 创建目录
            Files.createDirectories(testDir);
            System.out.println("创建目录: " + testDir.toAbsolutePath());
            
            // 写入文件
            List<String> lines = Arrays.asList(
                "这是第一行",
                "这是第二行",
                "Java I/O演示",
                "文件操作示例"
            );
            Files.write(testFile, lines, StandardCharsets.UTF_8);
            System.out.println("写入文件: " + testFile.getFileName());
            
            // 读取文件
            List<String> readLines = Files.readAllLines(testFile, StandardCharsets.UTF_8);
            System.out.println("读取文件内容:");
            readLines.forEach(line -> System.out.println("  " + line));
            
            // 文件属性
            System.out.println("\n文件属性:");
            System.out.println("  文件大小: " + Files.size(testFile) + " 字节");
            System.out.println("  是否存在: " + Files.exists(testFile));
            System.out.println("  是否为文件: " + Files.isRegularFile(testFile));
            System.out.println("  是否可读: " + Files.isReadable(testFile));
            System.out.println("  是否可写: " + Files.isWritable(testFile));
            
            // 复制文件
            Path copyFile = testDir.resolve("copy.txt");
            Files.copy(testFile, copyFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("  复制文件到: " + copyFile.getFileName());
            
            // 移动文件
            Path moveFile = testDir.resolve("moved.txt");
            Files.move(copyFile, moveFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("  移动文件到: " + moveFile.getFileName());
            
            // 清理测试文件
            Files.deleteIfExists(testFile);
            Files.deleteIfExists(moveFile);
            Files.deleteIfExists(testDir);
            System.out.println("清理测试文件完成");
            
        } catch (IOException e) {
            System.err.println("文件操作失败: " + e.getMessage());
        }
    }

    /**
     * Stream文件操作演示 (Java 8特性)
     * 使用Stream处理文件内容
     */
    @Java8Feature(value = "Stream文件操作", desc = "使用Stream API处理文件内容，支持大文件高效处理")
    public void demonstrateStreamFileOperations() {
        System.out.println("\n=== Stream文件操作演示 ===");
        
        try {
            // 创建测试文件
            Path testFile = Paths.get("demo-data.txt");
            List<String> data = Arrays.asList(
                "Java 8 Lambda表达式",
                "Java 11 var关键字",
                "Java 17 Records特性",
                "Java 21 虚拟线程",
                "Stream API处理",
                "函数式编程",
                "并发编程增强",
                "模式匹配功能"
            );
            Files.write(testFile, data, StandardCharsets.UTF_8);
            
            // 使用Stream读取和处理文件
            System.out.println("使用Stream处理文件内容:");
            
            // 读取所有行并进行处理
            try (Stream<String> lines = Files.lines(testFile, StandardCharsets.UTF_8)) {
                List<String> javaFeatures = lines
                    .filter(line -> line.contains("Java"))
                    .map(line -> line.toUpperCase())
                    .sorted()
                    .toList();
                
                System.out.println("Java相关特性:");
                javaFeatures.forEach(feature -> System.out.println("  " + feature));
            }
            
            // 统计文件信息
            try (Stream<String> lines = Files.lines(testFile)) {
                long lineCount = lines.count();
                System.out.println("文件总行数: " + lineCount);
            }
            
            try (Stream<String> lines = Files.lines(testFile)) {
                long wordCount = lines
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    .count();
                System.out.println("文件总词数: " + wordCount);
            }
            
            // 查找包含特定关键词的行
            try (Stream<String> lines = Files.lines(testFile)) {
                Optional<String> firstMatch = lines
                    .filter(line -> line.contains("虚拟线程"))
                    .findFirst();
                
                firstMatch.ifPresent(line -> 
                    System.out.println("找到匹配行: " + line));
            }
            
            // 清理测试文件
            Files.deleteIfExists(testFile);
            
        } catch (IOException e) {
            System.err.println("Stream文件操作失败: " + e.getMessage());
        }
    }

    /**
     * 目录遍历演示 (Java 8特性)
     * 使用Files.walk()和Files.find()遍历目录
     */
    @Java8Feature(value = "目录遍历", desc = "使用Stream API遍历目录结构，支持过滤和查找")
    public void demonstrateDirectoryTraversal() {
        System.out.println("\n=== 目录遍历演示 ===");
        
        try {
            // 创建测试目录结构
            Path rootDir = Paths.get("demo-structure");
            Path subDir1 = rootDir.resolve("java8");
            Path subDir2 = rootDir.resolve("java21");
            Path subDir3 = rootDir.resolve("features");
            
            Files.createDirectories(subDir1);
            Files.createDirectories(subDir2);
            Files.createDirectories(subDir3);
            
            // 创建测试文件
            Files.write(subDir1.resolve("lambda.java"), 
                Arrays.asList("// Lambda表达式示例"), StandardCharsets.UTF_8);
            Files.write(subDir1.resolve("stream.java"), 
                Arrays.asList("// Stream API示例"), StandardCharsets.UTF_8);
            Files.write(subDir2.resolve("virtual-threads.java"), 
                Arrays.asList("// 虚拟线程示例"), StandardCharsets.UTF_8);
            Files.write(subDir3.resolve("records.java"), 
                Arrays.asList("// Records示例"), StandardCharsets.UTF_8);
            Files.write(rootDir.resolve("README.md"), 
                Arrays.asList("# Java特性演示"), StandardCharsets.UTF_8);
            
            // 遍历目录结构
            System.out.println("目录结构遍历:");
            try (Stream<Path> paths = Files.walk(rootDir)) {
                paths.filter(Files::isRegularFile)
                    .forEach(file -> {
                        Path relativePath = rootDir.relativize(file);
                        System.out.println("  文件: " + relativePath);
                    });
            }
            
            // 查找特定类型的文件
            System.out.println("\n查找Java文件:");
            try (Stream<Path> paths = Files.find(rootDir, 3,
                    (path, attrs) -> path.toString().endsWith(".java"))) {
                paths.forEach(javaFile -> {
                    Path relativePath = rootDir.relativize(javaFile);
                    System.out.println("  Java文件: " + relativePath);
                });
            }
            
            // 统计文件类型
            try (Stream<Path> paths = Files.walk(rootDir)) {
                Map<String, Long> fileTypes = paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        String fileName = path.getFileName().toString();
                        int dotIndex = fileName.lastIndexOf('.');
                        return dotIndex > 0 ? fileName.substring(dotIndex) : "无扩展名";
                    })
                    .collect(java.util.stream.Collectors.groupingBy(
                        ext -> ext,
                        java.util.stream.Collectors.counting()
                    ));
                
                System.out.println("\n文件类型统计:");
                fileTypes.forEach((ext, count) -> 
                    System.out.println("  " + ext + ": " + count + "个文件"));
            }
            
            // 清理测试目录
            cleanupDirectory(rootDir);
            System.out.println("清理测试目录完成");
            
        } catch (IOException e) {
            System.err.println("目录遍历失败: " + e.getMessage());
        }
    }

    /**
     * 递归删除目录
     */
    private void cleanupDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            try (Stream<Path> paths = Files.walk(directory)) {
                paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("删除失败: " + path);
                        }
                    });
            }
        }
    }

    /**
     * NIO.2路径操作演示 (Java 8特性)
     * Path API的使用
     */
    @Java8Feature(value = "Path API", desc = "NIO.2的Path API提供更好的路径操作支持")
    public void demonstratePathOperations() {
        System.out.println("\n=== Path API操作演示 ===");
        
        // 路径创建和操作
        Path currentDir = Paths.get(".");
        Path absolutePath = currentDir.toAbsolutePath();
        Path parentPath = absolutePath.getParent();
        
        System.out.println("路径操作:");
        System.out.println("  当前目录: " + currentDir);
        System.out.println("  绝对路径: " + absolutePath);
        System.out.println("  父目录: " + parentPath);
        System.out.println("  文件名: " + absolutePath.getFileName());
        
        // 路径拼接
        Path configPath = Paths.get("config", "application.properties");
        Path dataPath = Paths.get("data").resolve("users.json");
        
        System.out.println("\n路径拼接:");
        System.out.println("  配置文件路径: " + configPath);
        System.out.println("  数据文件路径: " + dataPath);
        
        // 路径比较和关系
        Path path1 = Paths.get("src/main/java");
        Path path2 = Paths.get("src/main/resources");
        Path commonParent = path1.getParent().getParent();
        
        System.out.println("\n路径关系:");
        System.out.println("  路径1: " + path1);
        System.out.println("  路径2: " + path2);
        System.out.println("  共同父路径: " + commonParent);
        System.out.println("  路径1是否以src开头: " + path1.startsWith("src"));
        System.out.println("  路径1是否以java结尾: " + path1.endsWith("java"));
        
        // 相对路径计算
        Path relativePath = path1.relativize(path2);
        System.out.println("  相对路径(path1到path2): " + relativePath);
        
        // 路径标准化
        Path messyPath = Paths.get("src/../src/main/./java");
        Path normalizedPath = messyPath.normalize();
        
        System.out.println("\n路径标准化:");
        System.out.println("  原始路径: " + messyPath);
        System.out.println("  标准化后: " + normalizedPath);
    }

    /**
     * 文件监控演示 (Java 8特性)
     * 使用WatchService监控文件系统变化
     */
    @Java8Feature(value = "文件监控", desc = "WatchService提供文件系统变化监控功能")
    public void demonstrateFileWatching() {
        System.out.println("\n=== 文件监控演示 ===");
        
        try {
            // 创建监控目录
            Path watchDir = Paths.get("watch-demo");
            Files.createDirectories(watchDir);
            
            // 创建文件监控服务
            WatchService watchService = FileSystems.getDefault().newWatchService();
            
            // 注册监控事件
            watchDir.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
            
            System.out.println("开始监控目录: " + watchDir.toAbsolutePath());
            System.out.println("正在执行文件操作以触发监控事件...");
            
            // 在另一个线程中执行文件操作
            Thread fileOperations = new Thread(() -> {
                try {
                    Thread.sleep(100);
                    
                    // 创建文件
                    Path testFile = watchDir.resolve("监控测试.txt");
                    Files.write(testFile, Arrays.asList("测试内容"), StandardCharsets.UTF_8);
                    Thread.sleep(100);
                    
                    // 修改文件
                    Files.write(testFile, Arrays.asList("修改后的内容"), StandardCharsets.UTF_8);
                    Thread.sleep(100);
                    
                    // 删除文件
                    Files.delete(testFile);
                    Thread.sleep(100);
                    
                } catch (Exception e) {
                    System.err.println("文件操作失败: " + e.getMessage());
                }
            });
            fileOperations.start();
            
            // 监控文件系统事件
            int eventCount = 0;
            long startTime = System.currentTimeMillis();
            
            while (eventCount < 3 && (System.currentTimeMillis() - startTime) < 5000) {
                WatchKey key = watchService.poll(500, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (key != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        Path fileName = (Path) event.context();
                        
                        System.out.println("  检测到事件: " + kind.name() + " - " + fileName);
                        eventCount++;
                    }
                    key.reset();
                }
            }
            
            // 等待操作线程完成
            fileOperations.join(1000);
            
            // 清理资源
            watchService.close();
            Files.deleteIfExists(watchDir);
            System.out.println("文件监控演示完成");
            
        } catch (IOException | InterruptedException e) {
            System.err.println("文件监控失败: " + e.getMessage());
        }
    }

    /**
     * 内存映射文件演示 (Java 8特性)
     * 使用内存映射进行高效文件I/O
     */
    @Java8Feature(value = "内存映射文件", desc = "使用内存映射文件进行高效的大文件处理")
    public void demonstrateMemoryMappedFiles() {
        System.out.println("\n=== 内存映射文件演示 ===");
        
        try {
            Path largeFile = Paths.get("large-data.txt");
            
            // 创建一个较大的测试文件
            try (FileChannel channel = FileChannel.open(largeFile,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.READ)) {
                
                // 写入测试数据
                StringBuilder content = new StringBuilder();
                for (int i = 0; i < 10000; i++) {
                    content.append("这是第").append(i + 1).append("行数据，用于测试内存映射文件操作\n");
                }
                
                byte[] data = content.toString().getBytes(StandardCharsets.UTF_8);
                java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(data);
                channel.write(buffer);
                
                System.out.println("创建测试文件，大小: " + Files.size(largeFile) + " 字节");
                
                // 使用内存映射读取文件
                long fileSize = channel.size();
                java.nio.MappedByteBuffer mappedBuffer = channel.map(
                    FileChannel.MapMode.READ_ONLY, 0, fileSize);
                
                // 统计特定字符的出现次数
                int lineCount = 0;
                byte[] searchBytes = "行数据".getBytes(StandardCharsets.UTF_8);
                
                long startTime = System.nanoTime();
                
                for (int i = 0; i <= mappedBuffer.limit() - searchBytes.length; i++) {
                    boolean found = true;
                    for (int j = 0; j < searchBytes.length; j++) {
                        if (mappedBuffer.get(i + j) != searchBytes[j]) {
                            found = false;
                            break;
                        }
                    }
                    if (found) {
                        lineCount++;
                    }
                }
                
                long endTime = System.nanoTime();
                double duration = (endTime - startTime) / 1_000_000.0; // 转换为毫秒
                
                System.out.println("内存映射搜索结果:");
                System.out.println("  找到 '行数据' 出现次数: " + lineCount);
                System.out.println("  搜索耗时: " + String.format("%.3f", duration) + " 毫秒");
                
                // 对比传统文件读取方式
                startTime = System.nanoTime();
                String fileContent = Files.readString(largeFile, StandardCharsets.UTF_8);
                int traditionalCount = 0;
                int index = 0;
                while ((index = fileContent.indexOf("行数据", index)) != -1) {
                    traditionalCount++;
                    index += "行数据".length();
                }
                endTime = System.nanoTime();
                double traditionalDuration = (endTime - startTime) / 1_000_000.0;
                
                System.out.println("传统方式搜索结果:");
                System.out.println("  找到 '行数据' 出现次数: " + traditionalCount);
                System.out.println("  搜索耗时: " + String.format("%.3f", traditionalDuration) + " 毫秒");
                
                if (traditionalDuration > 0) {
                    double speedup = traditionalDuration / duration;
                    System.out.println("  内存映射性能提升: " + String.format("%.2f", speedup) + " 倍");
                }
            }
            
            // 清理测试文件
            Files.deleteIfExists(largeFile);
            
        } catch (IOException e) {
            System.err.println("内存映射文件操作失败: " + e.getMessage());
        }
    }

    /**
     * 文件属性和权限演示 (Java 8特性)
     * 文件属性查看和权限管理
     */
    @Java8Feature(value = "文件属性管理", desc = "文件属性查看和权限管理功能")
    public void demonstrateFileAttributes() {
        System.out.println("\n=== 文件属性和权限演示 ===");
        
        try {
            // 创建测试文件
            Path testFile = Paths.get("attributes-test.txt");
            Files.write(testFile, Arrays.asList("文件属性测试"), StandardCharsets.UTF_8);
            
            // 基本文件属性
            java.nio.file.attribute.BasicFileAttributes attrs = 
                Files.readAttributes(testFile, java.nio.file.attribute.BasicFileAttributes.class);
            
            System.out.println("基本文件属性:");
            System.out.println("  文件大小: " + attrs.size() + " 字节");
            System.out.println("  创建时间: " + attrs.creationTime());
            System.out.println("  最后修改时间: " + attrs.lastModifiedTime());
            System.out.println("  最后访问时间: " + attrs.lastAccessTime());
            System.out.println("  是否为目录: " + attrs.isDirectory());
            System.out.println("  是否为常规文件: " + attrs.isRegularFile());
            System.out.println("  是否为符号链接: " + attrs.isSymbolicLink());
            
            // 文件权限
            Set<java.nio.file.attribute.PosixFilePermission> permissions = 
                java.util.EnumSet.of(
                    java.nio.file.attribute.PosixFilePermission.OWNER_READ,
                    java.nio.file.attribute.PosixFilePermission.OWNER_WRITE,
                    java.nio.file.attribute.PosixFilePermission.GROUP_READ
                );
            
            try {
                Files.setPosixFilePermissions(testFile, permissions);
                System.out.println("设置POSIX权限成功");
            } catch (UnsupportedOperationException e) {
                System.out.println("当前系统不支持POSIX权限（Windows系统）");
            }
            
            // 修改文件时间
            java.nio.file.attribute.FileTime newTime = 
                java.nio.file.attribute.FileTime.fromMillis(System.currentTimeMillis() - 86400000); // 一天前
            Files.setLastModifiedTime(testFile, newTime);
            System.out.println("修改文件时间: " + Files.getLastModifiedTime(testFile));
            
            // 文件存储信息
            java.nio.file.FileStore store = Files.getFileStore(testFile);
            System.out.println("\n文件存储信息:");
            System.out.println("  存储名称: " + store.name());
            System.out.println("  文件系统类型: " + store.type());
            System.out.println("  总空间: " + formatBytes(store.getTotalSpace()));
            System.out.println("  可用空间: " + formatBytes(store.getUsableSpace()));
            System.out.println("  未分配空间: " + formatBytes(store.getUnallocatedSpace()));
            
            // 清理测试文件
            Files.deleteIfExists(testFile);
            
        } catch (IOException e) {
            System.err.println("文件属性操作失败: " + e.getMessage());
        }
    }

    /**
     * 格式化字节大小
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * 运行所有I/O演示
     */
    public void runAllDemos() {
        System.out.println("开始I/O操作增强演示...\n");
        
        demonstrateFilesEnhancements();
        demonstrateStreamFileOperations();
        demonstrateDirectoryTraversal();
        demonstratePathOperations();
        demonstrateFileWatching();
        demonstrateMemoryMappedFiles();
        demonstrateFileAttributes();
        
        System.out.println("\nI/O操作增强演示完成！");
    }
}