import java.util.Scanner;

public class Main {
    
    public static void main(String[] args){

        Board board = new Board(9, 9, 10);
        Scanner in = new Scanner(System.in);
        boolean gameOver =false;

        while(!gameOver){
            board.printBoard();
            System.out.print("o/f row column");
            String cmd = in.next();
            System.out.println("Διάβασα: [" + cmd + "] μήκος " + cmd.length());
            int r = in.nextInt();
            int c = in.nextInt();
            if(cmd.equals("o")){
                if(board.reveal(r, c)){
                    board.revealAllMines();
                    board.printBoard();
                    System.out.println("You lost.");
                    gameOver = true;
                }else if(board.isWon()){
                    board.printBoard();
                    System.out.println("You won.");
                    gameOver = true;
                }
            }else if(cmd.equals("F")){
                board.toggleFlag(r, c);
            }else{
                System.out.print("Invalid Choice.");
            }
            System.out.println();
        }
        in.close();
    }
}

