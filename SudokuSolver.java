import java.util.Scanner;

public class SudokuSolver {

    private static final int SIZE = 9;
    private static final int EMPTY = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int[][] grid = readGrid(scanner);

        if (!isValidInitialGrid(grid)) {
            System.out.println("\nThe puzzle has conflicting numbers and cannot be solved.");
            scanner.close();
            return;
        }

        System.out.println("\nUnsolved puzzle:");
        printGrid(grid);

        if (solve(grid)) {
            System.out.println("\nSolved puzzle:");
            printGrid(grid);
        } else {
            System.out.println("\nThis puzzle has no solution.");
        }

        scanner.close();
    }

    /** Reads the 9x9 grid from the user, one row at a time. */
    private static int[][] readGrid(Scanner scanner) {
        int[][] grid = new int[SIZE][SIZE];

        System.out.println("Enter the puzzle row by row.");
        System.out.println("Separate the 9 values with spaces. Use 0 or . for empty cells.");
        System.out.println("Example: 5 3 0 0 7 0 0 0 0\n");

        for (int row = 0; row < SIZE; row++) {
            while (true) {
                System.out.print("Row " + (row + 1) + ": ");
                String[] tokens = scanner.nextLine().trim().split("\\s+");

                if (tokens.length != SIZE) {
                    System.out.println("Please enter exactly " + SIZE + " values.");
                    continue;
                }

                int[] values = new int[SIZE];
                boolean valid = true;

                for (int col = 0; col < SIZE; col++) {
                    Integer value = parseCell(tokens[col]);
                    if (value == null) {
                        System.out.println("Invalid value '" + tokens[col] + "'. Use digits 1-9, or 0 / . for empty.");
                        valid = false;
                        break;
                    }
                    values[col] = value;
                }

                if (valid) {
                    grid[row] = values;
                    break;
                }
            }
        }
        return grid;
    }

    /** Converts one token to a cell value (0 for empty), or null if it is invalid. */
    private static Integer parseCell(String token) {
        if (token.equals(".")) {
            return EMPTY;
        }
        try {
            int value = Integer.parseInt(token);
            return (value >= 0 && value <= 9) ? value : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Checks that no given number breaks the Sudoku rules. */
    private static boolean isValidInitialGrid(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int value = grid[row][col];
                if (value != EMPTY) {
                    grid[row][col] = EMPTY;
                    boolean safe = isSafe(grid, row, col, value);
                    grid[row][col] = value;
                    if (!safe) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Fills the grid using backtracking. Returns true if a solution is found. */
    private static boolean solve(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == EMPTY) {
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (solve(grid)) {
                                return true;
                            }
                            grid[row][col] = EMPTY; // undo and try the next number
                        }
                    }
                    return false; // no number fits this cell
                }
            }
        }
        return true; // no empty cells left
    }

    /** Returns true if num can be placed at (row, col) without breaking any rule. */
    private static boolean isSafe(int[][] grid, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;
            }
        }

        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[boxRow + i][boxCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printGrid(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("------+-------+------");
            }
            for (int col = 0; col < SIZE; col++) {
                if (col % 3 == 0 && col != 0) {
                    System.out.print("| ");
                }
                System.out.print(grid[row][col] == EMPTY ? ". " : grid[row][col] + " ");
            }
            System.out.println();
        }
    }
}