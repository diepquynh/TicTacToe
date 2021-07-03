package view;

import model.*;
import components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class HistoryUI {
    private JFrame gui;
    private JPanel panel;
    private JTable table;
    private DefaultTableModel dtb;

    public HistoryUI() {
    }

    public void requestView(HistoryModel model) {
        if (model.getHistoryNodes() == null) {
            JOptionPane.showMessageDialog(null,
                    "There is no game history", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        this.gui = new JFrame("Game history");
        this.gui.setResizable(false);
        this.gui.setSize(500, 500);
        this.gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.table = new JTable();
        this.panel = new JPanel();

        this.dtb = new DefaultTableModel();
        this.dtb.addColumn("Date");
        this.dtb.addColumn("Game mode");
        this.dtb.addColumn("First player");
        this.dtb.addColumn("Second player");
        this.dtb.addColumn("Score");

        for (HistoryNode node : model.getHistoryNodes()) {
            Vector vector = new Vector();
            vector.add(node.getDate());
            vector.add(node.getGameMode());
            vector.add(node.getFirstPlayer());
            vector.add(node.getSecondPlayer());
            vector.add(node.getScore());
            this.dtb.addRow(vector);
        }

        this.table.setModel(this.dtb);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(145);
        this.panel.add(new JScrollPane(this.table));
        this.gui.add(this.panel, BorderLayout.CENTER);
        this.gui.setVisible(true);
    }
}
