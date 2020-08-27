package hy.sudoku.ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import hy.sudoku.board.SudokuActionListener;
import hy.sudoku.board.SudokuBoard;

public class SudokuPanel extends JPanel {
    private static final long          serialVersionUID = 1L;
    private final SudokuActionListener sudokuActionListener;

    private SudokuBoard                board;
    private long                       delayPeriod;
    private TimeUnit                   delayUnits;

    public SudokuPanel(SudokuBoard board) {
        this.board = Objects.requireNonNull(board, "Provided board is null.");
        sudokuActionListener = (r, c, v) -> delayedUpdate();
        setPreferredSize(new Dimension(300, 300));
        setMinimumSize(getPreferredSize());
        board.addListener(sudokuActionListener);
    }

    public void setBoard(SudokuBoard board) {
        SudokuBoard prevBoard = board;
        this.board = Objects.requireNonNull(board, "Provided board is null.");
        this.board.addListener(sudokuActionListener);
        prevBoard.removeListener(sudokuActionListener);
    }

    public void setDelay(long value, ChronoUnit units) {
        delayPeriod = value;
        delayUnits = TimeUnit.of(Objects.requireNonNull(units, "Provided delay time unit is null."));
    }

    public SudokuBoard getSudokuBoard() { return board; }

    public synchronized void setPuzzle(SudokuBoard puzzle) { board.setPuzzle(puzzle); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int slotWidth = (getWidth() - 4) / board.boardLength;
        int slotHeight = (getHeight() - 4) / board.boardLength;

        int width = slotWidth * board.boardLength;
        int height = slotHeight * board.boardLength;

        int widthPad = (getWidth() - width) / 2;
        int heightPad = (getHeight() - height) / 2;

        g2d.setColor(getBackground());
        g2d.fillRect(widthPad, heightPad, width, height);

        g2d.setColor(getForeground());

        for (int x = 0; x <= width; x += slotWidth) {
            if ((x / slotWidth) % board.blockLength == 0) {
                g2d.setStroke(new BasicStroke(2));

            } else {
                g2d.setStroke(new BasicStroke(1));
            }
            g2d.drawLine(x + widthPad, heightPad, x + widthPad, height + heightPad);
        }

        for (int y = 0; y <= height; y += slotHeight) {
            if ((y / slotHeight) % board.blockLength == 0) {
                g2d.setStroke(new BasicStroke(2));

            } else {
                g2d.setStroke(new BasicStroke(1));
            }
            g2d.drawLine(widthPad, y + heightPad, width + widthPad, y + heightPad);
        }

        Font normfont = getFont().deriveFont(26);
        Font boldFont = normfont.deriveFont(Font.BOLD);

        for (int r = 0; r < board.boardLength; r++) {
            for (int c = 0; c < board.boardLength; c++) {
                if (board.isFilled(r, c)) {
                    String strValue = String.valueOf(board.get(r, c));
                    Font font = board.wasProvided(r, c) ? boldFont : normfont;
                    g2d.setFont(font);

                    Rectangle2D txtBounds = font.getStringBounds(strValue, g2d.getFontRenderContext());
                    int txtWidth = (int) txtBounds.getWidth();
                    int txtHeight = (int) txtBounds.getHeight();

                    g2d.drawString(strValue, (c * slotWidth) + ((slotWidth / 2) + widthPad - (txtWidth / 2)),
                        (r * slotHeight) + ((slotHeight / 2) + heightPad + (txtHeight / 2)));
                }
            }
        }
    }

    private void delayedUpdate() {
        if (delayPeriod > 0) {
            try {
                delayUnits.sleep(delayPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        repaint();
    }

}
