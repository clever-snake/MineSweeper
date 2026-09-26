import java.awt.*;
import java.awt.event.*;

public class MineSweeperGUI extends Frame{

    private Board board;
    private Button[][] buttons;
    private Label statusLabel;
    private Label minesLabel;
    private Label timeLabel;
    private int rows;
    private int columns;
    private int mines;
    private long startTime;
    private volatile Thread timeThread;
    private boolean gameOver = false;

    public MineSweeperGUI(int rows, int columns, int mines){
        this.rows = rows;
        this.columns = columns;
        this.mines =mines;

        board = new Board(rows, columns, mines);
        setTitle("Minesweeper");
        statusLabel = new Label("Good Luck.");
        timeLabel = new Label("Time: 0    ");
        minesLabel = new Label("Mines: " + mines);

        Panel topPanel = new Panel(new FlowLayout());
        Button newGameButton= new Button("New Game");
        Choice difficulty = new Choice();
        
        difficulty.add("Easy");
        difficulty.add("Medium");
        difficulty.add("Hard");
        
        if(mines == 10){
            difficulty.select("Easy");
        }else if(mines ==40){
            difficulty.select("Medium");
        }else{
            difficulty.select("Hard");
        }

        topPanel.add(difficulty);
        topPanel.add(timeLabel);
        topPanel.add(minesLabel);
        topPanel.add(newGameButton);
        topPanel.add(statusLabel);
        add(topPanel, BorderLayout.NORTH);
        

        Panel gridPanel = new Panel(new GridLayout(rows,columns));
        buttons = new Button[rows][columns];

        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                buttons[i][j] = new Button("");
                gridPanel.add(buttons[i][j]);
                final int row = i;
                final int col = j;
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        boolean rightClick = e.getButton() == MouseEvent.BUTTON3 || e.isControlDown();
                        handleClick(row, col, rightClick);
                    }
                });
            }
        }
        

        add(gridPanel,BorderLayout.CENTER);

        newGameButton.addActionListener(e ->newGame());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                dispose();
                System.exit(0);
            }
        });
        
        difficulty.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                changeDifficulty(difficulty.getSelectedItem());
            }
        });

        setSize(Math.max(columns*30+40, 450),rows*30+100);
        setVisible(true);
    }

    private void handleClick(int rows, int columns,boolean rightClick){
        if(gameOver){
            return;
        }
        if(!rightClick && timeThread == null){
            startTimer();
        }
        if(rightClick){
            board.toggleFlag(rows, columns);
        }else if(board.reveal(rows, columns)){
            board.revealAllMines();
            statusLabel.setText("You Lost.");
            gameOver = true;
            stopTimer();
        }else if(board.isWon()){
            board.flagAllMines();
            statusLabel.setText("You Won!");
            gameOver = true;
            stopTimer();
        }
        updateButtons();
    }

    private void updateButtons(){
        for(int i=0; i<board.getRows(); i++){
            for(int j=0; j<board.getColumns(); j++){
                Cell cell = board.getCell(i, j);
                Button b = buttons[i][j];

                if(cell.isFlagged()){
                    b.setLabel("F");
                }else if(!cell.isReavealed()){
                    b.setLabel("");
                }else if(cell.hasMine()){
                    b.setLabel("*");
                    b.setEnabled(false);
                }else if(cell.getAdjacentMines() == 0){
                    b.setLabel(".");
                    b.setEnabled(false);
                }else{
                    b.setLabel(String.valueOf(cell.getAdjacentMines()));
                    b.setEnabled(false);
                }
            }
        }
        minesLabel.setText("Mines: "+ (board.getMineCount()-board.getFlagCount()));
    }

    private void newGame(){
        stopTimer();
        timeLabel.setText("Time: 0");
        board = new Board(rows, columns, mines);
        gameOver = false;
        statusLabel.setText("Good Luck!");
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                buttons[i][j].setLabel("");
                buttons[i][j].setEnabled(true);
            }
        }
        updateButtons();
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                buttons[i][j].repaint();
            }
        }
    }

    private void startTimer(){
        startTime = System.currentTimeMillis();
        Thread t = new Thread(new Runnable() {
            public void run(){
                while (timeThread == Thread.currentThread()){
                    final long secs = (System.currentTimeMillis() - startTime) / 1000;

                    EventQueue.invokeLater(new Runnable() {
                        public void run(){
                            timeLabel.setText("Time: "+secs);
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
