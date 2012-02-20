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

import java.io.File;

import javax.swing.JButton;
import javax.swing.JTextField;


public class DirectoryPicker extends JButton {
    private static final long serialVersionUID = -2267073837659182425L;
    private JTextField textField;
    
    public DirectoryPicker() {
        setIcon(Icons.getResource("/icons/folder.gif"));
    }

    public void setTextField(JTextField field) {
        this.textField = field;
    }

    public void setDefaultDirectory(File dir) {
        if(textField != null) {
            textField.setText(dir.getAbsolutePath());
        }
    }
}
