package com.ebay.rx;

import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.junit.Test;
import rx.Observable;
import rx.Observer;

import java.io.IOException;

public class _03_Whoops_ResponseObservableFunc {

    public static final Observer<Response> newObserver() {
        return new Observer<Response>() {
            @Override
            public void onCompleted() {
                System.out.println("YAY, done!");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("UGH");
                e.printStackTrace();
            }

            @Override
            public void onNext(Response response) {
                System.out.println("YAY, a response: " + response);
            }
        };
    }

    @Test
    public void testAsyncNing() throws IOException, InterruptedException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<Response> obs = NingObservable.create(client.prepareGet("http://localhost:6060/obs1?it=1&delay=1"));

        obs.subscribe(newObserver());
    }

}
