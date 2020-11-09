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
public class RowCallable implements Callable<Boolean> {

    private final int[][] sudoku;

    public RowCallable(int[][] sudoku) {
        this.sudoku = sudoku;

    }

    @Override
    public Boolean call() throws Exception {
        //quelle: https://stackoverflow.com/questions/34076389/java-sudoku-solution-verifier
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 8; column++) {
                if (sudoku[row][column] == sudoku[row][column + 1]) {
                    return false;
                }
            }
        }
        return true;
    }

}
