package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatTypeHandler implements TypeHandler<Float> {
    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull Float value) throws SQLException {
        stm.setFloat(i, value);
    }

    @Override
    public @Nullable Float getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return rs.getObject(columnName, Float.class);
    }
}
