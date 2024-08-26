package com.example.deal.utils;

public final class LoggerUtils {
    private LoggerUtils() {
    }

    public static <T> String cut(T object, int length) {
        return object.toString().substring(0, Math.min(object.toString().length(), length));
    }
}
