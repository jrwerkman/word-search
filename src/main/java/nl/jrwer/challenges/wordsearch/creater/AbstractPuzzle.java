package nl.jrwer.challenges.wordsearch.creater;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import nl.jrwer.challenges.wordsearch.solver.Direction;

public abstract class AbstractPuzzle {
    protected static final Random RANDOM = new Random();
    protected final char[][] grid;
    protected final char[] sentence;
    protected final int width;
    protected final int height;
    protected final int amountLetters;
    
    protected final Set<String> usedWords = new HashSet<>();
    protected final Set<Coord> unused = new HashSet<>();
    protected final Set<Coord> used = new HashSet<>();
    protected final Set<Coord> dontUse = new HashSet<>();

    
    protected AbstractPuzzle(int width, int height, String sentence) {
        this.grid = new char[width][height];
        this.height = height;
        this.width = width;
        this.amountLetters = width * height;
        this.sentence = sentence.toUpperCase().toCharArray();
        
        for(int x=0; x<width; x++)
            for(int y=0; y<height; y++)
                unused.add(new Coord(x, y));        
    }
    
    public abstract void create();
    
    protected void gridToFile(String filename) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            writer.write(this.toString());
        }
    }
    
    protected void wordsToFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        for(String word : usedWords)
            sb.append(word).append('\n');
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            writer.write(sb.toString());
        }
        
    }  
    
    protected void placeSentenceRandom() {
        List<Coord> randomCoords = new ArrayList<>();
        
        while(randomCoords.size() < sentence.length) {
            Coord c = getRandomCoord();
            
            if(!randomCoords.contains(c))
                randomCoords.add(c);
        }

        sortCoordsList(randomCoords);
        placeSentence(randomCoords);
    }
    
    protected Coord getRandomCoord() {
        int x = RANDOM.nextInt(width);
        int y = RANDOM.nextInt(height);
        
        return new Coord(x, y);
    }    
    
    protected void sortCoordsList(List<Coord> l) {
        Collections.sort(l, new Comparator<Coord>() {
            public int compare(Coord c1, Coord c2) {
                int result = Integer.compare(c1.y, c2.y);
                
                if (result == 0) 
                    result = Integer.compare(c1.x, c2.x);

                return result;
            }
        });
    }
    
    protected void placeSentence(List<Coord> coords) {
        int i=0;
        for(Coord c : coords) {
            grid[c.x][c.y] = sentence[i];

            used.add(c);
            dontUse.add(c);
            unused.remove(c);

            i++;
        }
    }
    
    protected boolean isSubWord(String word) {
        for(String used : usedWords)
            if(word.contains(used) || used.contains(word))
                return true;
        
        return false;
    }
    
    protected Direction getRandomDirection() {
        Direction[] dirs = Direction.values();
        
        return dirs[RANDOM.nextInt(dirs.length)];
    }
    
    protected static String getRandomSentence(int length) {
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<length; i++)
            sb.append(getRandomChar());
        
        return sb.toString();
    }
    
    public static char getRandomChar() {
        return (char) (RANDOM.nextInt(26) + 65);
    }    
}
