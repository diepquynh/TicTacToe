package model;

import view.*;
import utils.*;

public class ConfigModel {
    private SelectionUI v;
    private int gameMode;
    private int boardSize;

    public void registerView(SelectionUI v) {
        this.v = v;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public ConfigModel() {
        this.gameMode = Constants.GameModes.MODE_HUMAN;
        this.boardSize = Constants.BOARD_SIZE_DEFAULT;
    }

    public void requestView() {
        v.requestView();
    }

    public boolean getConfigFromFile() {
        return false;
    }
}
