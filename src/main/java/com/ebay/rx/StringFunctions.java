package com.ebay.rx;

import rx.util.functions.Func1;

public final class StringFunctions {

    private StringFunctions() {
    }

    public static Func1<String, String> decorateWithThreadName = new Func1<String, String>() {
        @Override
        public String call(String s) {
            return "[" + Thread.currentThread().getName() + ": " + s + "]";
        }
    };

    public static Func1<byte[], String> byteArrayToString = new Func1<byte[], String>() {
        @Override
        public String call(byte[] bytes) {
            return new String(bytes);
        }
    };

}
