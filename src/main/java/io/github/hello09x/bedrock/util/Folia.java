package io.github.hello09x.bedrock.util;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

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

    public static @Nullable WrappedTask runTaskTimer(
            @NotNull Plugin plugin,
            @NotNull Entity entity,
            @NotNull Consumer<@NotNull WrappedTask> task,
            int delay,
            int period
    ) {
        if (isFolia()) {
            var t = entity.getScheduler().runAtFixedRate(plugin, x -> {
                task.accept(new WrappedScheduleTask(x));
            }, null, delay, period);
            if (t == null) {
                return null;
            }
            return new WrappedScheduleTask(t);
        } else {
            var t = new BukkitRunnable() {
                @Override
                public void run() {
                    task.accept(new WrappedBukkitTask(this));
                }
            };
            return new WrappedBukkitTask(t);
        }
    }


    public interface WrappedTask {
        boolean isCancelled();

        void cancel();
    }

    public static class WrappedScheduleTask implements WrappedTask {

        @NotNull
        private final ScheduledTask delegate;

        public WrappedScheduleTask(@NotNull ScheduledTask delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean isCancelled() {
            return delegate.isCancelled();
        }

        @Override
        public void cancel() {
            delegate.cancel();
        }
    }

    public static class WrappedBukkitTask implements WrappedTask {

        @NotNull
        private final BukkitRunnable delegate;

        public WrappedBukkitTask(@NotNull BukkitRunnable delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean isCancelled() {
            return delegate.isCancelled();
        }

        @Override
        public void cancel() {
            delegate.cancel();
        }
    }


}
