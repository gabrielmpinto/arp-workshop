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

    private Verticle _____;

    @Test
    void verticleDeployed(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new SucceedingVerticle(), ar -> {
            if (ar.succeeded()) {
                testContext.completeNow();
            } else {
                testContext.failNow(ar.cause());
            }
        });
    }

    @Test
    void failedDeployment(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new FailingVerticle(), ar -> {
            if (ar.succeeded()) {
                testContext.failNow(ar.cause());
            } else {
                testContext.completeNow();
            }
        });
    }

    public class SucceedingVerticle extends AbstractVerticle {
        @Override
        public void start(Promise<Void> startPromise) {
            startPromise.complete();
        }
    }

    public class FailingVerticle extends AbstractVerticle {
        @Override
        public void start(Promise<Void> startPromise) {
            startPromise.fail("Failed to deploy");
        }
    }

    @Test
    void sendEventBusMessage(Vertx vertx, VertxTestContext testContext) {
        vertx.eventBus().consumer("address").handler(handler -> {
            testContext.completeNow();
        });

        vertx.eventBus().send("address", "message");
    }

    @Test
    void startHttpServer(Vertx vertx, VertxTestContext testContext) {
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
