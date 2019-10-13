package com.mindera.arp_workshop.rxjava;

import com.mindera.arp_workshop.rxjava.util.LessonResources.CarnivalFood;
import com.mindera.arp_workshop.rxjava.util.LessonResources.ElevatorPassenger;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MapAndFlatMapAndBasicOperators {

    private String _____;
    private int ____;
    private String mStringA;
    private String mStringB;
    private String mStringC;
    private TestObserver<Object> mObserver;

    @BeforeEach
    public void setup() {
        mObserver = new TestObserver<>();
    }

    @Test
    public void _1_mapAppliesAFunctionToEachItemAndEmitsDataOnTheOtherSide() {
        Observable.fromIterable(Arrays.asList("kewl", "leet", "speak"))
            .map(word -> word.replace("e", "3"))
            .map(word -> word.replace("l", "1"))
            .subscribe(mObserver);

        assertThat(mObserver.values()).contains(_____);
        assertThat(mObserver.values()).contains(_____);
        assertThat(mObserver.values()).contains(_____);
    }

    @Test
    public void _2_flatMapUnwrapsOneLevelOfNestingInAnObservableStream() {
        List<CarnivalFood> funnelCakeCart = Arrays.asList(new CarnivalFood("Cheese Pizza", 5.95),
            new CarnivalFood("Funnel Cake", 3.95),
            new CarnivalFood("Candied Apple", 1.50),
            new CarnivalFood("Jumbo Corn Dog", 2.25),
            new CarnivalFood("Deluxe Corned Beef Hoagie with Swiss Cheese", 6.75),
            new CarnivalFood("Faygo", 1.95));

        List<CarnivalFood> chineseFoodCart = Arrays.asList(new CarnivalFood("Duck Teriyaki Kabobs", 12.95),
            new CarnivalFood("Vegetable Dumplings", 2.50),
            new CarnivalFood("Poor Quality Shrimp Lo Mein", 4.75),
            new CarnivalFood("Green Tea Ice Cream", 3.95),
            new CarnivalFood("Basic Mandarin Chicken", 5.25));

        Observable<List<CarnivalFood>> foodCartItemsObservable = Observable.just(funnelCakeCart, chineseFoodCart);

        foodCartItemsObservable
            .map(Observable::fromIterable)
            .subscribe(mObserver);

        assertThat(mObserver.values()).hasSize(____);

        mObserver = new TestObserver<>();
        Observable<CarnivalFood> individualItemsObservable = foodCartItemsObservable
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

    /**
     * Reduce is helpful for aggregating a set of data and emitting a final result
     */
    @Test
    public void _3_theReduceOperatorAccumulatesValuesAndEmitsTheResult() {
        TestObserver<Integer> testObserver = new TestObserver<>();

        List<ElevatorPassenger> elevatorPassengers = Arrays.asList(
            new ElevatorPassenger("Max", 168),
            new ElevatorPassenger("Mike", 234),
            new ElevatorPassenger("Ronald", 192),
            new ElevatorPassenger("William", 142),
            new ElevatorPassenger("Jacqueline", 114));

        Observable.fromIterable(elevatorPassengers)
            .reduce(0, (accumulatedWeight, elevatorPassenger) -> elevatorPassenger.mWeightInPounds + accumulatedWeight)
            .subscribe(testObserver);

        assertThat(testObserver.values().get(0)).isEqualTo(____);
    }

    @Test
    public void _4_repeatOperatorRepeatsThePreviousOperationANumberOfTimes() {
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
    public void _5_composableFunctions() {
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
    public void _6_convertingEvents() {
        mStringA = "";
        Observable.just("wE", "hOpe", "yOU", "aRe", "eNjOyInG", "thIS")
            .map(s -> _____)
            .map(s -> _____)
            .subscribe(s -> mStringA += s);

        assertThat(mStringA).isEqualTo("we hope you are enjoying this ");
    }


}
