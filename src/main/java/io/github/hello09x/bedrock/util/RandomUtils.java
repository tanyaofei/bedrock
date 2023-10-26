package io.github.hello09x.bedrock.util;

import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static <T> T choose(Collection<T> collection) {
        var index = ThreadLocalRandom.current().nextInt(collection.size());
        return Iterables.get(collection, index);
    }

    public static <T> T choose(T[] array) {
        var index = ThreadLocalRandom.current().nextInt(array.length);
        return array[index];
    }

    public static boolean lucky(double probability) {
        if (probability <= 0) {
            return false;
        }
        return ThreadLocalRandom.current().nextInt(100) < probability * 100;
    }

}
