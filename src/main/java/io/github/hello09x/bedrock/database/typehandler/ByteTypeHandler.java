package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteTypeHandler implements TypeHandler<Byte> {
    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull Byte value) throws SQLException {
        stm.setByte(i, value);
    }

    @Override
    public @Nullable Byte getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return rs.getObject(columnName, Byte.class);
    }
}
