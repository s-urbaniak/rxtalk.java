package com.ebay.rx;

import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import org.junit.Test;
import rx.Observable;

public class _08 {

    @Test
    public void testMap() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<HttpResponseBodyPart> obs1 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=10&delay=100"));

        Observable<HttpResponseBodyPart> obs2 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs2?it=10&delay=100"));

        Observable<String> merged = Observable.merge(obs1, obs2)
                .map((chunk) -> new String(chunk.getBodyPartBytes()))
                .map((s) -> s.replaceAll("obs1", "###"));

        merged.toBlockingObservable().forEach(Actions.sout);
    }

}
