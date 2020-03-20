package me.ClockWarps.main;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class ClockWarps extends JavaPlugin {

	CommandHandler commandHandler;
	WarpData warpData;

	Material defaultIcon = Material.FILLED_MAP;
	Material spawnWarpIcon = Material.ARMOR_STAND;
	ChatColor defaultColor = ChatColor.BLUE;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();

		warpData = new WarpData(this);
		warpData.setup();
		commandHandler = new CommandHandler(this);

//		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
		consoleMessage("ClockWarps loaded");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return commandHandler.onCommand(sender, command, label, args);
	}

	public void consoleMessage(String message) {
		getLogger().info(message);
	}
}
