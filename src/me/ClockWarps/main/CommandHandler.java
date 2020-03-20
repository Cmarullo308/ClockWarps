package me.ClockWarps.main;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandHandler {
	ClockWarps plugin;

	public CommandHandler(ClockWarps plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		switch (command.getLabel().toLowerCase()) {
		case "test":
			testCommand(sender, args);
			break;
		default:
			break;
		}

		return true;
	}

	private void testCommand(CommandSender sender, String[] args) {
		if (args[0].equalsIgnoreCase("1")) {

			Player player = (Player) sender;

			sender.sendMessage("ClockWarps");

			Warp warp = new Warp(plugin, args[1], player.getLocation());
			warp.setDescription("Test warp");
			warp.setColor(ChatColor.BLUE);
			warp.setOwner(player.getUniqueId());
//			warp.addToSharedWith(UUID.randomUUID());

			plugin.warpData.publicWarps.add(warp);
//			plugin.warpData.addToPlayersPrivateWarps(player.getUniqueId(), warp);

			plugin.warpData.saveWarps();
		} else if (args[0].equalsIgnoreCase("2")) {
			for (Warp warp : plugin.warpData.publicWarps) {
				plugin.consoleMessage(warp.getName());
			}
		} else if (args[0].equalsIgnoreCase("3")) {
			Player player = (Player) sender;

			sender.sendMessage("ClockWarps");

			Warp warp = new Warp(plugin, args[1], player.getLocation());
			warp.setDescription("Test warp");
			warp.setColor(ChatColor.BLUE);
			warp.setOwner(player.getUniqueId());
			warp.addToSharedWith(UUID.randomUUID());

//			plugin.warpData.publicWarps.add(warp);
			plugin.warpData.addToPlayersPrivateWarps(player.getUniqueId(), warp);

			plugin.warpData.saveWarps();
		} else if (args[0].equalsIgnoreCase("4")) {
			Player player = (Player) sender;
			for (Warp warp : plugin.warpData.playerWarps.get(player.getUniqueId()).getPrivateWarps()) {
				plugin.consoleMessage(warp.getName());
			}
		} else if (args[0].equalsIgnoreCase("5")) {
			Player player = (Player) sender;
			Warp newSpawnWarp = new Warp(plugin, "Spawn-Warp", player.getLocation());
			plugin.warpData.spawnWarp = newSpawnWarp;
			plugin.warpData.saveWarps();
		} else if (args[0].equalsIgnoreCase("6")) {
			Player player = (Player) sender;
			plugin.warpData.spawnWarp.sendPlayer(player);
		} else if (args[0].equalsIgnoreCase("7")) {
			Player player = (Player) sender;
			for(Warp warp : plugin.warpData.playerWarps.get(player.getUniqueId()).getWarpsSharedWithPlayer()) {
				plugin.consoleMessage(warp.getName());
			}
		}

	}

}
