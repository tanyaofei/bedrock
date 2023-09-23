package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UUIDTypeHandler implements TypeHandler<UUID> {

    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull UUID value) throws SQLException {
        stm.setString(i, value.toString());
    }

    @Override
    public @Nullable UUID getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return Optional.ofNullable(rs.getString(columnName)).map(UUID::fromString).orElse(null);
    }
}
