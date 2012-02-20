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
package net.erdfelt.android.sdkfido.ui.debug;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import net.erdfelt.android.sdkfido.logging.Logging;
import net.erdfelt.android.sdkfido.ui.utils.WindowUtils;

public class DebugFrame extends JFrame {
    private static final long serialVersionUID = -4480425800072583247L;
    
    static {
        Logging.config();
    }

    public DebugFrame(Component component) {
        super();
        setTitle("Debug Frame: " + component.getClass().getName());
        setLayout(new BorderLayout());
        add(component, BorderLayout.CENTER);
        pack();
        
        Dimension dim = component.getPreferredSize();
        setMinimumSize(dim);
        setSize(dim);
        
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = getRootPane();
        rootPane.registerKeyboardAction(new EscClose(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        WindowUtils.centerWindowOnPrimaryScreen(this);
    }

    class EscClose implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
        }
    }
}
