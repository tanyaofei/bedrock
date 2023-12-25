package io.github.hello09x.bedrock.util;

import org.bukkit.Bukkit;

public abstract class Threads {

    public void ensurePrimaryThread() {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Must run in primary thread");
        }
    }


}
