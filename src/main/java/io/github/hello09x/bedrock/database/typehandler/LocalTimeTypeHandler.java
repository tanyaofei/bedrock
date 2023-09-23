package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;

public class LocalTimeTypeHandler implements TypeHandler<LocalTime> {

    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull LocalTime value) throws SQLException {
        stm.setTime(i, Time.valueOf(value));
    }

    @Override
    public @Nullable LocalTime getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return Optional.ofNullable(rs.getTime(columnName)).map(Time::toLocalTime).orElse(null);
    }
}
