# Number Guessing Game

**Oasis Infobyte SIP | Track: Java Development | Task 2**
**Intern:** Muhammad Ibrahim

## Description
A console-based Java game where the computer picks a random number and the player
guesses it with "Too High!" / "Too Low!" / "Correct!" hints.

## Features
- Random number generation using `java.util.Random`
- Hints after every guess: Too High / Too Low / Correct
- Attempt counter shown during the game
- Maximum attempts limit; on loss, "You Lost!" and the number is revealed
- Play Again option after each round
- Round-by-round summary at the end (e.g. "Round 1 - guessed in 4 attempts")
- Bonus: 3 difficulty levels
  - Easy: 1-50, 10 attempts
  - Medium: 1-100, 7 attempts
  - Hard: 1-200, 5 attempts
- Input validation (non-numeric and out-of-range input is rejected)

## Tech Stack
- Java 8 or higher
- Console (Scanner)

## How to Run
```bash
javac NumberGuessingGame.java
java NumberGuessingGame
```

## Screenshots
Add screenshots of gameplay here (win, loss, play again, final summary).

## Concepts Used
`Random`, `Scanner`, loops, `if-else`, `switch`, `ArrayList`, exception handling.
