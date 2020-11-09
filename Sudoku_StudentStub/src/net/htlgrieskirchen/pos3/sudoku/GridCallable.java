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
public class GridCallable implements Callable<Boolean> {

    private final int[][] sudoku;

    public GridCallable(int[][] sudoku) {
        this.sudoku = sudoku;

    }

    @Override
    public Boolean call() throws Exception {
        //quelle: https://stackoverflow.com/questions/34076389/java-sudoku-solution-verifier
        for (int row = 0; row < 9; row += 3) {
            for (int column = 0; column < 9; column += 3) {
                for (int position1 = 0; position1 < 8; position1++) {
                    for (int position2 = position1 + 1; position2 < 9; position2++) {
                        if (sudoku[row + position1 % 3][column + position1 / 3] == sudoku[row + position2 % 3][column + position2 / 3]) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

}
