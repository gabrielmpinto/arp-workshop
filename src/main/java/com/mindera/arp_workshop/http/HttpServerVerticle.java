package com.mindera.arp_workshop.http;

import com.mindera.arp_workshop.delegate.WeatherDelegate;
import com.mindera.arp_workshop.utils.Loggable;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class HttpServerVerticle extends AbstractVerticle implements Loggable {

    private final Router router;
    private final JsonObject config;
    private final WeatherDelegate weatherDelegate;

    public HttpServerVerticle(Router router,
                              WeatherDelegate weatherDelegate,
                              JsonObject config) {
        this.router = router;
        this.weatherDelegate = weatherDelegate;
        this.config = config;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        Integer port = config.getInteger("port", 8080);

        router.route().handler(BodyHandler.create().setBodyLimit(1024));
        router.get("/city/:id").handler(this::getCity);

        vertx.createHttpServer()
            .requestHandler(router)
            .rxListen(port)
            .subscribe(http -> {
                log().info("HTTP server started on port " + port);
                startPromise.complete();
            }, error -> {
                log().error("Failed to start HTTP server on port " + port, error);
                startPromise.fail(error);
            });
    }

    private void getCity(RoutingContext ctx) {
        Integer id = Integer.valueOf(ctx.pathParam("id"));
        weatherDelegate.getPerson(id)
            .subscribe(city -> {
                ctx.response().setStatusCode(OK.code()).end(city.encode());
            }, e -> {
                log().error("Failed to get city.", e);
                ctx.response().setStatusCode(INTERNAL_SERVER_ERROR.code()).end();
            });
    }
}
