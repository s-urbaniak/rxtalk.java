package com.ebay.rx;

import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

public class _09Take {

    @Test
    public void testTake() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> bodies = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=20&delay=500"))
                .map(HttpResponseBodyParts.toString)
                .take(2, TimeUnit.SECONDS);

        bodies.toBlockingObservable().forEach(Actions.sout);
    }

}
