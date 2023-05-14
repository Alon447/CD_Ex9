package test;


public class Word {

	Tile[] tiles;
	int row;
	int col;
	boolean vertical; // true=vertical, false= horizontal
	
	//con
	public Word(Tile[] tiles, int row, int col,	boolean vertical)
	{
		this.tiles=tiles;
		this.row=row;
		this.col=col;
		this.vertical=vertical;
	}
	//gets
	public Tile[] getTiles()
	{
		return this.tiles;
	}
	
	public int getRow()
	{
		return this.row;
	}
	
	public int getCol()
	{
		return this.col;
	}
	
	public boolean getVertical()
	{
		return this.vertical;
	}
	
	public boolean equals(Word w)
	{
		if(this.col==w.col && this.row==w.row && this.vertical==w.vertical && this.tiles.length==w.tiles.length)
		{
			for( int i=0; i<this.tiles.length; i++)
			{
				if(!this.tiles[i].equals(w.tiles[i]))
					return false;
			}
			return true;
		}
		return false;
		
	}
	
	public String toString() 
	{
		String s="";
		for(Tile t:tiles)
		{
			if(t==null)
				s+="_";
			else
				s+=t.letter;
		}
		return s;
	}
}
