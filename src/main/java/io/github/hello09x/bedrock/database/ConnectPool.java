package io.github.hello09x.bedrock.database;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectPool {

    public final static ThreadLocal<HoldingConnection> CURRENT_CONNECT = new ThreadLocal<>();
    private final LinkedBlockingQueue<HoldingConnection> idle;
    private final Set<HoldingConnection> using;
    private final String url;
    @Nullable
    private final String username;
    @Nullable
    private final String password;

    public ConnectPool(
            @NotNull String driverClassName,
            @NotNull String url,
            @Nullable String username,
            @Nullable String password,
            int size
    ) {
        if (size <= 0) {
            throw new IllegalArgumentException("Invalid size: " + size);
        }
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid driver class name: " + driverClassName);
        }

        this.url = url;
        this.username = username;
        this.password = password;
        this.idle = new LinkedBlockingQueue<>();
        this.using = Collections.synchronizedSet(new HashSet<>());

        for (int i = 0; i < size; i++) {
            this.idle.add(connect());
        }
    }

    private @NotNull HoldingConnection connect() {
        try {
            return new HoldingConnection(DriverManager.getConnection(url, username, password));
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to connect to database", e);
        }
    }

    public void add(@NotNull HoldingConnection connection) {
        idle.add(connection);
    }

    public @NotNull HoldingConnection get() {
        var conn = CURRENT_CONNECT.get();
        if (conn != null) {
            conn.increase();
            return conn;
        }

        try {
            conn = idle.poll(5, TimeUnit.SECONDS);
            if (conn == null) {
                throw new IllegalStateException("No idle connection after waiting for 5 seconds");
            }

            if (conn.getRaw().isClosed()) {
                conn = connect();
            }

            using.add(conn);
            CURRENT_CONNECT.set(conn);
            conn.increase();
            return conn;
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted while waiting a connection", e);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void close(@NotNull HoldingConnection connection) {
        if (connection.decrease() <= 0) {
            try {
                this.using.remove(connection);
                idle.add(connection);
            } finally {
                CURRENT_CONNECT.remove();
            }
        }
    }

}
