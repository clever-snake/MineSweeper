public class Main {
    
    public static void main(String[] args){

        Board board = new Board(9, 9, 10);

        board.printDebug();

        System.out.println();

        if(board.reveal(0, 0)){
            System.out.println("You lost.");
        }

        board.printBoard();
    }
}
