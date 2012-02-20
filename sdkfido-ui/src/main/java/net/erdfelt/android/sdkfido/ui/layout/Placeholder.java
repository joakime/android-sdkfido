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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Placeholder extends JPanel {
    private static final long  serialVersionUID = -7344932201253810354L;

    private static final int[] colors           = { 0xFF0000, 0x800000, 0xFFFF00, 0x808000, 0x00FF00, 0x008000,
            0x00FFFF, 0x008080, 0x0000FF, 0x000080, 0xFF00FF, 0x800080 };
    private static int         nextColor        = 0;

    public Placeholder(String text) {
        super(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        filler.setOpaque(false);
        setOpaque(true);
        setBackground(new Color(colors[nextColor]));
        setLayout(new BorderLayout());
        add(filler, BorderLayout.CENTER);

        nextColor++;
        if (nextColor >= colors.length) {
            nextColor = 0;
        }
    }
}
