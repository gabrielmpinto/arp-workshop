package com.mindera.arp_workshop.api;

import com.mindera.arp_workshop.utils.Loggable;
import io.reactivex.Single;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

public class WeatherApi extends AbstractVerticle implements Loggable {
    private final JsonObject config;

    private WebClient client;

    public WeatherApi(JsonObject config) {
        this.config = config.getJsonObject("api", new JsonObject());
    }

    public void start(Promise<Void> start) {
        this.client = initWebClient();

        vertx.eventBus().<Integer>consumer("weather").toFlowable()
            .subscribe(message -> {
                Integer body = message.body();

                weather(body)
                    .subscribe(message::reply, e -> {
                        log().error("Failed to query db.", e);
                        message.fail(404, "Failed to query db.");
                    });
            });
    }

    private WebClient initWebClient() {
        WebClientOptions options = new WebClientOptions()
            .setDefaultHost(config.getString("host"))
            .setDefaultPort(config.getInteger("port"))
            .setSsl(config.getBoolean("ssl"));

        return WebClient.create(vertx, options);
    }

    private Single<JsonObject> weather(final Integer woeid) {
        return client.get("/api/location/" + woeid)
            .rxSend()
            .map(HttpResponse::bodyAsJsonObject);
    }
}
