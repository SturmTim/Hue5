package net.htlgrieskirchen.pos3.sudoku;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        SudokuSolver sudokuSolver = new SudokuSolver();
        SudokuSolver sudokuSolverP = new SudokuSolver();
        File file = new File("1_sudoku_level1.csv");
        int[][] sudoku = sudokuSolver.readSudoku(file);

        System.out.println(">----------- ORIGINAL -----------<");
        sudokuSolver.printSudoku(sudoku);

        int[][] solved = sudokuSolver.solveSudoku(sudoku);
//        sudokuSolverP.solveSudokuParallel(sudoku);
//        int[][] solvedParallel = sudokuSolverP.sudoku.getSudoku();

        System.out.println(">----------- SOLUTION -----------<");
        sudokuSolver.printSudoku(solved);
        System.out.println("SOLVED    = " + sudokuSolver.checkSudoku(solved));
        System.out.println(">------- SOLUTIONPARALLEL -------<");
//        sudokuSolverP.printSudoku(solvedParallel);
//        System.out.println("SOLVEDPARALLEL   = " + sudokuSolverP.checkSudokuParallel(solved));
        System.out.println("|--------------------------------|");
        System.out.println("Benchmark: " + sudokuSolver.benchmark(sudoku) + "ms");
//        System.out.println("BenchmarkParallel: " + sudokuSolver.benchmarkParallel(sudoku) + "ms");

    }
}
