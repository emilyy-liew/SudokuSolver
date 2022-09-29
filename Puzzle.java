public class Puzzle {
    static int[][][][] puzzle;
    static int[][] countOfEach;
    private static boolean solved;

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

    public static void input(int rowNumbers, int row) {
        int number = 100000000;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                puzzle[row/3][x][row%3][z] = rowNumbers / number;
                rowNumbers %= number;
                number /= 10;
            }
        }
    }

    public static void output() {
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

    public static void fillDraft() {
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

    public static void scanPuzzle() {
        for (int w = 0; w < 3; w++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        solved = checkSolved();
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
            }
        }
        setOnlyOneinCol();
    }

    public static void eliminateNumberInBox(int eliminate, int w, int x, int y, int z) {
        if (Integer.toString(puzzle[w][x][y][z]).indexOf(Integer.toString(eliminate)) != -1) {
            puzzle[w][x][y][z] = Integer.valueOf(Integer.toString(puzzle[w][x][y][z]).substring(0, Integer.toString(puzzle[w][x][y][z]).indexOf(Integer.toString(eliminate))) + Integer.toString(puzzle[w][x][y][z]).substring(Integer.toString(puzzle[w][x][y][z]).indexOf(Integer.toString(eliminate)) + 1));
        }
    }

    public static void eliminateNumberinRow(int eliminate, int w, int y) {
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (puzzle[w][x][y][z] > 9) {
                    eliminateNumberInBox(eliminate, w, x, y, z);
                }
            }
        }
    }

    public static void eliminateNumberinColumn(int eliminate, int x, int z) {
        for (int w = 0; w < 3; w++) {
            for (int y = 0; y < 3; y++) {
                if (puzzle[w][x][y][z] > 9) {
                    eliminateNumberInBox(eliminate, w, x, y, z);
                }
            }
        }
    }

    public static void eliminateNumberin3By3(int eliminate, int w, int x) {
        for (int y = 0; y < 3; y++) {
            for (int z = 0; z < 3; z++) {
                if(puzzle[w][x][y][z] > 9) {
                    eliminateNumberInBox(eliminate, w, x, y, z);
                }
            }
        }
    }

    public static void setOnlyOneinRow(int w, int y) {
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

    public static void setOnlyOneinCol() {
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

    public static boolean checkSolved() {
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

    public static void clearCountRow() {
        for(int c = 0; c < 9; c++) {
            countOfEach[0][c] = 0;
        }
    }

    public static void clearCount() {
        for(int r = 0; r < 9; r++) {
            for(int c = 0; c < 9; c++) {
                countOfEach[r][c] = 0;
            }
        }
    }
    // sort
}
