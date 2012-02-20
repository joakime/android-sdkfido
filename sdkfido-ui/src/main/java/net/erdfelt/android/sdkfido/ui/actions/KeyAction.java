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
