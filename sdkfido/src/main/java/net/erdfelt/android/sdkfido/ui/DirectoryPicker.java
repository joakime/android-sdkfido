package net.erdfelt.android.sdkfido.ui;

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
