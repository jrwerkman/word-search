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
        

    }
    
    private void fillGridRandom() {
        for(int x=0; x<width; x++)
            for(int y=0; y<height; y++)
                grid[x][y] = getRandomChar();
    }
    
    private void placeWords() {
        for(int y=0; y<height; y++)
            for(int x=0; x<width; x++)
                while(!placeWord(x, y)) {
                    //retry
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
            
            if(c == null)
                return false;
            
            sb.append(c);
        }
        
        String word = sb.toString();
        
        // Succes, word is found
        // TODO check sentence letter are not used
        if(!isSubWord(word) && !gridContainsWord(word)) {
            // TODO add word, set used, mark grid, remove unused
            
            return true;
        }
        
        return false;
    }
    private boolean gridContainsWord(String word) {
        return findWord(word.toCharArray()) > 1;
    }
    
    public int findWord(char[] word) {
        int amount = 0;
        for(int x=0; x<this.width; x++)
            for(int y=0; y<this.height; y++) 
                if(grid[x][y] == word[0] && findWord(x, y, word))
                    amount++;
        
        return amount;
    }
    
    public boolean findWord(int x, int y, char[] word) {
        return find(x, y, word, Direction.RIGHT) || find(x, y, word, Direction.LEFT) 
                || find(x, y, word, Direction.BOTTOM) || find(x, y, word, Direction.TOP)
                || find(x, y, word, Direction.LEFT_TOP) || find(x, y, word, Direction.LEFT_BOTTOM)
                || find(x, y, word, Direction.RIGHT_TOP) || find(x, y, word, Direction.RIGHT_BOTTOM);
    }
    
    public boolean find(int x, int y, char[] word, Direction dir) {
        for(int i=1; i<word.length; i++)
            if(!equals(x + (i * dir.hor), y + (i * dir.ver), word[i]))
                return false;
    
        return true;
        
    }
    
    public Character get(int x, int y) {
        if(y < 0 || x < 0 || x >= width || y >= height)
            return null;
        
        return grid[x][y];
    } 
    
    public boolean equals(int y, int x, char c) {
        return get(x, y) == c;
    }    
    
    private boolean coordUsed() {
        // TODO
        return false;
    }
    
    private int randomLength() {
        return RANDOM.nextInt(maxWordLength - 3) + 3;
    }
    
    private boolean overlapSentence() {
        //TODO
    }
    
}
