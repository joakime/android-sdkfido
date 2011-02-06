package net.erdfelt.android.sdkfido.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

public class KeyAction extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = 5630105435464460821L;

    private String command;

    private ActionListener listener;

    /**
     * Create KeyAction
     */
    public KeyAction() {
        command = null;
        listener = null;
    }

    /**
     * Create KeyAction
     * 
     * @param listener
     * @param command
     */
    public KeyAction(ActionListener listener, String command) {
        this.listener = listener;
        this.command = command;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (command != null) {
            ActionEvent nevt = new ActionEvent(e.getSource(), e.getID(), command, e.getWhen(), e.getModifiers());
            if (listener != null) {
                this.listener.actionPerformed(nevt);
            }
        }
    }

    /**
     * @return Returns the command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return Returns the listener.
     */
    public ActionListener getListener() {
        return listener;
    }

    /**
     * @param command The command to set.
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @param listener The listener to set.
     */
    public void setListener(ActionListener listener) {
        this.listener = listener;
    }
}
