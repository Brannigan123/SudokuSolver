package hy.sudoku.solver.backtrack;

import java.util.Objects;

import hy.sudoku.board.SudokuBoard;
import hy.sudoku.solver.SudokuSolver;

public class BacktrackSudokuSolver implements SudokuSolver {

    public static void name() {
        
    }
    @Override
    public boolean solve(SudokuBoard board) {
        return trySolve(Objects.requireNonNull(board, "Provided board is null"));
    }

    private boolean trySolve(SudokuBoard board) {
        for (int row = 0; row < board.boardLength; row++) {
            for (int column = 0; column < board.boardLength; column++) {
                if (board.isBlank(row, column)) {
                    for (int v = board.start + 1; v <= board.boardLength; v++) {
                        board.set(row, column, v);
                        if (board.isLegal(row, column) && trySolve(board)) { return true; }
                        board.set(row, column, board.start);
                    }
                    return false;
                }
            }
        }
        return true;
    }

}
