package com.ebay.rx.ning;

import com.ning.http.client.HttpResponseBodyPart;
import rx.util.functions.Func1;

public final class HttpResponseBodyParts {
    private HttpResponseBodyParts() {
    }

    public static Func1<HttpResponseBodyPart, String> toString = new Func1<HttpResponseBodyPart, String>() {
        @Override
        public String call(HttpResponseBodyPart httpResponseBodyPart) {
            return new String(httpResponseBodyPart.getBodyPartBytes());
        }
    };
}
