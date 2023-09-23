package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleTypeHandler implements TypeHandler<Double> {
    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull Double value) throws SQLException {
        stm.setDouble(i, value);
    }

    @Override
    public @Nullable Double getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return rs.getObject(columnName, Double.class);
    }
}
