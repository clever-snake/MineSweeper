import java.util.Random;

public class Board {

    private int rows;
    private int columns;
    private int mineCount;
    private Cell[][] grid;
    
    public Board(int rows,int columns,int mineCount){
        this.rows = rows;
        this.columns = columns;
        this.mineCount = mineCount;
        grid = new Cell[rows][columns];
        fillGrid();
        placeMines();
        calculateAdjacentMines();
    }

    private void fillGrid(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                this.grid[i][j] = new Cell();
            }
        }
    }
    
    public void printBoard(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(grid[i][j].hasMine()){
                    System.out.print("* ");
                }else{        
                //else if(grid[i][j].getAdjacentMines()>0){
                    System.out.print(grid[i][j].getAdjacentMines() + " ");
                }
                //else{
                  //  System.out.print(" ");
                //}
            }
            System.out.println();
        }
    }

    private void placeMines(){
        int placed = 0;
        while(placed<mineCount){
            Random rand = new Random();
            int rrow = rand.nextInt(rows);
            int rcolumn = rand.nextInt(columns);
            if(!grid[rrow][rcolumn].hasMine()) {grid[rrow][rcolumn].setHasMine(true); placed++;}
        }
    }

    private boolean isInBounds(int r, int c){
        return ((0<=r && r<rows) && (0<=c &&c<columns));
    }

    private void calculateAdjacentMines(){
        int count;
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(grid[i][j].hasMine()) continue;
                count =0;
                for(int dr=-1; dr<2; dr++){
                    for(int dc=-1; dc<2; dc++){
                        if(dr==0 && dc==0) continue;
                        int nr = i + dr;
                        int nc = j + dc;
                        if (isInBounds(nr, nc) && grid[nr][nc].hasMine()) {
                            count++;
                        }
                    }
                }
                grid[i][j].setAdjacentMines(count);
            }
        }
    }
}
