package com.ebay.rx;

import com.ebay.rx.apache.ObservableHttpResponses;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.junit.Test;
import rx.Observable;
import rx.apache.http.ObservableHttp;

import java.io.IOException;

public class _07FlatMap {

    @Test
    public void testAsyncApache() throws IOException {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();

        Observable<String> obs = ObservableHttp
                .createRequest(HttpAsyncMethods.createGet("http://localhost:6060/obs1?it=5&delay=100&nl"), client)
                .toObservable()
                .map(ObservableHttpResponses.toContent)
                .flatMap(ObservableHttpResponses.toString);

        obs.toBlockingObservable().forEach(Actions.sout);
        client.close();
    }

}
