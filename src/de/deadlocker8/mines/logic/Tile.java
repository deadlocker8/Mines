package de.deadlocker8.mines.logic;

public class Tile
{
	private int numberOfNearBombs;
	private boolean revealed;
	private boolean flagged;

	public Tile(int numberOfNearBombs)
	{		
		this.numberOfNearBombs = numberOfNearBombs;
		this.revealed = false;
		this.flagged = false;
	}

	public int getNumberOfNearBombs()
	{
		return numberOfNearBombs;
	}

	public void setNumberOfNearBombs(int numberOfNearBombs)
	{
		this.numberOfNearBombs = numberOfNearBombs;
	}
	
	public void setBomb()
	{
		numberOfNearBombs = -1;
	}
	
	public void increase()
	{
		numberOfNearBombs +=1;
	}
	
	public boolean isBomb()
	{
		if(numberOfNearBombs == -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isRevealed()
	{
		return revealed;
	}

	public void setRevealed(boolean revealed)
	{
		this.revealed = revealed;
	}

	public boolean isFlagged()
	{
		return flagged;
	}

	public void setFlagged(boolean flagged)
	{
		this.flagged = flagged;
	}	
}