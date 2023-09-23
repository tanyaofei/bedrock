package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BytesTypeHandler implements TypeHandler<byte[]> {

    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, byte @NotNull [] value) throws SQLException {
        stm.setBytes(i, value);
    }

    @Override
    public byte @Nullable [] getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return rs.getBytes(columnName);
    }
}
