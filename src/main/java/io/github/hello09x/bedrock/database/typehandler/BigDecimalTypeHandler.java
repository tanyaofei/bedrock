package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalTypeHandler implements TypeHandler<BigDecimal>  {
    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull BigDecimal value) throws SQLException {
        stm.setBigDecimal(i, value);
    }

    @Override
    public @Nullable BigDecimal getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return rs.getBigDecimal(columnName);
    }
}
