import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SudokuSolver extends JFrame {

    private JTextField[][] cells = new JTextField[9][9];

    private JButton solveButton;
    private JButton clearButton;
    private JButton exampleButton;

    private JLabel statusLabel;

    public SudokuSolver() {

        setTitle("SkillCraft Technology - Sudoku Solver");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel(
                "SUDOKU SOLVER",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(
                BorderFactory.createEmptyBorder(15, 5, 15, 5)
        );

        add(title, BorderLayout.NORTH);

        // Create Sudoku grid
        JPanel mainGrid = new JPanel(
                new GridLayout(3, 3, 3, 3)
        );

        mainGrid.setBackground(Color.BLACK);

        for (int boxRow = 0; boxRow < 3; boxRow++) {

            for (int boxCol = 0; boxCol < 3; boxCol++) {

                JPanel box = new JPanel(
                        new GridLayout(3, 3)
                );

                box.setBorder(
                        new LineBorder(Color.BLACK, 2)
                );

                for (int row = 0; row < 3; row++) {

                    for (int col = 0; col < 3; col++) {

                        int r = boxRow * 3 + row;
                        int c = boxCol * 3 + col;

                        cells[r][c] = new JTextField();

                        cells[r][c].setHorizontalAlignment(
                                JTextField.CENTER
                        );

                        cells[r][c].setFont(
                                new Font("Arial", Font.BOLD, 22)
                        );

                        cells[r][c].setBorder(
                                new LineBorder(Color.GRAY, 1)
                        );

                        box.add(cells[r][c]);
                    }
                }

                mainGrid.add(box);
            }
        }

        JPanel gridContainer = new JPanel(
                new GridBagLayout()
        );

        gridContainer.add(mainGrid);

        add(gridContainer, BorderLayout.CENTER);

        // Create buttons
        solveButton = new JButton("Solve Sudoku");
        clearButton = new JButton("Clear");
        exampleButton = new JButton("Load Example");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exampleButton);

        // Status label
        statusLabel = new JLabel(
                "Enter Sudoku numbers and click Solve.",
                SwingConstants.CENTER
        );

        statusLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        JPanel bottomPanel = new JPanel(
                new BorderLayout()
        );

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 5, 15, 5)
        );

        add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        solveButton.addActionListener(e -> solveSudoku());

        clearButton.addActionListener(e -> clearGrid());

        exampleButton.addActionListener(e -> loadExample());
    }

    // Read and solve Sudoku
    private void solveSudoku() {

        int[][] board = new int[9][9];

        // Read values from input grid
        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                String value = cells[row][col]
                        .getText().trim();

                if (value.isEmpty()) {

                    board[row][col] = 0;

                } else if (value.matches("[1-9]")) {

                    board[row][col] =
                            Integer.parseInt(value);

                } else {

                    showError(
                            "Enter only numbers from 1 to 9."
                    );

                    return;
                }
            }
        }

        // Check whether the puzzle is valid
        if (!isValidBoard(board)) {

            showError(
                    "Invalid Sudoku! Duplicate numbers found."
            );

            return;
        }

        statusLabel.setText("Solving Sudoku...");

        // Solve puzzle
        if (solve(board)) {

            // Display completed puzzle
            for (int row = 0; row < 9; row++) {

                for (int col = 0; col < 9; col++) {

                    cells[row][col].setText(
                            String.valueOf(board[row][col])
                    );
                }
            }

            statusLabel.setText(
                    "Sudoku solved successfully!"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Sudoku solved successfully!"
            );

        } else {

            statusLabel.setText(
                    "No solution exists."
            );

            showError(
                    "This Sudoku puzzle has no solution."
            );
        }
    }

    // Backtracking algorithm
    private boolean solve(int[][] board) {

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                if (board[row][col] == 0) {

                    for (int number = 1;
                         number <= 9;
                         number++) {

                        if (isSafe(
                                board, row, col, number)) {

                            board[row][col] = number;

                            if (solve(board)) {
                                return true;
                            }

                            // Backtracking
                            board[row][col] = 0;
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    // Check row, column and 3x3 box
    private boolean isSafe(
            int[][] board,
            int row,
            int col,
            int number) {

        // Check row
        for (int i = 0; i < 9; i++) {

            if (board[row][i] == number) {
                return false;
            }
        }

        // Check column
        for (int i = 0; i < 9; i++) {

            if (board[i][col] == number) {
                return false;
            }
        }

        // Check 3x3 box
        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                if (board[startRow + i][startCol + j]
                        == number) {

                    return false;
                }
            }
        }

        return true;
    }

    // Validate initial Sudoku
    private boolean isValidBoard(int[][] board) {

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                int number = board[row][col];

                if (number != 0) {

                    board[row][col] = 0;

                    boolean valid = isSafe(
                            board, row, col, number
                    );

                    board[row][col] = number;

                    if (!valid) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // Clear Sudoku grid
    private void clearGrid() {

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                cells[row][col].setText("");
            }
        }

        statusLabel.setText(
                "Grid cleared. Enter a new puzzle."
        );
    }

    // Load sample Sudoku puzzle
    private void loadExample() {

        int[][] example = {

            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},

            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},

            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                if (example[row][col] == 0) {

                    cells[row][col].setText("");

                } else {

                    cells[row][col].setText(
                            String.valueOf(example[row][col])
                    );
                }
            }
        }

        statusLabel.setText(
                "Example Sudoku puzzle loaded."
        );
    }

    // Display error message
    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Sudoku Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            SudokuSolver app = new SudokuSolver();

            app.setVisible(true);
        });
    }
}