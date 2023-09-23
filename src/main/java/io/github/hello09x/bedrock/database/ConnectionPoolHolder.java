package io.github.hello09x.bedrock.database;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class ConnectionPoolHolder {

    private final ConcurrentMap<String, ConnectPool> pools = new ConcurrentHashMap<>();

    public @NotNull ConnectPool getOrCreate(
            @NotNull Plugin plugin,
            @NotNull Supplier<DatasourceConfig> config
    ) {
        return pools.computeIfAbsent(plugin.getName(), k -> {
            var c = config.get();
            return new ConnectPool(
                    c.getDriverClass(),
                    c.getUrl(),
                    c.getUsername(),
                    c.getPassword(),
                    c.getSize()
            );
        });
    }

}
