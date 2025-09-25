package com.demo;


public class Result<T> {
    private T data;

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public static <T> Result<T> succ(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    public static Result<String> fail() {
        Result<String> result = new Result<>();
        result.setData("失败");
        return result;
    }


}
