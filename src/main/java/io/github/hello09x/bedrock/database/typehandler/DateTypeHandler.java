package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

public class DateTypeHandler implements TypeHandler<Date> {

    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull Date value) throws SQLException {
        stm.setTimestamp(i, Timestamp.from(value.toInstant()));
    }

    @Override
    public @Nullable Date getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return Optional.ofNullable(rs.getTimestamp(columnName)).map(Timestamp::toInstant).map(Date::from).orElse(null);
    }

}
