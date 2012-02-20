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
package net.erdfelt.android.sdkfido.console;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TablePrinter {
    private String[]       headers;
    private int            columnCount;
    private List<String[]> rows = new ArrayList<String[]>();
    private String[]       currentRow;
    private int            currentColIndex;

    public TablePrinter(String... headers) {
        this.headers = headers;
        this.columnCount = this.headers.length;
    }

    public void startRow() {
        currentRow = new String[columnCount];
        currentColIndex = 0;
    }

    public void addCell(Object obj) {
        if (obj == null) {
            addCell("");
        } else {
            addCell(String.valueOf(obj));
        }
    }

    public void addCell(String contents) {
        if (contents == null) {
            currentRow[currentColIndex++] = "";
        } else {
            currentRow[currentColIndex++] = contents;
        }
    }

    public void endRow() {
        rows.add(currentRow);
    }

    public void print(PrintStream out) {
        String indent = "  ";
        
        // Need to calculate widths
        int widths[] = new int[columnCount];

        // Establish baseline values with headers row
        for (int col = 0; col < columnCount; col++) {
            widths[col] = headers[col].length();
        }

        // Now parse all of the rows to grow the column widths to fit.
        for (String[] row : rows) {
            for (int col = 0; col < columnCount; col++) {
                widths[col] = Math.max(row[col].length(), widths[col]);
            }
        }

        // Print Header Row
        out.print(indent);
        boolean delim = false;
        for (int col = 0; col < columnCount; col++) {
            if (delim) {
                out.print(" | ");
            }
            out.print(StringUtils.rightPad(headers[col], widths[col]));
            delim = true;
        }
        out.println();

        // Print Horiz Rule
        out.print("--");
        delim = false;
        for (int col = 0; col < columnCount; col++) {
            if (delim) {
                out.print("-+-");
            }
            out.print(StringUtils.rightPad("", widths[col], '-'));
            delim = true;
        }
        out.println("--");

        // Print Rows
        for (String[] row : rows) {
            out.print(indent);
            delim = false;
            for (int col = 0; col < columnCount; col++) {
                if (delim) {
                    out.print(" | ");
                }
                out.print(StringUtils.rightPad(row[col], widths[col]));
                delim = true;
            }
            out.println();
        }
    }
}
