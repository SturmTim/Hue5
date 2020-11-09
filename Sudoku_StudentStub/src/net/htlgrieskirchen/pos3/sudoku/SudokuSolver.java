package net.htlgrieskirchen.pos3.sudoku;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Please enter here an answer to task four between the tags:
 * <answerTask6>
 *
 * </answerTask6>
 */
public class SudokuSolver implements ISodukoSolver {

    public SudokuSolver() {

    }

    @Override
    public final int[][] readSudoku(File file) {
        int[][] result = new int[9][9];
        AtomicInteger j = new AtomicInteger();

        try {
            if (!file.canRead() || !file.isFile()) {
                System.out.println("ERROR!");
                System.exit(0);
            } else {

                Files.lines(file.toPath())
                        .forEach(string -> {
                            String[] nStrings = string.split(";");

                            for (int i = 0; i < result.length; i++) {
                                result[j.intValue()][i] = Integer.parseInt(nStrings[i]);

                            }
                            j.addAndGet(1);

                        });
            }

        } catch (IOException ex) {
            System.out.println("IOException");
        }
        return result;

    }

    public boolean checkSudokuParallel(int[][] rawSudoku) {
        ExecutorService executor = Executors.newCachedThreadPool();

        List<Callable<Boolean>> callableList = new ArrayList();
        callableList.add(new ColumnCallable(rawSudoku));
        callableList.add(new GridCallable(rawSudoku));
        callableList.add(new RowCallable(rawSudoku));
        boolean[] temp = new boolean[3];
        int i = 0;

        try {
            List<Future<Boolean>> result = executor.invokeAll(callableList);
            for (Future<Boolean> future : result) {
                temp[i] = future.get();
                i++;
            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(SudokuSolver.class.getName()).log(Level.SEVERE, null, ex);
        }

        executor.shutdown();
        return temp[0] && temp[1] && temp[2];
    }

    @Override
    public boolean checkSudoku(int[][] unsolvedSudoku) {
        for (int col2 = 0; col2 < 9; col2++) {
            for (int row2 = 0; row2 < 8; row2++) {
                if (unsolvedSudoku[row2][col2] == unsolvedSudoku[row2 + 1][col2]) {
                    return false;
                }
            }
        }

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 8; column++) {
                if (unsolvedSudoku[row][column] == unsolvedSudoku[row][column + 1]) {
                    return false;
                }
            }
        }

        for (int row = 0; row < 9; row += 3) {
            for (int column = 0; column < 9; column += 3) {
                for (int position1 = 0; position1 < 8; position1++) {
                    for (int position2 = position1 + 1; position2 < 9; position2++) {
                        if (unsolvedSudoku[row + position1 % 3][column + position1 / 3] == unsolvedSudoku[row + position2 % 3][column + position2 / 3]) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {
        return null;
//        ExecutorService executor = Executors.newCachedThreadPool();
//
//        List<Callable<Boolean>> callableList = new ArrayList();
//        for (int i = 0; i < 9; i++) {
//            callableList.add(new SolverCallable(rawSudoku, i, 0));
//        }
//
//        try {
//            executor.invokeAll(callableList);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(SudokuSolver.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        executor.shutdown();
    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {
        solveSuduko(rawSudoku, 0, 0);
        return rawSudoku;
    }

    static boolean solveSuduko(int[][] rawSudoku, int row, int col) {
        int number = 9;
        if (row == number - 1 && col == number) {
            return true;
        }
        if (col == number) {
            row++;
            col = 0;
        }

        if (rawSudoku[row][col] != 0) {
            return solveSuduko(rawSudoku, row, col + 1);
        }

        for (int possibleNumber = 1; possibleNumber <= 9; possibleNumber++) {

            if (isSolved(rawSudoku, row, col, possibleNumber)) {
                rawSudoku[row][col] = possibleNumber;
                if (solveSuduko(rawSudoku, row, col + 1)) {
                    return true;
                }
            }

            rawSudoku[row][col] = 0;
        }
        return false;
    }

    // add helper methods here if necessary
    static boolean isSolved(int[][] rawSudoku, int row, int col, int num) {

        for (int x = 0; x <= 8; x++) {
            if (rawSudoku[row][x] == num) {
                return false;
            }
        }

        for (int x = 0; x <= 8; x++) {
            if (rawSudoku[x][col] == num) {
                return false;
            }
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (rawSudoku[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public long benchmark(int[][] rawSudoku) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            checkSudoku(rawSudoku);
            solveSudoku(rawSudoku);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public long benchmarkParallel(int[][] rawSudoku) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            checkSudokuParallel(rawSudoku);
            solveSudokuParallel(rawSudoku);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public void printSudoku(int[][] input) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');

        for (int i = 0; i <= 9; i++) {
            stringBuilder.append("- - ");
        }
        stringBuilder.append('\n');

        int rows = 1;
        int colums = 1;
        String string;

        for (int i = 0; i < input.length; i++) {

            for (int j = 0; j < input.length; j++) {

                if (rows % 3 == 0) {
                    string = " | ";
                    rows++;

                } else {
                    string = " : ";
                    rows++;
                }
                if (input[i][j] == 0) {
                    System.out.print(" " + string);
                } else {
                    System.out.print(input[i][j] + string);
                }
            }

            if (colums % 3 == 0) {
                string = stringBuilder.toString();
                string = string.substring(0, string.length() - 5);
                colums++;
            } else {
                string = "";
                colums++;
            }
            System.out.println(string);
        }
    }

}
