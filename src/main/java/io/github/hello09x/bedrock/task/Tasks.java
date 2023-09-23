package io.github.hello09x.bedrock.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class Tasks {

    public static void join(@NotNull Plugin plugin, @NotNull VoidExecution execution) throws Throwable {
        join(plugin, () -> {
            execution.execute();
            return null;
        });
    }

    public static <T> T join(
            @NotNull Plugin plugin,
            @NotNull Execution<T> execution
    ) throws Throwable {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Never call this method in bukkit primary thread");
        }

        var blocker = Thread.currentThread();
        var exception = new AtomicReference<Throwable>();
        var ret = new AtomicReference<T>();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    ret.set(execution.execute());
                } catch (Throwable e) {
                    exception.set(e);
                } finally {
                    LockSupport.unpark(blocker);
                }
            }
        }.runTaskLater(plugin, 0L);
        LockSupport.park(blocker);
        var e = exception.get();
        if (e != null) {
            throw e;
        }
        return ret.get();
    }

    public static void runNextTick(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(plugin);
    }

    public static void runNextTickAsync(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(plugin);
    }

    public static void runLaterAsync(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLaterAsynchronously(plugin, delay);
    }

    public static void runAsync(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLaterAsynchronously(plugin, 0L);
    }

    public static void run(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(plugin, 0L);
    }

    public static void runLater(@NotNull Plugin plugin, long delay, @NotNull Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(plugin, delay);
    }

}
