import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class GUISlidingNumber extends JPanel {
    private int size = 3; 
    private int nbTiles = size * size - 1; 
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
    // private TimerThread timerThread;
    private Thread thread;
    private int hour, minute, second;
    private String bestTime = "00:00:00";
    private int highScore = 0;
    private boolean stop;
    private Logic logic; 
    private KlikPuzzle klik; 
    //Untuk menampung data yang telah diambil dari database 
    private ResultSet rs; 
    private Score score;
    private String username; 

    public GUISlidingNumber(int dim, int mar, String username) {
        this.dimension = dim;
        this.margin = mar;
        this.clickNum = 0;
        this.username = username;
        highScore = KoneksiDatabase.cekSkor_Tinggi();
        bestTime = KoneksiDatabase.cekWaktu();
        

        this.gridSize = (dimension - 2 * margin);
        this.tileSize = gridSize / size;
        this.tiles = new int[size * size];

        this.logic = new Logic();
        this.klik = new KlikPuzzle(this);

        setPreferredSize(new Dimension(dimension + 200, dimension + margin));
        setBackground(Color.BLACK);
        setForeground(FOREGROUND_COLOR);
        setFont(new Font("Courier New", Font.BOLD, 30));

        gameOver = true;

        addMouseListener(klik);

    // Tombol reset
    JButton reset = new JButton("Reset");
    add(reset);
    reset.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            // Jika tiles yang bernilai 0 tidak di posisi akhir, akan memunculkan warning message
            if (tiles[size * size - 1] != 0) {
                // Memuat dan mengubah ukuran ikon gambar
                ImageIcon icon = new ImageIcon("assets/error2.jpeg");
                Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Ubah ukuran gambar menjadi 50x50
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                JOptionPane.showMessageDialog(null, 
                                            "Pindahkan posisi kosong ke ujung kanan bawah sebelum mereset!", 
                                            "Tidak bisa reset!", 
                                            JOptionPane.WARNING_MESSAGE,
                                            scaledIcon);
            } else {
                newGame(); // Reset the game state
                stopThread();
                clickNum = 0;
                hour = 0; minute = 0; second = 0;
                repaint(); // Redraw the game UI
            }
        }
    });


    // Tombol kembali
    JButton backButton = new JButton("Back");
    add(backButton);
    backButton.addActionListener(ae -> {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GUISlidingNumber.this);
        topFrame.dispose(); 
        SwingUtilities.invokeLater(() -> new Menu("No user").setVisible(true)); // Open the menu
    });

    // Memulai game
    newGame(); 
}



public void insertData(int skor_tinggi, String waktu) {
    try (Connection conn = KoneksiDatabase.getConnection()) {
        String sql = "INSERT INTO data (skor_tinggi, waktu, id) VALUES (?, ?,2)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, skor_tinggi);
            pstmt.setString(2, waktu);

            pstmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public int getDimension(){
        return this.dimension;
      }
      public int getMargin(){
        return this.margin;
      }
      public int getGridSize(){
        return this.size;
      }
      public int[] getTiles(){
        return this.tiles;
      }
      public boolean getGameStatus(){
        return this.gameOver;
      }
      public void setGameOver(){
        this.gameOver = true;
      }
      public void addClickNum(){
        this.clickNum++;
      }
      public int getClickNum(){
        return this.clickNum;
      }
      public String getTime(){
          String waktu = String.valueOf(hour) + " h " + String.valueOf(minute) + " m " + String.valueOf(second) + " s";
          return waktu;
      }
      public void setHighScore(int clickNum){
        highScore = KoneksiDatabase.cekSkor_Tinggi();
        bestTime = KoneksiDatabase.cekWaktu();
      }


    public void startThread(){
        stop = false;
        // objek untuk mengatur thread
        Time time = new Time();
        time.start();
      }

    public void stopThread() {
        stop = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gtd = (Graphics2D) g;
        gtd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(gtd);
        // drawUsername(gtd); 
        drawBestTime(gtd);
        drawHighScore(gtd);
        drawTime(gtd);
        drawClickNum(gtd);
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
                    String time = (hour +" h " + minute+ " m " +second);
                    int detik = hour*60*60+minute*60+second;
                    KoneksiDatabase.setDataMenang(clickNum,time,2,detik);
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

    // private void drawUsername(Graphics2D g) {
    //     g.setFont(getFont().deriveFont(Font.BOLD, 20));
    //     g.setColor(FOREGROUND_COLOR);
    //     g.drawString("Nama User", dimension + 55, (int) (0.2 * dimension));
    //     g.drawString(username, dimension + 10, (int) (0.25 * dimension)); // Tampilkan username
    // }

    private void drawCenteredString(Graphics2D g, String s, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s, x + (tileSize - fm.stringWidth(s)) / 2,
                y + (asc + (tileSize - (asc + desc)) / 2));
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

    public void newGame(){
        do{
          reset(); // Mengembalikan semua tile seperti semula
          shuffle(); // Mengacak tile
        } while(!logic.isSolvable(this)); // Looping dilakukan selama Puzzle tidak bisa diselesaikan
        gameOver = false;
        stopThread();
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

    //Thread waktu
    class Time extends Thread{
        @Override
        public void run(){
            try{
              for (hour= 0; hour<=24; hour++){
                for (minute = 0; minute<=60; minute++){
                    for (second = 0; second<=60; second++){
                        repaint(); //berfungsi untuk tetap mengupdate tampilan waktu
                        sleep(1000);
                        if(stop){break;}
                    }
                    if(stop){break;}
                }
                if(stop){break;}
              }
            }catch(InterruptedException e){
              System.out.println(e.getMessage());
            }
        }
      }
}



