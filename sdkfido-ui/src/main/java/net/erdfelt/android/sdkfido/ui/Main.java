package net.erdfelt.android.sdkfido.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import net.erdfelt.android.sdkfido.logging.Logging;

public class Main {
    public static void main(String[] args) {
        Logging.config();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SdkFidoFrame ui = new SdkFidoFrame();
                    ui.setVisible(true);
                } catch (Throwable t) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Unhandled Throwable", t);
                }
            }
        });
    }
}
