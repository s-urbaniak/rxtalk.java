package com.ebay.rx;

import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import org.junit.Test;
import rx.Observable;
import rx.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

public class _14HotCold {

    @Test
    public void testMeasureBad() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<HttpResponseBodyPart> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=9&delay=300&jitter=1000"));

        Observable<String> chunks = obs.map(HttpResponseBodyParts.toString);
        chunks.subscribe(Actions.sout);

        Observable<Integer> measurement = obs
                .buffer(1, TimeUnit.SECONDS)
                .map((l) -> l.size());
        measurement.toBlockingObservable().forEach((i) -> System.out.println(i + " chunks/sec"));
    }

    @Test
    public void testMeasureGood() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        ConnectableObservable<HttpResponseBodyPart> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=9&delay=300&jitter=1000"))
                .publish();

        Observable<Integer> measurement = obs.buffer(1, TimeUnit.SECONDS).map((l) -> l.size());
        measurement.subscribe((i) -> System.out.println(i + " chunks/sec"));

        Observable<String> chunks = obs.map(HttpResponseBodyParts.toString);
        chunks.subscribe(Actions.sout);

        obs.connect();
        obs.toBlockingObservable().last();
    }

}
