import java.util.Scanner;
import java.util.ArrayList;
public class SudokuSolver {
    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle();
        Scanner in = new Scanner(System.in);
        // int[] easy = {19300082, 800149, 85104000, 1037820, 932000010, 500060403, 800700231, 190000700, 700618000};
        // int[] medium = {800312040, 200040000, 6000500, 590000, 20000051, 60000000, 400000030, 1780000, 80000906};
        // int[] hard = {960000800, 50000, 1400000, 200, 250006000, 39105000, 8502, 310000400, 6000013};

        for (int x = 0; x < 9; x++) {
            System.out.print("Please input the numbers of row " + (x+1) + " with 0's for the blank spaces (Ex: 700000100): ");
            puzzle.input(in.nextInt(), x);
            in.nextLine();
            //puzzle.input(medium[x], x);
        }
        puzzle.fillDraft();
        int x = 0;
        do {
            puzzle.scanPuzzle();
            x++;
        } while (!(puzzle.getSolved()) && x < 30);
        puzzle.output();
    }
}
// same number of unique elements as boxes they take up
