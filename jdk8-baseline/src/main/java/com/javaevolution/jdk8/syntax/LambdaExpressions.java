package com.javaevolution.jdk8.syntax;

import java.util.*;
import java.util.function.*;

/**
 * Lambda 表达式与函数式接口完整演示
 * JEP 126: Lambda Expressions & Virtual Extension Methods
 */
public class LambdaExpressions {

    /**
     * 演示所有内置函数式接口
     */
    public static class FunctionalInterfacesDemo {
        
        // Predicate<T>: T -> boolean
        public void predicateExample() {
            Predicate<String> isEmpty = String::isEmpty;
            Predicate<String> isNotEmpty = isEmpty.negate();
            Predicate<String> startsWithJ = s -> s.startsWith("J");
            Predicate<String> combined = isNotEmpty.and(startsWithJ);
            
            System.out.println(combined.test("Java")); // true
            System.out.println(combined.test("Python")); // false
        }
        
        // Function<T, R>: T -> R
        public void functionExample() {
            Function<String, Integer> strLength = String::length;
            Function<Integer, Integer> square = x -> x * x;
            Function<String, Integer> lengthSquared = strLength.andThen(square);
            
            System.out.println(lengthSquared.apply("Java")); // 16
        }
        
        // Consumer<T>: T -> void
        public void consumerExample() {
            Consumer<String> print = System.out::println;
            Consumer<String> upperPrint = s -> System.out.println(s.toUpperCase());
            Consumer<String> combined = print.andThen(upperPrint);
            
            combined.accept("java"); // java \n JAVA
        }
        
        // Supplier<T>: () -> T
        public void supplierExample() {
            Supplier<Double> randomSupplier = Math::random;
            Supplier<List<String>> listSupplier = ArrayList::new;
            
            System.out.println(randomSupplier.get());
            List<String> list = listSupplier.get();
        }
        
        // BiFunction<T, U, R>: (T, U) -> R
        public void biFunctionExample() {
            BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
            BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;
            
            System.out.println(add.apply(3, 4)); // 7
            System.out.println(multiply.apply(3, 4)); // 12
        }
        
        // UnaryOperator<T>: T -> T (特殊的 Function)
        public void unaryOperatorExample() {
            UnaryOperator<String> toUpper = String::toUpperCase;
            UnaryOperator<Integer> doubleIt = x -> x * 2;
            
            System.out.println(toUpper.apply("java")); // JAVA
            System.out.println(doubleIt.apply(5)); // 10
        }
        
        // BinaryOperator<T>: (T, T) -> T (特殊的 BiFunction)
        public void binaryOperatorExample() {
            BinaryOperator<Integer> max = Math::max;
            BinaryOperator<String> concat = (a, b) -> a + b;
            
            System.out.println(max.apply(5, 10)); // 10
            System.out.println(concat.apply("Hello", "World")); // HelloWorld
        }
    }

    /**
     * 方法引用完整演示 (4种类型)
     */
    public static class MethodReferenceDemo {
        
        // 1. 静态方法引用: ClassName::staticMethod
        public void staticMethodReference() {
            Function<String, Integer> parseInt = Integer::parseInt;
            BiFunction<Integer, Integer, Integer> max = Math::max;
            
            System.out.println(parseInt.apply("123")); // 123
            System.out.println(max.apply(10, 20)); // 20
        }
        
        // 2. 实例方法引用: object::instanceMethod
        public void instanceMethodReference() {
            String str = "Hello";
            Supplier<String> upperSupplier = str::toUpperCase;
            Supplier<Integer> lengthSupplier = str::length;
            
            System.out.println(upperSupplier.get()); // HELLO
            System.out.println(lengthSupplier.get()); // 5
        }
        
        // 3. 类型方法引用: ClassName::instanceMethod
        public void typeMethodReference() {
            Function<String, String> toUpper = String::toUpperCase;
            BiFunction<String, String, Boolean> startsWith = String::startsWith;
            
            System.out.println(toUpper.apply("java")); // JAVA
            System.out.println(startsWith.apply("Java", "Ja")); // true
        }
        
        // 4. 构造器引用: ClassName::new
        public void constructorReference() {
            Supplier<List<String>> listSupplier = ArrayList::new;
            Function<Integer, int[]> arrayCreator = int[]::new;
            BiFunction<String, Integer, Person> personCreator = Person::new;
            
            List<String> list = listSupplier.get();
            int[] array = arrayCreator.apply(10);
            Person person = personCreator.apply("Alice", 30);
        }
        
        static class Person {
            String name;
            int age;
            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }
    }

    /**
     * Lambda 表达式高级用法
     */
    public static class AdvancedLambdaDemo {
        
        // 闭包与变量捕获 (Effectively Final)
        public void closureExample() {
            int multiplier = 10; // 必须是 effectively final
            
            Function<Integer, Integer> multiply = x -> x * multiplier;
            System.out.println(multiply.apply(5)); // 50
            
            // multiplier = 20; // 编译错误: 变量不是 effectively final
        }
        
        // 柯里化 (Currying)
        public Function<Integer, Function<Integer, Integer>> curry() {
            return a -> b -> a + b;
        }
        
        public void curryExample() {
            Function<Integer, Function<Integer, Integer>> add = curry();
            Function<Integer, Integer> add5 = add.apply(5);
            
            System.out.println(add5.apply(10)); // 15
            System.out.println(add5.apply(20)); // 25
        }
        
        // 递归 Lambda (需要技巧)
        interface RecursiveFunction<T> extends Function<T, T> {
            default T apply(T t) {
                return calculate().apply(t);
            }
            Function<T, T> calculate();
        }
        
        public void recursiveLambda() {
            RecursiveFunction<Integer> factorial = () -> n -> 
                n == 0 ? 1 : n * factorial.apply(n - 1);
            
            System.out.println(factorial.apply(5)); // 120
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Lambda Expressions Demo ===");
        
        FunctionalInterfacesDemo fiDemo = new FunctionalInterfacesDemo();
        fiDemo.predicateExample();
        fiDemo.functionExample();
        
        MethodReferenceDemo mrDemo = new MethodReferenceDemo();
        mrDemo.constructorReference();
        
        AdvancedLambdaDemo advDemo = new AdvancedLambdaDemo();
        advDemo.curryExample();
    }
}
