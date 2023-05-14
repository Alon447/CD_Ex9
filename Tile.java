package test;

import java.util.Arrays;

public class Tile {
	
	public final char letter;
	public final int score;
	
	private Tile(char letter, int score) //constructor
	{
		this.letter=letter;
		this.score=score;
	}
	
	public Tile(Tile t1)
	{
		this.letter=t1.letter;
		this.score=t1.score;
	}
	
	public boolean equals(Tile t) // equals checks if two objects are equal
	{
		if(this.letter==t.letter && this.score==t.score)
			return true;
		return false;
		
	}
	
	public int hashCode() // for every tile his hash code is the numerical value of his letter
	{
		
		return Character.getNumericValue(letter);
		
	}
	
	public String toString()
	{
		String s=Character.toString(letter);
		return s;
		
	}
	
	int getScore()
	{
		return this.score;
	}
	
	public static class Bag {
		private int[] letterCount = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1}; // the number of tiles for each letter in the bag
		private int[] maxCount= Arrays.copyOf(letterCount, letterCount.length); // the max number of tiles for each letter
		public Tile[] tiles; // all the tiles in the bag
		private int[] score= {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10}; // the score for each letter
	
		
		public Bag() 
		{
			this.tiles = new Tile[26];
			for(int i=0; i<26; i++)
			{
				tiles[i]=new Tile(Character.valueOf((char) (i + 65)),score[i]);
			}
		}
		
		public int[] getQuantities()
		{
			return Arrays.copyOf(letterCount, letterCount.length);
		}
		
		public int getSize()
		{
			int[] current=this.getQuantities();
			int size=0;
			for(int i=0; i<current.length;i++)
			{
				size+=current[i];
			}
			return size;
		}
		
		public Tile getRand() // return a random tile from the bag
		{
			int rand=(int)(Math.random()*25);
			
			if(this.getSize()==-0) // if there are no more tile in the bag
				return null;
			
			while(this.letterCount[rand]==0)
			{
				rand=(int)Math.random()*25;
			}
			this.letterCount[rand]--;
			Tile t = this.tiles[rand];
			return t;
		}
		
		public Tile getTile(char letter)// return a tile from the bag based on the letter given as an input
		{
			int index=Character.valueOf(letter)-65;

			if(index<0 || index>25||this.letterCount[index]==0)
				return null;
			this.letterCount[index]--;
			return this.tiles[index];
		}
		
		public void put(Tile t)// gets a tile back to the bag if possible
		{
			if(this.letterCount[Character.getNumericValue(t.letter)-10]+1 <= this.maxCount[Character.getNumericValue(t.letter)-10])
			{
				this.letterCount[Character.getNumericValue(t.letter)-10]++;

			}
		}

		public static Bag getBag() 
		{
			return null;
			
		}
		
		
		
	}
}
