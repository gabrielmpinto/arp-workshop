package com.mindera.arp_workshop;

import com.mindera.arp_workshop.api.WeatherApi;
import com.mindera.arp_workshop.db.DbClient;
import com.mindera.arp_workshop.delegate.WeatherDelegate;
import com.mindera.arp_workshop.http.HttpServerVerticle;
import com.mindera.arp_workshop.utils.Loggable;
import io.reactivex.Flowable;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;

public class MainVerticle extends AbstractVerticle implements Loggable {
    private static final String DEFAULT_CONFIG_FILE = "config.json";

    @Override
    public void start(Promise<Void> startPromise) {
        buildConfigRetriever()
            .rxGetConfig()
            .subscribe(config -> {
                final var router = Router.router(vertx);
                final var weatherDelegate = new WeatherDelegate(vertx.eventBus());

                final var httpServerVerticle = new HttpServerVerticle(router, weatherDelegate, config);
                final var dbClient = new DbClient(config);
                final var weatherApi = new WeatherApi(config);

                Flowable.<AbstractVerticle>fromArray(weatherApi, dbClient, httpServerVerticle)
                    .flatMapSingle(verticle -> vertx.rxDeployVerticle(verticle))
                    .toList()
                    .subscribe(deployments -> {
                        log().info("Successfully deployed all verticles.");
                        startPromise.complete();
                    }, e -> {
                        log().error("Failed to start application.", e);
                        startPromise.fail(e);
                    });
            }, e -> {
                log().error("Failed to retrieve configuration.", e);
                startPromise.fail(e);
            });
    }

    private ConfigRetriever buildConfigRetriever() {
        var fileStore = new ConfigStoreOptions()
            .setType("file")
            .setConfig(new JsonObject().put("path", DEFAULT_CONFIG_FILE));
        var options = new ConfigRetrieverOptions()
            .addStore(fileStore);
        return ConfigRetriever.create(vertx, options);
    }

    private ShiroAuthOptions buildShiroAuthOptions() {
        return new ShiroAuthOptions()
            .setType(ShiroAuthRealmType.PROPERTIES)
            .setConfig(new JsonObject());
    }
}
