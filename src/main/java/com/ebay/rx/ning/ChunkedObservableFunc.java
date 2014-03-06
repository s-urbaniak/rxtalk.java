package com.ebay.rx.ning;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import java.io.IOException;

public class ChunkedObservableFunc implements Observable.OnSubscribeFunc<HttpResponseBodyPart> {

    private final AsyncHttpClient.BoundRequestBuilder builder;

    public ChunkedObservableFunc(AsyncHttpClient.BoundRequestBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Subscription onSubscribe(final Observer<? super HttpResponseBodyPart> obs) {
        try {
            return Subscriptions.from(builder.execute(newAsyncHandler(obs)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AsyncCompletionHandler<Response> newAsyncHandler(final Observer<? super HttpResponseBodyPart> obs) {
        return new AsyncCompletionHandler<Response>() {
            @Override
            public STATE onBodyPartReceived(HttpResponseBodyPart content) throws Exception {
                obs.onNext(content);
                return super.onBodyPartReceived(content);
            }

            @Override
            public Response onCompleted(Response response) throws Exception {
                obs.onCompleted();
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                obs.onError(t);
            }
        };
    }
}
