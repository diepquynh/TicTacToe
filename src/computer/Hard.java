
/*
 * Hard bot mode
 */

package computer;

import algorithm.MinimaxAB;
import components.Board;

public class Hard extends Computer {
    public Hard() {
    }

    @Override
    public void makeTurn(Board board, char marker, char playerMarker) {
        int[] move = MinimaxAB.calculateNextMove(board, 4);
        if (move == null) {
            canMakeTurn = false;
            return;
        }

        setX(move[0]);
        setY(move[1]);
    }
}