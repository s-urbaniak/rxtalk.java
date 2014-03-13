package com.ebay.rx;

import com.google.common.base.Stopwatch;
import com.ning.http.client.AsyncHttpClient;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class _01Future {

    @Test
    public void testSync() throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient client1 = new AsyncHttpClient();
        List<String> result = new ArrayList<>(2);

        Stopwatch stopwatch = Stopwatch.createStarted();
        result.add(client1.prepareGet("http://localhost:6060/obs1?it=5&delay=100&nl").execute().get().getResponseBody());
        result.add(client1.prepareGet("http://localhost:6060/obs2?it=5&delay=100&nl").execute().get().getResponseBody());
        System.out.println(stopwatch.stop().elapsed(MILLISECONDS) + " | " + result);
    }

}
