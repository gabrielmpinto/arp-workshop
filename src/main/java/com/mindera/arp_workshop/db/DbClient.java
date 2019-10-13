package com.mindera.arp_workshop.db;

import com.mindera.arp_workshop.utils.Loggable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Tuple;
import io.vertx.sqlclient.PoolOptions;
import org.flywaydb.core.Flyway;

import static io.reactivex.internal.functions.Functions.identity;

public class DbClient extends AbstractVerticle implements Loggable {
    private final JsonObject config;

    private PgPool client;

    public DbClient(JsonObject config) {
        this.config = config.getJsonObject("db", new JsonObject());
    }

    @Override
    public void start() {
        client = initDbClient(vertx, config);

        vertx.eventBus().<Integer>consumer("findCity")
            .toFlowable()
            .subscribe(message -> {
                Integer body = message.body();

                findPerson(body)
                    .subscribe(message::reply, e -> {
                        log().error("Failed to query db.", e);
                        message.fail(404, "Failed to query db.");
                    });
            });
    }

    private PgPool initDbClient(Vertx vertx, JsonObject config) {
        var dbHost = config.getString("host");
        var db = config.getString("db");
        var user = config.getString("user");
        var pass = config.getString("pass");

        Flyway flyway = Flyway.configure()
            .dataSource("jdbc:postgresql://" + dbHost + "/" + db, user, pass)
            .load();
        flyway.migrate();

        PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(5432)
            .setHost(dbHost)
            .setDatabase(db)
            .setUser(user)
            .setPassword(pass);

        PoolOptions poolOptions = new PoolOptions()
            .setMaxSize(5);

        return PgPool.pool(vertx, connectOptions, poolOptions);
    }

    private Single<JsonObject> findPerson(Integer id) {
        return client.rxPreparedQuery("select * from city where id = $1", Tuple.of(id))
            .flattenAsObservable(identity())
            .map(result -> new JsonObject()
                .put("id", result.getInteger(0))
                .put("name", result.getString(1))
                .put("woeid", result.getInteger(2)))
            .firstOrError();
    }
}
