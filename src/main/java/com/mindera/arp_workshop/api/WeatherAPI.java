package com.mindera.arp_workshop.api;

import com.mindera.arp_workshop.utils.Loggable;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

public class WeatherAPI extends AbstractVerticle implements Loggable {

    private WebClient webClient;

    @Override
    public void start() {
        var apiConfig = config().getJsonObject("api");

        var options = new WebClientOptions()
            .setDefaultHost(apiConfig.getString("host", "localhost"))
            .setDefaultPort(apiConfig.getInteger("port", 8081))
            .setSsl(apiConfig.getBoolean("ssl", false));

        webClient = WebClient.create(vertx, options);

        vertx.eventBus().<Integer>consumer("getCityWeather")
            .toObservable()
            .subscribe(this::getCityWeather);
    }

    private void getCityWeather(Message<Integer> message) {
        Integer id = message.body();

        webClient.get("/api/location/" + id)
            .rxSend()
            .map(HttpResponse::bodyAsJsonObject)
            .filter(res -> !res.containsKey("detail"))
            .subscribe(message::reply, e -> {
                log().error("Failed to get weather for city {0}", e, id);
                message.fail(404, "Failed to get weather for city");
            });
    }
}
