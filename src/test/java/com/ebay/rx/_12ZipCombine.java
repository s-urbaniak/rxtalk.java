package com.ebay.rx;

import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import org.junit.Test;
import rx.Observable;

public class _12ZipCombine {

    @Test
    public void testZip() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> obs1 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=4&delay=300"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> obs2 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=4&delay=300"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> zipped = Observable.zip(obs1, obs2, (s1, s2) -> s1 + "|" + s2);

        zipped.toBlockingObservable().forEach(Actions.sout);
    }

    @Test
    public void testCombine() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> obs1 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=4&delay=300"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> obs2 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=4&delay=300"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> zipped = Observable.combineLatest(obs1, obs2, (s1, s2) -> s1 + "|" + s2);

        zipped.toBlockingObservable().forEach(Actions.sout);
    }

}
