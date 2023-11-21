package io.github.hello09x.bedrock.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class CompletableTask {

    private final static BukkitScheduler SCHEDULER = Bukkit.getScheduler();

    public static CompletableFuture<Void> join(@NotNull Plugin plugin, @NotNull NoResultExecution execution) {
        return join(plugin, () -> {
            execution.execute();
            return null;
        });
    }

    public static CompletableFuture<Void> joinAsync(@NotNull Plugin plugin, @NotNull NoResultExecution execution) {
        return joinAsync(plugin, () -> {
            execution.execute();
            return null;
        });
    }

    public static <T> CompletableFuture<T> joinAsync(@NotNull Plugin plugin, @NotNull Execution<T> execution) {
        return CompletableFuture.supplyAsync(() -> {
            var blocker = Thread.currentThread();
            var exception = new AtomicReference<Throwable>();
            var ret = new AtomicReference<T>();
            SCHEDULER.runTaskAsynchronously(plugin, () -> {
                try {
                    ret.set(execution.execute());
                } catch (Throwable e) {
                    exception.set(e);
                } finally {
                    LockSupport.unpark(blocker);
                }
            });
            LockSupport.park(blocker);
            var e = exception.get();
            if (e != null) {
                throw encodeException(e);
            }
            return ret.get();
        });
    }

    public static <T> CompletableFuture<T> join(@NotNull Plugin plugin, @NotNull Execution<T> execution) {
        return CompletableFuture.supplyAsync(() -> {
            var blocker = Thread.currentThread();
            var exception = new AtomicReference<Throwable>();
            var ret = new AtomicReference<T>();
            SCHEDULER.runTask(plugin, () -> {
                try {
                    ret.set(execution.execute());
                } catch (Throwable e) {
                    exception.set(e);
                } finally {
                    LockSupport.unpark(blocker);
                }
            });
            LockSupport.park(blocker);
            var e = exception.get();
            if (e != null) {
                throw encodeException(e);
            }
            return ret.get();
        });
    }

    private static @NotNull CompletionException encodeException(@NotNull Throwable e) {
        if (e instanceof CompletionException ce) {
            return ce;
        }
        return new CompletionException(e);
    }

}
