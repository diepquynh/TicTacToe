package model;

/*
 * The model class is where the current state of the game
 * as well as the winning logic resides. The model class calls
 * the view to update the gui according to the current state of
 * the game.
 */

import components.Board;
import components.Mark;
import computer.Computer;
import computer.Easy;
import computer.Hard;
import computer.Medium;
import utils.Constants;
import utils.DatabaseUtils;
import utils.GameLogger;
import view.GameUI;

import java.sql.SQLException;

import static components.Mark.*;

public class GameModel {
    private GameUI v;
    private int currPlayerId;
    private Board board;
    private String message;
    private int boardSize;
    private int gameMode;
    private int[] scores;
    private String firstPlayerName;
    private String secondPlayerName;

    // default constructor
    public GameModel() {
        this.boardSize = Constants.BOARD_SIZE_DEFAULT;
        this.board = new Board(boardSize);
        this.currPlayerId = 1;
        this.gameMode = Constants.GameModes.MODE_HUMAN;
        this.scores = new int[2];
    }

    // initializing the reference of view class
    public void registerView(GameUI v) {
        this.v = v;
    }

    //setters and getters
    public int getCurrPlayerId() {
        return currPlayerId;
    }

    public void setCurrPlayerId(int currPlayerId) {
        this.currPlayerId = currPlayerId;
    }

    public int getMovesCount() {
        return this.board.getMovesCount();
    }

    public void setMovesCount(int movesCount) {
        this.board.setMovesCount(movesCount);
    }

    public char[][] getBoard() {
        return board.getBoard();
    }

    public void setBoard(char[][] board) {
        this.board.setBoard(board);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getBoardSize() {
        return board.getBoardSize();
    }

    public void setBoardSize(int boardSize) {
        if (boardSize < 3)
            return;

        this.board.setBoardSize(boardSize);
        this.boardSize = boardSize;
        this.ResetModel();
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public String getFirstPlayerName() {
        return firstPlayerName;
    }

    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    public String getSecondPlayerName() {
        return secondPlayerName;
    }

    public void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
    }

    public Computer getBot() {
        Computer c = null;

        switch (getGameMode()) {
            case Constants.GameModes.MODE_COMPUTER_EASY:
                c = new Easy();
                break;
            case Constants.GameModes.MODE_COMPUTER_MEDIUM:
                c = new Medium();
                break;
            case Constants.GameModes.MODE_COMPUTER_HARD:
                c = new Hard();
                break;
        }

        return c;
    }

    // function to update the board model
    public void PlayMove(int x, int y) {
        if (board.getMovesCount() > 0) {
            // mark the board with x or o depending on playerId
            if (currPlayerId % 2 != 0) {
                board.setMarkAt(x, y, X);
            } else {
                board.setMarkAt(x, y, O);
            }

            // reduce the count of moves left
            board.setMovesCount(board.getMovesCount() - 1);

            // check if playerId has won or if game is tied,
            // and send message accordingly to view, also update the view
            if (isWinner(x, y)) {
                String player = currPlayerId % 2 != 0 ?
                        getFirstPlayerName() : getSecondPlayerName();
                setMessage("Player " + player + " is the Winner!");
                setWinner(x, y, false);
            } else if (board.getMovesCount() == 0) {
                setMessage("No Winner! Game ended in a Tie");
                setWinner(x, y, true);
            } else {
                if (currPlayerId % 2 != 0) {
                    // toggle the playerId
                    setCurrPlayerId(2);
                    setMessage("'O':  Player " + getSecondPlayerName());
                } else {
                    setCurrPlayerId(1);
                    setMessage("'X':  Player " + getFirstPlayerName());
                }

                // update the board with message for next player
                v.update(x, y, board.getMarkAt(x, y), getMessage());
            }
        }

        // Human always moves first
        if (getGameMode() != Constants.GameModes.MODE_HUMAN &&
                currPlayerId % 2 == 0 && !board.isGameEnded()) {
            Computer c = this.getBot();
            c.makeTurn(board, 'O', 'X');
            if (!c.botCanMakeTurn()) {
                setMessage("No Winner! Game ended in a Tie");
                setWinner(x, y, true);
            } else {
                PlayMove(c.getX(), c.getY());
            }
        }

        try {
            if (board.isGameEnded())
                DatabaseUtils.resetResumeDatabase();
        } catch (SQLException e) {
            GameLogger.logToFile(e.getMessage());
        }
    }

    private void setWinner(int x, int y, boolean isTie) {
        if (!isTie) {
            scores[currPlayerId % 2 != 0 ? 0 : 1]++;
            v.setScore(scores[0] + "-" + scores[1]);
        }

        v.isWinner(x, y, board.getMarkAt(x, y), getMessage());

        board.setGameEnded(true);
    }

    // function to check if there is a winner
    private boolean isWinner(int x, int y, int xFront, int xBack,
                             int yFront, int yBack) {
        int winLength = board.getBoardSize() <= 5 ? board.getBoardSize() : 5;
        boolean winCondition = false;
        boolean frontCountered = false;
        boolean backCountered = false;
        int xPos, yPos;
        int count = 0;
        Mark otherSymbol;
        Mark symbol;
        if (getCurrPlayerId() % 2 != 0) {
            symbol = X;
            otherSymbol = O;
        } else {
            symbol = O;
            otherSymbol = X;
        }

        // Check front positions
        xPos = x;
        yPos = y;
        while (true) {
            if (xPos == board.getBoardSize() || yPos == board.getBoardSize()
                    || xPos < 0 || yPos < 0)
                break;

            if (board.getMarkAt(xPos, yPos) == otherSymbol)
                frontCountered = true;

            if (board.getMarkAt(xPos, yPos) != symbol)
                break;

            count++;
            xPos += xFront;
            yPos += yFront;
        }

        // Check back positions
        xPos = x + xBack;
        yPos = y + yBack;
        while (true) {
            if (xPos == board.getBoardSize() || yPos == board.getBoardSize()
                    || xPos < 0 || yPos < 0)
                break;

            if (board.getMarkAt(xPos, yPos) == otherSymbol)
                backCountered = true;

            if (board.getMarkAt(xPos, yPos) != symbol)
                break;

            count++;
            xPos += xBack;
            yPos += yBack;
        }

        winCondition = count > winLength ||
                (count == winLength && !(frontCountered && backCountered));

        return winCondition;
    }

    /*
     * Horizontal: xFront = 1, xBack = -1, yFront = 0, yBack = 0
     * Vertical: xFront = 0, xBack = 0, yFront = 1, yBack = -1
     * Left diag: xFront = 1, xBack = -1, yFront = 1, yBack = -1
     * Right diag: xFront = -1, xBack = 1, yFront = 1, yBack = -1
     */
    private boolean isWinner(int x, int y) {
        return isWinner(x, y, 1, -1, 0, 0) ||
                isWinner(x, y, 0, 0, 1, -1) ||
                isWinner(x, y, 1, -1, 1, -1) ||
                isWinner(x, y, -1, 1, 1, -1);
    }

    // function to clear the model and reset it to initial state
    public void ResetModel() {
        board.setBoardSize(boardSize);
        setCurrPlayerId(1);
        setMessage("");
        board.setGameEnded(false);
        if (v != null) {
            v.setPlayerTurnText("Player " + getFirstPlayerName() + " to play 'X'");
            v.resetGame();
        }
    }

    public void setGameConfiguration(int gameMode, int boardSize) {
        this.setGameMode(gameMode);
        this.setBoardSize(boardSize);
        v.setGameConfiguration(boardSize);
    }

    public void requestView() {
        setScores(new int[2]);
        ResetModel();
        v.setPlayerTurnText("Player " + getFirstPlayerName() + " to play 'X'");
        v.requestView();
    }

    public void saveHistory() {
        try {
            if (!board.isGameEnded())
                return;

            GameModel gm = this;
            DatabaseUtils.saveHistoryToDatabase(gm);
        } catch (SQLException e) {
            v.showFailedDbConnection();
            GameLogger.logToFile(e.getMessage());
        }
    }

    public void saveModel() {
        try {
            if (board.isGameEnded() || !v.checkSave())
                return;

            GameModel gm = this;
            DatabaseUtils.saveGameDataToDatabase(gm);
        } catch (SQLException e) {
            v.showFailedDbConnection();
            GameLogger.logToFile(e.getMessage());
        }
    }

    public void requestResume() {
        try {
            GameModel gm = DatabaseUtils.loadGameFromDatabase();
            if (gm == null) {
                v.showResumeNotFound();
                return;
            }

            if (!v.checkResume())
                return;

            this.setCurrPlayerId(gm.getCurrPlayerId());
            this.setBoardSize(gm.getBoardSize());
            this.setMovesCount(gm.getMovesCount());
            this.setBoard(gm.getBoard());
            this.setGameMode(gm.getGameMode());
            this.setScores(gm.getScores());
            this.setFirstPlayerName(gm.getFirstPlayerName());
            this.setSecondPlayerName(gm.getSecondPlayerName());

            v.setGameConfiguration(this.getBoardSize());
            if (currPlayerId % 2 != 0) {
                // toggle the playerId
                setMessage("'O':  Player " + getFirstPlayerName());
            } else {
                setMessage("'X':  Player " + getSecondPlayerName());
            }

            for (int i = 0; i < this.getBoardSize(); i++) {
                for (int j = 0; j < this.getBoardSize(); j++) {
                    if (!board.isTileMarked(i, j))
                        continue;

                    v.update(i, j, board.getMarkAt(i, j), getMessage());
                }
            }
            v.setScore(scores[0] + "-" + scores[1]);
            v.requestResume();
        } catch (SQLException e) {
            v.showFailedDbConnection();
            GameLogger.logToFile(e.getMessage());
        }
    }
}
