package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime>  {
    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull LocalDateTime value) throws SQLException {
        stm.setTimestamp(i, Timestamp.valueOf(value));
    }

    @Override
    public @Nullable LocalDateTime getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return Optional.ofNullable(rs.getTimestamp(columnName)).map(Timestamp::toLocalDateTime).orElse(null);
    }
}
