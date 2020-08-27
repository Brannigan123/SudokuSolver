package hy.sudoku.solver;

import hy.sudoku.board.SudokuBoard;

public interface SudokuSolver {
    boolean solve(SudokuBoard board);
}