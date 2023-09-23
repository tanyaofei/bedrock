package io.github.hello09x.bedrock.database.typehandler;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JsonTypeHandler implements TypeHandler<Object> {

    public final static Gson gson = new Gson();

    private final Class<Object> type;

    public JsonTypeHandler(Class<Object> type) {
        this.type = type;
    }

    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull Object value) throws SQLException {
        stm.setString(i, gson.toJson(value));
    }

    @Override
    public @Nullable Object getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return Optional.ofNullable(rs.getString(columnName)).map(json -> gson.fromJson(json, type)).orElse(null);
    }
}
