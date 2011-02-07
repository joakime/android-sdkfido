/*
 * Copyright 2009-2011 - Joakim Erdfelt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.erdfelt.android.sdkfido.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;


public class WindowHandler extends WindowAdapter {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(WindowHandler.class.getName());
    
    private static final String KEY_WIN_HEIGHT = "size.height";
    private static final String KEY_WIN_WIDTH = "size.width";
    private static final String KEY_WIN_X = "location.x";
    private static final String KEY_WIN_Y = "location.y";

    private boolean persistLocation;
    private boolean persistSize;
    private Dimension sizePreferred;
    private boolean systemExit;
    private Window win;
    private String winname;

    public WindowHandler(Window w) {
        this(w, false);
    }

    public WindowHandler(Window w, boolean exit) {
        if (w == null) {
            throw new IllegalArgumentException("Window cannot be null.");
        }
        
        if ( (w.getName() == null) || (w.getName().trim().length() <= 0) ){
            throw new IllegalArgumentException("Unable to restore window preferences from nameless window.");
        }
        this.win = w;
        this.winname = w.getName();
        this.systemExit = exit;
        this.persistLocation = true;
        this.persistSize = true;
        this.sizePreferred = w.getSize();
    }

    public void close() {
        locationSave();
        this.win.setVisible(false);
        this.win.dispose();
        if (this.systemExit) {
            System.exit(0);
        }
    }

    /**
     * @return Returns the sizePreferred.
     */
    public Dimension getSizePreferred() {
        return sizePreferred;
    }

    public boolean isPersisted() {
        return (this.persistLocation || this.persistSize);
    }

    /**
     * @return Returns the persistLocation.
     */
    public boolean isPersistLocation() {
        return persistLocation;
    }

    /**
     * @return Returns the persistSize.
     */
    public boolean isPersistSize() {
        return persistSize;
    }
    
    private int getInt(String key, int def) {
        return Prefs.getInt(winname + "." + key, def);
    }
    
    private void setInt(String key, int value) {
        Prefs.setInt(winname + "." + key, value);
    }

    public void locationRestore() {
        // calculate a centered location.
        Dimension size = this.sizePreferred;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - size.width) / 2;
        int y = (screenSize.height - size.height) / 2;
        
        if (this.persistLocation) {
            x = getInt(KEY_WIN_X, x);
            y = getInt(KEY_WIN_Y, y);
        }
        this.win.setLocation(x, y);

        if (this.persistSize) {
            int width = getInt(KEY_WIN_WIDTH, this.sizePreferred.width);
            int height = getInt(KEY_WIN_HEIGHT, this.sizePreferred.height);
            this.win.setSize(width, height);
        }
    }

    public void locationSave() {
        if (!isPersisted()) {
            return;
        }

        if (this.persistLocation) {
            Point loc = this.win.getLocation();
            setInt(KEY_WIN_X, loc.x);
            setInt(KEY_WIN_Y, loc.y);
        }

        if (this.persistSize) {
            Dimension size = this.win.getSize();
            setInt(KEY_WIN_WIDTH, size.width);
            setInt(KEY_WIN_HEIGHT, size.height);
        }

        Prefs.save();
    }

    /**
     * @param persistLocation The persistLocation to set.
     */
    public void setPersistLocation(boolean persistLocation) {
        this.persistLocation = persistLocation;
    }

    /**
     * @param persistSize The persistSize to set.
     */
    public void setPersistSize(boolean persistSize) {
        this.persistSize = persistSize;
    }

    /**
     * @param sizePreferred The sizePreferred to set.
     */
    public void setSizePreferred(Dimension sizePreferred) {
        this.sizePreferred = sizePreferred;
        locationRestore();
    }

    /**
     * 
     * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent e) {
        close();
    }
}
