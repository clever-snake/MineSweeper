public class Board {

    private int rows;
    private int columns;
    private int mineCount;
    private Cell[][] grid;
    
    public Board(int rows,int columns,int mineCount){
        this.rows = rows;
        this.columns = columns;
        this.mineCount = mineCount;
        fillGrid();
    }

    private void fillGrid(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                grid[i][j] = new Cell();
            }
        }
    }

}
