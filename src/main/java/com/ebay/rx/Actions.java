package com.ebay.rx;

import rx.util.functions.Action1;

public final class Actions {
    private Actions() {}

    public static Action1<String> sout = new Action1<String>() {
        @Override
        public void call(String s) {
            System.out.println(s);
        }
    };
}
