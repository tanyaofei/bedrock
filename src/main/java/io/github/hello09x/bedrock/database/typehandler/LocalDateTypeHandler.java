package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class LocalDateTypeHandler implements TypeHandler<LocalDate>  {
    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull LocalDate value) throws SQLException {
        stm.setDate(i, Date.valueOf(value));
    }

    @Override
    public @Nullable LocalDate getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return Optional.ofNullable(rs.getDate(columnName)).map(Date::toLocalDate).orElse(null);
    }
}
