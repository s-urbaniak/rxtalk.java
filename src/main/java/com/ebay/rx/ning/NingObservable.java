package com.ebay.rx.ning;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.Response;
import rx.Observable;

public final class NingObservable {
    private NingObservable() {
    }

    public static Observable<Response> create(final AsyncHttpClient.BoundRequestBuilder builder) {
        return Observable.create(new ResponseObservableFunc(builder));
    }

    public static Observable<HttpResponseBodyPart> createChunked(final AsyncHttpClient.BoundRequestBuilder builder) {
        return Observable.create(new ChunkedObservableFunc(builder));
    }
}
