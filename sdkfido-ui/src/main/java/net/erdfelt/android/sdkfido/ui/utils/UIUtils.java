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
package net.erdfelt.android.sdkfido.ui.utils;

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
