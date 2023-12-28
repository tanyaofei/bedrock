package io.github.hello09x.bedrock.util;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class Folia {
    private final static boolean IS_FOLIA = Bukkit.getServer().getName().equals("Folia");

    public static boolean isFolia() {
        return IS_FOLIA;
    }

    public static void runTask(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable runnable) {
        if (isFolia()) {
            entity.getScheduler().run(plugin, x -> runnable.run(), null);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runTaskLater(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable runnable, int delay) {
        if (isFolia()) {
            entity.getScheduler().runDelayed(plugin, x -> runnable.run(), null, delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        }
    }

    public static void runTaskTimer(@NotNull Plugin plugin, @NotNull Runnable runnable, int delay, int period) {
        if (isFolia()) {
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, x -> runnable.run(), delay, period);
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
        }
    }

}
