import java.util.*;

public class Player {

    private int points = 0;
    private int winCount = 0;
    private LinkedList<Character> letterList = new LinkedList<>();
    private String playerName;
    private LetterPool letterPool; // Reference to the shared letter pool
    private int addChances = 0;
    private int addChancesPerTurn = 2;

    public Player(String name, boolean helperBool, LetterPool pool) {
        this.playerName = name;
        this.letterPool = pool;
        drawInitialLetters(helperBool);
    }

    //mutator for points
    public void setPoints(int pointsAdded) {
        points += pointsAdded;
    }

    //accessor for points
    public int getPoints() {
        return points;
    }

    //mutator for wins
    public void incrementWins() {
        winCount++;
    }

    //accessor for wins
    public int getWins() {
        return winCount;
    }

    //accessor for name
    public String getName() {
        return playerName;
    }

    public LinkedList<Character> getLetterList() {
        return letterList;
    }

    public void setLetterList(LinkedList<Character> newLetters) {
        letterList = newLetters;
    }

    public int getAvailableAdds() {
        return addChances;
    }

    public void grantAddChances() {
        addChances += addChancesPerTurn;
    }

    public void useAddChances(int used) {
        addChances = Math.max(addChances - used, 0);
    }

    public void addLetters(int numToAdd) {
        numToAdd = Math.min(numToAdd, addChances);
        if(numToAdd <= 0) return;
        
        drawMoreLetters(numToAdd);
        addChances -= numToAdd;
        System.out.println("Added " + numToAdd + " letters. Remaining chances: " + addChances);
    }
    
    public void drawInitialLetters(boolean helperBool) {
        letterList.clear();
        if (helperBool) {
            letterList.add(letterPool.drawVowel()); // Guaranteed vowel
            for (int i = 0; i < 6; i++) {
                letterList.add(letterPool.drawLetter());
            }
        } else {
            for (int i = 0; i < 7; i++) {
                letterList.add(letterPool.drawLetter());
            }
        }
    }

    public void drawMoreLetters(int numLetters) {
        for (int i = 0; i < numLetters; i++) {
            letterList.add(letterPool.drawLetter());
        }
    }

    public boolean hasLettersFor(String word) {
        List<Character> tempLetters = new ArrayList<>(letterList);
        try {
            for (char c : word.toUpperCase().toCharArray()) {
                if (!tempLetters.remove((Character) Character.toUpperCase(c))) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void useLetters(String word) {
        // Convert input to uppercase and validate
        String upperWord = word.toUpperCase();
        if (!new Word().canFormWord(this, upperWord)) {
            throw new IllegalStateException("Invalid letters for word");
        }
    
        // Remove each letter from rack (case-insensitive)
        for (char c : upperWord.toCharArray()) {
            Character character = Character.toUpperCase(c);
            if (!letterList.remove(character)) {
                throw new IllegalStateException("Player doesn't have required letter: " + character);
            }
        }
    
        // Replenish tiles (works with infinite pool)
        drawMoreLetters(upperWord.length());
    }

    public void reset() {
        points = 0;
        addChances = 0; 
        letterList.clear();
    }
}