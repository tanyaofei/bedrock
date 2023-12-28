package io.github.hello09x.bedrock.util;

import org.jetbrains.annotations.NotNull;

public class Exceptions {

    @SuppressWarnings("unchecked")
    public static <T extends RuntimeException> T sneaky(@NotNull Throwable e) {
        return (T) e;
    }

}
