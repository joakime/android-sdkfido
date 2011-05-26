Android SDK Fido
================

Fetch that AOSP version and create a local project for it.

This project was created to create a source jar for the AOSP branch/tag so that
it can be used for in-place source browsing while in an IDE.

Many developers have asked for the source jar from Google by filing and starring
a now famous [code.google.com issue #979](http://code.google.com/p/android/issues/detail?id=979)

So, just to scratch an itch of my own, I decided to wrap up a jgit client, some
metadata describing where to find the source, how to assemble it, and put some
maven project generation logic into the mix as well.

Quick Start
-----------

Grab the code, get yourself a copy of Maven 3.0 and compile the tree.

    [android-sdkfido]$ mvn clean install

Now go into the sdkfido project and run the main jar (will automatically pull
in the associated jars in /lib/ by itself, no need to create a classpath yourself)

    [android-sdkfido]$ cd sdkfido
    [sdkfido]$ java -Xmx512m -jar target/sdkfido-1.0-SNAPSHOT.jar --outputType MAVEN_MULTI --outputDirectory $HOME/maven-android froyo

At this point you should see the git processes to checkout the various git trees,
and setup the maven multimodule project tree.
Change into this tree and perform the maven build.

    [sdkfido]$ cd $HOME/maven-android/android-froyo
    [android-froyo]$ mvn clean install
    
Now you have a full maven build, and the various maven artifacts in your local
maven repository (usually at `$HOME/.m2/repository/com/android/sdk/`)

Configuration
-------------

There are several configurables present on the sdkfido command line.
To access the list just call sdkfido without any arguments.

    [sdkfido]$ java -jar target/sdkfido-1.0-SNAPSHOT.jar
    
Example below:

    Usage: java -jar net.erdfelt.android.sdkfido.Main [options...] [fetch-targets...]
    
    .\ Miscellaneous \.___________________________________________________________
    --config <File>                  : the filename to persist configuration options in
                                       (default value: /Users/joakim/.sdkfido/config.properties)
    --help                           : show this help screen
    --save-config                    : save configuration options to disk
    
    .\ Options \._________________________________________________________________
    --dryRun <boolean>               : Dry Run (show what would be done, but dont do it)
                                       (default value: false)
    --maven.artifactId <String>      : Maven Artifact ID (used as prefix in MAVEN_MULTI ProjectType)
    --maven.groupId <String>         : Maven Group ID
    --maven.includeStubJar <boolean> : Include SDK android.jar (stub) as attached artifact in build
    --outputDir <Dir>                : Output Directory (ignored for SDK ProjectType)
                                       (default value: /Users/joakim/.sdkfido/projects)
    --outputType <ProjectType>       : Output Project Type
                                       "SDK" - Create /sources/ folder in Android SDK
                                       "ANT" - Create 1 massive Ant Project
                                       "MAVEN" - Create 1 massive Maven Project
                                       "MAVEN_MULTI" - Create multi-module Maven Project
                                       (default value: SDK)
    --platformsDir <Dir>             : Location of the Android SDK (that has platforms dir)
                                       (default value: /Users/joakim/Java/android/android-sdk-mac_x86)
    --workDir <Dir>                  : Work Directory
                                       (default value: /Users/joakim/.sdkfido/work)
                                       
Fetch Targets
-------------

A Fetch Target in sdkfido is a simple string indicating what you wantn to fetch
and have a project built from.

SdkFido supports target names as APILEVELS, CODENAME, VERSION, TAG, or BRANCH ids.

Running the default, no-argument, command line will show the list of targets
that are available to you.

Example:
    
    Available Android Java Fetch Targets:
    (Sorted newest to oldest within type groupings)
    
      Target               | Type     | API | Version | Codename    | Branch               | SDK Avail?    
    -----------------------+----------+-----+---------+-------------+----------------------+-----------------
      10                   | APILEVEL | 10  | 2.3.3   | gingerbread |                      | Available     
      9                    | APILEVEL | 9   | 2.3     | gingerbread | android-2.3_r1       | Available     
      8                    | APILEVEL | 8   | 2.2     | froyo       | android-sdk-2.2_r2   | Available     
      7                    | APILEVEL | 7   | 2.1     | eclair      | android-sdk-2.1_r1   | Available     
      6                    | APILEVEL | 6   | 2.0.1   | eclair      | android-sdk-2.0.1_r1 | not in sdk dir
      5                    | APILEVEL | 5   | 2.0     | eclair      | android-sdk-2.0_r1   | not in sdk dir
      4                    | APILEVEL | 4   | 1.6     | donut       | android-sdk-1.6_r2   | Available     
      3                    | APILEVEL | 3   | 1.5     | cupcake     | android-sdk-1.5_r3   | Available     
      2                    | APILEVEL | 2   | 1.1     |             |                      | not in sdk dir
      1                    | APILEVEL | 1   | 1.0     |             | android-1.0          | not in sdk dir
      gingerbread          | CODENAME | 10  | 2.3.3   | gingerbread |                      | Available     
      gingerbread          | CODENAME | 9   | 2.3     | gingerbread | android-2.3_r1       | Available     
      froyo                | CODENAME | 8   | 2.2     | froyo       | android-sdk-2.2_r2   | Available     
      eclair               | CODENAME | 7   | 2.1     | eclair      | android-sdk-2.1_r1   | Available     
      eclair               | CODENAME | 6   | 2.0.1   | eclair      | android-sdk-2.0.1_r1 | not in sdk dir
      eclair               | CODENAME | 5   | 2.0     | eclair      | android-sdk-2.0_r1   | not in sdk dir
      donut                | CODENAME | 4   | 1.6     | donut       | android-sdk-1.6_r2   | Available     
      cupcake              | CODENAME | 3   | 1.5     | cupcake     | android-sdk-1.5_r3   | Available     
      2.3.3                | VERSION  | 10  | 2.3.3   | gingerbread |                      | Available     
      2.3                  | VERSION  | 9   | 2.3     | gingerbread | android-2.3_r1       | Available     
      2.2                  | VERSION  | 8   | 2.2     | froyo       | android-sdk-2.2_r2   | Available     
      2.1                  | VERSION  | 7   | 2.1     | eclair      | android-sdk-2.1_r1   | Available     
      2.0.1                | VERSION  | 6   | 2.0.1   | eclair      | android-sdk-2.0.1_r1 | not in sdk dir
      2.0                  | VERSION  | 5   | 2.0     | eclair      | android-sdk-2.0_r1   | not in sdk dir
      1.6                  | VERSION  | 4   | 1.6     | donut       | android-sdk-1.6_r2   | Available     
      1.5                  | VERSION  | 3   | 1.5     | cupcake     | android-sdk-1.5_r3   | Available     
      1.1                  | VERSION  | 2   | 1.1     |             |                      | not in sdk dir
      1.0                  | VERSION  | 1   | 1.0     |             | android-1.0          | not in sdk dir
      android-2.3.2_r1     | TAG      | 9   | 2.3.2   | gingerbread | android-2.3.2_r1     | Available     
      android-2.3.1_r1     | TAG      | 9   | 2.3.1   | gingerbread | android-2.3.1_r1     | Available     
      android-2.3_r1       | TAG      | 9   | 2.3     | gingerbread | android-2.3_r1       | Available     
      android-2.2.1_r2     | TAG      | 8   | 2.2.1   | froyo       | android-2.2.1_r2     | Available     
      android-sdk-2.2_r2   | TAG      | 8   | 2.2     | froyo       | android-sdk-2.2_r2   | Available     
      android-sdk-2.2_r1   | TAG      | 8   | 2.2     | froyo       | android-sdk-2.2_r1   | Available     
      android-sdk-2.1_r1   | TAG      | 7   | 2.1     | eclair      | android-sdk-2.1_r1   | Available     
      android-sdk-2.0.1_r1 | TAG      | 6   | 2.0.1   | eclair      | android-sdk-2.0.1_r1 | not in sdk dir
      android-sdk-2.0_r1   | TAG      | 5   | 2.0     | eclair      | android-sdk-2.0_r1   | not in sdk dir
      android-sdk-1.6_r2   | TAG      | 4   | 1.6     | donut       | android-sdk-1.6_r2   | Available     
      android-sdk-1.6_r1   | TAG      | 4   | 1.6     | donut       | android-sdk-1.6_r1   | Available     
      android-sdk-1.5_r3   | TAG      | 3   | 1.5     | cupcake     | android-sdk-1.5_r3   | Available     
      android-sdk-1.5_r1   | TAG      | 3   | 1.5     | cupcake     | android-sdk-1.5_r1   | Available     
      android-1.0          | TAG      | 1   | 1.0     |             | android-1.0          | not in sdk dir
      gingerbread-release  | BRANCH   | 9   | 2.3     | gingerbread | gingerbread-release  | Available     
      froyo-release        | BRANCH   | 8   | 2.2     | froyo       | froyo-release        | Available     
      eclair-release       | BRANCH   | 5   | 2.0     | eclair      | eclair-release       | not in sdk dir
      donut-release2       | BRANCH   | 4   | 1.6     | donut       | donut-release2       | Available     
      donut-release        | BRANCH   | 4   | 1.6     | donut       | donut-release        | Available     
      cupcake-release      | BRANCH   | 3   | 1.5     | cupcake     | cupcake-release      | Available     
      release-1.0          | BRANCH   | 1   | 1.0     |             | release-1.0          | not in sdk dir
        
    
