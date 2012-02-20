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
