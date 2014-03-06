package com.ebay.rx.apache;

import rx.Observable;
import rx.apache.http.ObservableHttpResponse;
import rx.util.functions.Func1;

public final class ObservableHttpResponses {
    private ObservableHttpResponses() {
    }

    public static Func1<ObservableHttpResponse, Observable<String>> toString = new Func1<ObservableHttpResponse, Observable<String>>() {
        @Override
        public Observable<String> call(ObservableHttpResponse resp) {
            return resp.getContent().map(new Func1<byte[], String>() {
                @Override
                public String call(byte[] bytes) {
                    return new String(bytes);
                }
            });
        }
    };
}
