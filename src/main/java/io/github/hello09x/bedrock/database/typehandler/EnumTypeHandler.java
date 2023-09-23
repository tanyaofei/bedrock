package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EnumTypeHandler<E extends Enum<E>> implements TypeHandler<E> {

    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        this.type = type;
    }


    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull E value) throws SQLException {
        stm.setString(i, value.name());
    }

    @Nullable
    @Override
    public E getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return Optional.ofNullable(rs.getString(columnName)).map(name -> Enum.valueOf(type, name)).orElse(null);
    }
}
