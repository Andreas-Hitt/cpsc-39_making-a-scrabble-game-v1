import java.util.*;

public class LetterPool {
    private static final Map<Character, Integer> LETTER_WEIGHTS = createWeightMap();
    private List<Character> weightedLetters;
    private Random random = new Random();

    private static Map<Character, Integer> createWeightMap() {
        Map<Character, Integer> weights = new HashMap<>();
        // Standard Scrabble distribution weights
        weights.put('A', 9);  weights.put('B', 2);  weights.put('C', 2);
        weights.put('D', 4);  weights.put('E', 12); weights.put('F', 2);
        weights.put('G', 3);  weights.put('H', 2);  weights.put('I', 9);
        weights.put('J', 1);  weights.put('K', 1);  weights.put('L', 4);
        weights.put('M', 2);  weights.put('N', 6);  weights.put('O', 8);
        weights.put('P', 2);  weights.put('Q', 1);  weights.put('R', 6);
        weights.put('S', 4);  weights.put('T', 6);  weights.put('U', 4);
        weights.put('V', 2);  weights.put('W', 2);  weights.put('X', 1);
        weights.put('Y', 2);  weights.put('Z', 1);
        return weights;
    }

    public LetterPool() {
        initializeWeightedList();
    }

    private void initializeWeightedList() {
        weightedLetters = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : LETTER_WEIGHTS.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                weightedLetters.add(entry.getKey());
            }
        }
    }

    public char drawLetter() {
        // Random selection using original weights
        double totalWeight = LETTER_WEIGHTS.values().stream().mapToInt(Integer::intValue).sum();
        double random = Math.random() * totalWeight;
        
        for (Map.Entry<Character, Integer> entry : LETTER_WEIGHTS.entrySet()) {
            random -= entry.getValue();
            if (random <= 0) {
                return entry.getKey();
            }
        }
        return 'A'; // Fallback
    }

    // Special draw for guaranteed vowels
    public char drawVowel() {
        List<Character> vowels = List.of('A', 'E', 'I', 'O', 'U');
        return vowels.get(random.nextInt(vowels.size()));
    }
}