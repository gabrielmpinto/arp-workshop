package com.mindera.arp_workshop.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class VertxFunctionalityTests {

    Verticle _____;

    @Test
    void verticle_deployed(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(_____, testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void failedDeployment(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(_____, testContext.failing(id -> testContext.completeNow()));
    }

    public class SucceedingVerticle extends AbstractVerticle {
        @Override
        public void start(Promise<Void> startPromise) {
        }
    }

    public class FailingVerticle extends AbstractVerticle {
        @Override
        public void start(Promise<Void> startPromise) {
        }
    }

    @Test
    void sendEventBusMessage(Vertx vertx, VertxTestContext testContext) {
        vertx.eventBus().consumer("address").handler(handler -> {
            testContext.completeNow();
        });
    }

    @Test
    void name(Vertx vertx, VertxTestContext testContext) {
        vertx.createHttpServer()
            .requestHandler(req -> testContext.completeNow())
            .listen(8888, http -> {
                if (http.succeeded()) {
                    System.out.println("HTTP server started on port 8888");
                } else {
                    testContext.failNow(http.cause());
                }
            });
    }
}
