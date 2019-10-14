package com.mindera.arp_workshop;

import com.mindera.arp_workshop.api.WeatherAPI;
import com.mindera.arp_workshop.db.DbClient;
import com.mindera.arp_workshop.htto.WeatherVerticle;
import com.mindera.arp_workshop.htto.HttpServerVerticle;
import com.mindera.arp_workshop.utils.Loggable;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;

public class MainVerticle extends AbstractVerticle implements Loggable {
    @Override
    public Completable rxStart() {
        return getConfigRetriever().rxGetConfig()
            .flatMapCompletable(this::deployVerticles);
    }

    private CompletableSource deployVerticles(JsonObject config) {
        var router = Router.router(vertx);

        return Observable.fromArray(
            new DbClient(),
            new WeatherAPI(),
            new WeatherVerticle(router),
            new HttpServerVerticle(router)
        )
            .flatMapSingle(verticle -> deployVerticle(config, verticle))
            .toList()
            .doOnSuccess(id -> log().info("All verticles deployed successfully"))
            .doOnError(e -> log().error("Failed to deploy verticles"))
            .ignoreElement();
    }

    private Single<String> deployVerticle(JsonObject config, AbstractVerticle verticle) {
        var options = new DeploymentOptions()
            .setConfig(config);
        return vertx.rxDeployVerticle(verticle, options);
    }

    private ConfigRetriever getConfigRetriever() {
        var fileStore = new ConfigStoreOptions()
            .setType("file")
            .setConfig(new JsonObject().put("path", "config.json"));
        var options = new ConfigRetrieverOptions()
            .addStore(fileStore);
        return ConfigRetriever.create(vertx, options);
    }
}
