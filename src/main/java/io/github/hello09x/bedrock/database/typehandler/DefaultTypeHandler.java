package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultTypeHandler implements TypeHandler<Object> {

    private final TypeHandler<Object> delegate;

    public DefaultTypeHandler(TypeHandler<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull Object value) throws SQLException {
        this.delegate.setParameter(stm, i, value);
    }

    @Override
    public @Nullable Object getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return this.delegate.getResult(rs, columnName);
    }
}
