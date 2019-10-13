package com.mindera.arp_workshop.delegate;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.Message;

public class WeatherDelegate {
    private EventBus eventBus;

    public WeatherDelegate(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Single<JsonObject> getPerson(Integer id) {
        Single<JsonObject> cityObs = findCity(id);

        Single<Object> weather = cityObs
            .map(city -> city.getInteger("woeid"))
            .flatMap(this::getCityWeather);

        return Single.zip(cityObs, weather, (c, w) -> c.put("weather", w));
    }

    private Single<JsonObject> getCityWeather(Integer woeid) {
        return eventBus.<JsonObject>rxRequest("weather", woeid)
            .map(Message::body);
    }

    private Single<JsonObject> findCity(Integer id) {
        return eventBus.<JsonObject>rxRequest("findCity", id)
            .map(Message::body);
    }
}
