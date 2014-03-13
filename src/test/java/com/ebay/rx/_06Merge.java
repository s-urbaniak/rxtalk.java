package com.ebay.rx;

import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import org.junit.Test;
import rx.Observable;

public class _06Merge {
    @Test
    public void testMerge() {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<HttpResponseBodyPart> obs1 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=10&delay=200&jitter=2000"));

        Observable<HttpResponseBodyPart> obs2 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs2?it=10&delay=200&jitter=2000"));

        Observable<String> bodies = Observable
                .merge(obs1, obs2)
                .map(HttpResponseBodyParts.toString)
                .map(StringFunctions.decorateWithThreadName);

        bodies.toBlockingObservable().forEach(Actions.sout);
    }
}
