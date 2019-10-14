package com.mindera.arp_workshop.htto;

import com.mindera.arp_workshop.utils.Loggable;
import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;

public class HttpServerVerticle extends AbstractVerticle implements Loggable {
    private final Router router;

    public HttpServerVerticle(final Router router) {
        this.router = router;
    }

    @Override
    public Completable rxStart() {
        Integer port = config().getInteger("port");

        return vertx.createHttpServer()
            .requestHandler(router)
            .rxListen(port)
            .doOnSuccess(id -> log().info("Server started successfully on port {0}", port))
            .doOnError(e -> log().error("Failed to start server"))
            .ignoreElement();
    }
}
