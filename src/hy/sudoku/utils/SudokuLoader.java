package hy.sudoku.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import hy.sudoku.board.SudokuBoard;

public class SudokuLoader {

    public static SudokuBoard readBoardFromConsole() {
        System.out.println("Waiting for sudoku board data.");
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter length of Sudoku board's sides: ");
            int blockLength = scanner.nextInt();
            System.out.println("Enter Sudoku data seperated by whitespaces: ");
            return readBoardFromScanner(scanner, blockLength);
        }
    }

    public static SudokuBoard readBoardFromFile(String filepath) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(filepath))) {
            return readBoardFromScanner(scanner);
        }
    }

    public static SudokuBoard readBoardFromString(String stringData) {
        try (Scanner scanner = new Scanner(stringData)) {
            return readBoardFromScanner(scanner);
        }
    }

    public static SudokuBoard readBoardFromScanner(Scanner scanner, int boardLength) {
        Objects.requireNonNull(scanner, "Provided scanner is null.");

        int blockLength = (int) Math.sqrt(boardLength);
        if (Math.pow(blockLength, 2) != boardLength)
            throw new IllegalArgumentException("Board is not square of it's block's dimensions");

        SudokuBoard board = new SudokuBoard(blockLength);
        int[][] puzzle = new int[boardLength][boardLength];

        for (int row = 0; row < boardLength; row++) {
            for (int column = 0; column < boardLength; column++) { puzzle[row][column] = scanner.nextInt(); }
        }

        board.setPuzzle(puzzle);

        return board;
    }

    static Pattern whitespaces = Pattern.compile("[\\W]+");

    public static SudokuBoard readBoardFromScanner(Scanner scanner) {
        Objects.requireNonNull(scanner, "Provided scanner is null.").useDelimiter(whitespaces);
        List<String> strings = scanner.tokens().collect(Collectors.toList());

        int boardLength = (int) Math.sqrt(strings.size());
        int blockLength = (int) Math.sqrt(boardLength);

        if (Math.pow(blockLength, 4) != strings.size())
            throw new IllegalArgumentException("Board is not square of it's block's dimensions");

        SudokuBoard board = new SudokuBoard(blockLength);
        int[][] puzzle = new int[boardLength][boardLength];

        for (int index = 0, row = 0; row < boardLength; row++) {
            for (int column = 0; column < boardLength; index++, column++) {
                puzzle[row][column] = Integer.parseInt(strings.get(index));
            }
        }

        board.setPuzzle(puzzle);

        return board;
    }
}
