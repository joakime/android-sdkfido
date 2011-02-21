package net.erdfelt.android.sdkfido.project;

import net.erdfelt.android.sdkfido.configer.ConfigOption;

public enum OutputProjectType {
    @ConfigOption(description="Create /sources/ folder in Android SDK")
    SDK,
    @ConfigOption(description="Create 1 massive Ant Project")
    ANT, 
    @ConfigOption(description="Create 1 massive Maven Project")
    MAVEN, 
    @ConfigOption(description="Create multi-module Maven Project")
    MAVEN_MULTI;
}
