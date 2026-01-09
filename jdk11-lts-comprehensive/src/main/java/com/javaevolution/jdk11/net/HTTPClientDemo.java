package com.javaevolution.jdk11.net;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.*;

/**
 * HTTP Client API 完整演示
 * JEP 321: HTTP Client (Standard) - JDK 11
 * 替代旧的 HttpURLConnection
 */
public class HTTPClientDemo {

    /**
     * 同步 HTTP 请求
     */
    public static class SyncRequests {
        
        public void simpleGetRequest() throws IOException, InterruptedException {
            // 1. 创建 HttpClient
            HttpClient client = HttpClient.newHttpClient();
            
            // 2. 创建 HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/users/octocat"))
                .build();
            
            // 3. 发送请求并获取响应
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            // 4. 处理响应
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Headers: " + response.headers());
            System.out.println("Body: " + response.body());
        }
        
        public void requestWithHeaders() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/users/octocat"))
                .header("User-Agent", "Java 11 HttpClient")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
            
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println(response.body());
        }
        
        public void postRequest() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            
            String jsonBody = "{\"name\":\"John\",\"age\":30}";
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/post"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonBody))
                .build();
            
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println("Response: " + response.body());
        }
        
        public void putDeleteRequest() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            
            // PUT
            HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/put"))
                .PUT(BodyPublishers.ofString("update data"))
                .build();
            
            HttpResponse<String> putResponse = client.send(putRequest, BodyHandlers.ofString());
            System.out.println("PUT: " + putResponse.statusCode());
            
            // DELETE
            HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/delete"))
                .DELETE()
                .build();
            
            HttpResponse<String> deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());
            System.out.println("DELETE: " + deleteResponse.statusCode());
        }
    }

    /**
     * 异步 HTTP 请求
     */
    public static class AsyncRequests {
        
        public void simpleAsyncRequest() {
            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/users/octocat"))
                .build();
            
            // 异步发送,返回 CompletableFuture
            CompletableFuture<HttpResponse<String>> futureResponse = 
                client.sendAsync(request, BodyHandlers.ofString());
            
            // 处理响应
            futureResponse
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
        }
        
        public void multipleAsyncRequests() {
            HttpClient client = HttpClient.newHttpClient();
            
            // 创建多个请求
            String[] urls = {
                "https://api.github.com/users/octocat",
                "https://api.github.com/users/torvalds",
                "https://api.github.com/users/gvanrossum"
            };
            
            CompletableFuture<?>[] futures = new CompletableFuture[urls.length];
            
            for (int i = 0; i < urls.length; i++) {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urls[i]))
                    .build();
                
                futures[i] = client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(body -> System.out.println("Response: " + body.substring(0, 100)));
            }
            
            // 等待所有请求完成
            CompletableFuture.allOf(futures).join();
        }
        
        public void asyncWithErrorHandling() {
            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://invalid-url-12345.com"))
                .timeout(Duration.ofSeconds(5))
                .build();
            
            client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(ex -> {
                    System.err.println("Error: " + ex.getMessage());
                    return "Error occurred";
                })
                .thenAccept(System.out::println)
                .join();
        }
    }

    /**
     * HttpClient 配置
     */
    public static class ClientConfiguration {
        
        public void customClient() {
            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)           // HTTP/2
                .followRedirects(HttpClient.Redirect.NORMAL)  // 跟随重定向
                .connectTimeout(Duration.ofSeconds(10))       // 连接超时
                .executor(Executors.newFixedThreadPool(5))    // 自定义线程池
                // .sslContext(sslContext)                    // SSL 配置
                // .authenticator(authenticator)              // 认证
                .build();
            
            System.out.println("Client version: " + client.version());
        }
        
        public void proxyConfiguration() {
            // 代理配置
            HttpClient client = HttpClient.newBuilder()
                .proxy(java.net.ProxySelector.of(
                    new java.net.InetSocketAddress("proxy.example.com", 8080)))
                .build();
        }
    }

    /**
     * 不同的响应体处理器
     */
    public static class BodyHandlersDemo {
        
        public void differentBodyHandlers() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/json"))
                .build();
            
            // 1. ofString - 字符串
            HttpResponse<String> stringResponse = 
                client.send(request, BodyHandlers.ofString());
            System.out.println(stringResponse.body());
            
            // 2. ofByteArray - 字节数组
            HttpResponse<byte[]> byteResponse = 
                client.send(request, BodyHandlers.ofByteArray());
            System.out.println("Bytes: " + byteResponse.body().length);
            
            // 3. ofFile - 保存到文件
            java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("download", ".json");
            HttpResponse<java.nio.file.Path> fileResponse = 
                client.send(request, BodyHandlers.ofFile(tempFile));
            System.out.println("Saved to: " + fileResponse.body());
            
            // 4. ofInputStream - 输入流
            HttpResponse<java.io.InputStream> streamResponse = 
                client.send(request, BodyHandlers.ofInputStream());
            // streamResponse.body().read(...)
            
            // 5. discarding - 丢弃响应体
            HttpResponse<Void> discardResponse = 
                client.send(request, BodyHandlers.discarding());
            System.out.println("Status: " + discardResponse.statusCode());
        }
    }

    /**
     * HTTP/2 推送 (Server Push)
     */
    public static class HTTP2PushDemo {
        
        public void serverPushExample() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://http2.golang.org/serverpush"))
                .build();
            
            // Push Promise Handler
            HttpResponse.PushPromiseHandler<String> pushHandler = 
                HttpResponse.PushPromiseHandler.of(
                    (pushRequest) -> BodyHandlers.ofString(),
                    new ConcurrentHashMap<>()
                );
            
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString(), pushHandler);
            
            System.out.println("Main response: " + response.statusCode());
        }
    }

    /**
     * WebSocket 示例
     */
    public static class WebSocketDemo {
        
        public void webSocketExample() throws InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            
            WebSocket.Builder wsBuilder = client.newWebSocketBuilder();
            
            CompletableFuture<WebSocket> wsFuture = wsBuilder
                .buildAsync(
                    URI.create("wss://echo.websocket.org"),
                    new WebSocket.Listener() {
                        @Override
                        public void onOpen(WebSocket webSocket) {
                            System.out.println("WebSocket opened");
                            webSocket.sendText("Hello WebSocket!", true);
                            WebSocket.Listener.super.onOpen(webSocket);
                        }
                        
                        @Override
                        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                            System.out.println("Received: " + data);
                            return WebSocket.Listener.super.onText(webSocket, data, last);
                        }
                        
                        @Override
                        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                            System.out.println("Closed: " + reason);
                            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
                        }
                        
                        @Override
                        public void onError(WebSocket webSocket, Throwable error) {
                            System.err.println("Error: " + error.getMessage());
                            WebSocket.Listener.super.onError(webSocket, error);
                        }
                    }
                );
            
            WebSocket ws = wsFuture.join();
            
            // 发送消息
            ws.sendText("Message 1", true);
            ws.sendText("Message 2", true);
            
            Thread.sleep(2000);
            
            // 关闭连接
            ws.sendClose(WebSocket.NORMAL_CLOSURE, "Goodbye").join();
        }
    }

    /**
     * 实战: 并发下载多个文件
     */
    public static class RealWorldExample {
        
        public void parallelDownload() throws IOException {
            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
            
            String[] urls = {
                "https://httpbin.org/json",
                "https://httpbin.org/uuid",
                "https://httpbin.org/base64/SFRUUEJJTiBpcyBhd2Vzb21l"
            };
            
            CompletableFuture<?>[] futures = new CompletableFuture[urls.length];
            
            for (int i = 0; i < urls.length; i++) {
                String url = urls[i];
                java.nio.file.Path path = java.nio.file.Path.of("download_" + i + ".json");
                
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
                
                futures[i] = client.sendAsync(request, BodyHandlers.ofFile(path))
                    .thenApply(response -> {
                        System.out.println("Downloaded: " + path + " (" + response.statusCode() + ")");
                        return response;
                    });
            }
            
            CompletableFuture.allOf(futures).join();
            System.out.println("All downloads completed!");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== HTTP Client Demo ===");
        
        // SyncRequests sr = new SyncRequests();
        // sr.simpleGetRequest();
        
        AsyncRequests ar = new AsyncRequests();
        // ar.simpleAsyncRequest();
        ar.multipleAsyncRequests();
    }
}
