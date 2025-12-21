import Models.Board;
import Models.Game;
import Models.enums.DifficultyLevel;
import generator.GameDriver;
import verification.SudokuVerifier;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Testing Full Game Generation Flow ===\n");

        try {
            // Create a valid solved Sudoku board
            int[][] solvedBoard = {
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {4, 5, 6, 7, 8, 9, 1, 2, 3},
                    {7, 8, 9, 1, 2, 3, 4, 5, 6},
                    {2, 3, 4, 5, 6, 7, 8, 9, 1},
                    {5, 6, 7, 8, 9, 1, 2, 3, 4},
                    {8, 9, 1, 2, 3, 4, 5, 6, 7},
                    {3, 4, 5, 6, 7, 8, 9, 1, 2},
                    {6, 7, 8, 9, 1, 2, 3, 4, 5},
                    {9, 1, 2, 3, 4, 5, 6, 7, 8}
            };

            System.out.println("1. Creating Board...");
            Board board = new Board(solvedBoard);

            System.out.println("2. Creating Game...");
            Game game = new Game(board, DifficultyLevel.EASY);

            System.out.println("3. Creating GameDriver...");
            SudokuVerifier verifier = new SudokuVerifier();
            GameDriver driver = new GameDriver(verifier);

            System.out.println("4. Generating games...");
            driver.generateGames(game);

            System.out.println("\n✅ SUCCESS: Games generated!");

        } catch (Exception e) {
            System.err.println("\n❌ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}