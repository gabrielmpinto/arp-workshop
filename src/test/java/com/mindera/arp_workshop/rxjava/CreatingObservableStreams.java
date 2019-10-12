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

    mSubscriber.assertValueCount(____);
    mSubscriber.assertValues(_____, _____);
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
    assertThat(mCount1).isEqualTo(____);
    assertThat(mCount2).isEqualTo(____);
    assertThat(mCount3).isEqualTo(____);
  }

  @Test
  public void _3_justCreatesAnObservableEmittingItsArguments() {
    String stoogeOne = "Larry";
    String stoogeTwo = "Moe";
    String stoogeThree = "Curly";
    Integer stoogeAge = 38;

    Observable<Object> stoogeDataObservable = Observable.just(_____, _____, _____, _____);
    stoogeDataObservable.subscribe(mSubscriber);

    List<Object> events = mSubscriber.values();
    assertThat(events).containsOnlyOnce(_____);
    assertThat(events).containsOnlyOnce(_____);
    assertThat(events).containsOnlyOnce(_____);
    assertThat(events).containsOnlyOnce(_____);
    assertThat(events).hasSize(____);
  }

  @Test
  public void _4_fromCreatesAnObservableThatEmitsEachElementFromAnIterable() {
    List<String> sandwichIngredients = Arrays.asList("bread (one)", "bread (two)", "cheese", "mayo", "turkey", "lettuce", "pickles", "jalapenos", "Sriracha sauce");

    Observable<String> favoriteFoodsObservable = Observable.fromIterable(sandwichIngredients);

    TestObserver<Object> subscriber = new TestObserver<>();
    favoriteFoodsObservable.subscribe(subscriber);
    assertThat(subscriber.values()).hasSize(____);
    assertThat(subscriber.values()).contains(_____);
//    assertThat(subscriber.values()).containsAll(_____);

    subscriber = new TestObserver<>();
    Observable.just(sandwichIngredients).subscribe(subscriber);
    assertThat(subscriber.values()).hasSize(____);
    assertThat(subscriber.values()).contains(_____);
  }

  @Test
  public void _5_nothingListensUntilYouSubscribe() {
    mSum = 0;

    Observable
      .range(1, 10)
      .doOnNext(integer -> mSum += integer);

    assertThat(mSum).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10);
  }

}
