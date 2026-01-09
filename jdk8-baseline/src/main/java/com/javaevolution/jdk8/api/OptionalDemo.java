package com.javaevolution.jdk8.api;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Optional 完整演示
 * JEP 126: Lambda Expressions (Optional 作为辅助类)
 */
public class OptionalDemo {

    public static class CreationMethods {
        public void createOptional() {
            // 1. Optional.of - 值不能为 null
            Optional<String> opt1 = Optional.of("Hello");
            // Optional<String> opt2 = Optional.of(null); // NullPointerException
            
            // 2. Optional.ofNullable - 值可以为 null
            Optional<String> opt3 = Optional.ofNullable("World");
            Optional<String> opt4 = Optional.ofNullable(null);
            
            // 3. Optional.empty - 空 Optional
            Optional<String> opt5 = Optional.empty();
        }
    }

    public static class BasicOperations {
        public void basicMethods() {
            Optional<String> opt = Optional.of("Java");
            
            // isPresent / isEmpty (Java 11+)
            if (opt.isPresent()) {
                System.out.println("Value: " + opt.get());
            }
            
            // get - 不推荐直接使用
            String value = opt.get();
            
            // orElse - 提供默认值
            String result1 = opt.orElse("Default");
            String result2 = Optional.<String>empty().orElse("Default");
            
            // orElseGet - 延迟提供默认值
            String result3 = opt.orElseGet(() -> "Computed Default");
            
            // orElseThrow - 抛出异常
            String result4 = opt.orElseThrow(() -> new RuntimeException("No value!"));
        }
    }

    public static class FunctionalOperations {
        public void mapAndFlatMap() {
            Optional<String> opt = Optional.of("Java");
            
            // map - 转换值
            Optional<Integer> length = opt.map(String::length);
            System.out.println(length.orElse(0)); // 4
            
            // map 链式调用
            Optional<String> upper = opt
                .map(String::toUpperCase)
                .map(s -> s + "!");
            System.out.println(upper.orElse("")); // JAVA!
            
            // flatMap - 避免 Optional<Optional<T>>
            Optional<String> result = opt.flatMap(s -> Optional.of(s.toUpperCase()));
        }
        
        public void filterAndIfPresent() {
            Optional<Integer> opt = Optional.of(42);
            
            // filter - 条件过滤
            Optional<Integer> filtered = opt.filter(n -> n > 30);
            System.out.println(filtered.isPresent()); // true
            
            Optional<Integer> filtered2 = opt.filter(n -> n > 50);
            System.out.println(filtered2.isPresent()); // false
            
            // ifPresent - 消费值
            opt.ifPresent(n -> System.out.println("Value: " + n));
        }
    }

    public static class AdvancedUsage {
        // 级联调用演示
        public void cascadingOptional() {
            User user = new User("Alice", 
                new Address("Beijing", new ZipCode("100000")));
            
            // 传统方式: null 检查地狱
            String zipCode1 = null;
            if (user != null && user.getAddress() != null && 
                user.getAddress().getZipCode() != null) {
                zipCode1 = user.getAddress().getZipCode().getCode();
            }
            
            // Optional 方式: 优雅的链式调用
            String zipCode2 = Optional.ofNullable(user)
                .flatMap(User::getAddress)
                .flatMap(Address::getZipCode)
                .map(ZipCode::getCode)
                .orElse("Unknown");
            
            System.out.println(zipCode2); // 100000
        }
        
        // 结合 Stream API
        public void optionalWithStream() {
            java.util.List<Optional<String>> list = java.util.Arrays.asList(
                Optional.of("A"),
                Optional.empty(),
                Optional.of("B"),
                Optional.empty(),
                Optional.of("C")
            );
            
            java.util.List<String> result = list.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(java.util.stream.Collectors.toList());
            
            System.out.println(result); // [A, B, C]
        }
    }

    static class User {
        private String name;
        private Address address;
        
        User(String name, Address address) {
            this.name = name;
            this.address = address;
        }
        
        Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }
    }

    static class Address {
        private String city;
        private ZipCode zipCode;
        
        Address(String city, ZipCode zipCode) {
            this.city = city;
            this.zipCode = zipCode;
        }
        
        Optional<ZipCode> getZipCode() {
            return Optional.ofNullable(zipCode);
        }
    }

    static class ZipCode {
        private String code;
        
        ZipCode(String code) {
            this.code = code;
        }
        
        String getCode() {
            return code;
        }
    }

    public static void main(String[] args) {
        AdvancedUsage au = new AdvancedUsage();
        au.cascadingOptional();
        au.optionalWithStream();
    }
}
