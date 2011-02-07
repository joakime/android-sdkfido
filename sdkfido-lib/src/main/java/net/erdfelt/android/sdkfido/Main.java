package net.erdfelt.android.sdkfido;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import net.erdfelt.android.sdkfido.ui.SdkFidoFrame;

import org.apache.commons.io.IOUtils;

public class Main {
    public static void main(String[] args) {
        initLogging();

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

    public static void initLogging() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource("logging.properties");
        if (url != null) {
            InputStream in = null;
            try {
                in = url.openStream();
                LogManager.getLogManager().readConfiguration(in);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
    }
}
