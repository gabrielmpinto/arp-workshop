package com.mindera.arp_workshop.htto;

import com.mindera.arp_workshop.utils.Loggable;
import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class HelloVerticle extends AbstractVerticle implements Loggable {

    private final Router router;

    public HelloVerticle(Router router) {
        this.router = router;
    }

    @Override
    public void start() {
        router.get("/hello/:name").handler(ctx -> {
            String name = ctx.pathParam("name");
            ctx.response()
                .setStatusCode(OK.code())
                .end("Hello, " + name);
        });
    }
}
