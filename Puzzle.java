import java.util.ArrayList;
import java.util.HashSet;

public class Puzzle {
    int[][][][] puzzle;
    int[][] countOfEach;
    private boolean solved;

    Puzzle() {
        puzzle = new int[3][3][3][3];
        countOfEach = new int[10][9];
        solved = false;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public boolean getSolved() {
        return solved;
    }

    public void input(int rowNumbers, int row) {
        int number = 100000000;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                puzzle[row/3][x][row%3][z] = rowNumbers / number;
                rowNumbers %= number;
                number /= 10;
            }
        }
    }

    public void output() {
        System.out.printf("%n-------------------------------------%n");
        for (int w = 0; w < 3; w++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        System.out.printf("[%s] %s", (puzzle[w][x][y][z] == 0) ? " " : puzzle[w][x][y][z], (x < 2 && z == 2) ? "|":"");
                    }
                }
                System.out.printf("%n-------------------------------------%n");
            }
        }
    }

    public void fillDraft() {
        for (int w = 0; w < 3; w++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        if(puzzle[w][x][y][z] == 0)
                            puzzle[w][x][y][z] = 123456789;
                    }
                }
            }
        }
    }

    public void scanPuzzle() {
        for (int w = 0; w < 3; w++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        if (checkSolved() || !checkCanSolve()) {
                            break;
                        }
                        if(puzzle[w][x][y][z] < 10) {
                            eliminateNumberinRow(puzzle[w][x][y][z], w, y);
                            eliminateNumberinColumn(puzzle[w][x][y][z], x, z);
                            eliminateNumberin3By3(puzzle[w][x][y][z], w, x);
                        } else {
                            for (int c = 1; c < 10; c++) {
                                if (Integer.toString(puzzle[w][x][y][z]).indexOf(Integer.toString(c)) != -1) {
                                    countOfEach[0][c-1] += 1;
                                    countOfEach[z + 1 + x * 3][c-1] += 1;
                                }
                            }
                        }
                    }
                }
                setOnlyOneinRow(w, y);
                sortRow(w, y);
            }
        }
        sortColumnAnd3By3();
        setOnlyOneinCol();
    }

    public void eliminateNumberInBox(int eliminate, int w, int x, int y, int z) {
        if (Integer.toString(puzzle[w][x][y][z]).indexOf(Integer.toString(eliminate)) != -1) {
            String first = Integer.toString(puzzle[w][x][y][z]).substring(0, Integer.toString(puzzle[w][x][y][z]).indexOf(Integer.toString(eliminate)));
            String last = Integer.toString(puzzle[w][x][y][z]).substring(Integer.toString(puzzle[w][x][y][z]).indexOf(Integer.toString(eliminate)) + 1);
            puzzle[w][x][y][z] = Integer.valueOf(first + last);
        }
    }

    public void eliminateNumberinRow(int eliminate, int w, int y) {
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (puzzle[w][x][y][z] > 9) {
                    eliminateNumberInBox(eliminate, w, x, y, z);
                }
            }
        }
    }

    public void eliminateNumberinColumn(int eliminate, int x, int z) {
        for (int w = 0; w < 3; w++) {
            for (int y = 0; y < 3; y++) {
                if (puzzle[w][x][y][z] > 9) {
                    eliminateNumberInBox(eliminate, w, x, y, z);
                }
            }
        }
    }

    public void eliminateNumberin3By3(int eliminate, int w, int x) {
        for (int y = 0; y < 3; y++) {
            for (int z = 0; z < 3; z++) {
                if(puzzle[w][x][y][z] > 9) {
                    eliminateNumberInBox(eliminate, w, x, y, z);
                }
            }
        }
    }

    private void eliminateRowWithoutSafety(HashSet<Integer> safe, int eliminate, int w, int y) {
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (safe.contains(z + x * 3) || puzzle[w][x][y][z] < 10) {
                    // do nothing
                } else {
                    for (int l = 0; l < Integer.toString(eliminate).length(); l++) {
                        eliminateNumberInBox(Integer.valueOf(Integer.toString(eliminate).substring(l, l+1)), w, x, y, z);
                    }
                }
            }
        }
    }

    private void eliminateColumnWithoutSafety(HashSet<Integer> safe, int eliminate, int x, int z) {
        for (int w = 0; w < 3; w++) {
            for (int y = 0; y < 3; y++) {
                if (safe.contains(y + w * 3) || puzzle[w][x][y][z] < 10) {
                    // do nothing
                } else {
                    for (int l = 0; l < Integer.toString(eliminate).length(); l++) {
                        eliminateNumberInBox(Integer.valueOf(Integer.toString(eliminate).substring(l, l+1)), w, x, y, z);
                    }
                }
            }
        }
    }

    private void eliminateR3By3WithoutSafety(HashSet<Integer> safe, int eliminate, int w, int x) {
        for (int y = 0; y < 3; y++) {
            for (int z = 0; z < 3; z++) {
                if (safe.contains(z + y * 3) || puzzle[w][x][y][z] < 10) {
                    // do nothing
                } else {
                    for (int l = 0; l < Integer.toString(eliminate).length(); l++) {
                        eliminateNumberInBox(Integer.valueOf(Integer.toString(eliminate).substring(l, l+1)), w, x, y, z);
                    }
                }
            }
        }
    }


    public void setOnlyOneinRow(int w, int y) {
        for (int c = 0; c < 9; c++) {
            if (countOfEach[0][c] == 1) {
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        if (Integer.toString(puzzle[w][x][y][z]).indexOf(Integer.toString(c+1)) != -1) {
                            puzzle[w][x][y][z] = c + 1;
                        }
                    }
                }
            }
        }
        clearCountRow();
    }

    public void setOnlyOneinCol() {
        for (int r = 1; r < 10; r++) {
            for (int c = 0; c < 9; c++) {
                if (countOfEach[r][c] == 1) {
                    for (int w = 0; w < 3; w++) {
                        for (int y = 0; y < 3; y++) {
                            if (Integer.toString(puzzle[w][(r-1)/3][y][(r-1)%3]).indexOf(Integer.toString(c+1)) != -1) {
                                puzzle[w][(r-1)/3][y][(r-1)%3] = c + 1;
                            }
                        }
                    }
                }
            }
        }
        clearCount();
    }
    // ONLY ONES IN 3 BY 3
    // TODO: COMBINE CHECKSOLVED WITH CANCHECKSOLVE
    public boolean checkSolved() {
        for (int w = 0; w < 3; w++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        if(puzzle[w][x][y][z] > 9) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    // TODO: IMPLEMENT THIS
    public boolean checkCanSolve() {
        for (int w = 0; w < 3; w++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        if(puzzle[w][x][y][z] == 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void clearCountRow() {
        for(int c = 0; c < 9; c++) {
            countOfEach[0][c] = 0;
        }
    }

    public void clearCount() {
        for(int r = 0; r < 9; r++) {
            for(int c = 0; c < 9; c++) {
                countOfEach[r][c] = 0;
            }
        }
    }

    public void sortRow(int w, int y) {
        int[] sorted = new int[9];
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        int singles = 0;

        for (int x = 0; x < sorted.length; x++) {
            sorted[x] = puzzle[w][x/3][y][x%3];
        }

        mergeSort(sorted, indices, 0, sorted.length - 1);

        for (int x = 0; x < sorted.length - 1; x++) {
            HashSet<Integer> count = new HashSet<Integer>();
            HashSet<Integer> safety = new HashSet<Integer>();
            if (sorted[x] < 10) {
                singles++;
            } else {
                if (Integer.toString(sorted[x]).length() != 9 - singles) {
                    for (int a = 0; a < Integer.toString(sorted[x]).length(); a++) {
                        count.add(Integer.valueOf(Integer.toString(sorted[x]).substring(a, a+1)));
                    }
                    safety.add(indices[x]);
                    for (int z = x + 1; z < sorted.length; z++) {
                        for (int a = 0; a < Integer.toString(sorted[z]).length(); a++) {
                            count.add(Integer.valueOf(Integer.toString(sorted[z]).substring(a, a+1)));
                        }
                        safety.add(indices[z]);
                        if (count.size() == safety.size() && count.size() != 9 - singles) {
                            for (int i: count) {
                                eliminateRowWithoutSafety(safety, i, w, y);
                            }
                        }
                    }
                }
            }
        }
    }

    public void sortColumnAnd3By3() {
        int[] sorted = new int[9];

        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                for (int w = 0; w < 3; w++) {
                    for (int y = 0; y < 3; y++) {
                        sorted[y + w * 3] = puzzle[w][x][y][z];
                    }
                    sort3By3(w, x);
                }
                int singles = 0;
                int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8};

                mergeSort(sorted, indices, 0, sorted.length - 1);

                for (int a = 0; a < sorted.length - 1; a++) {
                    HashSet<Integer> count = new HashSet<Integer>();
                    HashSet<Integer> safety = new HashSet<Integer>();
                    if (sorted[a] < 10) {
                        singles++;
                    } else {
                        if (Integer.toString(sorted[a]).length() != 9 - singles) {
                            for (int b = 0; b < Integer.toString(sorted[a]).length(); b++) {
                                count.add(Integer.valueOf(Integer.toString(sorted[a]).substring(b, b + 1)));
                            }
                            safety.add(indices[a]);
                            for (int c = a + 1; c < sorted.length; c++) {
                                for (int b = 0; b < Integer.toString(sorted[c]).length(); b++) {
                                    count.add(Integer.valueOf(Integer.toString(sorted[c]).substring(b, b + 1)));
                                }
                                safety.add(indices[c]);
                                if (count.size() == safety.size() && count.size() != 9 - singles) {
                                    for (int i: count) {
                                        eliminateColumnWithoutSafety(safety, i, x, z);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void sort3By3(int w, int x) {
        int[] sorted = new int[9];
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        int singles = 0;

        for (int y = 0; y < sorted.length; y++) {
            sorted[y] = puzzle[w][x][y/3][y%3];
        }

        mergeSort(sorted, indices, 0, sorted.length - 1);

        for (int a = 0; a < sorted.length - 1; a++) {
            HashSet<Integer> count = new HashSet<Integer>();
            HashSet<Integer> safety = new HashSet<Integer>();
            if (sorted[a] < 10) {
                singles++;
            } else {
                if (Integer.toString(sorted[a]).length() != 9 - singles) {
                    for (int b = 0; b < Integer.toString(sorted[a]).length(); b++) {
                        count.add(Integer.valueOf(Integer.toString(sorted[a]).substring(b, b + 1)));
                    }
                    safety.add(indices[a]);
                    for (int z = a + 1; z < sorted.length; z++) {
                        for (int b = 0; b < Integer.toString(sorted[z]).length(); b++) {
                            count.add(Integer.valueOf(Integer.toString(sorted[z]).substring(b, b + 1)));
                        }
                        safety.add(indices[z]);
                        if (count.size() == safety.size() && count.size() != 9 - singles) {
                            for (int i: count) {
                                eliminateR3By3WithoutSafety(safety, i, w, x);
                            }
                        }
                    }
                }
            }
        }
    }

    // private void eliminateOnlyInRowIn3By3()
    // private void eliminateOnlyInColoumnIn3By3()

    public void mergeSort(int[] toSort, int[] indices, int left, int right) {
        if (left < right) {
            int mid = (right - left)/2 + left;

            mergeSort(toSort, indices, left, mid);
            mergeSort(toSort, indices, mid + 1, right);

            merge(toSort, indices, left, mid, right);
        }
    }

    public void merge(int[] toSort, int[] indices, int left, int mid, int right) {
        int[] leftArray = new int[mid - left + 1];
        int[] leftIndex = new int[mid - left + 1];
        int[] rightArray = new int[right - mid];
        int[] rightIndex = new int[right - mid];

        for (int x = 0; x < leftArray.length; x++) {
            leftArray[x] = toSort[left + x];
            leftIndex[x] = indices[left + x];
        }

        for (int x = 0; x < rightArray.length; x++) {
            rightArray[x] = toSort[mid + 1 + x];
            rightIndex[x] = indices[mid + 1 + x];
        }

        int i = 0, j = 0;
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                toSort[left + i + j] = leftArray[i];
                indices[left + i + j] = leftIndex[i];
                i++;
            } else {
                toSort[left + i + j] = rightArray[j];
                indices[left + i + j] = rightIndex[j];
                j++;
            }
        }

        while (i < leftArray.length) {
            toSort[left + i + j] = leftArray[i];
            indices[left + i + j] = leftIndex[i];
            i++;
        }

        while (j < rightArray.length) {
            toSort[left + i + j] = rightArray[j];
            indices[left + i + j] = rightIndex[j];
            j++;
        }

    }
}
