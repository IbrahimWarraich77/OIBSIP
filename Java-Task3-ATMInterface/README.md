# ATM Interface

**Oasis Infobyte SIP | Track: Java Development | Task 3**
**Intern:** YOUR FULL NAME HERE

## Description
A console-based ATM simulation in Java built with Object-Oriented design.
Users log in with a User ID and PIN, then perform banking operations.

## Features
- Login with User ID and PIN; access denied after 3 incorrect attempts
- Main menu:
  1. Transaction History
  2. Withdraw
  3. Deposit
  4. Transfer
  5. Quit
- Balance check before withdraw and transfer ("Insufficient Funds" if too low)
- All transactions stored in an ArrayList and shown in Transaction History
- Transfer updates both sender and receiver accounts
- Input validation (non-numeric, zero or negative amounts rejected)

## Project Structure
| Class | Responsibility |
|---|---|
| Main | Creates the bank, demo accounts and starts the ATM |
| ATM | Login and menu handling (user interface) |
| Bank | Stores accounts, authenticates users, handles transfers |
| Account | User data, balance, PIN check, transaction list |
| Transaction | One record: type, amount, balance after, timestamp |

OOP concepts: encapsulation (private fields + getters), composition, ArrayList, HashMap, switch-case.

## Demo Accounts
| User ID | PIN | Opening Balance |
|---|---|---|
| user1 | 1234 | 5000 |
| user2 | 4321 | 3000 |

## Tech Stack
- Java (JDK 22)
- Console (Scanner)

## How to Run
```bash
javac *.java
java Main
```

