package nl.jrwer.challenges.wordsearch.creater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jrwer.challenges.wordsearch.solver.Direction;

public class SuperPuzzle extends AbstractPuzzle {
    private final int maxWordLength;
    
    public SuperPuzzle(int width, int height, String sentence) {
        super(width, height, sentence);
        
        if(width >= 20 || height >= 20)
            maxWordLength = 20;
        else
            maxWordLength = Math.max(width, height);
    }   
    
    public void create(String gridOutput, String wordsOutput) throws IOException {
        create();
        
        gridToFile(gridOutput);
        wordsToFile(wordsOutput);
    }
    
  
    @Override
    public void create() {
        fillGridRandom();
        placeSentenceRandom(); // dont use coords
        placeWords();
        placeOnUnusedLetters();
    }
    
    private void fillGridRandom() {
        for(int x=0; x<width; x++)
            for(int y=0; y<height; y++)
                grid[x][y] = getRandomChar();
    }
    
    private void placeWords() {
        for(int y=0; y<height; y++)
            for(int x=0; x<width; x++)
            	if(shouldPlace())
	                placeWord(x, y);
    }
    
    private void placeOnUnusedLetters() {
    	while(!unused.isEmpty()) {
    		Coord c = unused.iterator().next();
   			placeWord(c.x, c.y);
    	}
    }

    private boolean placeWord(int x, int y) {
        List<Coord> coord = new ArrayList<>();
        int wordLength = randomLength();
        Direction d = getRandomDirection();
        
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<wordLength; i++) {
            Character c = get(x + (i * d.hor), y + (i * d.ver));
            coord.add(new Coord(x + (i * d.hor), y + (i * d.ver)));
            
            if(c == 0)
                return false;
            
            sb.append(c);
        }
        
        String word = sb.toString();
        
        // Succes, word is found
        // TODO check sentence letter are not used
        if(!isSubWord(word) && !gridContainsWord(word) && !overlapsSentence(coord)) {
            used.addAll(coord);
            unused.removeAll(coord);
            usedWords.add(word);
        	
//            System.out.println(word);
            
            return true;
        }
        
        return false;
    }
    
    private boolean overlapsSentence(List<Coord> wordCoords) {
    	for(Coord wordCoord : wordCoords)
    		for(Coord sentenceCoord : dontUse)
    			if(sentenceCoord.equals(wordCoord))
    				return true;
    	
    	return false;
    }
    
    private boolean gridContainsWord(String word) {
        return findWord(word.toCharArray()) > 1;
    }
    
    public int findWord(char[] word) {
        int amount = 0;
        
        for(int x=0; x<this.width; x++)
            for(int y=0; y<this.height; y++) 
                if(grid[x][y] == word[0])
                	for(Direction dir : Direction.values())
                		if(find(x, y, word, dir))
                			amount++;
                		
        return amount;
    }
    
    public boolean find(int x, int y, char[] word, Direction dir) {
        for(int i=1; i<word.length; i++)
            if(!equals(x + (i * dir.hor), y + (i * dir.ver), word[i]))
                return false;
    
        return true;
    }
    
    public char get(int x, int y) {
        if(x < 0 || y < 0 || x >= width || y >= height)
            return 0;
        
        return grid[x][y];
    } 
    
    public boolean equals(int x, int y, char c) {
    	char cc = get(x, y);
    	
        return cc == 0 ? false : c == cc;
    }    
    
    private int randomLength() {
        return RANDOM.nextInt(maxWordLength - 3) + 3;
    }
    
    private boolean shouldPlace() {
    	return RANDOM.nextBoolean();
    }
}
