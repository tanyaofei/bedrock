package io.github.hello09x.bedrock.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mth {

        /**
     * 将 num 以 base 向下取整
     * <ul>
     *     <li>3.0, 0.5 -> 3.0</li>
     *     <li>3.1, 0.5 -> 3.0</li>
     *     <li>3.6, 0.5 -> 3.5</li>
     * </ul>
     *
     * @param num  数
     * @param base 基数
     * @return 取整后的数
     */
    public static double clamp(double num, double base) {
        if (num % base == 0) {
            return num;
        }
        return Math.floor(num / base) * base;
    }

    public static double clamp(double min, double max, double value) {
        return Math.min(max, Math.max(min, value));
    }

    public static int clamp(int min, int max, int value) {
        return Math.min(max, Math.max(min, value));
    }

    public static long clamp(long min, long max, long value) {
        return Math.min(max, Math.max(min, value));
    }

}
