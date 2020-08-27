package hy.sudoku.generator;

import java.util.Arrays;

import hy.sudoku.board.SudokuBoard;
import hy.sudoku.solver.SudokuSolver;
import hy.sudoku.solver.backtrack.BacktrackSudokuSolver;

public class SudokuGenerator {

    private static SudokuSolver solver = new BacktrackSudokuSolver();
    private static Mersenne     rand   = new Mersenne();

    public static SudokuBoard generate(int blockLength, int start) {
        SudokuBoard board = new SudokuBoard(blockLength);
        int boardLength = blockLength * blockLength;

        int rowDelta, colDelta;

        int[] used = new int[boardLength];
        int[][] mat = new int[boardLength][boardLength];

        for (int i = 0; i < boardLength; i++) {
            int value, offsetX, offsetY;
            do { value = Math.abs(rand.nextInt(boardLength)); } while (used[value] == blockLength);
            used[value]++;
            offsetX = (i / blockLength) * blockLength;
            offsetY = (i % blockLength) * blockLength;
            rowDelta = Math.abs(rand.nextInt(blockLength));
            colDelta = Math.abs(rand.nextInt(blockLength));
            mat[offsetX + rowDelta][offsetY + colDelta] = start + value + 1;

            System.err.println(Arrays.deepToString(mat));
        }

        board.setPuzzle(mat);
        System.out.println(board);
        solver.solve(board);

        for (int i = 0; i < 2 * boardLength; i++) {
            board.setPuzzle(mat);
            rowDelta = Math.abs(rand.nextInt(boardLength));
            colDelta = Math.abs(rand.nextInt(boardLength));
            if (mat[rowDelta][colDelta] == start) mat[rowDelta][colDelta] = start;
        }

        board.setPuzzle(mat);

        return board;
    }

}
