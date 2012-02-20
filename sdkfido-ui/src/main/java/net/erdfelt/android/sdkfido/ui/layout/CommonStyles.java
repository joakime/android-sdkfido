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
package net.erdfelt.android.sdkfido.ui.layout;

public class CommonStyles {
    public static GBCStyles baseline() {
        GBCStyles styles = new GBCStyles();

        styles.define("label").margin(5, 5, 0, 0).left();
        styles.define("value").margin(5, 5, 0, 5).fillWide();
        styles.define("dirtext").margin(5, 5, 0, 5).fillWide();
        styles.define("dirpicker").margin(5, 5, 0, 5);
        styles.define("table").margin(5, 5, 0, 5).fillBoth();
        styles.define("progressbar").margin(5, 5, 0, 5).fillWide();
        styles.define("button").margin(0, 2, 0, 2);
        styles.define("button_bar").margin(5, 5, 5, 5).left().fillWide();

        return styles;
    }
}
