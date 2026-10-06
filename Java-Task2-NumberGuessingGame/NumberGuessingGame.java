import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Oasis Infobyte SIP - Java Development
 * Task 2: Number Guessing Game
 */
public class NumberGuessingGame {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    // Difficulty settings
    private static int maxNumber = 100;
    private static int maxAttempts = 7;

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      NUMBER GUESSING GAME");
        System.out.println("=====================================");

        List<String> roundSummaries = new ArrayList<>();
        int round = 0;
        int roundsWon = 0;
        boolean playAgain;

        do {
            round++;
            chooseDifficulty();
            int target = random.nextInt(maxNumber) + 1;
            int attemptsUsed = 0;
            boolean guessedCorrectly = false;

            System.out.println("\n--- Round " + round + " ---");
            System.out.println("I am thinking of a number between 1 and " + maxNumber + ".");
            System.out.println("You have " + maxAttempts + " attempts.\n");

            while (attemptsUsed < maxAttempts) {
                System.out.println("Attempt " + (attemptsUsed + 1) + " of " + maxAttempts);
                int guess = readInt("Enter your guess: ", 1, maxNumber);
                attemptsUsed++;

                if (guess == target) {
                    System.out.println("Correct! You guessed it in " + attemptsUsed + " attempt(s).");
                    guessedCorrectly = true;
                    break;
                } else if (guess > target) {
                    System.out.println("Too High!");
                } else {
                    System.out.println("Too Low!");
                }
                System.out.println("Attempts left: " + (maxAttempts - attemptsUsed) + "\n");
            }

            if (guessedCorrectly) {
                roundsWon++;
                roundSummaries.add("Round " + round + " - guessed in " + attemptsUsed + " attempts");
            } else {
                System.out.println("You Lost! The number was " + target + ".");
                roundSummaries.add("Round " + round + " - lost (number was " + target + ")");
            }

            playAgain = askYesNo("\nPlay again? (yes/no): ");
        } while (playAgain);

        printSummary(roundSummaries, round, roundsWon);
        System.out.println("\nThanks for playing. Goodbye!");
        scanner.close();
    }

    private static void chooseDifficulty() {
        System.out.println("\nSelect difficulty:");
        System.out.println("1. Easy   (1-50,  10 attempts)");
        System.out.println("2. Medium (1-100, 7 attempts)");
        System.out.println("3. Hard   (1-200, 5 attempts)");
        int choice = readInt("Your choice (1-3): ", 1, 3);

        switch (choice) {
            case 1:
                maxNumber = 50;
                maxAttempts = 10;
                break;
            case 3:
                maxNumber = 200;
                maxAttempts = 5;
                break;
            default:
                maxNumber = 100;
                maxAttempts = 7;
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) {
                return true;
            }
            if (input.equals("no") || input.equals("n")) {
                return false;
            }
            System.out.println("Please type 'yes' or 'no'.");
        }
    }

    private static void printSummary(List<String> summaries, int rounds, int won) {
        System.out.println("\n=====================================");
        System.out.println("           GAME SUMMARY");
        System.out.println("=====================================");
        for (String s : summaries) {
            System.out.println(s);
        }
        System.out.println("-------------------------------------");
        System.out.println("Rounds played: " + rounds + " | Won: " + won + " | Lost: " + (rounds - won));
    }
}