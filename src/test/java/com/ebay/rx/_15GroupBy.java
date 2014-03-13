package com.ebay.rx;

import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import org.junit.Test;
import rx.Observable;

public class _15GroupBy {

    @Test
    public void testGroupBy() {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> obs1 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs1?it=10&delay=100"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> obs2 = NingObservable
                .createChunked(client.prepareGet("http://localhost:6060/obs2?it=5&delay=1000"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> obs = Observable
                .merge(obs1, obs2)
                .groupBy((s) -> Integer.parseInt(s.substring(s.length() - 1, s.length())))
                .flatMap((g) -> {
                    if (g.getKey() % 2 == 0) {
                        return g.map((s) -> "even " + s);
                    } else {
                        return g.map((s) -> "odd " + s);
                    }
                });

        obs.toBlockingObservable().forEach(Actions.sout);
    }

}
