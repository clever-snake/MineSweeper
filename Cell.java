public class Cell {
    
    private boolean hasMine;
    private boolean Revealed;
    private boolean isFlagged;
    private int adjacentMines;

    public Cell(){
        hasMine = false;
        Revealed = false;
        isFlagged = false;
        adjacentMines = 0;
    }

    public boolean hasMine() {return hasMine;}

    public boolean isReavealed() {return Revealed;}

    public boolean isFlagged() {return isFlagged;}

    public int getAdjacentMines() {return adjacentMines;}

    public void setHasMine(boolean hasMine){
        this.hasMine = hasMine;
    }

    public void setReavealed(boolean Revealed){
        this.Revealed = Revealed;
    }

    public void setFlagged(boolean isFlagged){
        this.isFlagged = isFlagged;
    }

    public void setAdjacentMines(int adjacentMines){
        this.adjacentMines = adjacentMines;
    }
}

