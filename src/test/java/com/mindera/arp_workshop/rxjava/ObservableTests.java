package com.mindera.arp_workshop.rxjava;

import com.mindera.arp_workshop.rxjava.util.LessonResources;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ObservableTests {

    private Integer mSum;
    private String _____;
    private int ____;
    private TestObserver<Object> mObserver;
    private int mCount1;
    private int mCount2;
    private int mCount3;
    private String mStringA;
    private String mStringB;
    private String mStringC;

    @BeforeEach
    public void setup() {
        mObserver = new TestObserver<>();
    }

    @Test
    public void anObservableStreamOfEventsAndDataEmitsEachItemInOrder() {
        Observable.just("Foo", "Bar")
            .subscribe(mObserver);

        mObserver.assertValueCount(____);
        mObserver.assertValues(_____, _____);
    }

    @Test
    public void anObservableStreamEmitsThreeMajorEventTypes() {
        Observable<Integer> pipelineOfData = Observable.just(1, 2, 3, 4, 5);

        pipelineOfData
            .doOnNext(integer -> mCount1++)
            .doOnComplete(() -> mCount2++)
            .doOnError(throwable -> mCount3++)
            .subscribe(mObserver);

        mObserver.awaitTerminalEvent();
        assertThat(mCount1).isEqualTo(____);
        assertThat(mCount2).isEqualTo(____);
        assertThat(mCount3).isEqualTo(____);
    }

    @Test
    public void justCreatesAnObservableEmittingItsArguments() {
        String stoogeOne = "Larry";
        String stoogeTwo = "Moe";
        String stoogeThree = "Curly";
        Integer stoogeAge = 38;

        Observable<Object> stoogeDataObservable = Observable.just(_____, _____, _____, _____);
        stoogeDataObservable.subscribe(mObserver);

        List<Object> events = mObserver.values();
        assertThat(events).containsOnlyOnce(_____);
        assertThat(events).containsOnlyOnce(_____);
        assertThat(events).containsOnlyOnce(_____);
        assertThat(events).containsOnlyOnce(_____);
        assertThat(events).hasSize(____);
    }

    @Test
    public void fromCreatesAnObservableThatEmitsEachElementFromAnIterable() {
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
    public void nothingListensUntilYouSubscribe() {
        mSum = 0;

        Observable
            .range(1, 10)
            .doOnNext(integer -> mSum += integer);

        assertThat(mSum).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10);
    }

    @Test
    public void mapAppliesAFunctionToEachItemAndEmitsDataOnTheOtherSide() {
        Observable.fromIterable(Arrays.asList("kewl", "leet", "speak"))
            .map(word -> word.replace("e", "3"))
            .map(word -> word.replace("l", "1"))
            .subscribe(mObserver);

        assertThat(mObserver.values()).contains(_____);
        assertThat(mObserver.values()).contains(_____);
        assertThat(mObserver.values()).contains(_____);
    }

    @Test
    public void flatMapUnwrapsOneLevelOfNestingInAnObservableStream() {
        List<LessonResources.CarnivalFood> funnelCakeCart = Arrays.asList(new LessonResources.CarnivalFood("Cheese Pizza", 5.95),
            new LessonResources.CarnivalFood("Funnel Cake", 3.95),
            new LessonResources.CarnivalFood("Candied Apple", 1.50),
            new LessonResources.CarnivalFood("Jumbo Corn Dog", 2.25),
            new LessonResources.CarnivalFood("Deluxe Corned Beef Hoagie with Swiss Cheese", 6.75),
            new LessonResources.CarnivalFood("Faygo", 1.95));

        List<LessonResources.CarnivalFood> chineseFoodCart = Arrays.asList(new LessonResources.CarnivalFood("Duck Teriyaki Kabobs", 12.95),
            new LessonResources.CarnivalFood("Vegetable Dumplings", 2.50),
            new LessonResources.CarnivalFood("Poor Quality Shrimp Lo Mein", 4.75),
            new LessonResources.CarnivalFood("Green Tea Ice Cream", 3.95),
            new LessonResources.CarnivalFood("Basic Mandarin Chicken", 5.25));

        Observable<List<LessonResources.CarnivalFood>> foodCartItemsObservable = Observable.just(funnelCakeCart, chineseFoodCart);

        foodCartItemsObservable
            .map(Observable::fromIterable)
            .subscribe(mObserver);

        assertThat(mObserver.values()).hasSize(____);

        mObserver = new TestObserver<>();
        Observable<LessonResources.CarnivalFood> individualItemsObservable = foodCartItemsObservable
            .flatMap(Observable::fromIterable);
        individualItemsObservable.subscribe(mObserver);
        assertThat(mObserver.values()).hasSize(____);

        mObserver = new TestObserver<>();

        individualItemsObservable
            .filter(food -> food.mPrice < 5.00)
            .subscribe(mObserver);

        assertThat(mObserver.values()).hasSize(____);

        System.out.println("With my 5 bucks I can buy: " + mObserver.values());
    }

    @Test
    public void theReduceOperatorAccumulatesValuesAndEmitsTheResult() {
        TestObserver<Integer> testObserver = new TestObserver<>();

        List<LessonResources.ElevatorPassenger> elevatorPassengers = Arrays.asList(
            new LessonResources.ElevatorPassenger("Max", 168),
            new LessonResources.ElevatorPassenger("Mike", 234),
            new LessonResources.ElevatorPassenger("Ronald", 192),
            new LessonResources.ElevatorPassenger("William", 142),
            new LessonResources.ElevatorPassenger("Jacqueline", 114));

        Observable.fromIterable(elevatorPassengers)
            .reduce(0, (accumulatedWeight, elevatorPassenger) -> elevatorPassenger.mWeightInPounds + accumulatedWeight)
            .subscribe(testObserver);

        assertThat(testObserver.values().get(0)).isEqualTo(____);
    }

    @Test
    public void repeatOperatorRepeatsThePreviousOperationANumberOfTimes() {
        String weapon = "A Boomerang made of Pure Gold";
        TestObserver<Object> observer = new TestObserver<>();

        Observable<String> repeatingObservable = Observable.just(weapon).repeat(4);
        repeatingObservable.subscribe(observer);
        assertThat(observer.values()).hasSize(____);

        observer = new TestObserver<>();

        repeatingObservable
            .repeat(4)
            .subscribe(observer);

        assertThat(observer.values()).hasSize(____);
    }

    @Test
    public void composableFunctions() {
        mStringA = "";
        mStringB = "";
        mStringC = "";

        Observable.range(1, 6)
            .doOnNext(integer -> mStringA += integer)
            .doOnNext(integer -> {
                if (integer % 2 == 0) {
                    mStringB += integer;
                }
            })
            .doOnNext(integer -> mStringC += integer)
            .subscribe(integer -> mStringC += integer);

        assertThat(mStringA).isEqualTo("____");
        assertThat(mStringB).isEqualTo("____");
        assertThat(mStringC).isEqualTo("____");
    }

    @Test
    public void convertingEvents() {
        mStringA = "";
        Observable.just("wE", "hOpe", "yOU", "aRe", "eNjOyInG", "thIS")
            .map(s -> _____)
            .map(s -> _____)
            .subscribe(s -> mStringA += s);

        assertThat(mStringA).isEqualTo("we hope you are enjoying this ");
    }
}
