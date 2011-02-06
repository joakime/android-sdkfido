package net.erdfelt.android.sdkfido.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

public class WindowUtils {
    public static void centerWindowOnParent(Window win) {
        Component comp = win.getParent();
        if (comp == null) {
            centerWindowOnScreen(win);
            return;
        }

        win.setLocationRelativeTo(comp);
        Dimension compSize = comp.getSize();
        Point compCenter = new Point(compSize.width / 2, compSize.height / 2);
        Point winCenterRelative = new Point(win.getSize().width / 2, win.getSize().height / 2);
        win.setLocation((compCenter.x - winCenterRelative.x), (compCenter.y - winCenterRelative.y));
    }

    public static void centerWindowOnScreen(Window win) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        Point screenCenter = new Point(screenSize.width / 2, screenSize.height / 2);
        Point winCenterRelative = new Point(win.getSize().width / 2, win.getSize().height / 2);
        win.setLocation((screenCenter.x - winCenterRelative.x), (screenCenter.y - winCenterRelative.y));
    }

    public static void centerWindowOnPrimaryScreen(Window win) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        
        Rectangle bounds = gs[0].getDefaultConfiguration().getBounds();
        Point screenCenter = new Point(bounds.width / 2, bounds.height / 2);
        Point winCenterRelative = new Point(win.getSize().width / 2, win.getSize().height / 2);
        win.setLocation((screenCenter.x - winCenterRelative.x), (screenCenter.y - winCenterRelative.y));
    }

    public static boolean isResizable(Window win) {
        boolean resizable = false;

        if (win instanceof Frame) {
            resizable = ((Frame) win).isResizable();
        } else if (win instanceof Dialog) {
            resizable = ((Dialog) win).isResizable();
        }

        return resizable;
    }
}
