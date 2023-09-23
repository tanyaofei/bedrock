package io.github.hello09x.bedrock.i18n;

import org.bukkit.plugin.java.JavaPlugin;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

public interface I18nSupported {

    /**
     * 返回插件的 classLoader
     *
     * @return classLoader
     * @see JavaPlugin#getClassLoader()
     */
    @NotNull ClassLoader classLoader();

    /**
     * @return 插件标识
     */
    @NotNull
    @Pattern("([a-z0-9_\\-.]+:)?[a-z0-9_\\-./]+")
    String identifier();

}
