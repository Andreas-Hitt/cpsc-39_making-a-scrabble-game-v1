// Andreas Hitt
// Prof. Kanemoto
// CPSC - 39
// March 13, 2025
// Scrabble-Like Game

import java.util.*;

//Scrabble-like word game with variable number of players
//player with most number of points at the end wins (configurable)
//players can play infinitely, wins will be recorded
//this class handles the 'user interface' or real gameplay
public class ScrabbleMain {
    private char helperVowel;
    private static int playerNum;
    private static boolean wantsToPlayAgain = true;
    private boolean helperBool; // True if guaranteed vowel, false otherwise
    private int winCondition;

    private ArrayList<Player> players = new ArrayList<>(); // Store all players across game instances
    private Scanner scnr = new Scanner(System.in);
    private LetterPool sharedLetterPool = new LetterPool();  // Create a single letter pool for the game


    public static void main(String[] args) {
        ScrabbleMain game = new ScrabbleMain();
        game.startGame();
        game.initializePlayers(playerNum);
        game.runGameLoop(); 
    }

    private void runGameLoop() {
        while (wantsToPlayAgain) {
            for (Player currentPlayer : players) {
                handlePlayerTurn(currentPlayer);
            }
        }
    }

    private void handlePlayerTurn(Player player) {
        // Show rack FIRST before any actions
        System.out.println("\n=== " + player.getName().toUpperCase() + "'S TURN ===");
        System.out.println("Current letters: " + player.getLetterList());
        
        // Then handle add chances
        player.grantAddChances();
        System.out.println("Add chances available: " + player.getAvailableAdds());
        
        // Letter addition phase
        if (player.getAvailableAdds() > 0) {
            System.out.print("Want to add letters to your rack? (y/n): ");
            if (scnr.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.print("How many to add? (Max " + player.getAvailableAdds() + "): ");
                int numToAdd = scnr.nextInt();
                scnr.nextLine(); // Clear buffer
                player.addLetters(numToAdd);
                // Show updated rack after adding
                System.out.println("Updated letters: " + player.getLetterList()); 
            }
        }
        playTurn(player);
    }

    public void startGame() {
        System.out.println("Enter number of players: ");
        playerNum = scnr.nextInt();
        scnr.nextLine();

        System.out.println("Do you want to start with a guaranteed vowel? (y/n)");
        while (true) {
            helperVowel = scnr.next().charAt(0);
            scnr.nextLine();
            if (helperVowel == 'y' || helperVowel == 'Y') {
                helperBool = true;
                break;
            } else if (helperVowel == 'n' || helperVowel == 'N') {
                helperBool = false;
                break;
            } else {
                System.out.println("Invalid Character. Please enter \"y\" or \"n\"");
            }
        }

        // Ask for a variable win condition
        System.out.println("Set a win condition between 1 - 2000 points (recommended 75): ");
        while (true) {
            int choice = scnr.nextInt();
            if (choice >= 1 && choice <= 2000) {
                winCondition = choice;
                break;
            } else {
                System.out.println("Invalid choice. Please enter a number between 1 and 2000.");
            }
        }
    }

    public void initializePlayers(int playerNum) {
        scnr.nextLine();
        for (int i = 1; i <= playerNum; i++) {
            System.out.println("Enter the name of player " + i + ".");
            String playerName = scnr.nextLine();
            players.add(new Player(playerName, helperBool, sharedLetterPool)); // Pass helperBool to constructor and letter pool
        }
    }

    private void playTurn(Player currentPlayer) {
        System.out.println("\n" + currentPlayer.getName() + "'s turn");
        System.out.println("Your letters: " + currentPlayer.getLetterList());
        
        Word wordValidator = new Word();
        String attempt;
        boolean validWord;
        
        do {
            System.out.print("Enter your word (or 'pass' to skip): ");
            attempt = scnr.nextLine().trim().toUpperCase(); // FIXED input
            
            if (attempt.equals("PASS")) {
                System.out.println("Player passed their turn");
                return;
            }

            validWord = wordValidator.isValidWord(attempt) && 
                       wordValidator.canFormWord(currentPlayer, attempt);
            
            if (!validWord) {
                System.out.println("Invalid word or insufficient letters. Try again.");
            }
        } while (!validWord);

        int score = wordValidator.calculateWordScore(attempt);
        currentPlayer.setPoints(score);
        currentPlayer.useLetters(attempt);
        
        System.out.println("Word accepted! Score: " + score);
        if (currentPlayer.getPoints() >= winCondition) {
            handleGameEnd();
            return;
        }
    }

    private void displayScores() {
        System.out.println("\nCurrent Scores:");
        players.forEach(p -> 
            System.out.printf("- %s: %d points%n", p.getName(), p.getPoints())
        );
    }

    public void resetGame() {
        players.forEach(player -> {
            player.reset();
            player.drawInitialLetters(helperBool);
        });
    }


    private void handleGameEnd() {
        displayScores();
        System.out.println("\nGAME OVER! WINNERS:");
        players.stream()
            .filter(p -> p.getPoints() >= winCondition)
            .forEach(winner -> {
                winner.incrementWins();
                System.out.println(winner.getName() + " (Wins: " + winner.getWins() + ")");
            });
        
        // Prepare for replay
        System.out.println("\nPlay again with same players? (y/n)");
        if (scnr.next().equalsIgnoreCase("y")) {
            resetGame();
            runGameLoop();
        } else {
            wantsToPlayAgain = false;
        }
    }
}
