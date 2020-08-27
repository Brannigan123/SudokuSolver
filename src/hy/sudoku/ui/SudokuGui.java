package hy.sudoku.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatDarkLaf;

import hy.sudoku.board.SudokuBoard;
import hy.sudoku.solver.SudokuSolver;
import hy.sudoku.solver.backtrack.BacktrackSudokuSolver;
import hy.sudoku.utils.SudokuLoader;

public class SudokuGui {

    private SudokuSolver          sudokuSolver;

    private JFileChooser          fileChooser;

    private JFrame                frame;
    private JMenuBar              topBar;
    private JButton               sudokoLoadBtn;
    private JPanel                bottomPanel;
    private JTextField            delayField;
    private JButton               solveButton;
    private JLabel                delayLabel;
    private JPanel                sudokuPanel;
    private JPanel                solutionPanel;
    private JPanel                puzzlePanel;
    private JPanel                bottomLeftPanel;
    private JPanel                bottomRightPanel;
    private JLabel                solutionTitle;
    private JLabel                puzzleTitle;

    private JButton               resetButton;
    private JComboBox<ChronoUnit> delayUnitsCombo;
    private Component             horizontalStrut;
    private Component             horizontalStrut_1;
    private JTextField            sourcePathField;
    private Component             horizontalStrut_2;

    private SudokuPanel           puzzleView;
    private SudokuPanel           solutionView;
    private JLabel                sourceLabel;
    private Component             horizontalStrut_3;
    private JButton               showFileBrowserButton;
    private Component             horizontalStrut_4;

    public SudokuGui() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                FlatDarkLaf.install();
                initialize();
                frame.setLocation(80, 300);
                frame.setVisible(true);
            }
        });
    }

    private void initialize() {
        sudokuSolver = new BacktrackSudokuSolver();

        fileChooser = new JFileChooser(new File("sample").getAbsoluteFile());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        frame = new JFrame();
        frame.setTitle("Hy Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(4, 4));
        frame.setJMenuBar(getTopBar());
        frame.getContentPane().add(getBottomPanel(), BorderLayout.SOUTH);
        frame.getContentPane().add(getSudokuPanel(), BorderLayout.CENTER);
        frame.setMinimumSize(frame.getPreferredSize());
    }

    private JMenuBar getTopBar() {
        if (topBar == null) {
            topBar = new JMenuBar();
            topBar.setMargin(new Insets(4, 4, 4, 4));
            topBar.add(getSourceLabel());
            topBar.add(getHorizontalStrut_3_1());
            topBar.add(getSourcePathField());
            topBar.add(getHorizontalStrut_4());
            topBar.add(getShowFileBrowserButton());
            topBar.add(getHorizontalStrut_2());

            topBar.add(getSudokoLoadBtn());
        }
        return topBar;
    }

    private JLabel getSourceLabel() {
        if (sourceLabel == null) { sourceLabel = new JLabel("File / URL"); }
        return sourceLabel;
    }

    private Component getHorizontalStrut_3_1() {
        if (horizontalStrut_3 == null) { horizontalStrut_3 = Box.createHorizontalStrut(4); }
        return horizontalStrut_3;
    }

    private JButton getShowFileBrowserButton() {
        if (showFileBrowserButton == null) {
            showFileBrowserButton = new JButton("...");
            showFileBrowserButton.addActionListener(new ShowFileBrowserButtonActionListener());
        }
        return showFileBrowserButton;
    }

    private Component getHorizontalStrut_4() {
        if (horizontalStrut_4 == null) { horizontalStrut_4 = Box.createHorizontalStrut(2); }
        return horizontalStrut_4;
    }

    private JButton getSudokoLoadBtn() {
        if (sudokoLoadBtn == null) {
            sudokoLoadBtn = new JButton("load");
            sudokoLoadBtn.addActionListener(new SudokoLoadBtnActionListener());
        }
        return sudokoLoadBtn;
    }

    private JPanel getBottomPanel() {
        if (bottomPanel == null) {
            bottomPanel = new JPanel();
            bottomPanel.setLayout(new GridLayout(1, 2, 0, 0));
            bottomPanel.add(getBottomLeftPanel());
            bottomPanel.add(getBottomRightPanel());
        }
        return bottomPanel;
    }

    private JTextField getDelayField() {
        if (delayField == null) {
            delayField = new JTextField();
            delayField.addActionListener(new DelayFieldActionListener());
            delayField.setText("0");
            delayField.setToolTipText("");
            delayField.setColumns(10);
        }
        return delayField;
    }

    private JButton getSolveButton() {
        if (solveButton == null) {
            solveButton = new JButton("solve");
            solveButton.addActionListener(new SolveButtonActionListener());
        }
        return solveButton;
    }

    private JLabel getDelayLabel() {
        if (delayLabel == null) { delayLabel = new JLabel("Delay"); }
        return delayLabel;
    }

    private JPanel getSudokuPanel() {
        if (sudokuPanel == null) {
            sudokuPanel = new JPanel();
            sudokuPanel.setLayout(new GridLayout(1, 2, 4, 4));
            sudokuPanel.add(getPuzzlePanel());
            sudokuPanel.add(getSolutionPanel());
        }
        return sudokuPanel;
    }

    private JPanel getPuzzlePanel() {
        if (puzzlePanel == null) {
            puzzlePanel = new JPanel();
            puzzlePanel.setLayout(new BorderLayout(4, 4));
            puzzlePanel.add(getPuzzleTitle(), BorderLayout.NORTH);
            puzzlePanel.add(getPuzzleView(), BorderLayout.CENTER);
        }
        return puzzlePanel;
    }

    private JPanel getSolutionPanel() {
        if (solutionPanel == null) {
            solutionPanel = new JPanel();
            solutionPanel.setLayout(new BorderLayout(4, 4));
            solutionPanel.add(getSolutionTitle(), BorderLayout.NORTH);
            solutionPanel.add(getSolutionView(), BorderLayout.CENTER);
        }
        return solutionPanel;
    }

    private SudokuPanel getPuzzleView() {
        if (puzzleView == null) { puzzleView = new SudokuPanel(SudokuBoard.getDefault()); }
        return puzzleView;
    }

    private SudokuPanel getSolutionView() {
        if (solutionView == null) {
            solutionView = new SudokuPanel(getPuzzleView().getSudokuBoard().clone());
        }
        return solutionView;
    }

    private JPanel getBottomLeftPanel() {
        if (bottomLeftPanel == null) {
            bottomLeftPanel = new JPanel();
            FlowLayout flowLayout = (FlowLayout) bottomLeftPanel.getLayout();
            flowLayout.setVgap(4);
            flowLayout.setHgap(4);
            bottomLeftPanel.setBackground(new Color(48, 50, 52));
            bottomLeftPanel.add(getDelayLabel());
            bottomLeftPanel.add(getHorizontalStrut_1());
            bottomLeftPanel.add(getDelayField());
            bottomLeftPanel.add(getHorizontalStrut());
            bottomLeftPanel.add(getDelayUnitsCombo());
        }
        return bottomLeftPanel;
    }

    private JPanel getBottomRightPanel() {
        if (bottomRightPanel == null) {
            bottomRightPanel = new JPanel();
            FlowLayout flowLayout = (FlowLayout) bottomRightPanel.getLayout();
            flowLayout.setVgap(4);
            flowLayout.setHgap(4);
            flowLayout.setAlignment(FlowLayout.TRAILING);
            bottomRightPanel.add(getResetButton());
            bottomRightPanel.add(getSolveButton());
            bottomRightPanel.setBackground(new Color(48, 50, 52));
        }
        return bottomRightPanel;
    }

    private JLabel getSolutionTitle() {
        if (solutionTitle == null) {
            solutionTitle = new JLabel("Solution");
            solutionTitle.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return solutionTitle;
    }

    private JLabel getPuzzleTitle() {
        if (puzzleTitle == null) {
            puzzleTitle = new JLabel("Puzzle");
            puzzleTitle.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return puzzleTitle;
    }

    private JButton getResetButton() {
        if (resetButton == null) {
            resetButton = new JButton("reset");
            resetButton.addActionListener(new ResetButtonActionListener());
        }
        return resetButton;
    }

    private JComboBox<ChronoUnit> getDelayUnitsCombo() {
        if (delayUnitsCombo == null) {
            ChronoUnit[] validUnits = Stream.of(ChronoUnit.values()).filter(ChronoUnit::isTimeBased)
                    .toArray(ChronoUnit[]::new);
            delayUnitsCombo = new JComboBox<>(new DefaultComboBoxModel<>(validUnits));
            delayUnitsCombo.addActionListener(new DelayUnitsComboActionListener());
            delayUnitsCombo.setSelectedItem(ChronoUnit.MILLIS);
        }
        return delayUnitsCombo;
    }

    private Component getHorizontalStrut() {
        if (horizontalStrut == null) { horizontalStrut = Box.createHorizontalStrut(16); }
        return horizontalStrut;
    }

    private Component getHorizontalStrut_1() {
        if (horizontalStrut_1 == null) { horizontalStrut_1 = Box.createHorizontalStrut(8); }
        return horizontalStrut_1;
    }

    private JTextField getSourcePathField() {
        if (sourcePathField == null) {
            sourcePathField = new JTextField();
            sourcePathField.setColumns(10);
        }
        return sourcePathField;
    }

    private Component getHorizontalStrut_2() {
        if (horizontalStrut_2 == null) { horizontalStrut_2 = Box.createHorizontalStrut(16); }
        return horizontalStrut_2;
    }

    private class ResetButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { getSolutionView().getSudokuBoard().reset(); }
    }

    private class SolveButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> sudokuSolver.solve(getSolutionView().getSudokuBoard())).start();
        }
    }

    private class DelayFieldActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { updateDelay(); }
    }

    private class DelayUnitsComboActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { updateDelay(); }
    }

    private class SudokoLoadBtnActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String path = getSourcePathField().getText().trim();
            String exceptions = "";
            try {
                Scanner scanner = null;
                if (path.equals("")) try {
                    scanner = scannerFromURI(path);
                } catch (Exception exception) {
                    exceptions += "\n" + exception;
                    scanner = scannerFromFile(path);
                    fileChooser.setCurrentDirectory(new File(path).getParentFile().getAbsoluteFile());
                }
                setPuzzle(SudokuLoader.readBoardFromScanner(scanner));
                sourcePathField.setText(path);
                scanner.close();
            } catch (Exception exception) {
                exceptions += "\n" + exception;
                JOptionPane.showMessageDialog(null, "Failed to read input from '" + path + "'" + exceptions,
                    "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        Scanner scannerFromURI(String path) throws MalformedURLException, IOException, URISyntaxException {
            URI uri = new URI(path);
            InputStream is = uri.toURL().openStream();
            return new Scanner(is);
        }

        Scanner scannerFromFile(String path) throws IOException { return new Scanner(new File(path)); }
    }

    private class ShowFileBrowserButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    Scanner scanner = new Scanner(selectedFile);
                    setPuzzle(SudokuLoader.readBoardFromScanner(scanner));
                    sourcePathField.setText(selectedFile.toString());
                    fileChooser.setCurrentDirectory(selectedFile.getParentFile().getAbsoluteFile());
                    scanner.close();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null,
                        "Failed to read input from '" + selectedFile + "'\n" + exception, "Load Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void updateDelay() {
        try {
            int period = Integer.parseInt(getDelayField().getText());
            ChronoUnit unit = delayUnitsCombo.getItemAt(getDelayUnitsCombo().getSelectedIndex());
            getSolutionView().setDelay(period, unit);
            delayUnitsCombo.grabFocus();
        } catch (Exception exception) {}
    }

    public synchronized void setPuzzle(SudokuBoard board) {
        getPuzzleView().setPuzzle(board);
        getSolutionView().setPuzzle(board);
    }
}
