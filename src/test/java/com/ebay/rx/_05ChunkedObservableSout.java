package com.ebay.rx;

import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import org.junit.Test;
import rx.Observable;

import java.io.IOException;

public class _05ChunkedObservableSout {

    @Test
    public void testAsyncNing() throws IOException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> obs1 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=10&delay=200&jitter=1000"))
                .map(HttpResponseBodyParts.toString);

        obs1.toBlockingObservable().forEach(Actions.sout);
    }
}
