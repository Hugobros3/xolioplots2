package io.xol.plots;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.interfaces.EntityCreative;
import io.xol.chunkstories.api.events.EventHandler;
import io.xol.chunkstories.api.events.Listener;
import io.xol.chunkstories.api.events.player.PlayerSpawnEvent;
import io.xol.chunkstories.api.events.player.voxel.PlayerVoxelModificationEvent;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.generator.WorldGenerator;
import io.xol.plots.PlotHelper.TileType;

public class PlotsEventListener implements Listener {

	PlotsPlugin plugin;
	
	public PlotsEventListener(PlotsPlugin corePlugin)
	{
		this.plugin = corePlugin;
	}
	
	@EventHandler (priority = EventHandler.EventPriority.HIGHEST, listenToChildEvents = EventHandler.ListenToChildEvents.RECURSIVE)
	public void onPlayerVoxelModification(PlayerVoxelModificationEvent event) {
		Player player = event.getPlayer();
		
		//player.sendMessage("#FF5060PlayerVoxelModificationEvent " + event.getVoxel().getX() + " " + event.getVoxel().getY() + " " + event.getVoxel().getZ());
		//player.sendMessage(event.getModificationCause().getName() + " " + event.getModification());
		
		
		World world = player.getWorld();
		WorldGenerator wg = world.getGenerator();
		
		if(wg.getClass().getSimpleName().equals("PlotsWorldGenerator")) {
			
			int x = event.getVoxel().getX();
			int z = event.getVoxel().getZ();
			
			TileType type = PlotHelper.getTileType(x, z);
			
			if(type.equals(TileType.PLOT))
			{
				int plotx = (x - PlotHelper.HALF_ROAD_WIDTH) / PlotHelper.PLOT_SIZE;
				int plotz = (z - PlotHelper.HALF_ROAD_WIDTH) / PlotHelper.PLOT_SIZE;
				
				//player.sendMessage("CurrentPlot: "+plotx+": "+plotz);
				
				Plot plot = plugin.plots.get(plotx +" "+plotz);
				if(plot == null)
				{
					player.sendMessage("This plot isn't claimed. You can claim it using /plot claim");
					event.setCancelled(true);
				}
				else
				{
					if(!plot.canBuild(player))
					{
						player.sendMessage("This plot is already claimed by #FF8080"+plot.getOwnerPlayer()+", maybe ask him for building permission ?");
						event.setCancelled(true);
					}
				}
			}
			else
			{
				player.sendMessage("You need to be on a plot to build.");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerSpawn(PlayerSpawnEvent event) {
		Entity e = event.getEntity();
		if(e instanceof EntityCreative) {
			((EntityCreative) e).setCreativeMode(true);
			event.getPlayer().sendMessage("You spawned, set your mode to CREATIVE !"); 
		}
	}
}
