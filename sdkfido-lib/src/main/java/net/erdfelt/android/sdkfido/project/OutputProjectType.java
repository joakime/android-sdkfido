package net.erdfelt.android.sdkfido.project;

import net.erdfelt.android.sdkfido.configer.ConfigOption;

public enum OutputProjectType {
    @ConfigOption(description="Create /sources/ folder in Android SDK")
    SDK_SOURCE,
    @ConfigOption(description="Create 1 massive Ant Project")
    ANT_BUILD, 
    @ConfigOption(description="Create 1 massive Maven Project")
    MAVEN_BUILD, 
    @ConfigOption(description="Create multi-module Maven Project")
    MAVEN_BUILD_MULTI;
}
