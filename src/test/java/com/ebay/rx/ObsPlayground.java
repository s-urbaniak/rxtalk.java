package com.ebay.rx;

import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class ObsPlayground {
    @Test
    public void testMe() {
        Observable<Integer> obs = Observable.create(observer -> {
            System.out.println("Subscribed on " + Thread.currentThread().getName());
            observer.onNext(1);
            observer.onNext(2);
            observer.onNext(3);
            observer.onCompleted();
            return Subscriptions.empty();
        });

        Observable<Integer> o2 = obs.subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread());

        o2.subscribe((s) -> System.out.println(s + " observed on " + Thread.currentThread().getName()));

        o2.toList().toBlockingObservable().single();
    }
}
