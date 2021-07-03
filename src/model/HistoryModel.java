package model;

import components.HistoryNode;
import utils.DatabaseUtils;
import utils.GameLogger;
import view.HistoryUI;

import java.sql.SQLException;

public class HistoryModel {
    private HistoryNode[] historyNodes;
    private HistoryUI v;

    public HistoryNode[] getHistoryNodes() {
        return historyNodes;
    }

    public void setHistoryNodes(HistoryNode[] historyNodes) {
        this.historyNodes = historyNodes;
    }

    public void registerView(HistoryUI v) {
        this.v = v;
    }

    public void requestView() {
        try {
            setHistoryNodes(DatabaseUtils.loadHistoryFromDatabase());
            v.requestView(this);
        } catch (SQLException e) {
            GameLogger.logToFile(e.getMessage());
        }
    }
}
