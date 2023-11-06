package io.github.hello09x.bedrock.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Blocks {

    public static @Nullable Block getNearbyBlock(@NotNull Location location, int radius, @NotNull Predicate<Block> predicate) {
        return getNearbyBlock(location, radius, radius, radius, predicate);
    }

    public static @Nullable Block getNearbyBlock(@NotNull Location location, int x, int y, int z, @NotNull Predicate<Block> predicate) {
        var blocks = getNearbyBlocks(location, x, y, z, predicate, true);
        if (blocks.isEmpty()) {
            return null;
        }
        return blocks.get(0);
    }

    public static @NotNull List<Block> getNearbyBlocks(
            @NotNull Location location,
            int x,
            int y,
            int z,
            @Nullable Predicate<Block> predicate
    ) {
        return getNearbyBlocks(location, x, y, z, predicate, false);
    }

    private static @NotNull List<Block> getNearbyBlocks(
            @NotNull Location location,
            int x,
            int y,
            int z,
            @Nullable Predicate<Block> predicate,
            boolean onlyOne
    ) {
        var world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Missing world in location argument");
        }

        var blocks = new LinkedList<Block>();
        for (int x0 = location.getBlockX() - x; x0 <= location.getBlockX() + x; x0++) {
            for (int y0 = location.getBlockY() - y; y0 <= location.getBlockY() + y; y0++) {
                for (int z0 = location.getBlockZ() - z; z0 <= location.getBlockZ() + z; z0++) {
                    var block = world.getBlockAt(x0, y0, z0);
                    if (predicate == null || predicate.test(block)) {
                        blocks.add(block);
                        if (onlyOne) {
                            break;
                        }
                    }
                }
            }
        }

        return blocks;
    }

    public static @NotNull List<Block> getNearbyBlocks(@NotNull Location location, int x, int y, int z) {
        return getNearbyBlocks(location, x, y, z, null);
    }

    public static @NotNull List<Block> getNearbyBlocks(@NotNull Location location, int radius, @Nullable Predicate<Block> predicate) {
        return getNearbyBlocks(location, radius, radius, radius, predicate);
    }

    public static @NotNull List<Block> getNearbyBlocks(@NotNull Location location, int radius, @NotNull Material type) {
        return getNearbyBlocks(location, radius, b -> b.getType() == type);
    }

    public static @NotNull List<Block> getNearbyBlocks(@NotNull Location location, int radius) {
        return getNearbyBlocks(location, radius, (Predicate<Block>) null);
    }


}
