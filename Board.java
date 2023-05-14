package test;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	
	 Tile[][] board;
	 int [][] bonusPoints;
	 
	 public Board()
	 {
		 this.board= new Tile[15][15];
		 this.initializeBonus();
		 //this.printBonus();
	 }
	 
	 private void initializeBonus()
	 { // 1-double letter, 2-triple letter, 3-double word, 4-triple word
		 this.bonusPoints= new int[15][15];
		 for(int i=0; i<15; i++)
		 {
			 for(int j=0; j<15; j++)
			 {
				 if( ( (i==0||i==14||i==7) && (j==3 || j==11) ) || (i==6||i==8) && (j==2||j==6||j==8||j==12) || (i==3||i==11) && (j==0||j==7||j==14) || (i==2||i==12)&&(j==6||j==8) )// 1- double letter
					 this.bonusPoints[i][j]=1;
				 if((i==1||i==13)&&(j==5||j==9) || (i==5||i==9)&&(j==1||j==5||j==9||j==13))// 2-triple letter
					 this.bonusPoints[i][j]=2;
				 if((i==1||i==13)&&(j==1||j==13) || ((i==2||i==12)&&(j==2||j==12))|| ((i==3||i==11)&&(j==3||j==11))|| ((i==4||i==10)&&(j==4||j==10)) || (i==7 && j==7))// 3-double word
						 this.bonusPoints[i][j]=3;
				 if((i==0||i==14)&&(j==0||j==14||j==7) || ((i==7)&&(j==0||j==14)))// 4-triple word
					 this.bonusPoints[i][j]=4;
			 }
		 }
	 }
	 
	 public  void printBonus() {
		    int rows = 15;
		    int cols = 15;
		    for (int i = 0; i < rows; i++) {
		        for (int j = 0; j < cols; j++) {
		        	if(this.bonusPoints[i][j]==0)
			            System.out.print( "_ ");
		        	else
		        		System.out.print(this.bonusPoints[i][j] + " ");
		        }
		        System.out.println();
		    }
		}
	 
	 public Tile[][] getTiles() // return a 2d array of a copy of the board
	 {
		 Tile[][] copyBoard=new Tile[this.board.length][];
		 for(int i=0; i<this.board.length; i++)
			 copyBoard[i]=Arrays.copyOf(this.board[i], this.board[i].length);
		 return copyBoard;
	 }
	 
	 
	 public boolean isWordExistOnBoard(int len, int row, int col,boolean vertical) 
	 { // a function that checks if the whole word exists on the board
		 for(int i=0; i<len; i++)
		 {
			 if(vertical)
			 {
				 if(this.board[row+i]==null || this.board[row+i][col]==null)
				 {
					 return false;
				 } 
			 }
			 else
			 {
				 if(this.board[row]==null|| this.board[row][col+i]==null)
				 {
					 return false;
				 } 
			 }
		 }
		 return true;
	 }
	 
	 public boolean starValue(int len, int row, int col,boolean vertical) // function that return true if this is the first turn and the word is on the star
	 {
		 if(this.board[7] != null && this.board[7][7] != null)
			 return false;
		 if((vertical && col==7 && row <= 7 && row + len >= 7) || (!vertical && row==7 && col <= 7 && col + len >= 7))
			 return true;
		return false;
		 
	 }
	 
	 public boolean isEnoughSpace(int len, int row, int col,boolean vertical)// function that checks if the chosen word is fitting in the board
	 {
		 if((vertical && row + len > 14) || (!vertical && col + len > 14))
			 return false;
		 return true;
	 }
	 
	 private boolean isEmpty() {
			for(Tile[] innerBoard: this.board)
			{
				for(Tile t: innerBoard)
				{
					if(t != null)
						return false;
				}
			}
			return true;
		}
	 
	 public boolean boardLegal(Word w) // gets a word and return true if the word is legal
	 {
		 // check if the whole word is already on the board 
		 int totalLength=w.tiles.length;
		 int w_row = w.row;
		 int w_col = w.col;
		 
		 if(w.row<0)
			 w_row+=15;
		 if(w.col<0)
			 w_col+=15;
		 
		 Tile[] wordOnBoard=new Tile[totalLength]; // create an array for the supposed to be word on the board
		 boolean empty= this.isEmpty(); // check if there is already an existing word on the board
		 boolean isExist = this.isWordExistOnBoard(totalLength, w_row, w_col, w.vertical);
		 boolean star = this.starValue(totalLength, w_row, w_col, w.vertical);
		 boolean space = this.isEnoughSpace(totalLength, w_row, w_col, w.vertical);
		 boolean underscore= this.underscore(w);
		 if(!space)
			 return false;
		 
		 // first word needs to be in the middle
		 if(star && empty)
			 return true;
		 
		 //if there is already a word on the board then check if the word already exists fully on the board
		 if(!empty)
		 {
			 for(int i=0; i<totalLength; i++)
			 {
				 if(w.vertical)
				 {
					 if(this.board[w.row+i][w.col]!=null)
					 {
						 wordOnBoard[i]=this.board[w.row+i][w.col];
					 }
				 }
				 else
				 {
					 if(this.board[w.row][w.col+i]!=null)
					 {
						 wordOnBoard[i]=this.board[w.row][w.col+i];
					 }
				 }
			 }
			 Word word=new Word(wordOnBoard,w.col,w.row,w.vertical);
			 if(w.equals(word))
				 return true;
			 if(this.dependsOnAnotherWord(totalLength,w_row,w_col,w.vertical))
				 return true;
			 /*
			 for(int i=0; i<totalLength; i++)
			 {
				 if(wordOnBoard[i]==w.tiles[i])
				 {
					 return true;
				 }
			 }
			 */
			 if(underscore) // the word depends on existing letter
			 {
				 for(int i=0; i<totalLength; i++)
				 {
					 if(w.vertical)
					 {
						 if(!(w.tiles[i] != null & this.board[w_row+i][w_col] == null)& !(w.tiles[i] == null & this.board[w_row+i][w_col] != null))
						 {
							 return false;
						 } 
					 }
					 else
					 {
						 if(!(w.tiles[i] != null & this.board[w_row][w_col+i] == null)& !(w.tiles[i] == null & this.board[w_row][w_col+i] != null))
						 {
							 return false;
						 }  
					 }
					
						 
				 }
				 return true;
			 }
		 }
		 
		 return false;
	 }
	 

	private boolean dependsOnAnotherWord(int len, int row, int col, boolean vertical) {
		if(vertical)
		{
			for(int i=0;i<len;i++)
			{
				if(this.board[row+i][col+1]!=null || this.board[row+i][col-1]!=null)
					return true;
			}
			if (this.board[row-1][col]!=null || this.board[row+len][col]!=null)
				return true;

		}
		else
		{
			for(int i=0;i<len;i++)
			{
				if(this.board[row+1][col+i]!=null || this.board[row-1][col+i]!=null)
					return true;
			}
			if (this.board[row][col-1]!=null || this.board[row][col+len]!=null)
				return true;
		}
		return false;
	}

	private boolean underscore(Word w) {
		for(int i=0; i<w.tiles.length; i++)
		{
			if(w.tiles[i]==null)
				return true;
		}
		return false;
	}

	public boolean dictionaryLegal(Word w)
	 {
		 return true;
	 }
	
	public  void printBoard() {
	    int rows = 15;
	    int cols = 15;
	    for (int i = 0; i < rows; i++) {
	        for (int j = 0; j < cols; j++) {
	        	if(this.board[i][j]==null)
		            System.out.print( "_ ");
	        	else
	        		System.out.print(this.board[i][j] + " ");
	        }
	        System.out.println();
	    }
	}
	
	public ArrayList<Word> getVerticalWords() // return all the vertical words on the board 
	{
		ArrayList<Word> words= new ArrayList<Word>();
		Tile[] ts= new Tile[15];
		Tile[] word;
		Word temp;
		boolean vert=true;
		int len=0;
		
		for(int i=0; i<15 ; i++)
		{
			
			for( int j=0; j<14; j++)
			{
				while(this.board[j][i] != null)
				{
					ts[len]=this.board[j][i];
					len++;
					if(j<14)
						j++;
					else // the end of the column
						break;
				}
				if(len>1)
				{
					word= Arrays.copyOf(ts, len);
					temp=new Word(word,j-len,i,true);
					words.add(temp);
				}
				len=0;
				
			}
		}
		//System.out.println(words);
		return words;
	}
	 
	public ArrayList<Word> getHorizontalWords() {

		ArrayList<Word> words= new ArrayList<Word>();
		ArrayList<Tile> tiles= new ArrayList<Tile>();
		Tile[] ts= new Tile[15];
		Tile[] word;
		Word temp;
		int len=0;
		
		for(int j=0; j<15 ; j++)
		{
			
			for( int i=0; i<14; i++)
			{
				while(this.board[j][i] != null)
				{
					ts[len]=this.board[j][i];
					len++;
					if(i<14)
						i++;
					else // the end of the column
						break;
				}
				if(len>1)
				{
					word= Arrays.copyOf(ts, len);
					temp=new Word(word,j,i-len,false);
					words.add(temp);
				}
				len=0;
				
			}
		}
		//System.out.println(words);
		return words;
		
	}
	
	public ArrayList<Word> getWords(Word w)
	 {
		ArrayList<Word> Words= this.getVerticalWords();
		Words.addAll(getHorizontalWords());
		return Words;
		 
	 }

	public int getScore(ArrayList<Word> words)
	 {
		 int score=0;
		 int temp=0;
		 Word w;
		 boolean doubleWord=false;
		 boolean tripleWord=false;
		 for (int i=0; i< words.size(); i++) // move over all the words in the list
		 {
			 w=words.get(i);
			 int w_row = w.row;
			 int w_col = w.col;
				 
			 if(w.row<0)
				w_row+=15;
			 if(w.col<0)
				w_col+=15;
			 for(int j=0; j < w.tiles.length; j++) // move over all the tiles in the word
			 {
			 	int bonus;
			 	if(w.vertical)
			 	{
			 		bonus=this.bonusPoints[w_row+j][w_col];
			 		switch(bonus)
					{
					case 1:
						temp+=w.tiles[j].getScore()*2;
						break;

					case 2:
						temp+=w.tiles[j].getScore()*3;
						break;

					case 3:
						temp+=w.tiles[j].getScore();
						doubleWord=true;
						break;
						
					case 4:
						temp+=w.tiles[j].getScore();
						tripleWord=true;
						break;
						
					default:
						temp+=w.tiles[j].getScore();
					}
			 	}
			 	else
			 	{
			 		bonus=this.bonusPoints[w_row][w_col+j];
			 		switch(bonus)
					{
					case 1:
						temp+=w.tiles[j].getScore()*2;
						break;

					case 2:
						temp+=w.tiles[j].getScore()*3;
						break;

					case 3:
						temp+=w.tiles[j].getScore();
						doubleWord=true;
						break;
						
					case 4:
						temp+=w.tiles[j].getScore();
						tripleWord=true;
						break;
						
					default:
						temp+=w.tiles[j].getScore();
					}
				
				 
				}
			 }
			 if(doubleWord)
				 temp*=2;
			 if(tripleWord)
				 temp*=3;
			 doubleWord=false;
			 tripleWord=false;
			 score+=temp;
			 temp=0;
		 }
		 if(!this.isEmpty())
			 this.bonusPoints[7][7]=0;
		 return score;
	 }
	
	public void placeWord(Word w)// update the board with the new word
	 {
		 int totalLength=w.tiles.length;
		 int w_row = w.row;
		 int w_col = w.col;
		 
		 if(w.row<0)
			 w_row+=15;
		 if(w.col<0)
			 w_col+=15;
		 
		 for(int i=0; i<totalLength; i++)
		 {
			 if(w.vertical)
			 {
				 if(w.tiles[i] != null)
					 this.board[w_row+i][w_col]=w.tiles[i];
				 
			 }
			 else
			 {
				 if(w.tiles[i] != null)
					 this.board[w_row][w_col+i]=w.tiles[i];
			 }
		 }
		// this.printBoard();
		 
	 }
	 
	private ArrayList<Word> newWords(ArrayList<Word> a1,ArrayList<Word> a2)
	{
		ArrayList<Word> uniqueList = new ArrayList<Word>();
		for(Word w1: a1)
		{
			boolean unique=true;
			for(Word w2: a2)
			{
				if(w1.equals(w2))
				{
					unique=false;
					break;
				}
			}
			if(unique)
				uniqueList.add(w1);
		}
		return uniqueList;
	}

	public int tryPlaceWord(Word w) // place the word if possible and return the score 
	{
		if(boardLegal(w) && dictionaryLegal(w)) // check if the word can be placed
		{
			ArrayList<Word> oldwords=this.getWords(w); // list of words before the new word been placed
			//System.out.println("******************************************");
			this.placeWord(w); // placing the new word
			ArrayList<Word> newWords=this.getWords(w); // a list of updated words on the board
			 // newWords will only contain the new words on the board
			newWords=this.newWords(newWords, oldwords);
			//System.out.println("New words:" +newWords);
			int score=this.getScore(newWords);
			//System.out.println("Word placed:"+ w+" Score:"+score);
			return score;
		}
		return 0;
	}

}
