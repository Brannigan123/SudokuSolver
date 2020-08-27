package hy.sudoku.board;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import hy.sudoku.utils.SudokuLoader;

public class SudokuBoard implements Cloneable {
    public final int                        boardLength;
    public final int                        blockLength;

    public final int                        start;

    private final int[][]                   puzzle;
    private final int[][]                   entries;

    private final Set<SudokuActionListener> listeners;

    public SudokuBoard(int blockSize) { this(blockSize, 0); }

    public SudokuBoard(int blockSize, int start) {
        this.boardLength = blockSize * blockSize;
        this.blockLength = blockSize;
        this.start = start;
        puzzle = new int[boardLength][boardLength];
        entries = new int[boardLength][boardLength];
        listeners = new LinkedHashSet<>();
    }

    public static SudokuBoard getDefault() {
        String text = "" + //
            "8 0 0 0 0 0 0 0 0\r\n" + //
            "0 0 3 6 0 0 0 0 0\r\n" + //
            "0 7 0 0 9 0 2 0 0\r\n" + //
            "0 5 0 0 0 7 0 0 0\r\n" + //
            "0 0 0 0 4 5 7 0 0\r\n" + //
            "0 0 0 1 0 0 0 3 0\r\n" + //
            "0 0 1 0 0 0 0 6 8\r\n" + //
            "0 0 8 5 0 0 0 1 0\r\n" + //
            "0 9 0 0 0 0 4 0 0";
        return SudokuLoader.readBoardFromString(text);
    }

    // public int[][] getPuzzle() { return puzzle; }
    //
    // public int[][] getEntries() { return entries; }

    public void addListener(SudokuActionListener listener) {
        if (Objects.nonNull(listener)) listeners.add(listener);
    }

    public void removeListener(SudokuActionListener listener) {
        if (Objects.nonNull(listener)) listeners.remove(listener);
    }

    public void setPuzzle(SudokuBoard puzzle) {
        setPuzzle(Objects.requireNonNull(puzzle, "Provided puzzle is null").puzzle);
    }

    public void setPuzzle(int[][] puzzle) {
        for (int r = 0; r < boardLength; r++) {
            for (int c = 0; c < boardLength; c++) {
                int fr = r, fc = c, value = puzzle[r][c];
                if (value != entries[fr][fc]) {
                    this.puzzle[fr][fc] = puzzle[fr][fc];
                    this.entries[fr][fc] = puzzle[fr][fc];
                    listeners.forEach(listener -> listener.onChange(fr, fc, value));
                }
            }
        }
    }

    public void reset() {
        for (int r = 0; r < boardLength; r++) {
            for (int c = 0; c < boardLength; c++) {
                int fr = r, fc = c, value = puzzle[r][c];
                if (value == start) {
                    entries[fr][fc] = value;
                    listeners.forEach(listener -> listener.onChange(fr, fc, value));
                }
            }
        }
    }

    public int get(int row, int column) { return entries[row][column]; }

    public void set(int row, int column, int value) {
        entries[row][column] = value;
        listeners.forEach(listener -> listener.onChange(row, column, value));
    }

    public boolean wasProvided(int row, int column) { return puzzle[row][column] != start; }

    public boolean wasNotProvided(int row, int column) { return puzzle[row][column] == start; }

    public boolean isBlank(int row, int column) { return entries[row][column] == start; }

    public boolean isFilled(int row, int column) { return entries[row][column] != start; }

    public boolean isLegal(int row, int column) {
        int value = entries[row][column];
        for (int r = 0; r < boardLength; r++) { if (r != row && entries[r][column] == value) return false; }
        for (int c = 0; c < boardLength; c++) { if (c != column && entries[row][c] == value) return false; }
        int rowBlock = row / blockLength;
        int columnBlock = column / blockLength;
        for (int r = rowBlock * blockLength; r < (rowBlock + 1) * blockLength; r++) {
            for (int c = columnBlock * blockLength; c < (columnBlock + 1) * blockLength; c++) {
                if (r != row && c != column && entries[r][c] == value) return false;
            }
        }
        return true;
    }

    @Override
    public SudokuBoard clone() {
        SudokuBoard board = new SudokuBoard(blockLength, start);
        for (int r = 0; r < boardLength; r++) {
            for (int c = 0; c < boardLength; c++) {
                board.puzzle[r][c] = puzzle[r][c];
                board.entries[r][c] = entries[r][c];
            }
        }
        return board;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < boardLength; r++) {
            int[] row = entries[r];
            for (int c = 0; c < boardLength; c++) {
                int value = row[c];
                sb.append(value == start ? " " : Integer.toString(value));
                sb.append(c % blockLength == blockLength - 1 ? "  " : " ");
            }
            sb.append(r % blockLength == blockLength - 1 ? "\n\n" : "\n");
        }
        return sb.toString();
    }

}
