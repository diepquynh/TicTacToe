
/*
 * Board object, responsible for the game board
 */

package components;

import utils.Constants;

import java.util.ArrayList;

import static components.Mark.BLANK;

public class Board {
    private Mark[][] board;
    private boolean isGameEnded;
    private int movesCount;
    private int boardSize;

    public Board() {
        this.boardSize = Constants.BOARD_SIZE_DEFAULT;
        this.isGameEnded = false;
        this.board = new Mark[boardSize][boardSize];
        initialiseBoard();
    }

    public Board(Board board) {
        this.setBoardSize(board.getBoardSize());
        this.setBoard(board.getBoard());
        this.setGameEnded(board.isGameEnded());
        this.setMovesCount(board.getMovesCount());
    }

    public Board(int boardSize) {
        this.boardSize = boardSize;
        this.isGameEnded = false;
        board = new Mark[boardSize][boardSize];
        initialiseBoard();
    }

    public void setBoardSize(int boardSize) {
        if (boardSize < 3)
            return;

        this.boardSize = boardSize;
        this.board = new Mark[boardSize][boardSize];
        initialiseBoard();
    }

    public char[][] getBoard() {
        char[][] board = new char[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = this.board[i][j].getMark();
            }
        }

        return board;
    }

    public void setBoard(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                this.board[i][j] = Mark.toMark(board[i][j]);
            }
        }
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }

    private void initialiseBoard() {
        this.movesCount = this.boardSize * this.boardSize;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = BLANK;
            }
        }
    }

    public boolean isTileMarked(int row, int column) {
        return board[row][column].isMarked();
    }

    @Override
    public String toString() {
        StringBuilder strBldr = new StringBuilder();
        for (Mark[] row : board) {
            for (Mark tile : row) {
                strBldr.append(tile).append(' ');
            }
            strBldr.append("\n");
        }
        return strBldr.toString();
    }

    public Mark getMarkAt(int row, int column) {
        return board[row][column];
    }

    public ArrayList<int[]> generateMoves() {
        ArrayList<int[]> moveList = new ArrayList<int[]>();

        int boardSize = getBoardSize();

        // Look for cells that has at least one stone in an adjacent cell.
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                if (board[i][j].isMarked())
                    continue;

                if (i > 0) {
                    if (j > 0) {
                        if (board[i - 1][j - 1].isMarked() ||
                                board[i][j - 1].isMarked()) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (j < boardSize - 1) {
                        if (board[i - 1][j + 1].isMarked() ||
                                board[i][j + 1].isMarked()) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (board[i - 1][j].isMarked()) {
                        int[] move = {i, j};
                        moveList.add(move);
                        continue;
                    }
                }
                if (i < boardSize - 1) {
                    if (j > 0) {
                        if (board[i + 1][j - 1].isMarked() ||
                                board[i][j - 1].isMarked()) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (j < boardSize - 1) {
                        if (board[i + 1][j + 1].isMarked() ||
                                board[i][j + 1].isMarked()) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (board[i + 1][j].isMarked()) {
                        int[] move = {i, j};
                        moveList.add(move);
                        continue;
                    }
                }

            }
        }

        return moveList;
    }

    public void setMarkAt(int row, int column, Mark newMark) {
        board[row][column] = newMark;
    }

    public int getBoardSize() {
        return boardSize;
    }
}
