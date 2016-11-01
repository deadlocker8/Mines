package de.deadlocker8.mines.logic;

public class Settings
{
	private int width;
	private int height;
	private int numberOfBombs;	
	
	public Settings(int width, int height, int numberOfBombs)
	{		
		this.width = width;
		this.height = height;
		this.numberOfBombs = numberOfBombs;			
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
	
	public String toString()
	{
		return "width: " + width + ", heigh: " + height + ", numberOfBombs: " + numberOfBombs; 		
	}	
}