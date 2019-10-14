package com.mindera.arp_workshop.rxjava;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CreatingObservableStreams {

    private Integer mSum;
    private String _____;
    private int ____;
    private TestObserver<Object> mSubscriber;
    private int mCount1;
    private int mCount2;
    private int mCount3;

    @BeforeEach
    public void setup() {
        mSubscriber = new TestObserver<>();
    }

    @Test
    public void _1_anObservableStreamOfEventsAndDataEmitsEachItemInOrder() {
        Observable.just("Foo", "Bar")
            .subscribe(mSubscriber);

        mSubscriber.assertValueCount(2);
        mSubscriber.assertValues("Foo", "Bar");
    }

    @Test
    public void _2_anObservableStreamEmitsThreeMajorEventTypes() {
        Observable<Integer> pipelineOfData = Observable.just(1, 2, 3, 4, 5);

        pipelineOfData
            .doOnNext(integer -> mCount1++)
            .doOnComplete(() -> mCount2++)
            .doOnError(throwable -> mCount3++)
            .subscribe(mSubscriber);

        mSubscriber.awaitTerminalEvent();
        assertThat(mCount1).isEqualTo(5);
        assertThat(mCount2).isEqualTo(1);
        assertThat(mCount3).isEqualTo(0);
    }

    @Test
    public void _3_justCreatesAnObservableEmittingItsArguments() {
        String stoogeOne = "Larry";
        String stoogeTwo = "Moe";
        String stoogeThree = "Curly";
        Integer stoogeAge = 38;

        Observable<Object> stoogeDataObservable = Observable.just(stoogeOne, stoogeTwo, stoogeThree, stoogeAge);
        stoogeDataObservable.subscribe(mSubscriber);

        List<Object> events = mSubscriber.values();
        assertThat(events).containsOnlyOnce(stoogeOne);
        assertThat(events).containsOnlyOnce(stoogeTwo);
        assertThat(events).containsOnlyOnce(stoogeThree);
        assertThat(events).containsOnlyOnce(stoogeAge);
        assertThat(events).hasSize(4);
    }

    @Test
    public void _4_fromCreatesAnObservableThatEmitsEachElementFromAnIterable() {
        List<String> sandwichIngredients = Arrays.asList("bread (one)", "bread (two)", "cheese", "mayo", "turkey", "lettuce", "pickles", "jalapenos", "Sriracha sauce");

        Observable<String> favoriteFoodsObservable = Observable.fromIterable(sandwichIngredients);

        TestObserver<Object> subscriber = new TestObserver<>();
        favoriteFoodsObservable.subscribe(subscriber);
        assertThat(subscriber.values()).hasSize(sandwichIngredients.size());
//        assertThat(subscriber.values()).contains(sandwichIngredients);
        assertThat(subscriber.values()).containsAll(sandwichIngredients);

        subscriber = new TestObserver<>();
        Observable.just(sandwichIngredients).subscribe(subscriber);
        assertThat(subscriber.values()).hasSize(1);
        assertThat(subscriber.values()).contains(sandwichIngredients);
    }

    @Test
    public void _5_nothingListensUntilYouSubscribe() {
        mSum = 0;

        Observable
            .range(1, 10)
            .doOnNext(integer -> mSum += integer)
            .subscribe();

        assertThat(mSum).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10);
    }

}
