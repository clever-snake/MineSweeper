import java.awt.*;
import java.awt.event.*;

public class MinseSweeperGUI extends Frame{

    private Board board;
    private Button[][] buttons;
    private Label statusLabel;

    MinseSweeperGUI(int rows, int columns, int mines){
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
}
