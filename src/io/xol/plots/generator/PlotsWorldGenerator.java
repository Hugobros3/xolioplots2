package io.xol.plots.generator;

import io.xol.chunkstories.api.Content.WorldGenerators.WorldGeneratorType;
import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.WorldGenerator;
import io.xol.chunkstories.api.world.chunk.Chunk;
import io.xol.chunkstories.world.chunk.CubicChunk;
import io.xol.chunkstories.world.region.RegionImplementation;

public class PlotsWorldGenerator extends WorldGenerator {

	public PlotsWorldGenerator(WorldGeneratorType type, World world) {
		super(type, world);
		
		//TODO: Make this work better
		//world.setDefaultSpawnLocation(new Location(world, 0, GROUND_HEIGHT + 1, 0));
	}

	private int GROUND_HEIGHT = 48;
	
	private int PLOT_SIZE = 64;
	private int PLOT_SIZE_INNER = 64 - 6;
	private int ROAD_WIDTH = 6;
	private int HALF_ROAD_WIDTH = ROAD_WIDTH / 2;
	private int HALF_ROAD_WIDTH2 = ROAD_WIDTH - HALF_ROAD_WIDTH;
	
	public TileType getTileType(int x, int z)
	{
		int modx = x % PLOT_SIZE;
		int modz = z % PLOT_SIZE;
		
		if(modx > HALF_ROAD_WIDTH && modx < PLOT_SIZE - HALF_ROAD_WIDTH)
		{
			//X is on plot
			if(modz > HALF_ROAD_WIDTH && modz < PLOT_SIZE - HALF_ROAD_WIDTH)
			{
				//Z is on plot
				return TileType.PLOT;
			}
		}
		else if(modx == HALF_ROAD_WIDTH || modx == PLOT_SIZE - HALF_ROAD_WIDTH)
		{
			if(modz >= HALF_ROAD_WIDTH && modz <= PLOT_SIZE - HALF_ROAD_WIDTH)
			{
				return TileType.PLOT_BORDER;
			}
			else
			{
			}
			/*
			//X is on border
			if(modz == HALF_ROAD_WIDTH || modz == PLOT_SIZE - HALF_ROAD_WIDTH)
			{
				//Z is on border
				return TileType.PLOT_BORDER;
			}*/
		}
		if(modz == HALF_ROAD_WIDTH || modz == PLOT_SIZE - HALF_ROAD_WIDTH)
		{
			if(modx >= HALF_ROAD_WIDTH && modx <= PLOT_SIZE - HALF_ROAD_WIDTH)
			{
				return TileType.PLOT_BORDER;
			}
			else
			{
				
			}
		}
		
		return TileType.ROAD;
	}
	
	public enum TileType {
		PLOT,
		PLOT_BORDER,
		ROAD;
	}
	
	@Override
	public Chunk generateChunk(RegionImplementation region, int cx, int cy, int cz) {
		
		CubicChunk c = new CubicChunk(region, cx, cy, cz);
		for (int x = 0; x < 32; x++)
			for (int z = 0; z < 32; z++)
			{
				for(int y = cy * 32; y < GROUND_HEIGHT; y++)
				{
					c.setVoxelDataWithoutUpdates(x, y % 32, z, 3);
				}
				
				if(GROUND_HEIGHT >= cy * 32  && cy * 32 + 32 > GROUND_HEIGHT)
				{
					TileType type = getTileType(cx * 32 + x, cz * 32 + z);
					int topTile = 0;
					switch(type)
					{
						case PLOT:
							topTile = 2;
							break;
						case PLOT_BORDER:
							topTile = 27;
							break;
						case ROAD:
							topTile = 29;
							break;
					}
					
					c.setVoxelDataWithoutUpdates(x, GROUND_HEIGHT % 32, z, topTile);
				}
			}
		return c;
	}

	@Override
	public int getTopDataAt(int x, int z) {
		TileType type = getTileType(x, z);
		int topTile = 0;
		switch(type)
		{
			case PLOT:
				topTile = 2;
				break;
			case PLOT_BORDER:
				topTile = 27;
				break;
			case ROAD:
				topTile = 29;
				break;
		}
		
		return topTile;
	}

	@Override
	public int getHeightAt(int x, int z) {
		
		return GROUND_HEIGHT;
	}

}
