package me.ClockWarps.main;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Warp implements Comparable<Warp> {
	ClockWarps plugin;
	private String name;
	private String description;
	private Material icon;
	private ChatColor color;
	private String worldName;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private UUID owner;
	private ArrayList<UUID> sharedWith;

	public Warp(ClockWarps plugin, String name, Location location) {
		this.plugin = plugin;
		this.name = name;
		this.worldName = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
		this.icon = plugin.defaultIcon;
		this.color = plugin.defaultColor;
		this.description = "";
		this.sharedWith = new ArrayList<UUID>();
	}

	public Warp(ClockWarps plugin, String name, String world, double x, double y, double z, float yaw, float pitch) {
		this.plugin = plugin;
		this.name = name;
		this.worldName = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.icon = plugin.defaultIcon;
		this.color = plugin.defaultColor;
		this.description = "";
		this.sharedWith = new ArrayList<UUID>();
	}

	public Warp(ClockWarps plugin, UUID owner, String name, String world, double x, double y, double z, float yaw,
			float pitch) {
		this.plugin = plugin;
		this.name = name;
		this.worldName = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.icon = plugin.defaultIcon;
		this.color = plugin.defaultColor;
		this.description = "";
		this.sharedWith = new ArrayList<UUID>();
	}

	public Warp(ClockWarps plugin, UUID owner, String name, Location location) {
		this.plugin = plugin;
		this.name = name;
		this.worldName = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
		this.icon = plugin.defaultIcon;
		this.color = plugin.defaultColor;
		this.description = "";
		this.owner = owner;
		this.sharedWith = new ArrayList<UUID>();
	}

	public Location getLocation() {
		return new Location(Bukkit.getServer().getWorld(this.worldName), this.x, this.y, this.z, this.yaw, this.pitch);
	}
	
	public void sendPlayer(Player player) {
		player.teleport(getLocation());
	}

	public void addToSharedWith(UUID playerID) {
		sharedWith.add(playerID);
	}

	public void removeFromSharedwith(UUID playerID) {
		sharedWith.remove(playerID);
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Material getIcon() {
		return icon;
	}

	public void setIcon(Material icon) {
		this.icon = icon;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
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

	public ArrayList<UUID> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(ArrayList<UUID> sharedWith) {
		this.sharedWith = sharedWith;
	}

	public void setLocation(Location location) {
		this.worldName = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}

	@Override
	public int compareTo(Warp anotherWarp) {
		return this.name.compareTo(anotherWarp.name);
	}
}
