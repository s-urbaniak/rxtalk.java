package com.ebay.rx;

import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

public class _13Measure {

    @Test
    public void testMeasure() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<Integer> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=20&delay=100&jitter=1000"))
                .buffer(1, TimeUnit.SECONDS)
                .map((l) -> l.size());

        obs.toBlockingObservable().forEach((c) -> System.out.println(c + " chunks/sec"));
    }

}
