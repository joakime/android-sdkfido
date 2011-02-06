package net.erdfelt.android.sdkfido.ui.debug;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class DebugComponent implements ComponentListener {
    private String              name;

    public DebugComponent(String name) {
        this.name = name;
        DEBUG("[%s] Listening to Component Events", name);
    }

    private void DEBUG(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Component c = e.getComponent();
        DEBUG("[%s] Resize: %d x %d", name, c.getWidth(), c.getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        Component c = e.getComponent();
        DEBUG("[%s] Moved: %d, %d", name, c.getX(), c.getY());
    }

    @Override
    public void componentShown(ComponentEvent e) {
        DEBUG("[%s] Shown", name);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        DEBUG("[%s] Hidden", name);
    }
}
