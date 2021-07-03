package computer;

import components.Board;

public class Computer {
    protected boolean canMakeTurn = true;
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    protected void setY(int y) {
        this.y = y;
    }

    public boolean botCanMakeTurn() {
        return canMakeTurn;
    }

    public Computer() {
    }

    public void makeTurn(Board board, char marker, char playerMarker) {
    }
}
