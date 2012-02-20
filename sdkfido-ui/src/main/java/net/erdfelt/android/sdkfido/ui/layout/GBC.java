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
package net.erdfelt.android.sdkfido.ui.layout;

import java.awt.GridBagConstraints;

public class GBC extends GridBagConstraints {
    /**
     * 
     */
    private static final long serialVersionUID = 7938601503431301097L;
    // Fill options
    public static final int WIDE = GridBagConstraints.HORIZONTAL;
    public static final int TALL = GridBagConstraints.VERTICAL;
    public static final int BOTH = GridBagConstraints.BOTH;

    // Alignment
    public static final int TOP = GridBagConstraints.NORTH;
    public static final int TOP_RIGHT = GridBagConstraints.NORTHEAST;
    public static final int RIGHT = GridBagConstraints.EAST;
    public static final int BOTTOM_RIGHT = GridBagConstraints.SOUTHEAST;
    public static final int BOTTOM = GridBagConstraints.SOUTH;
    public static final int BOTTOM_LEFT = GridBagConstraints.SOUTHWEST;
    public static final int LEFT = GridBagConstraints.WEST;
    public static final int TOP_LEFT = GridBagConstraints.NORTHWEST;
    public static final int CENTER = GridBagConstraints.CENTER;

    public GBC() {
        super();
    }

    public GBC align(int alignment) {
        this.anchor = alignment;
        return this;
    }

    public GBC center() {
        this.anchor = GridBagConstraints.CENTER;
        return this;
    }

    public GBC endBoth() {
        this.gridwidth = GridBagConstraints.REMAINDER;
        this.gridheight = GridBagConstraints.REMAINDER;
        return this;
    }

    public GBC endCol() {
        this.gridheight = GridBagConstraints.REMAINDER;
        return this;
    }

    public GBC endRow() {
        this.gridwidth = GridBagConstraints.REMAINDER;
        return this;
    }

    public GBC fillBoth() {
        this.weightx = 1.0;
        this.weighty = 1.0;
        this.fill = GridBagConstraints.BOTH;
        return this;
    }

    public GBC fillBoth(int xCells, int yCells) {
        this.weightx = (1.0 * xCells);
        this.weighty = (1.0 * yCells);
        this.fill = GridBagConstraints.BOTH;
        this.gridwidth = xCells;
        this.gridwidth = yCells;
        return this;
    }

    public GBC fillTall() {
        this.weighty = 1.0;
        this.fill = GridBagConstraints.VERTICAL;
        return this;
    }

    public GBC fillWide() {
        this.weightx = 1.0;
        this.fill = GridBagConstraints.HORIZONTAL;
        return this;
    }

    public GBC left() {
        this.anchor = GridBagConstraints.WEST;
        return this;
    }

    public GBC margin(int top, int left, int bottom, int right) {
        this.insets.top = top;
        this.insets.left = left;
        this.insets.bottom = bottom;
        this.insets.right = right;
        return this;
    }

    public GBC marginBottom(int bottom) {
        this.insets.bottom = bottom;
        return this;
    }

    public GBC marginLeft(int left) {
        this.insets.left = left;
        return this;
    }

    public void relativeX() {
        this.gridx = GridBagConstraints.RELATIVE;
    }

    public void reset() {
        this.gridx = RELATIVE;
        this.gridy = RELATIVE;
        this.gridwidth = 1;
        this.gridheight = 1;

        this.weightx = 0;
        this.weighty = 0;
        this.anchor = CENTER;
        this.fill = NONE;

        this.insets.set(0, 0, 0, 0);
        this.ipadx = 0;
        this.ipady = 0;
    }

    public GBC right() {
        this.anchor = GridBagConstraints.EAST;
        return this;
    }

    public GBC spanCol(int count) {
        this.gridwidth = count;
        return this;
    }

    public GBC spanRow(int count) {
        this.gridheight = count;
        return this;
    }

    public GBC stretch(int mode) {
        this.fill = mode;
        return this;
    }

    public GBC weightTall(double weight) {
        this.weighty = weight;
        return this;
    }

    public GBC weightWide(double weight) {
        this.weightx = weight;
        return this;
    }

    public GBC x(int xgrid) {
        this.gridx = xgrid;
        return this;
    }

    public GBC xy(int x, int y) {
        this.gridx = x;
        this.gridy = y;
        return this;
    }

    public GBC y(int ygrid) {
        this.gridy = ygrid;
        return this;
    }
}
