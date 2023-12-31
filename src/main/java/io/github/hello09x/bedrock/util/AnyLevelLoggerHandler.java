package io.github.hello09x.bedrock.util;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class AnyLevelLoggerHandler extends Handler {

    @NotNull
    private final Plugin plugin;

    public AnyLevelLoggerHandler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void publish(LogRecord record) {
        var level = record.getLevel();
        if (level.intValue() < Level.INFO.intValue() && level.intValue() >= plugin.getLogger().getLevel().intValue()) {
            record.setLevel(Level.INFO);
            record.setMessage("[" + LEVEL_MAPPING.getOrDefault(level.getName(), level.getName()) + "] " + record.getMessage());
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }


    private final static Map<String, String> LEVEL_MAPPING = Map.of(
            "SEVERE", "ERROR",
            "WARNING", "WARN",
            "INFO", "INFO",
            "CONFIG", "DEBUG",
            "FINE", "TRACE",
            "FINER", "TRACE",
            "FINEST", "TRACE"
    );

}
