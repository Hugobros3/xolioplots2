package io.xol.plots;

import java.util.HashSet;
import java.util.Set;

import io.xol.chunkstories.api.player.Player;

public class Plot {
	
	private final int x, z;
	private String ownerPlayer;
	
	private Set<String> authorisedPlayers = new HashSet<String>();
	
	public Plot(int x, int z, String playerName) {
		super();
		this.x = x;
		this.z = z;
		this.ownerPlayer = playerName;
	}

	public String getOwnerPlayer() {
		return ownerPlayer;
	}

	public void setOwnerPlayer(String playerName) {
		this.ownerPlayer = playerName;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}
	
	public void authorisePlayer(String playerName)
	{
		authorisedPlayers.add(playerName);
	}
	
	public boolean deauthorisePlayer(String playerName)
	{
		return authorisedPlayers.remove(playerName);
	}
	
	public boolean canBuild(Player player)
	{
		String playerName = player.getName();
		if(ownerPlayer.equals(playerName))
			return true;
		
		if(authorisedPlayers.contains(playerName))
			return true;
		
		return false;
	}

	public String[] authorisedPlayers() {
		
		String[] a = new String[authorisedPlayers.size()];
		int i = 0;
		for(String s : authorisedPlayers)
		{
			if(i >= a.length)
				break;
			
			a[i] = s;
			i++;
		}
		
		return a;
	}
}
