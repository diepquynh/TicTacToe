package components;

/*
 * ComboBox renderer
 * src: https://stackoverflow.com/questions/23882640/how-to-set-the-title-of-a-jcombobox-when-nothing-is-selected
 */

import javax.swing.*;
import java.awt.*;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer {
    private String _title;

    public ComboBoxRenderer(String title) {
        _title = title;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        if (index == -1 && value == null) setText(_title);
        else setText(value.toString());
        return this;
    }
}
