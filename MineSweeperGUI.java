import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MineSweeperGUI extends JFrame {

    private static final Color[] NUMBER_COLORS = {
        null,
        new Color(0, 0, 255),     
        new Color(0, 128, 0),     
        new Color(255, 0, 0),      
        new Color(0, 0, 128),     
        new Color(128, 0, 0),      
        new Color(0, 128, 128),
        Color.BLACK,
        Color.GRAY
    };
    private static final Color REVEALED_COLOR = new Color(220, 220, 220);
    private static final int CELL_SIZE = 30;

    private Board board;
    private JButton[][] buttons;
    private JLabel statusLabel;
    private JLabel minesLabel;
    private JLabel timeLabel;
    private int rows;
    private int columns;
    private int mines;
    private long startTime;
    private volatile Thread timeThread;
    private boolean gameOver = false;

   
    private javax.swing.border.Border defaultBorder;
    private Color defaultBackground;

    public MineSweeperGUI(int rows, int columns, int mines){
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;

        board = new Board(rows, columns, mines);
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        statusLabel = new JLabel("Good Luck.");
        timeLabel = new JLabel("Time: 0");
        minesLabel = new JLabel("Mines: " + mines);

        JPanel topPanel = new JPanel(new FlowLayout());
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFocusable(false);

        JComboBox<String> difficulty = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficulty.setFocusable(false);

        if(mines == 10){
            difficulty.setSelectedItem("Easy");
        }else if(mines == 40){
            difficulty.setSelectedItem("Medium");
        }else{
            difficulty.setSelectedItem("Hard");
        }

        topPanel.add(difficulty);
        topPanel.add(timeLabel);
        topPanel.add(minesLabel);
        topPanel.add(newGameButton);
        topPanel.add(statusLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(rows, columns));
        buttons = new JButton[rows][columns];

        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                JButton b = new JButton("");
                b.setFocusable(false);
                b.setMargin(new Insets(0, 0, 0, 0)); 
                b.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                b.setFont(new Font("SansSerif", Font.BOLD, 14));
                buttons[i][j] = b;
                gridPanel.add(b);

                final int row = i;
                final int col = j;
                b.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        boolean rightClick = SwingUtilities.isRightMouseButton(e) || e.isControlDown();
                        handleClick(row, col, rightClick);
                    }
                });
            }
        }
        defaultBorder = buttons[0][0].getBorder();
        defaultBackground = buttons[0][0].getBackground();

        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(gridPanel);
        add(centerPanel, BorderLayout.CENTER);

        newGameButton.addActionListener(e -> newGame());

        difficulty.addActionListener(e -> changeDifficulty((String) difficulty.getSelectedItem()));

        pack();                     
        setResizable(false);
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private void handleClick(int r, int c, boolean rightClick){
        if(gameOver){
            return;
        }
        if(!rightClick && timeThread == null){
            startTimer();
        }
        boolean lost = false;
        if(rightClick){
            board.toggleFlag(r, c);
        }else if(board.reveal(r, c)){
            board.revealAllMines();
            statusLabel.setText("You Lost.");
            gameOver = true;
            lost = true;
            stopTimer();
        }else if(board.isWon()){
            board.flagAllMines();
            statusLabel.setText("You Won!");
            gameOver = true;
            stopTimer();
        }
        updateButtons();
        if(lost){
            buttons[r][c].setBackground(Color.RED);  
        }
    }

    private void updateButtons(){
        for(int i=0; i<board.getRows(); i++){
            for(int j=0; j<board.getColumns(); j++){
                Cell cell = board.getCell(i, j);
                JButton b = buttons[i][j];

                if(cell.isFlagged()){
                    showClosed(b);
                    b.setText("F");
                    b.setForeground(Color.RED);
                }else if(!cell.isReavealed()){
                    showClosed(b);
                    b.setText("");
                }else if(cell.hasMine()){
                    showOpen(b);
                    b.setText("*");
                    b.setForeground(Color.BLACK);
                }else if(cell.getAdjacentMines() == 0){
                    showOpen(b);
                    b.setText("");
                }else{
                    showOpen(b);
                    int n = cell.getAdjacentMines();
                    b.setText(String.valueOf(n));
                    b.setForeground(NUMBER_COLORS[n]);
                }
            }
        }
        minesLabel.setText("Mines: " + (board.getMineCount() - board.getFlagCount()));
    }

    // Κλειστό κελί: κανονικό "ανάγλυφο" κουμπί
    private void showClosed(JButton b){
        b.setBorder(defaultBorder);
        b.setBackground(defaultBackground);
    }

    private void showOpen(JButton b){
        b.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        b.setBackground(REVEALED_COLOR);
    }

    private void newGame(){
        stopTimer();
        timeLabel.setText("Time: 0");
        board = new Board(rows, columns, mines);
        gameOver = false;
        statusLabel.setText("Good Luck!");
        updateButtons();
    }

    private void startTimer(){
        startTime = System.currentTimeMillis();
        Thread t = new Thread(new Runnable() {
            public void run(){
                while (timeThread == Thread.currentThread()){
                    final long secs = (System.currentTimeMillis() - startTime) / 1000;

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run(){
                            timeLabel.setText("Time: " + secs);
                        }
                    });
                    try{
                        Thread.sleep(200);
                    }catch(InterruptedException e){
                        return;
                    }
                }
            }
        });
        timeThread = t;
        t.start();
    }

    private void stopTimer(){
        timeThread = null;
    }

    private void changeDifficulty(String difficulty){
        stopTimer();
        dispose();
        if(difficulty.equals("Easy")){
            new MineSweeperGUI(9, 9, 10);
        }else if(difficulty.equals("Medium")){
            new MineSweeperGUI(16, 16, 40);
        }else{
            new MineSweeperGUI(16, 30, 99);
        }
    }

}
