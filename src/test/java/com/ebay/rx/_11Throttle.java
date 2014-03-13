package com.ebay.rx;

import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

public class _11Throttle {

    @Test
    public void testThrottleFirst() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=20&delay=20"))
                .throttleFirst(50, TimeUnit.MILLISECONDS)
                .map(HttpResponseBodyParts.toString);

        obs.toBlockingObservable().forEach(Actions.sout);
    }

    @Test
    public void testThrottleLast() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=20&delay=20"))
                .throttleLast(50, TimeUnit.MILLISECONDS)
                .map(HttpResponseBodyParts.toString);

        obs.toBlockingObservable().forEach(Actions.sout);
    }

}
