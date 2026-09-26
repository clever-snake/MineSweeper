import java.util.Random;

public class Board {

    private int rows;
    private int columns;
    private int mineCount;
    private boolean minesPlaced;
    private Cell[][] grid;
    
    public Board(int rows,int columns,int mineCount){
        this.rows = rows;
        this.columns = columns;
        this.mineCount = mineCount;
        minesPlaced = false;
        grid = new Cell[rows][columns];
        fillGrid();
    }

    private void fillGrid(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                this.grid[i][j] = new Cell();
            }
        }
    }
    
    public void printDebug(){
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

    private void placeMines(int safeR, int safeC){
        int placed = 0;
        Random rand = new Random();
        while(placed<mineCount){
            int rrow = rand.nextInt(rows);
            int rcolumn = rand.nextInt(columns);
            if (Math.abs(rrow - safeR) <= 1 && Math.abs(rcolumn - safeC) <= 1) continue;
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

    public void printBoard(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(grid[i][j].isFlagged()){ 
                    System.out.print("F "); continue;
                }else if(!grid[i][j].isReavealed()) {
                    System.out.print("# "); continue;
                }else if(grid[i][j].hasMine() && grid[i][j].isReavealed()){ 
                    System.out.print("* "); continue;
                }else if(grid[i][j].getAdjacentMines()==0) {
                    System.out.print(". "); continue;
                }else System.out.print(grid[i][j].getAdjacentMines()+ " ");
            }
            System.out.println();
        }
    }

    public boolean reveal(int r, int c){
        if(!minesPlaced){
            placeMines(r, c);
            calculateAdjacentMines();
            minesPlaced = true;
        }
        if(!isInBounds(r, c) || grid[r][c].isReavealed() || grid[r][c].isFlagged()){ 
            return false;
        }
        grid[r][c].setReavealed(true);
        if(grid[r][c].hasMine()) return true;
        if(grid[r][c].getAdjacentMines() ==0){
            for(int dr=-1; dr<2; dr++){
            for(int dc=-1; dc<2; dc++){
                if(dr==0 && dc ==0) continue;
                reveal(r + dr, c + dc);
            }
        }
        }
        return false;
    }

    public void toggleFlag(int r, int c){
        if(!isInBounds(r, c) || grid[r][c].isReavealed()){
            return;
        }
        grid[r][c].setFlagged(!grid[r][c].isFlagged());;
    }

    public boolean isWon(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(!grid[i][j].isReavealed()&& !grid[i][j].hasMine()) return false;
            }
        }
        return true;
    }

    public void revealAllMines(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(grid[i][j].hasMine()){
                    grid[i][j].setFlagged(false);
                    grid[i][j].setReavealed(true);
                }
            }
        }
    }

    public void flagAllMines(){
    for(int i=0; i<rows; i++){
        for(int j=0; j<columns; j++){
            if(grid[i][j].hasMine()){
                grid[i][j].setFlagged(true);
            }
        }
    }
}

    public int getFlagCount(){
        int count=0;
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(grid[i][j].isFlagged()) count++;
            }
        }
        return count;
    }

    public int getMineCount(){return mineCount;}

    public int getRows() {return rows;}

    public int getColumns() {return columns;}


        
    public Cell getCell(int r, int c){
        return grid[r][c];
    }

}
