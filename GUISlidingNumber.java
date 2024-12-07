import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GUISlidingNumber extends JPanel {
    private int size = 3; // Ukuran 3x3 grid
    private int nbTiles = size * size - 1; // 8 tiles untuk grid 3x3
    private int dimension;
    private static final Color FOREGROUND_COLOR = new Color(144, 213, 255);
    private static final Random RANDOM = new Random();
    private int[] tiles;
    private int tileSize;
    private int blankPos;
    private int margin;
    private int gridSize;
    private int clickNum;
    private boolean gameOver;
    private TimerThread timerThread;
    private Thread thread;
    private int hour, minute, second;
    private String bestTime = "00:00:00";
    private int highScore = 0;

    public GUISlidingNumber(int dim, int mar) {
        this.dimension = dim;
        this.margin = mar;
        this.clickNum = 0;

        this.gridSize = (dimension - 2 * margin);
        this.tileSize = gridSize / size;
        this.tiles = new int[size * size];

        setPreferredSize(new Dimension(dimension + 200, dimension + margin));
        setBackground(Color.BLACK);
        setForeground(FOREGROUND_COLOR);
        setFont(new Font("Courier New", Font.BOLD, 30));

        gameOver = true;
        addMouseListener(new ClickListener());

        JButton reset = new JButton("Reset");
        add(reset);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                newGame();
                repaint();
            }
        });

        // Membuat tombol kembali
        JButton backButton = new JButton("Back");
        add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // Menutup tampilan puzzle dan kembali ke login
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GUISlidingNumber.this);
                topFrame.dispose(); // Menutup frame puzzle
                // SwingUtilities.invokeLater(() -> new Login().setVisible(true)); // Menampilkan login
            }
        });

        newGame();
    }

    private void newGame() {
        reset();
        shuffle();
        gameOver = false;
        clickNum = 0;

        // Reset timer
        stopThread();
        hour = minute = second = 0;
        timerThread = new TimerThread(this);
        thread = new Thread(timerThread);
        thread.start();
    }

    private void reset() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
        }
        blankPos = tiles.length - 1;
    }

    private void shuffle() {
        int n = nbTiles;
        while (n > 1) {
            int r = RANDOM.nextInt(n--);
            int tmp = tiles[r];
            tiles[r] = tiles[n];
            tiles[n] = tmp;
        }
    }

    private void drawGrid(Graphics2D g) {
        for (int i = 0; i < tiles.length; i++) {
            int row = i / size;
            int col = i % size;

            int x = margin + col * tileSize;
            int y = margin + row * tileSize;

            if (tiles[i] == 0) {
                if (gameOver) {
                    g.setColor(FOREGROUND_COLOR);
                    drawCenteredString(g, "Done!", x, y);
                }
                continue;
            }

            g.setColor(getForeground());
            g.fillRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.WHITE);

            drawCenteredString(g, String.valueOf(tiles[i]), x, y);
        }
    }

    private void drawCenteredString(Graphics2D g, String s, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s, x + (tileSize - fm.stringWidth(s)) / 2,
                y + (asc + (tileSize - (asc + desc)) / 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gtd = (Graphics2D) g;
        gtd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(gtd);

        // Tambahkan informasi waktu, skor, dll.
        drawBestTime(gtd);
        drawHighScore(gtd);
        drawTime(gtd);
        drawClickNum(gtd);
    }

    private class ClickListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (gameOver) {
                newGame();
                repaint();
                return;
            }

            int ex = e.getX() - margin;
            int ey = e.getY() - margin;

            if (ex < 0 || ex > gridSize || ey < 0 || ey > gridSize) return;

            int c1 = ex / tileSize;
            int r1 = ey / tileSize;

            int clickPos = r1 * size + c1;

            int dir = 0;
            if (clickPos == blankPos - 1 && blankPos % size != 0) dir = 1;
            else if (clickPos == blankPos + 1 && blankPos % size != size - 1) dir = -1;
            else if (clickPos == blankPos - size) dir = size;
            else if (clickPos == blankPos + size) dir = -size;

            if (dir != 0) {
                tiles[blankPos] = tiles[blankPos + dir];
                tiles[blankPos + dir] = 0;
                blankPos += dir;
                clickNum++;
                repaint();
            }
        }
    }

    public void incrementTime() {
        second++;
        if (second == 60) {
            second = 0;
            minute++;
            if (minute == 60) {
                minute = 0;
                hour++;
            }
        }
        repaint();
    }

    public void stopThread() {
        if (timerThread != null) {
            timerThread.stop();
        }
    }

    private void drawBestTime(Graphics2D g) {
        g.setFont(getFont().deriveFont(Font.BOLD, 20));
        g.setColor(FOREGROUND_COLOR);
        g.drawString("Best Time", dimension + 15, (int) (0.35 * dimension));
        g.drawString(bestTime, dimension + 10, (int) (0.4 * dimension));
    }

    private void drawHighScore(Graphics2D g) {
        g.setFont(getFont().deriveFont(Font.BOLD, 20));
        g.setColor(FOREGROUND_COLOR);
        g.drawString("High Score", dimension + 15, (int) (0.45 * dimension));
        g.drawString(String.valueOf(highScore), dimension + 70, (int) (0.5 * dimension));
    }

    private void drawTime(Graphics2D g) {
        g.setFont(getFont().deriveFont(Font.BOLD, 20));
        g.setColor(FOREGROUND_COLOR);
        g.drawString("Time", dimension + 55, (int) (0.55 * dimension));
        g.drawString(String.format("%02d h %02d m %02d s", hour, minute, second), dimension + 10, (int) (0.6 * dimension));
    }

    private void drawClickNum(Graphics2D g) {
        g.setFont(getFont().deriveFont(Font.BOLD, 20));
        g.setColor(FOREGROUND_COLOR);
        g.drawString("Click Numbers", dimension, (int) (0.65 * dimension));
        g.drawString(String.valueOf(this.clickNum), (dimension + 70), (int) (0.7 * dimension));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GUISlidingNumber puzzle = new GUISlidingNumber(600, 30);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Sliding Number");
        frame.add(puzzle);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class TimerThread implements Runnable {
    private boolean running = true;
    private GUISlidingNumber puzzle;

    public TimerThread(GUISlidingNumber puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);
                puzzle.incrementTime();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
