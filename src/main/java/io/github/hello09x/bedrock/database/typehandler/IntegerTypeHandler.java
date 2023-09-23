package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerTypeHandler implements TypeHandler<Integer> {
    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull Integer value) throws SQLException {
        stm.setInt(i, value);
    }

    @Override
    public @Nullable Integer getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return rs.getObject(columnName, Integer.class);
    }
}
