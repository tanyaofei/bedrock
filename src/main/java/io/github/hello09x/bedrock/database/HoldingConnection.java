package io.github.hello09x.bedrock.database;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;


public class HoldingConnection {

    @Getter
    public Connection raw;
    private int count = 0;

    public HoldingConnection(@NotNull Connection connection) {
        this.raw = connection;
    }

    public int increase() {
        return ++this.count;
    }

    public int decrease() {
        return --this.count;
    }

}
