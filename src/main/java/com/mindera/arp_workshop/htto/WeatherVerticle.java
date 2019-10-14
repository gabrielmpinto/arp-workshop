package com.mindera.arp_workshop.htto;

import com.mindera.arp_workshop.utils.Loggable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.web.Router;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class WeatherVerticle extends AbstractVerticle implements Loggable {

    private final Router router;

    public WeatherVerticle(Router router) {
        this.router = router;
    }

    @Override
    public void start() {
        router.get("/weather/:id").handler(ctx -> {
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            Single<JsonObject> city = vertx.eventBus().<JsonObject>rxRequest("findCity", id)
                .map(Message::body);

            Single<JsonObject> weather = city
                .map(result -> result.getInteger("woeid"))
                .flatMap(this::getCityWeather);

            Single.zip(city, weather, (c, w) -> c.put("weather", w))
                .subscribe(result -> {
                    ctx.response()
                        .setStatusCode(OK.code())
                        .end(result.encode());
                }, e -> {
                    ReplyException ex = (ReplyException) e;
                    ctx.response().setStatusCode(ex.failureCode()).end();
                });
        });
    }

    private Single<JsonObject> getCityWeather(Integer woeid) {
        return vertx.eventBus().<JsonObject>rxRequest("getCityWeather", woeid)
            .map(Message::body);
    }
}
