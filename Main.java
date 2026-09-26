import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args){
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
           
        }
        SwingUtilities.invokeLater(() -> new MineSweeperGUI(9, 9, 10));
    }
}
