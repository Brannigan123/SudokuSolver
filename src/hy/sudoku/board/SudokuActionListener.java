package hy.sudoku.board;

public interface SudokuActionListener {
    void onChange(int row, int column, int value);
}
