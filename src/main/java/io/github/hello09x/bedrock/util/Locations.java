package io.github.hello09x.bedrock.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Locations {

    /**
     * 转成区域 key
     *
     * @param pos    坐标
     * @param length 区域边长
     * @return 区域 key
     */
    public static long toRegionKey(@NotNull Location pos, int length) {
        return toRegionKey((long) pos.getBlockX(), (long) pos.getBlockZ(), length);
    }

    /**
     * 转成区域 key
     *
     * @param x      X 坐标
     * @param z      Z 坐标
     * @param length 边长
     * @return 区域 key
     */
    public static long toRegionKey(int x, int z, int length) {
        return toRegionKey((long) x, z, length);
    }

    /**
     * 转成区域 key
     *
     * @param x      X 坐标
     * @param z      Z 坐标
     * @param length 边长
     * @return 区域 key
     */
    public static long toRegionKey(long x, long z, int length) {
        return (x / length & 0xffffffffL) | (z / length & 0xffffffffL) << 32;
    }

    /**
     * 区域 key 转回区域左上角坐标
     *
     * @param world     世界
     * @param regionKey 区域 key
     * @param length    区域边长
     * @return 该区域的左上角坐标
     */
    public static @NotNull Location toLocation(@NotNull World world, long regionKey, int length) {
        var x = (int) (regionKey & 0xffffffffL) * length;
        var z = (int) (regionKey >> 32 & 0xffffffffL) * length;
        return new Location(world, x, 0, z);
    }

    public static @NotNull String toString(@NotNull Location pos) {
        var world = pos.getWorld();
        if (world != null) {
            return "([%s]%.1f, %.1f, %.1f)".formatted(world.getName(), pos.getX(), pos.getY(), pos.getZ());
        }
        return "(%.1f, %.1f, %.1f)".formatted(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * 以 {@code center} 为圆心, {@code r} 为半径, 将圆边拆成 {@code limits} 个点, 返回每个点的坐标
     *
     * @param center 圆心
     * @param r      半径
     * @param limits 点数
     * @return 每个点的坐标
     */
    public @NotNull
    static Location[] getCirclePoints(@NotNull Location center, double r, int limits) {
        var world = center.getWorld();
        var x = center.getX();
        var y = center.getY();
        var z = center.getZ();
        double m = (2 * Math.PI) / limits;

        var points = new Location[limits];
        for (int i = 0; i < limits; i++) {
            double x1 = x + r * Math.sin(m * i);
            double z1 = z + r * Math.cos(m * i);
            points[i] = new Location(world, x1, y, z1);
        }
        return points;
    }

}
