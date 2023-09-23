package io.github.hello09x.bedrock.progress;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class TimeProgress {

    private final static Timer timer = new Timer();

    private final Plugin plugin;

    private final LocalDateTime startedAt;

    private final LocalDateTime finishedAt;

    @Getter
    private final BossBar bossBar;

    private final Runnable onFinish;
    private final Consumer<Double> onProgress;

    private final ProgressType progressType;
    private final double total;
    private final long period;
    private TimerTask task;

    private volatile boolean started = false;

    public TimeProgress(
            @NotNull Plugin plugin,
            @NotNull LocalDateTime startedAt,
            @NotNull LocalDateTime finishedAt,
            @NotNull BossBar bossBar,
            long period,
            @NotNull ProgressType progressType,
            @Nullable Consumer<Double> onProgress,
            @Nullable Runnable onFinish
    ) {
        if (period < 1) {
            throw new IllegalArgumentException("Invalid period: " + period);
        }
        if (startedAt.isAfter(finishedAt)) {
            throw new IllegalArgumentException("startAt must be before finishedAt");
        }

        this.plugin = plugin;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.bossBar = bossBar;
        this.total = (double) Duration.between(startedAt, finishedAt).toSeconds();
        this.onFinish = onFinish;
        this.onProgress = onProgress;
        this.period = period;
        this.progressType = progressType;
    }

    public synchronized void start() {
        if (started) {
            throw new IllegalStateException("Started");
        }

        started = true;
        var task = new TimerTask() {
            @Override
            public void run() {
                var now = LocalDateTime.now();
                var last = (double) Duration.between(startedAt, now).toSeconds();
                var remains = total - last;
                if (remains < 0) {
                    try {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                bossBar.removeAll();
                            }
                        }.runTaskLater(plugin, 0L);
                        if (onFinish != null) {
                            onFinish.run();
                        }
                    } finally {
                        cancel();
                    }
                } else {
                    var progress = switch (progressType) {
                        case FORWARD -> last / total;
                        case REWARD -> remains / total;
                    };
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            bossBar.setProgress(progress);
                        }
                    }.runTaskLater(plugin, 1L);
                    if (onProgress != null) {
                        onProgress.accept(progress);
                    }
                }
            }
        };

        timer.schedule(task, 0, period);
        this.task = task;
    }

    public void cancel() {
        this.task.cancel();
        this.bossBar.removeAll();
    }

}
