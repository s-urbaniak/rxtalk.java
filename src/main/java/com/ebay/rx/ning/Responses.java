package com.ebay.rx.ning;

import com.ning.http.client.Response;
import rx.util.functions.Func1;

import java.io.IOException;

public final class Responses {
    private Responses() {
    }

    public final static Func1<Response, String> toString = new Func1<Response, String>() {
        @Override
        public String call(Response response) {
            try {
                return response.getResponseBody();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };
}
