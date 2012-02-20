/*******************************************************************************
 *    Copyright 2012 - Joakim Erdfelt
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
package net.erdfelt.android.sdkfido.ui.utils;

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
