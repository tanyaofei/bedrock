package io.github.hello09x.bedrock.util;

import org.bukkit.Bukkit;

public abstract class MCUtils {


    public static void ensureMainThread() {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Must run in main thread");
        }
    }


}
