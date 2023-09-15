// Importing necessary libraries for the GUI and logic
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.stream.IntStream;

// Defining the class TicTacToe
public class TicTacToe {
    // Creating the main frame for the game
    private JFrame frame;
    // Creating an array to hold all the game buttons
    private JButton[] buttons = new JButton[9];
    // Creating an array to represent the game board state
    private char[] board = new char[9];
    // Current player variable. It starts as 'X' by default
    private char currentPlayer = 'X';

    // Constructor for the TicTacToe class
    public TicTacToe() {
        // Initializing the main frame with the title "Tic Tac Toe"
        frame = new JFrame("Tic Tac Toe");
        // Setting the layout manager to BorderLayout
        frame.setLayout(new BorderLayout());
        // Setting the size of the frame
        frame.setSize(300, 350);
        // Setting the default close operation to exit when closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating a panel to hold the grid of buttons
        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        // Initializing the game buttons, adding them to the grid panel and attaching action listeners
        IntStream.range(0, 9).forEach(i -> {
            buttons[i] = new JButton("");
            gridPanel.add(buttons[i]);
            buttons[i].addActionListener(this::playerMove);
        });

        // Adding the grid panel to the center of the frame
        frame.add(gridPanel, BorderLayout.CENTER);
        // Adding a "Restart" button at the bottom
        frame.add(new JButton("Restart") {{
            addActionListener(e -> resetGame());
        }}, BorderLayout.SOUTH);
        // Making the frame visible
        frame.setVisible(true);
    }

    // This function is called when a player clicks on a button
    private void playerMove(ActionEvent e) {
        // Finding which button was clicked
        int index = IntStream.range(0, 9).filter(i -> e.getSource() == buttons[i]).findFirst().orElse(-1);
        // If a valid button was clicked and it's empty, make the player move and then let the AI move
        if (index != -1 && buttons[index].getText().isEmpty()) {
            makeMove(index);
            if (!isGameOver()) aiMove();
        }
    }

    // Updates the game's state and UI based on the given move
    private void makeMove(int index) {
        // Update the button's text and the board state
        buttons[index].setText(String.valueOf(currentPlayer));
        board[index] = currentPlayer;
        // If the game is over after this move, display the winner and reset the game. Otherwise, switch the current player
        if (isGameOver()) {
            displayWinner(checkWinner());
            resetGame();
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
    }

    // Check if the game is over (either because there's a winner or because the board is full)
    private boolean isGameOver() {
        char winner = checkWinner();
        if (winner != '\0') return true;
        return IntStream.range(0, 9).noneMatch(i -> board[i] == '\0');
    }

    // Check if there's a winner in the current state
    private char checkWinner() {
        // Checking rows and columns for a win
        for (int i = 0; i < 3; i++) {
            if (board[i * 3] != '\0' && board[i * 3] == board[i * 3 + 1] && board[i * 3] == board[i * 3 + 2]) return board[i * 3];
            if (board[i] != '\0' && board[i] == board[i + 3] && board[i] == board[i + 6]) return board[i];
        }
        // Checking diagonals for a win
        if (board[0] != '\0' && board[0] == board[4] && board[0] == board[8]) return board[0];
        if (board[2] != '\0' && board[2] == board[4] && board[2] == board[6]) return board[2];
        // If no winner, return the null character
        return '\0';
    }

    // Display a dialog box indicating who won or if it's a tie
    private void displayWinner(char winner) {
        if (winner == 'X') JOptionPane.showMessageDialog(frame, "You win!");
        else if (winner == 'O') JOptionPane.showMessageDialog(frame, "AI wins!");
        else JOptionPane.showMessageDialog(frame, "It's a tie!");
    }

    // Resets the game to its initial state
    private void resetGame() {
        IntStream.range(0, 9).forEach(i -> {
            board[i] = '\0';
            buttons[i].setText("");
        });
        currentPlayer = 'X';
    }

    // The AI's move logic using the minimax algorithm
    private void aiMove() {
        int bestScore = Integer.MIN_VALUE;
        int move = -1;
        // Checking every possible move for the AI
        for (int i = 0; i < 9; i++) {
            if (board[i] == '\0') {
                board[i] = 'O';
                int score = minimax(board, 0, false);
                board[i] = '\0';
                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
            }
        }
        // Making the best move found
        if (move != -1) {
            makeMove(move);
        }
    }

    // Recursive function for the minimax algorithm, used to determine the best move for the AI
    private int minimax(char[] boardState, int depth, boolean isMaximizing) {
        char winner = checkWinner();
        if (winner == 'O') return 10 - depth;
        if (winner == 'X') return depth - 10;
        if (IntStream.range(0, 9).noneMatch(i -> board[i] == '\0')) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            // Simulate the AI's possible moves
            for (int i = 0; i < 9; i++) {
                if (boardState[i] == '\0') {
                    boardState[i] = 'O';
                    int score = minimax(boardState, depth + 1, false);
                    boardState[i] = '\0';
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            // Simulate the player's possible moves
            for (int i = 0; i < 9; i++) {
                if (boardState[i] == '\0') {
                    boardState[i] = 'X';
                    int score = minimax(boardState, depth + 1, true);
                    boardState[i] = '\0';
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    // Main function to run the Tic Tac Toe game
    public static void main(String[] args) {
        new TicTacToe();
    }
}
