package hy.sudoku.solver;

import java.awt.EventQueue;
import java.time.temporal.ChronoUnit;

import javax.swing.JFrame;

import com.formdev.flatlaf.FlatDarkLaf;

import hy.sudoku.board.SudokuBoard;
import hy.sudoku.solver.backtrack.BacktrackSudokuSolver;
import hy.sudoku.ui.SudokuPanel;
import hy.sudoku.utils.SudokuLoader;

public class ConsoleTest {
    static int numOfSteps = 0;

    public static void main(String[] args) {
        SudokuBoard board = SudokuLoader.readBoardFromConsole();
        board.addListener(ConsoleTest::onActionPerformed);

        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SudokuPanel panel = new SudokuPanel(board);
        panel.setDelay(2, ChronoUnit.MICROS);
        frame.add(panel);

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FlatDarkLaf.install();
                    frame.setLocationByPlatform(true);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        new BacktrackSudokuSolver().solve(board);
        System.out.printf("Solved in %d steps", numOfSteps);
    }

    public static void onActionPerformed(int row, int column, int value) { numOfSteps++; }
}
