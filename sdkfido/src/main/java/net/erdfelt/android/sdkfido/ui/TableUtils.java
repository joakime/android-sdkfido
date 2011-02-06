package net.erdfelt.android.sdkfido.ui;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public final class TableUtils {

    public static void setMinimumHeight(int height, JTable table, JScrollPane scroller) {
        Dimension dim = table.getPreferredScrollableViewportSize();
        dim.height = height;
        table.setPreferredScrollableViewportSize(dim);
        table.setMinimumSize(dim);
        // table.setFillsViewportHeight(true);
        
        Dimension dimHeader = table.getTableHeader().getPreferredSize();
        dim.height += dimHeader.height;
        scroller.setMinimumSize(dim);
    }
}
