package com.mindera.arp_workshop.db;

import com.mindera.arp_workshop.utils.Loggable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Tuple;
import io.vertx.sqlclient.PoolOptions;
import org.flywaydb.core.Flyway;

public class DbClient extends AbstractVerticle implements Loggable {

    private PgPool pgPool;

    @Override
    public void start() {
        var dbConfig = config().getJsonObject("db");
        var dbHost = dbConfig.getString("host");
        var port = dbConfig.getInteger("port", 5432);
        var db = dbConfig.getString("db");
        var user = dbConfig.getString("user");
        var pass = dbConfig.getString("pass");

        Flyway flyway = Flyway.configure()
            .dataSource("jdbc:postgresql://" + dbHost + "/" + db, user, pass)
            .load();
        flyway.migrate();

        var options = new PgConnectOptions()
            .setPort(port)
            .setHost(dbHost)
            .setDatabase(db)
            .setUser(user)
            .setPassword(pass);

        var poolOptions = new PoolOptions()
            .setMaxSize(5);

        pgPool = PgPool.pool(vertx, options, poolOptions);

        vertx.eventBus().<Integer>consumer("findCity")
            .toObservable()
            .subscribe(message -> {
                Integer id = message.body();

                findCity(id).subscribe(message::reply, e -> {
                    log().error("Failed to query db.", e);
                    message.fail(404, "Failed to query db.");
                });
            });
    }

    private Single<JsonObject> findCity(Integer id) {
        return pgPool.rxPreparedQuery("select * from city where id = $1", Tuple.of(id))
            .flattenAsObservable(a -> a)
            .firstOrError()
            .map(row -> new JsonObject()
                .put(row.getColumnName(0), row.getInteger(0))
                .put(row.getColumnName(1), row.getString(1))
                .put(row.getColumnName(2), row.getInteger(2)));
    }

}
