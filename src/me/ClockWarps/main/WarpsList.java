package me.ClockWarps.main;

import java.util.ArrayList;
import java.util.UUID;

public class WarpsList {
	ClockWarps plugin;
	private UUID playerID;
	private ArrayList<Warp> privateWarps;
	private ArrayList<Warp> warpsSharedWithPlayer;

	public WarpsList(ClockWarps plugin, UUID playerID) {
		this.plugin = plugin;
		this.playerID = playerID;
		privateWarps = new ArrayList<Warp>();
		warpsSharedWithPlayer = new ArrayList<Warp>();
	}

	public ArrayList<Warp> getPrivateWarps() {
		return privateWarps;
	}

	public void addToPrivateWarps(Warp warp) {
		privateWarps.add(warp);
	}

	public ArrayList<Warp> getWarpsSharedWithPlayer() {
		return warpsSharedWithPlayer;
	}

	public void setWarpsSharedWithPlayer(ArrayList<Warp> warpsSharedWithPlayer) {
		this.warpsSharedWithPlayer = warpsSharedWithPlayer;
	}

	public void removeFromPlayerWarps(String nameOfWarp) {
		for (Warp warp : privateWarps) {
			if (warp.getName().equals(nameOfWarp)) {
				privateWarps.remove(privateWarps.indexOf(warp));
				return;
			}
		}
	}

	public void addToWarpsSharedWithPlayer(Warp warp) {
		warpsSharedWithPlayer.add(warp);
	}

	public void removeFromWarpsSharedWithPlayer(String nameOfWarp, UUID owner) {
		for (Warp warp : warpsSharedWithPlayer) {
			if (warp.getName().equals(nameOfWarp) && warp.getOwner().equals(owner)) {
				warpsSharedWithPlayer.remove(warpsSharedWithPlayer.indexOf(warp));
				return;
			}
		}
	}

	public UUID getPlayerID() {
		return playerID;
	}

	public void setPlayerID(UUID playerID) {
		this.playerID = playerID;
	}
}
