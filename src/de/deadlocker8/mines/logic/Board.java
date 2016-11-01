package de.deadlocker8.mines.logic;

import java.util.Random;

public class Board
{
	private int width;
	private int height;
	private int numberOfBombs;
	private Tile[][] tiles;
	
	public Board(int width, int height, int numberOfBombs)
	{		
		this.width = width;
		this.height = height;
		this.numberOfBombs = numberOfBombs;		
		init();
	}		
	
	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	
	public int getNumberOfBombs()
	{
		return numberOfBombs;
	}

	public Tile[][] getTiles()
	{
		return tiles;
	}
	
	private void init()
	{
		tiles = new Tile[height][width];		
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				tiles[y][x] = new Tile(0);			
			}	
		}		
		
		placeBombs();
		placeNumbers();
	}
	
	private void placeBombs()
	{
		Random r = new Random();		
		int bombCounter = 0;
		
		if(numberOfBombs > height * width)
		{
			numberOfBombs = height * width;
		}
		
		while(bombCounter < numberOfBombs)
		{			
			int randomX = r.nextInt(width);
			int randomY = r.nextInt(height);
			
			if(!tiles[randomY][randomX].isBomb())
			{
				tiles[randomY][randomX].setBomb();
				bombCounter++;
			}
		}	
	}
	
	private void placeNumbers()
	{
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(tiles[y][x].isBomb())
				{
					increaseNumbers(x, y);
				}
			}	
		}	
	}
	
	private void increaseNumbers(int x, int y)
	{		
		/*
		 * x-1, y-1		y-1		x+1, y-1 	
		 * x-1			B  		x+1
		 * x-1, y+1		y+1  	x+1, y+1
		 * 
		 * B = bomb
		 * m = minus 1
		 * p = plus 1
		 */
		int mx = x-1;
		int px = x+1;
		int my = y-1;
		int py = y+1;
		
		//top left corner
		if(mx > -1 && my > -1)
		{
			if(!tiles[my][mx].isBomb())
			{
				tiles[my][mx].increase();
			}
		}
		
		//top
		if(my > -1)
		{
			if(!tiles[my][x].isBomb())
			{
				tiles[my][x].increase();
			}
		}
		
		//top right corner
		if(px < width && my > -1)
		{
			if(!tiles[my][px].isBomb())
			{		
				tiles[my][px].increase();
			}
		}
		
		//left
		if(mx > -1)
		{
			if(!tiles[y][mx].isBomb())
			{
				tiles[y][mx].increase();
			}
		}
		
		//right
		if(px < width)
		{
			if(!tiles[y][px].isBomb())
			{
				tiles[y][px].increase();
			}
		}
		
		//bottom left corner
		if(mx > -1 && py < height)
		{
			if(!tiles[py][mx].isBomb())
			{
				tiles[py][mx].increase();
			}
		}
		
		//bottom
		if(py < height)
		{
			if(!tiles[py][x].isBomb())
			{
				tiles[py][x].increase();
			}
		}
		
		//bottom right corner
		if(px < width && py < height)
		{
			if(!tiles[py][px].isBomb())
			{
				tiles[py][px].increase();
			}
		}
	}
	
	public int getNumberofUnrevealedTiles()
	{
		int counter = 0;
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(!tiles[y][x].isRevealed())
				{
					counter++;
				}
			}	
		}	
		
		return counter;
	}
	
	public String toString()
	{
		String result = "";
		for(int y = 0; y < height; y++)
		{			
			for(int x = 0; x < width; x++)
			{
				if(tiles[y][x].isBomb())
				{				
					result += "X  ";
				}
				else
				{
					result += tiles[y][x].getNumberOfNearBombs() + "  ";
				}
			}
			result += "\n";
		}	
		
		return result;
	}	
	
	public static void main(String[] args)
	{
		Board b = new Board(5, 5, 5);
		System.out.println(b);
	}
}