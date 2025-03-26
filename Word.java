import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class Word {

    private static final String DICTIONARY_FILE = "CollinsScrabbleWords.txt";
    private ArrayList<String> wordList;

    public Word() {
        wordList = new ArrayList<>();
        loadDictionary(); 
    }

    private void loadDictionary() {
        System.out.println("Attempting to load dictionary..."); // ADDED
        try (BufferedReader reader = new BufferedReader(new FileReader(DICTIONARY_FILE))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length > 0) {
                    wordList.add(parts[0].trim().toUpperCase());
                    count++;
                }
            }
            System.out.println("Loaded " + count + " words from dictionary"); // ADDED
            Collections.sort(wordList);
        } catch (IOException e) {
            System.err.println("FATAL ERROR: Dictionary file not found!");
            System.err.println("Place 'CollinsScrabbleWords.txt' in same directory as the game");
            System.exit(1); // Force quit game
        }
    }

    public boolean isValidWord(String userWord) {
        if (userWord == null || userWord.length() < 2) {
            return false; // Scrabble requires at least 2 letters
        }
        
        String sanitizedWord = userWord.trim().toUpperCase();
        return Collections.binarySearch(wordList, sanitizedWord) >= 0;
    }

    public boolean canFormWord(Player player, String word) {
        Map<Character, Long> wordCounts = word.toUpperCase().chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        Map<Character, Long> playerCounts = player.getLetterList().stream()
            .map(Character::toUpperCase)
            .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        return wordCounts.entrySet().stream()
            .allMatch(entry -> playerCounts.getOrDefault(entry.getKey(), 0L) >= entry.getValue());
    }

    private static final Map<Character, Integer> LETTER_VALUES = createLetterValueMap();
    
    private static Map<Character, Integer> createLetterValueMap() {
        Map<Character, Integer> map = new HashMap<>();
        // Standard Scrabble letter values
        map.put('A', 1); map.put('B', 3); map.put('C', 3); map.put('D', 2);
        map.put('E', 1); map.put('F', 4); map.put('G', 2); map.put('H', 4);
        map.put('I', 1); map.put('J', 8); map.put('K', 5); map.put('L', 1);
        map.put('M', 3); map.put('N', 1); map.put('O', 1); map.put('P', 3);
        map.put('Q', 10); map.put('R', 1); map.put('S', 1); map.put('T', 1);
        map.put('U', 1); map.put('V', 4); map.put('W', 4); map.put('X', 8);
        map.put('Y', 4); map.put('Z', 10);
        return Collections.unmodifiableMap(map);
    }

    public int calculateWordScore(String word) {
        if (word == null || word.isEmpty()) return 0;
        
        return word.toUpperCase().chars()
            .map(c -> LETTER_VALUES.getOrDefault((char) c, 0))
            .sum();
    }

    public void askForInput() {
        System.out.println("");
    }

    public Character getPlayerDeck() {
        return ' '; // Placeholder - replace with actual logic later
    }
}
