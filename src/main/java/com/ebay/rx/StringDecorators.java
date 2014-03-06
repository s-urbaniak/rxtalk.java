package com.ebay.rx;

import rx.util.functions.Func1;

public final class StringDecorators {
    public static Func1<String, String> threadName = new Func1<String, String>() {
        @Override
        public String call(String s) {
            return "[" + Thread.currentThread().getName() + ": " + s + "]";
        }
    };

    private StringDecorators() {
    }
}
