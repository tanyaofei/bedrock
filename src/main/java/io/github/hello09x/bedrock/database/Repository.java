package io.github.hello09x.bedrock.database;

import com.google.common.reflect.TypeToken;
import io.github.hello09x.bedrock.page.Page;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Repository<T> {

    private final static ConnectionPoolHolder holder = new ConnectionPoolHolder();
    private static Map<Plugin, DatasourceConfig> configs = new ConcurrentHashMap<>();

    private final Plugin plugin;
    private final ConnectPool pool;
    private final TableInfo tableInfo;

    public Repository(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.pool = holder.getOrCreate(
                plugin,
                () -> configs.computeIfAbsent(plugin, p -> new DatasourceConfig(p, null))
        );

        this.initTables();
        this.tableInfo = TableInfo.parse(getModelType());
    }

    @SneakyThrows
    public static @NotNull DBType getDBType(@NotNull Plugin plugin) {
        var connection = holder.getOrCreate(
                plugin,
                () -> configs.computeIfAbsent(plugin, p -> new DatasourceConfig(p, null))
        ).get().getRaw();

        return switch (connection.getMetaData().getDatabaseProductName()) {
            case "SQLite" -> DBType.SQLITE;
            case "MySQL" -> DBType.MYSQL;
            default -> throw new UnsupportedOperationException("Unsupported database");
        };
    }

    protected abstract void initTables();

    public @Nullable T selectById(@NotNull Serializable id) {
        var sql = "select * from " + tableInfo.tableName() + " where " + getPk() + " = ?";

        return execute(connect -> {
            try (PreparedStatement stm = connect.prepareStatement(sql)) {
                stm.setObject(1, id);
                return this.mapOne(stm.executeQuery());
            }
        });
    }

    public boolean existsById(@NotNull Serializable id) {
        var sql = String.format("select exists (select 1 from %s where %s = ?)", tableInfo.tableName(), getPk());
        return execute(connection -> {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setObject(1, id);
                return stm.executeQuery().getInt(1) == 1;
            }
        });
    }

    public @NotNull List<T> selectAll() {
        var sql = "select * from " + tableInfo.tableName();
        return execute(connection -> {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                return mapMany(stm.executeQuery());
            }
        });
    }

    public @NotNull List<T> selectByIds(@NotNull Collection<? extends Serializable> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        var parameters = new String[ids.size()];
        Arrays.fill(parameters, "?");

        var sql = String.format(String.format(
                """
                        select * from %s where %s in ( %s )
                        """,
                tableInfo.tableName(),
                getPk(),
                String.join(",", parameters))
        );

        return execute(connection -> {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                int i = 1;
                for (var id : ids) {
                    stm.setObject(i++, id);
                }
                return mapMany(stm.executeQuery());
            }
        });
    }

    public int deleteById(@NotNull Serializable id) {
        var sql = """
                delete from %s where %s = ?
                """;
        return execute(connection -> {
            try (PreparedStatement stm = connection.prepareStatement(String.format(
                    sql,
                    tableInfo.tableName(),
                    getPk()))
            ) {
                stm.setObject(1, id);
                return stm.executeUpdate();
            }
        });
    }

    public int deleteByIds(@NotNull List<? extends Serializable> ids) {
        if (ids.isEmpty()) {
            return 0;
        }

        var sql = "delete from %s where %s in (%s)".formatted(
                tableInfo.tableName(),
                IntStream.range(0, ids.size()).mapToObj(x -> "?").collect(Collectors.joining(", "))
        );

        return execute(connection -> {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                int i = 1;
                for (var id : ids) {
                    stm.setObject(i++, id);
                }
                return stm.executeUpdate();
            }
        });

    }

    public int count() {
        var sql = "select count(*) from " + tableInfo.tableName();
        return execute(connection -> {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                return stm.executeQuery().getInt(1);
            }
        });
    }

    public @NotNull Page<T> selectPage(int page, int size) {
        return selectPage("select * from " + tableInfo.tableName(), page, size);
    }

    protected @NotNull Page<T> selectPage(@NotNull String rawSql, int page, int size) {
        var countSql = "select count(*) from (" + rawSql + ")";
        int total = execute(connection -> {
            try (PreparedStatement stm = connection.prepareStatement(countSql)) {
                return stm.executeQuery().getInt(1);
            }
        });
        if (total == 0) {
            return Page.empty();
        }

        var offset = (page - 1) * size;
        var sql = rawSql + " LIMIT ?, ?";
        return execute(connection -> {
            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setInt(1, offset);
                stm.setInt(2, size);
                return new Page<>(
                        mapMany(stm.executeQuery()),
                        page,
                        size,
                        (int) Math.ceil((double) total / (double) size),
                        total
                );
            }
        });
    }

    private @NotNull String getPk() {
        var pk = tableInfo.primaryKey();
        if (pk == null) {
            throw new IllegalStateException("No primary key specified: " + tableInfo.type());
        }
        return pk.columnName();
    }

    protected <R> @NotNull R execute(@NotNull SQLExecution<R> execution) {
        var connection = pool.get();
        R ret;
        try {
            ret = execution.execute(connection.getRaw());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.close(connection);
        }
        return ret;
    }

    protected void execute(@NotNull SQLRunnable runnable) {
        var connection = pool.get();
        try {
            runnable.run(connection.getRaw());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.close(connection);
        }
    }

    protected @Nullable T mapOne(@NotNull ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        var values = new Object[tableInfo.columns().size()];
        var itr = tableInfo.columns().listIterator();
        while (itr.hasNext()) {
            var i = itr.nextIndex();
            var column = itr.next();
            values[i] = column.typeHandler().getResult(rs, column.columnName());
        }

        @SuppressWarnings("unchecked")
        var ret = (T) tableInfo.newInstance(values);
        return ret;
    }

    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    private Class<T> getModelType() {
        return (Class<T>) TypeToken
                .of(this.getClass()).getSupertype(Repository.class)
                .resolveType(Repository.class.getTypeParameters()[0])
                .getRawType();
    }

    protected List<T> mapMany(@NotNull ResultSet rs) throws SQLException {
        var entities = new ArrayList<T>();
        T entity;
        while ((entity = mapOne(rs)) != null) {
            entities.add(entity);
        }
        return entities;
    }

}
