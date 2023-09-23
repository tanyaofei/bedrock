package io.github.hello09x.bedrock.database.typehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {


    void setParameter(@NotNull PreparedStatement stm, int i, @NotNull T value) throws SQLException;

    @Nullable T getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException;

}
