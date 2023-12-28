package gg.jps.jpstosogame.data;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class LocationData {

    private double x;
    private double y;
    private double z;
    private String world;
    private float yaw;
    private float pitch;

    protected LocationData(double x, double y, double z, @NonNull World world, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world.getName();
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static @NotNull LocationData at(@NotNull Location location) {
        return new LocationData(location.getX(), location.getY(), location.getZ(),
                location.getWorld(), location.getYaw(), location.getPitch());
    }

    public static @NotNull LocationData at(double x, double y, double z, @NotNull World world, float yaw, float pitch) {
        return new LocationData(x, y, z, world, yaw, pitch);
    }

    public static @NotNull LocationData at(double x, double y, double z, @NotNull World world) {
        return new LocationData(x, y, z, world, 0, 0);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public World getWorld() {
        return Bukkit.getWorlds().stream().filter(world -> this.world.equalsIgnoreCase(world.getName())).findFirst().orElse(Bukkit.getWorlds().get(0));
    }

    public void setWorld(@NotNull World world) {
        this.world = world.getName();
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public @NotNull Location getLocation() {
        final World world = getWorld();
        if (world != null) return new Location(world, x, y, z, yaw, pitch);
        return Bukkit.getWorlds().stream().findAny().orElse(Bukkit.getWorlds().get(0)).getSpawnLocation();
    }

    public double distanceBetween(@NotNull LocationData other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2));
    }

    public @NotNull LocationData interpolate(@NotNull LocationData next, double scalar) {
        return LocationData.at(
                x + (next.x - x) * scalar,
                y + (next.y - y) * scalar,
                z + (next.z - z) * scalar,
                getWorld(),
                (float) (yaw + (next.yaw - yaw) * scalar),
                (float) (pitch + (next.pitch - pitch) * scalar)
        );
    }

    public @NotNull String toString() {
        return "(x: " + x + ", y: " + y + ", z: " + z + ", world: " + world +
                ", yaw: " + yaw + ", pitch: " + pitch + ")";
    }
}