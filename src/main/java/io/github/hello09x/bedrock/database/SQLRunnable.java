package io.github.hello09x.bedrock.database;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLRunnable {


    void run(@NotNull Connection connection) throws SQLException;


}
