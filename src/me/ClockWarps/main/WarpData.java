package me.ClockWarps.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class WarpData {
	ClockWarps plugin;

	ArrayList<Warp> publicWarps;
	HashMap<UUID, WarpsList> playerWarps;
	Warp spawnWarp;

	public FileConfiguration warpsFileConfig;
	public File warpsFile;

	public WarpData(ClockWarps plugin) {
		this.plugin = plugin;
		publicWarps = new ArrayList<Warp>();
		playerWarps = new HashMap<UUID, WarpsList>();
	}

	public void addToPlayersPrivateWarps(UUID playerID, Warp warp) {
		if (playerWarps.get(playerID) == null) {
			playerWarps.put(playerID, new WarpsList(plugin, playerID));
		}
		playerWarps.get(playerID).addToPrivateWarps(warp);
	}

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		warpsFile = new File(plugin.getDataFolder(), "warps.yml");

		if (!warpsFile.exists()) {
			try {
				warpsFile.createNewFile();
			} catch (IOException e) {
				plugin.getServer().getLogger().info(ChatColor.RED + "Could not create bankItemsData.yml file");
			}
		}

		warpsFileConfig = YamlConfiguration.loadConfiguration(warpsFile);
		loadWarpData();
	}

	private void loadWarpData() {
		// Public warps
		Set<String> warps;
		try {
			warps = warpsFileConfig.getConfigurationSection("Public-Warps").getKeys(false);
		} catch (NullPointerException e) {
			warps = null;
		}

		if (warps != null) {
			// For each public warp in warps.yml
			for (String warpName : warps) {
				plugin.consoleMessage(warpName + "\n");
				if (warpWithThisNameExists(warpName)) {
					plugin.consoleMessage("A global warp with the name \"" + warpName + "\" already exists. Ignoring");
					continue;
				}

				String path = "Public-Warps." + warpName;

				// Description
				String description = warpsFileConfig.getString(path + ".description");

				// Icon
				Material icon;
				try {
					icon = Material.valueOf(warpsFileConfig.getString(path + ".icon"));
				} catch (IllegalArgumentException e) {
					icon = plugin.defaultIcon;
				}

				// Color
				String colorString = warpsFileConfig.getString(path + ".color");
				ChatColor color;
				try {
					color = ChatColor.valueOf(colorString);
				} catch (Exception e) {
					color = plugin.defaultColor;
				}

				// Location
				String world = warpsFileConfig.getString(path + ".world");
				double x = warpsFileConfig.getDouble(path + ".x");
				double y = warpsFileConfig.getDouble(path + ".y");
				double z = warpsFileConfig.getDouble(path + ".z");
				float yaw = (float) warpsFileConfig.getDouble(path + ".yaw");
				float pitch = (float) warpsFileConfig.getDouble(path + ".pitch");

				// Make warp and set info
				Warp newWarp = new Warp(plugin, warpName, world, x, y, z, yaw, pitch);
				newWarp.setDescription(description);
				newWarp.setIcon(icon);
				newWarp.setColor(color);

				plugin.warpData.publicWarps.add(newWarp);
			}
		}

		// Private Warps
		Set<String> uuids;

		// ---Private warps
		try {
			uuids = warpsFileConfig.getConfigurationSection("Private-Warps").getKeys(false);
		} catch (NullPointerException e) {
			uuids = null;
		}

		if (uuids != null) { // No private warps
			for (String uuidString : uuids) { // For each players warps
				UUID playerID = UUID.fromString(uuidString);
				if (!playerWarps.containsKey(playerID)) {
					playerWarps.put(playerID, new WarpsList(plugin, playerID));
				}
				try {
					warps = warpsFileConfig.getConfigurationSection("Private-Warps." + uuidString).getKeys(false);
				} catch (NullPointerException e) {
					warps = null;
				}

				if (warps != null) {
					for (String warpName : warps) {
						String path = "Private-Warps." + uuidString + "." + warpName;

						// Description
						String description = warpsFileConfig.getString(path + ".description");

						// Icon
						Material icon;
						try {
							icon = Material.valueOf(warpsFileConfig.getString(path + ".icon"));
						} catch (IllegalArgumentException e) {
							icon = plugin.defaultIcon;
						}

						// Color
						String colorString = warpsFileConfig.getString(path + ".color");
						ChatColor color;
						try {
							color = ChatColor.valueOf(colorString);
						} catch (Exception e) {
							color = plugin.defaultColor;
						}

						// Location
						String world = warpsFileConfig.getString(path + ".world");
						double x = warpsFileConfig.getDouble(path + ".x");
						double y = warpsFileConfig.getDouble(path + ".y");
						double z = warpsFileConfig.getDouble(path + ".z");
						float yaw = (float) warpsFileConfig.getDouble(path + ".yaw");
						float pitch = (float) warpsFileConfig.getDouble(path + ".pitch");

						// Owner
						String ownerString = warpsFileConfig.getString(path + ".owner");
						UUID owner = UUID.fromString(ownerString);

						// Make warp and set info
						Warp newWarp = new Warp(plugin, warpName, world, x, y, z, yaw, pitch);
						newWarp.setDescription(description);
						newWarp.setIcon(icon);
						newWarp.setColor(color);
						newWarp.setOwner(owner);

						// Shared With --
						List<String> sharedWithIDS;
						try {
							sharedWithIDS = warpsFileConfig.getStringList(path + ".sharedwith");
						} catch (NullPointerException e) {
							sharedWithIDS = null;
						}

						if (sharedWithIDS != null) {
							for (String id : sharedWithIDS) {
								UUID sharedWithID = UUID.fromString(id);
								newWarp.addToSharedWith(sharedWithID);
								addToPlayersSharedWarps(sharedWithID, newWarp);
							}
						}

						playerWarps.get(playerID).addToPrivateWarps(newWarp);
					}
				}
			}
		}

		if (warpsFileConfig.getConfigurationSection("Spawn-Warp") != null) {
			String worldName = warpsFileConfig.getString("Spawn-Warp" + ".world");
			double x = warpsFileConfig.getDouble("Spawn-Warp" + ".x");
			double y = warpsFileConfig.getDouble("Spawn-Warp" + ".y");
			double z = warpsFileConfig.getDouble("Spawn-Warp" + ".z");
			float yaw = (float) warpsFileConfig.getDouble("Spawn-Warp" + ".yaw");
			float pitch = (float) warpsFileConfig.getDouble("Spawn-Warp" + ".pitch");

			spawnWarp = new Warp(plugin, "Spawn-Warp", worldName, x, y, z, yaw, pitch);
			spawnWarp.setIcon(plugin.spawnWarpIcon);
		}

		saveWarps();
	}

	public void addToPlayersSharedWarps(UUID playerID, Warp warp) {
		if (!playerWarps.containsKey(playerID)) {
			playerWarps.put(playerID, new WarpsList(plugin, playerID));
		}

		playerWarps.get(playerID).addToWarpsSharedWithPlayer(warp);
	}

	public boolean warpWithThisNameExists(String name) {
		for (Warp warp : publicWarps) {
			if (warp.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public void saveWarps() {
		// Public warps
		if (publicWarps.isEmpty()) {
			warpsFileConfig.set("Public-Warps", new ArrayList<String>());
		} else {
			String path;
			for (Warp warp : publicWarps) {
				path = "Public-Warps." + warp.getName();
				warpsFileConfig.set(path + ".description", warp.getDescription());
				warpsFileConfig.set(path + ".icon", warp.getIcon().toString());
				warpsFileConfig.set(path + ".color", warp.getColor().getName().toUpperCase());
				warpsFileConfig.set(path + ".x", warp.getX());
				warpsFileConfig.set(path + ".y", warp.getY());
				warpsFileConfig.set(path + ".z", warp.getZ());
				warpsFileConfig.set(path + ".yaw", warp.getYaw());
				warpsFileConfig.set(path + ".pitch", warp.getPitch());
			}
		}

		// Private Warps
		// For each player
		for (Map.Entry<UUID, WarpsList> entry : playerWarps.entrySet()) {
			WarpsList warpList = entry.getValue();
			UUID playerID = entry.getKey();
			String path = "Private-Warps." + warpList.getPlayerID();

			// If player has warps
			if (!warpList.getPrivateWarps().isEmpty()) {
				// For each of the players warps
				for (Warp warp : warpList.getPrivateWarps()) {
					path = "Private-Warps." + playerID + "." + warp.getName();
					warpsFileConfig.set(path + ".description", warp.getDescription());
					warpsFileConfig.set(path + ".icon", warp.getIcon().toString());
					warpsFileConfig.set(path + ".color", warp.getColor().getName().toUpperCase());
					warpsFileConfig.set(path + ".x", warp.getX());
					warpsFileConfig.set(path + ".y", warp.getY());
					warpsFileConfig.set(path + ".z", warp.getZ());
					warpsFileConfig.set(path + ".yaw", warp.getYaw());
					warpsFileConfig.set(path + ".pitch", warp.getPitch());
					warpsFileConfig.set(path + ".owner", warp.getOwner().toString());

					ArrayList<String> uuidStrings = new ArrayList<String>();
					if (!warp.getSharedWith().isEmpty()) {
						for (UUID id : warp.getSharedWith()) {
							uuidStrings.add(id.toString());
						}
					}
					warpsFileConfig.set(path + ".sharedwith", uuidStrings);
				}
			} else {

			}
		}

		// Spawn Warp
		if (spawnWarp != null) {
			String path = "Spawn-Warp";
			warpsFileConfig.set(path + ".description", spawnWarp.getDescription());
			warpsFileConfig.set(path + ".color", spawnWarp.getColor().getName().toUpperCase());
			warpsFileConfig.set(path + ".x", spawnWarp.getX());
			warpsFileConfig.set(path + ".y", spawnWarp.getY());
			warpsFileConfig.set(path + ".z", spawnWarp.getZ());
			warpsFileConfig.set(path + ".yaw", spawnWarp.getYaw());
			warpsFileConfig.set(path + ".pitch", spawnWarp.getPitch());
		} else {
			warpsFileConfig.set("Spawn-Warp", null);
		}

		saveWarpData();
	}

	public void saveWarpData() {
		try {
			warpsFileConfig.save(warpsFile);
		} catch (IOException e) {
			plugin.getServer().getLogger().info(ChatColor.RED + "Could not save warps.yml file");
		}
	}

}
