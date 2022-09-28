import java.util.Scanner;
import java.util.ArrayList;
public class SudokuSolver {
    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle();
        Scanner in = new Scanner(System.in);

        for (int x = 0; x < 9; x++) {
            System.out.print("Please input the numbers of row " + (x+1) + " with 0's for the blank spaces (Ex: 700000100): ");
            puzzle.input(in.nextInt(), x);
            in.nextLine();
        }
        puzzle.fillDraft();
        do {
            puzzle.scanPuzzle();
        } while (!(puzzle.getSolved()));
        puzzle.output();

    }
}
