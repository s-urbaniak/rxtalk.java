package com.ebay.rx;

import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.google.common.collect.Lists;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import org.junit.Test;
import rx.Observable;
import rx.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

public class ObsTest8 {
    @Test
    public void testMap() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<HttpResponseBodyPart> obs1 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=10&delay=100"));

        Observable<HttpResponseBodyPart> obs2 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs2?it=10&delay=100"));

        Observable<String> merged = Observable.merge(obs1, obs2)
                .map(HttpResponseBodyParts.toString)
                .map((s) -> s.replaceAll("obs1", "###"));

        merged.toBlockingObservable().forEach(Actions.sout);
    }

    @Test
    public void testTake() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> bodies = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=10&delay=300"))
                .map(HttpResponseBodyParts.toString)
                .flatMap((b) -> Observable.from(Lists.newArrayList(b, b.replaceAll("obs1", "###"))))
                .take(2, TimeUnit.SECONDS);

        bodies.toBlockingObservable().forEach(Actions.sout);
    }

    @Test
    public void testMeasure() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<Integer> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=9&delay=300"))
                .buffer(1, TimeUnit.SECONDS)
                .map((l) -> l.size());

        obs.toBlockingObservable().forEach((c) -> System.out.println(c + " chunks/sec"));
    }

    @Test
    public void testThrottle() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=9&delay=300"))
                .throttleWithTimeout(300, TimeUnit.MILLISECONDS)
                .map(HttpResponseBodyParts.toString);

        obs.toBlockingObservable().forEach(Actions.sout);
    }

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
//        Observable<String> zipped = Observable.combineLatest(obs1, obs2, (s1, s2) -> s1 + "|" + s2);

        zipped.toBlockingObservable().forEach(Actions.sout);
    }

    @Test
    public void testMeasureAndPublishBad() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<HttpResponseBodyPart> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=9&delay=300"));

        Observable<String> s = obs.map(HttpResponseBodyParts.toString);
        Observable<Integer> m = obs.buffer(1, TimeUnit.SECONDS).map((l) -> l.size());

        s.subscribe(Actions.sout);
        m.toBlockingObservable().forEach((i) -> System.out.println(i + " chunks/sec"));
    }

    @Test
    public void testMeasureAndPublish() throws InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        ConnectableObservable<HttpResponseBodyPart> obs = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=9&delay=300"))
                .publish();

        Observable<String> s = obs.map(HttpResponseBodyParts.toString);
        Observable<Integer> m = obs.buffer(1, TimeUnit.SECONDS).map((l) -> l.size());

        s.subscribe(Actions.sout);
        m.subscribe((i) -> System.out.println(i + " chunks/sec"));

        obs.connect();
        m.toBlockingObservable().last();
    }
}
