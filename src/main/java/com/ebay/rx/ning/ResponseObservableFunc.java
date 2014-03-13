package com.ebay.rx.ning;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import java.io.IOException;

public class ResponseObservableFunc implements Observable.OnSubscribeFunc<Response> {

    private final AsyncHttpClient.BoundRequestBuilder builder;

    private final static Logger log = LoggerFactory.getLogger(ResponseObservableFunc.class);

    public ResponseObservableFunc(AsyncHttpClient.BoundRequestBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Subscription onSubscribe(final Observer<? super Response> obs) {
        try {
            log.info(obs + " subscribing to " + this);
            return Subscriptions.from(builder.execute(newAsyncHandler(obs)));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    static AsyncCompletionHandler<Response> newAsyncHandler(final Observer<? super Response> obs) {
        Preconditions.checkNotNull(obs);

        return new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                obs.onNext(response);
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
