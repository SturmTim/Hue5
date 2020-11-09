/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.sudoku;

import java.util.concurrent.Callable;

/**
 *
 * @author timst
 */
public class ColumnCallable implements Callable<Boolean> {

    private final int[][] sudoku;

    public ColumnCallable(int[][] sudoku) {
        this.sudoku = sudoku;

    }

    @Override
    public Boolean call() throws Exception {
        //quelle: https://stackoverflow.com/questions/34076389/java-sudoku-solution-verifier
        for (int col2 = 0; col2 < 9; col2++) {
            for (int row2 = 0; row2 < 8; row2++) {
                if (sudoku[row2][col2] == sudoku[row2 + 1][col2]) {
                    return false;
                }
            }
        }
        return true;
    }

}
