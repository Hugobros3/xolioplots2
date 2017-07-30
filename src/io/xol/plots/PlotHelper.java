package io.xol.plots;

public class PlotHelper {
	static int GROUND_HEIGHT = 48;
	
	static int PLOT_SIZE = 64;
	static int PLOT_SIZE_INNER = 64 - 6;
	static int ROAD_WIDTH = 6;
	static int HALF_ROAD_WIDTH = ROAD_WIDTH / 2;
	static int HALF_ROAD_WIDTH2 = ROAD_WIDTH - HALF_ROAD_WIDTH;
	
	public static TileType getTileType(int x, int z)
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
		}
		if(modz == HALF_ROAD_WIDTH || modz == PLOT_SIZE - HALF_ROAD_WIDTH)
		{
			if(modx >= HALF_ROAD_WIDTH && modx <= PLOT_SIZE - HALF_ROAD_WIDTH)
			{
				return TileType.PLOT_BORDER;
			}
		}
		
		return TileType.ROAD;
	}
	
	public enum TileType {
		PLOT,
		PLOT_BORDER,
		ROAD;
	}
}
