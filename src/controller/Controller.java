package controller;

/*
 * The Controller class is responsible for requesting the model
 * to update its state whenever there is an event on a button on
 * the game board.
 */

import model.ConfigModel;
import model.GameModel;
import model.HistoryModel;
import model.MenuModel;
import utils.Constants;

import java.util.ArrayList;

public class Controller {
    ConfigModel cm;
    MenuModel mm;
    GameModel gm;
    HistoryModel hm;

    // initializing the reference of model class
    public void setConfigModel(ConfigModel m) {
        this.cm = m;
    }

    public void setConfigModel(int gameMode, int boardSize) {
        cm.setGameMode(gameMode);
        cm.setBoardSize(boardSize);
    }

    public void setMenuModel(MenuModel m) {
        this.mm = m;
    }

    public void setGameModel(GameModel m) {
        this.gm = m;
    }

    public void setHistoryModel(HistoryModel m) {
        this.hm = m;
    }

    public void setGameRequest(ArrayList<Integer> position) {
        gm.PlayMove(position.get(0), position.get(1));
    }

    public void setGamePlayerNames(String p1, String p2) {
        gm.setFirstPlayerName(p1);
        gm.setSecondPlayerName(p2);
    }

    public void setGameSaveHistoryRequest() {
        gm.saveHistory();
    }

    public void setGameSaveRequest() {
        gm.saveModel();
    }

    public void setGameResumeRequest() {
        gm.requestResume();
    }

    public void setGameResetRequest() {
        gm.ResetModel();
    }

    public void setUIRequest(int request) {
        switch (request) {
            case Constants.UIConstants.UI_MENU:
                mm.requestView();
                break;
            case Constants.UIConstants.UI_SELECTION:
                cm.requestView();
                break;
            case Constants.UIConstants.UI_GAME:
                gm.setGameConfiguration(cm.getGameMode(),
                        cm.getBoardSize());
                gm.requestView();
                break;
            case Constants.UIConstants.UI_HISTORY:
                hm.requestView();
            default:
                break;
        }
    }
}
