package io.github.hello09x.bedrock.util;

import com.google.common.base.Throwables;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class RegistrablePlugin extends JavaPlugin {

    protected List<Runnable> onDisable = new ArrayList<>();

    @Override
    public void onDisable() {
        for (var runnable : onDisable) {
            try {
                runnable.run();
            } catch (Throwable e) {
                getLogger().severe(Throwables.getStackTraceAsString(e));
            }
        }
    }

    public void registerOnDisable(@NotNull Runnable runnable) {
        this.onDisable.add(runnable);
    }

}
