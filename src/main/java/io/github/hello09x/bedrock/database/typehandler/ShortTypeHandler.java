package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortTypeHandler implements TypeHandler<Short> {
    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull Short value) throws SQLException {
        stm.setShort(1, value);
    }

    @Override
    public @Nullable Short getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return rs.getObject(columnName, Short.class);
    }
}
