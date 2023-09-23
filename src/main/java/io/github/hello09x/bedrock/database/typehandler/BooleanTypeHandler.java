package io.github.hello09x.bedrock.database.typehandler;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanTypeHandler implements TypeHandler<Boolean> {

    @Override
    public void setParameter(@NotNull PreparedStatement stm, int i, @NotNull Boolean value) throws SQLException {
        stm.setBoolean(i, value);
    }

    @Override
    public @Nullable Boolean getResult(@NotNull ResultSet rs, @NotNull String columnName) throws SQLException {
        return rs.getObject(columnName, Boolean.class);
    }
}
