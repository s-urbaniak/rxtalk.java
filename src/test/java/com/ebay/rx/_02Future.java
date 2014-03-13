package com.ebay.rx;

import com.google.common.base.Stopwatch;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class _02Future {

    @Test
    public void testSyncBetter() throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient client1 = new AsyncHttpClient();
        List<String> result = new ArrayList<>(2);

        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Response> f1 = client1.prepareGet("http://localhost:6060/obs1?it=5&nl&delay=100").execute();
        Future<Response> f2 = client1.prepareGet("http://localhost:6060/obs2?it=5&nl&delay=100").execute();

        result.add(f1.get().getResponseBody());
        result.add(f2.get().getResponseBody());

        System.out.println(stopwatch.stop().elapsed(MILLISECONDS) + " | " + result);
    }

}
