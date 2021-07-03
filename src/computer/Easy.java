
/*
 * Easy bot mode
 * src: https://github.com/vasanthi-vuppuluri/tic-tac-toe/blob/master/Easy.java
 */

package computer;

import components.Board;

public class Easy extends Computer {
    public Easy() {
    }

    @Override
    public void makeTurn(Board board, char marker, char playerMarker) {
        int rand1;
        int rand2;
        int boardSize = board.getBoardSize();
        boolean madeMove = false;

        while (!madeMove) {
            rand1 = (int) (Math.random() * boardSize);
            rand2 = (int) (Math.random() * boardSize);

            if (!board.isTileMarked(rand1, rand2)) {
                setX(rand1);
                setY(rand2);
                madeMove = true;
            }
        }
    }
}