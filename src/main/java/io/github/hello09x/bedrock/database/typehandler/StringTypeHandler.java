package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHandler implements TypeHandler<String> {
    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull String value) throws SQLException {
        stm.setString(i, value);
    }

    @Override
    public @Nullable String getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return rs.getString(columnName);
    }
}
