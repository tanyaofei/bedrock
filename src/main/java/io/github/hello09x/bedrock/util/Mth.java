package io.github.hello09x.bedrock.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mth {

    public static double floor(double min, double max, double value) {
        return Math.min(max, Math.max(min, value));
    }

    public static double floor(int min, int max, int value) {
        return Math.min(max, Math.max(min, value));
    }

}
