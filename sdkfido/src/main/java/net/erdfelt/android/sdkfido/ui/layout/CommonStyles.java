package net.erdfelt.android.sdkfido.ui.layout;

public class CommonStyles {
    public static GBCStyles baseline() {
        GBCStyles styles = new GBCStyles();

        styles.define("label").margin(5, 5, 0, 0).left();
        styles.define("value").margin(5, 5, 0, 5).fillWide();
        styles.define("button").margin(0, 2, 0, 2);
        styles.define("button_bar").margin(5, 5, 5, 5);

        return styles;
    }
}
