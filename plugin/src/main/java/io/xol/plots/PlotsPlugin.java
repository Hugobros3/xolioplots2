package io.xol.plots;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import io.xol.chunkstories.api.plugin.PluginInformation;
import io.xol.chunkstories.api.plugin.ServerPlugin;
import io.xol.chunkstories.api.server.ServerInterface;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class PlotsPlugin extends ServerPlugin {

	Map<String, Plot> plots = new HashMap<String, Plot>();
	
	public PlotsPlugin(PluginInformation pluginInformation, ServerInterface clientInterface) {
		super(pluginInformation, clientInterface);
	}

	@Override
	public void onEnable() {
		System.out.println("ENABLE PlotsPlugin");
		this.getPluginManager().registerEventListener(new PlotsEventListener(this), this);
		this.getPluginManager().registerCommandHandler("plot", new PlotsCommandHandler(this));
		
		loadPlots();
	}

	void loadPlots() {
		File file = new File("plugins/XolioPlots2/plots");
		file.getParentFile().mkdirs();
		
		if(file.exists())
		{
			try 
			{
				FileInputStream fis = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
				String line;
				
				while((line = reader.readLine()) != null)
				{
					if(line.startsWith("plot"))
					{
						String[] s = line.split(" ");
						if(s.length >= 4)
						{
							int x = Integer.parseInt(s[1]);
							int z = Integer.parseInt(s[2]);
							String owner = s[3];
						
							Plot plot = new Plot(x, z, owner);
							
							//Loop for members
							while(true)
							{
								line = reader.readLine();
								if(line == null || line.startsWith("end"))
									break;
								
								plot.authorisePlayer(line);
							}
							
							plots.put(plot.getX()+" "+plot.getZ(), plot);
						}
					}
				}
				
				reader.close();
			}
			catch(IOException e)
			{
				
			}
		}
	}

	@Override
	public void onDisable() {
		savePlots();
	}

	void savePlots() {

		File file = new File("plugins/XolioPlots2/plots");
		file.getParentFile().mkdirs();
		
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			out.write("# File generated on " + new Time(System.currentTimeMillis()).toGMTString() + "\n");
			
			for(Plot plot : plots.values())
			{
				out.write("plot "+plot.getX()+" "+plot.getZ()+" "+plot.getOwnerPlayer()+"\n");
				
				for(String authorisedPlayer : plot.authorisedPlayers())
				{
					out.write(authorisedPlayer+"\n");
				}
				
				out.write("end"+"\n");
			}
			
			out.close();
			
		}
		catch(IOException e)
		{
			
		}
	}

}
