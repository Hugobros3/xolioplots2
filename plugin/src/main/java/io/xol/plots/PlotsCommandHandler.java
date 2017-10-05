package io.xol.plots;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.generator.WorldGenerator;
import io.xol.plots.PlotHelper.TileType;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class PlotsCommandHandler implements CommandHandler {

	PlotsPlugin plugin;
	
	public PlotsCommandHandler(PlotsPlugin corePlugin)
	{
		this.plugin = corePlugin;
	}
	
	@Override
	public boolean handleCommand(CommandEmitter emitter, Command command, String[] arguments) {
		
		if(!emitter.hasPermission("plots.use"))
		{
			emitter.sendMessage("You don't have the permission.");
			return true;
		}
		
		if(!(emitter instanceof Player))
		{
			emitter.sendMessage("You need to be a player to use this command.");
			return true;
		}
		
		Player player = (Player)emitter;
		
		World world = player.getWorld();
		WorldGenerator wg = world.getGenerator();
		
		if(wg.getClass().getSimpleName().equals("PlotsWorldGenerator")) {
			
			Location loc = player.getLocation();
			int x = (int)(double)loc.x();
			int z = (int)(double)loc.z();
			
			TileType type = PlotHelper.getTileType(x, z);
			
			Plot plot = null;
			int plotx = -1;
			int plotz = -1;
			
			if(type.equals(TileType.PLOT))
			{
				plotx = (x - PlotHelper.HALF_ROAD_WIDTH) / PlotHelper.PLOT_SIZE;
				plotz = (z - PlotHelper.HALF_ROAD_WIDTH) / PlotHelper.PLOT_SIZE;
				
				plot = plugin.plots.get(plotx + " " + plotz);
			}
		
				if(arguments.length == 0)
				{
					player.sendMessage("#FF8080XolioPlots#FF00002 #FFFFFFCreative building mode");
					player.sendMessage("#FFC0C0/plot claim #FFFF80Claims a plot for your usage");
					player.sendMessage("#FFC0C0/plot free #FFFF80Frees up a plot you no longer use");
					player.sendMessage("#FFC0C0/plot allow <name> #FFFF80Allows someone on this plot");
					player.sendMessage("#FFC0C0/plot disallow <name> #FFFF80Disallows someone from this plot");
					player.sendMessage("#FFC0C0/plot info [[x] [z]] #FFFF80Returns information on this plot");
					player.sendMessage("#FFC0C0/plot list #FFFF80Lists plots you own");
					player.sendMessage("#FFC0C0/plot tp <x> <z> #FFFF80Teleports you to a plot");
				}
				else
				{
					String subCommand = arguments[0];
					if(subCommand.equals("claim"))
					{
						if(type == PlotHelper.TileType.PLOT)
						{
							if(plot == null)
							{
								Plot plot2 = new Plot(plotx, plotz, player.getName());
								plugin.plots.put(plot2.getX() + " " +  plot2.getZ(), plot2);
								player.sendMessage("Successfully claimed plot #FFC0C0"+plotx+" "+plotz);
							}
							else
							{
								player.sendMessage("This plot is already claimed. Check out /plot info.");
							}
						}
						else
						{
							player.sendMessage("You aren't on a plot.");
						}
					}
					else if(subCommand.equals("free"))
					{
						if(type == PlotHelper.TileType.PLOT)
						{
							if(plot != null)
							{
								if(plot.getOwnerPlayer().equals(player.getName()))
								{
									plugin.plots.remove(plot.getX() +" "+  plot.getZ());
									player.sendMessage("Successfully freed plot #FFC0C0"+plotx+" "+plotz);
								}
								else
								{
									player.sendMessage("This plot isn't yours.");
								}
							}
							else
							{
								player.sendMessage("This plot isn't already claimed.");
							}
						}
						else
						{
							player.sendMessage("You aren't on a plot.");
						}
					}
					else if(subCommand.equals("info"))
					{
						if(type == PlotHelper.TileType.PLOT)
						{
							if(plot != null)
							{
								player.sendMessage("Plot #FFC0C0"+plotx+ " " + plotz + "#FFFFFF owned by #FFC0C0" + plot.getOwnerPlayer());
								String whitelist = "";
								for(String ap : plot.authorisedPlayers())
								{
									whitelist += ap + ", ";
								}
								player.sendMessage("Authorised players: "+whitelist);
							}
							else
							{
								player.sendMessage("This plot is free.");
							}
						}
						else
						{
							player.sendMessage("You aren't on a plot.");
						}
					}
					else if(subCommand.equals("allow"))
					{
						if(type == PlotHelper.TileType.PLOT)
						{
							if(plot != null)
							{
								if(plot.getOwnerPlayer().equals(player.getName()))
								{
									if(arguments.length == 2)
									{
										String otherPlayerName = arguments[1];
										plot.authorisePlayer(otherPlayerName);
										player.sendMessage("Added "+otherPlayerName + " to the list of authorised players.");
									}
									else
									{
										player.sendMessage("Usage: /plot allow <name>");
									}
								}
								else
								{
									player.sendMessage("This plot isn't yours.");
								}
							}
							else
							{
								player.sendMessage("This plot isn't already claimed.");
							}
						}
						else
						{
							player.sendMessage("You aren't on a plot.");
						}
					}
					else if(subCommand.equals("disallow"))
					{
						if(type == PlotHelper.TileType.PLOT)
						{
							if(plot != null)
							{
								if(plot.getOwnerPlayer().equals(player.getName()))
								{
									if(arguments.length == 2)
									{
										String otherPlayerName = arguments[1];
										plot.deauthorisePlayer(otherPlayerName);
										player.sendMessage("Removed "+otherPlayerName + " from the list of authorised players.");
									}
									else
									{
										player.sendMessage("Usage: /plot disallow <name>");
									}
								}
								else
								{
									player.sendMessage("This plot isn't yours.");
								}
							}
							else
							{
								player.sendMessage("This plot isn't already claimed.");
							}
						}
						else
						{
							player.sendMessage("You aren't on a plot.");
						}
					}
					else if(subCommand.equals("list"))
					{
						player.sendMessage("Listing plots you own.");
						for(Plot plot2 : plugin.plots.values())
						{
							if(plot2.getOwnerPlayer().equals(player.getName()))
							{
								player.sendMessage("You own a plot at " + plot2.getX() + " " + plot2.getZ() + ", with "+plot2.authorisedPlayers().length + " users.");
							}
						}
					}
					else if(subCommand.equals("tp"))
					{
						if(arguments.length == 3)
						{
							int xx = Integer.parseInt(arguments[1]);
							int zz = Integer.parseInt(arguments[2]);
							
							player.getControlledEntity().setLocation(new Location(world, xx * PlotHelper.PLOT_SIZE + PlotHelper.ROAD_WIDTH, PlotHelper.GROUND_HEIGHT + 1, zz * PlotHelper.PLOT_SIZE + PlotHelper.ROAD_WIDTH));
							player.sendMessage("Teleported you to plot "+xx + " " + zz);
						}
						else
						{
							player.sendMessage("Usage: /plot tp <x> <y>");
						}
					}
				}
			}
		
		return true;
	}
}
