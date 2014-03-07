package com.ebay.rx;

import com.ebay.rx.apache.ObservableHttpResponses;
import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ebay.rx.ning.Responses;
import com.google.common.base.Stopwatch;
import com.ning.http.client.AsyncHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.junit.Test;
import rx.Observable;
import rx.apache.http.ObservableHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ObsTest7 {

    @Test
    public void testSync() throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient client1 = new AsyncHttpClient();
        List<String> result = new ArrayList<>(2);

        Stopwatch stopwatch = Stopwatch.createStarted();
        result.add(client1.prepareGet("http://localhost:6060/obs1?it=5&delay=100").execute().get().getResponseBody());
        result.add(client1.prepareGet("http://localhost:6060/obs2?it=5&delay=100").execute().get().getResponseBody());

        System.out.println(stopwatch.stop().elapsed(MILLISECONDS) + " | " + result);
    }

    @Test
    public void testAsyncNing() throws IOException {
        AsyncHttpClient client = new AsyncHttpClient();
        Observable<String> obs1 = NingObservable.create(client.prepareGet("http://localhost:6060/obs1?it=5&delay=100"))
                .map(Responses.toString);

        Observable<String> obs2 = NingObservable.create(client.prepareGet("http://localhost:6060/obs2?it=5&delay=100"))
                .map(Responses.toString);

        Observable<String> bodies = Observable
                .merge(obs1, obs2)
                .map(StringDecorators.threadName);

        Stopwatch stopwatch = Stopwatch.createStarted();
        bodies.toBlockingObservable().forEach(Actions.sout);
        System.out.println(stopwatch.stop().elapsed(MILLISECONDS));
    }

    @Test
    public void testAsyncNingChunked() throws IOException {
        AsyncHttpClient client = new AsyncHttpClient();
        Observable<String> obs1 = NingObservable.createChunked(client.prepareGet("http://localhost:6060/obs1?it=5&delay=100"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> obs2 = NingObservable.createChunked(client.prepareGet("http://localhost:6060/obs2?it=5&delay=100"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> bodies = Observable
                .merge(obs1, obs2)
                .map(StringDecorators.threadName);

        bodies.toBlockingObservable().forEach(Actions.sout);
    }

    @Test
    public void testAsyncApache() throws IOException {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();

        Observable<String> obs1 = ObservableHttp
                .createRequest(HttpAsyncMethods.createGet("http://localhost:6060/obs1?it=5&delay=100&nl"), client)
                .toObservable()
                .flatMap(ObservableHttpResponses.toString);

        Observable<String> obs2 = ObservableHttp
                .createRequest(HttpAsyncMethods.createGet("http://localhost:6060/obs1?it=5&delay=100&nl"), client)
                .toObservable()
                .flatMap(ObservableHttpResponses.toString);

        Observable<String> bodies = Observable
                .merge(obs1, obs2)
                .map(StringDecorators.threadName);

        bodies.toBlockingObservable().forEach(Actions.sout);
    }
}
