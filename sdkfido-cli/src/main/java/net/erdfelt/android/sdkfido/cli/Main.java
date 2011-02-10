package net.erdfelt.android.sdkfido.cli;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.Config;
import net.erdfelt.android.sdkfido.Fetcher;
import net.erdfelt.android.sdkfido.SDKNotFoundException;
import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.local.AndroidPlatform;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.logging.Logging;
import net.erdfelt.android.sdkfido.sdks.AndroidSdks;
import net.erdfelt.android.sdkfido.sdks.Sdk;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Main {
    @Option(name = "--androidsdk", usage = "Location of Android Java SDK", metaVar = "DIR")
    private File    sdkDir;
    @Option(name = "--workdir", usage = "Location of Work Directory", metaVar = "DIR")
    private File    workDir;
    @Option(name = "--projectsdir", usage = "Location of Generated SDK Projects", metaVar = "DIR")
    private File    projectsDir;
    @Option(name = "--ant", usage = "Enable generation of Ant Build Script")
    private boolean generateAntBuild   = false;
    @Option(name = "--maven", usage = "Enable generation of Maven Build Script")
    private boolean generateMavenBuild = false;
    @Argument(index = 0, metaVar = "Version", usage = "SDK to fetch")
    private String  sdkVersion;
    @Option(name = "--verbose")
    private boolean verbose            = false;
    @Option(name = "--debug")
    private boolean debug              = false;

    public static void main(String[] args) {
        Logging.config();

        Main main = new Main();
        CmdLineParser parser = new CmdLineParser(main);

        try {
            parser.parseArgument(args);
            main.execute();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar sdkfido-cli.jar [options...] arguments...");
            parser.printUsage(System.err);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    private void execute() throws IOException {
        Fetcher fetcher = new Fetcher();

        Config config = new Config();
        if (sdkDir != null) {
            config.setPlatformsDir(sdkDir);
        }
        if (workDir != null) {
            config.setWorkDir(workDir);
        }
        if (projectsDir != null) {
            config.setProjectsDir(projectsDir);
        }

        config.setGenerateAntBuild(generateAntBuild);
        config.setGenerateMavenBuild(generateMavenBuild);

        fetcher.setConfig(config);

        try {
            Sdk sdk = fetcher.getSdks().getSdkByVersion(sdkVersion);
            TaskQueue tasks = fetcher.getFetchTasks(sdk);
            for (Task task : tasks) {
                System.out.printf("Task: %s - %s%n", task.getClass().getSimpleName(), task.getName());
            }
        } catch (SDKNotFoundException e) {
            showSdkList(fetcher);
        }
    }

    private void showSdkList(Fetcher fetcher) {
        System.out.println("Available Android Java Platforms:");
        LocalAndroidPlatforms platforms = fetcher.getPlatforms();
        AndroidSdks sdks = fetcher.getSdks();

        System.out.printf("Version | API | Status%n");
        for (Sdk sdk : sdks) {
            AndroidPlatform platform = platforms.getPlatform("android-" + sdk.getApilevel());
            String status = "Unknown";
            if (platform == null) {
                status = "Unavailable (go install it in your Android SDK Tools)";
            } else {
                status = "Available (" + platform.getDescription() + ")";
            }
            System.out.printf("%-7s | %3d | %s%n", sdk.getVersion(), sdk.getApilevel(), status);
        }
    }
}
