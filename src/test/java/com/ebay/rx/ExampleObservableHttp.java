package com.ebay.rx;

/**
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import rx.Observable;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;
import rx.util.functions.Action1;
import rx.util.functions.Func1;

import java.io.IOException;
import java.net.URISyntaxException;

public class ExampleObservableHttp {

    public static void main(String args[]) {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();

        try {
            httpclient.start();
            executeViaObservableHttpWithForEach(httpclient);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
        ObservableHttp.createGet("http://www.wikipedia.com", httpClient).toObservable();
        ObservableHttp.createRequest(HttpAsyncMethods.createGet("http://www.wikipedia.com"), httpClient).toObservable();
    }

    protected static void executeViaObservableHttpWithForEach(final HttpAsyncClient client) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("---- executeViaObservableHttpWithForEach");
        ObservableHttp.createRequest(HttpAsyncMethods.createGet("http://www.wikipedia.com"), client)
                .toObservable()
                .flatMap(new Func1<ObservableHttpResponse, Observable<String>>() {

                    @Override
                    public Observable<String> call(ObservableHttpResponse response) {
                        return response.getContent().map(new Func1<byte[], String>() {

                            @Override
                            public String call(byte[] bb) {
                                return new String(bb);
                            }

                        });
                    }
                })
                .toBlockingObservable()
                .forEach(new Action1<String>() {

                    @Override
                    public void call(String resp) {
                        System.out.println(resp);
                    }
                });
    }

}