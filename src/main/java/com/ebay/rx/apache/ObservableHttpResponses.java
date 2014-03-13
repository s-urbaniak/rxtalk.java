package com.ebay.rx.apache;

import com.ebay.rx.StringFunctions;
import rx.Observable;
import rx.apache.http.ObservableHttpResponse;
import rx.util.functions.Func1;

public final class ObservableHttpResponses {
    private ObservableHttpResponses() {
    }

    public static Func1<ObservableHttpResponse, Observable<byte[]>> toContent = new Func1<ObservableHttpResponse, Observable<byte[]>>() {
        @Override
        public Observable<byte[]> call(ObservableHttpResponse response) {
            return response.getContent();
        }
    };

    public static Func1<Observable<byte[]>, Observable<String>> toString = new Func1<Observable<byte[]>, Observable<String>>() {
        @Override
        public Observable<String> call(Observable<byte[]> observable) {
            return observable.map(StringFunctions.byteArrayToString);
        }
    };
}
