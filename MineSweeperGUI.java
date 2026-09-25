import java.awt.*;
import java.awt.event.*;

public class MineSweeperGUI extends Frame{

    private Board board;
    private Button[][] buttons;
    private Label statusLabel;
    private boolean gameOver = false;

    public MineSweeperGUI(int rows, int columns, int mines){
        board = new Board(rows, columns, mines);
        setTitle("Minesweeper");
        statusLabel = new Label("Good Luck.");
        add(statusLabel,BorderLayout.NORTH);
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

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                dispose();
                System.exit(0);
            }
        });
        
        setSize(600,550);
        setVisible(true);
    }

    private void handleClick(int rows, int columns,boolean rightClick){
        if(gameOver){
            return;
        }else if(rightClick){
            board.toggleFlag(rows, columns);
        }else if(board.reveal(rows, columns)){
            board.revealAllMines();
            statusLabel.setText("You Lost.");
            gameOver = true;
        }else if(board.isWon()){
            statusLabel.setText("You Won!");
            gameOver = true;
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
    }
}
