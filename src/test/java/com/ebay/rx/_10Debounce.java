package com.ebay.rx;

import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

public class _10Debounce {

    @Test
    public void testThrottle() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=10&delay=295&jitter=100"))
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(HttpResponseBodyParts.toString);

        obs.toBlockingObservable().forEach(Actions.sout);
    }

}
