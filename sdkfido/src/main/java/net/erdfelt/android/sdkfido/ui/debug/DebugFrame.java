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

import net.erdfelt.android.sdkfido.Main;
import net.erdfelt.android.sdkfido.ui.WindowUtils;

public class DebugFrame extends JFrame {
    private static final long serialVersionUID = -4480425800072583247L;
    
    static {
        Main.initLogging();
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
