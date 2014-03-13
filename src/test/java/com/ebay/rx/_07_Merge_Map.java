package com.ebay.rx;

import com.ebay.rx.apache.ObservableHttpResponses;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.junit.Test;
import rx.Observable;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;

import java.io.IOException;

public class _07_Merge_Map {

    @Test
    public void testAsyncApache() throws IOException {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();

        Observable<ObservableHttpResponse> obs = ObservableHttp
                .createRequest(HttpAsyncMethods.createGet("http://localhost:6060/obs1?it=5&delay=100&nl"), client)
                .toObservable();

        // OH NOES ... what now ??
        Observable<Observable<byte[]>> content = obs
                .map(ObservableHttpResponses.toContent);

        // First merge it (as in _06Merge)
        Observable<byte[]> merged = Observable
                .merge(content);

        // Then map it to an observable of strings
        Observable<String> chunks = merged
                .map(StringFunctions.byteArrayToString);

        chunks.toBlockingObservable().forEach(Actions.sout);
        client.close();
    }

}
