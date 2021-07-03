/*
 * This is the Driver class for the TicTacToe game.
 * It creates objects of Model, View and Controller
 * classes and aggregates them.
 */

import controller.Controller;
import model.ConfigModel;
import model.GameModel;
import model.HistoryModel;
import model.MenuModel;
import utils.DatabaseUtils;
import utils.GameLogger;
import view.GameUI;
import view.HistoryUI;
import view.MenuUI;
import view.SelectionUI;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class GameMain implements Runnable {
    private static JFrame gui = new JFrame("Tic Tac Toe");

    public static JFrame getGui() {
        return gui;
    }

    public void run() {
        // Create game database if not exist
        try {
            DatabaseUtils.createNewDatabase();
        } catch (SQLException e) {
            GameLogger.logToFile(e.getMessage());
        }

        // Initialize app GUI
        Controller c = new Controller();
        GameUI game = new GameUI(getGui());
        GameModel gm = new GameModel();
        SelectionUI secUI = new SelectionUI(getGui());
        ConfigModel cm = new ConfigModel();
        MenuUI menu = new MenuUI(getGui());
        MenuModel mm = new MenuModel();
        HistoryUI hisUI = new HistoryUI();
        HistoryModel hm = new HistoryModel();

        // Configure components, backend models
        gm.registerView(game);
        mm.registerView(menu);
        cm.registerView(secUI);
        hm.registerView(hisUI);
        c.setGameModel(gm);
        c.setMenuModel(mm);
        c.setConfigModel(cm);
        c.setHistoryModel(hm);
        game.setActionListener(c);
        menu.addButtonListener(c);
        secUI.setActionListener(c);
    }

    public static void main(String[] args) throws IOException {
        GameMain obj = new GameMain();
        Thread thread = new Thread(obj);
        thread.start();
    }
} 