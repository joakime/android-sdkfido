package net.erdfelt.android.sdkfido.ui;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UIUtils {
    private static final Log log = LogFactory.getLog(UIUtils.class);

    public static void setDefaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.warn("Unable to set System Look and Feel.", e);
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
    }

    public static void setJavaLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            log.warn("Unable to set Java Look and Feel.", e);
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
    }
}
