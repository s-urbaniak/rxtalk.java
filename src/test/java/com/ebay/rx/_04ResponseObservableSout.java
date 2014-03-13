package com.ebay.rx;

import com.ebay.rx.ning.NingObservable;
import com.ebay.rx.ning.Responses;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.junit.Test;
import rx.Observable;

import java.io.IOException;

public class _04ResponseObservableSout {
    @Test
    public void testAsyncNing() throws IOException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<Response> obs1 = NingObservable.create(client.prepareGet("http://localhost:6060/obs1?it=5&delay=100&nl"));
        Observable<String> bodies = obs1.map(Responses.toString);

        String body = bodies.toBlockingObservable().first();
        System.out.println(body);
    }
}
