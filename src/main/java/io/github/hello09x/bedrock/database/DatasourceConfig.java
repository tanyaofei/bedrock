package io.github.hello09x.bedrock.database;

import io.github.hello09x.bedrock.config.Config;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@Getter
@ToString
public class DatasourceConfig extends Config<DatasourceConfig> {

    private String driverClass;

    private String url;

    private String username;

    private String password;

    private int size;

    @Getter(AccessLevel.NONE)
    private boolean loaded;

    public DatasourceConfig(@NotNull Plugin plugin, @Nullable String version) {
        super(plugin, version);
        reload(false);
    }

    @Override
    protected void reload(@NotNull FileConfiguration file) {
        if (this.loaded) {
            return;
        }
        this.url = this.getURL(file);
        this.driverClass = file.getString("datasource.driverClass", "org.sqlite.JDBC");
        this.username = file.getString("datasource.username");
        this.password = file.getString("datasource.password");
        this.size = file.getInt("datasource.size", 1);
        this.loaded = true;
    }

    private @NotNull String getURL(@NotNull FileConfiguration file) {
        var url = file.getString("datasource.url");
        if (url == null) {
            url = "jdbc:sqlite:" + new File(super.plugin.getDataFolder(), "data.db");
        }
        return url;
    }


}
