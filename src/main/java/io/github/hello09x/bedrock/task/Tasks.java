package io.github.hello09x.bedrock.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class Tasks {

    public static void join(@NotNull NoReturnValueExecution execution, @NotNull Plugin plugin) throws Throwable {
        join(() -> {
            execution.execute();
            return null;
        }, plugin);
    }

    public static <T> T join(@NotNull Execution<T> execution, @NotNull Plugin plugin) throws Throwable {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Never call this method in bukkit primary thread");
        }

        var blocker = Thread.currentThread();
        var exception = new AtomicReference<Throwable>();
        var ret = new AtomicReference<T>();
        run(() -> {
            try {
                ret.set(execution.execute());
            } catch (Throwable e) {
                exception.set(e);
            } finally {
                LockSupport.unpark(blocker);
            }
        }, plugin);
        LockSupport.park(blocker);
        var e = exception.get();
        if (e != null) {
            throw e;
        }
        return ret.get();
    }


    public static void runAsync(@NotNull Runnable runnable, @NotNull Plugin plugin, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLaterAsynchronously(plugin, delay);
    }

    public static void runAsync(@NotNull Runnable runnable, @NotNull Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(plugin);
    }

    public static void run(@NotNull Runnable runnable, @NotNull Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(plugin);
    }

    public static void run(@NotNull Runnable runnable, @NotNull Plugin plugin, long delay, long period) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskTimer(plugin, delay, period);
    }

    public static void run(@NotNull Runnable runnable, @NotNull Plugin plugin, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(plugin, delay);
    }

}
