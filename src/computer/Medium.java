
/*
 * Medium bot mode
 */

package computer;

import algorithm.MinimaxAB;
import components.Board;

public class Medium extends Computer {
    public Medium() {
    }

    public void makeTurn(Board board, char marker, char playerMarker) {
        int[] move = MinimaxAB.calculateNextMove(board, 3);
        if (move == null) {
            canMakeTurn = false;
            return;
        }

        setX(move[0]);
        setY(move[1]);
    }
}
