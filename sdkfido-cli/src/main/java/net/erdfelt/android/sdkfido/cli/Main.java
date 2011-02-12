package net.erdfelt.android.sdkfido.cli;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.Config;
import net.erdfelt.android.sdkfido.Fetcher;
import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.local.AndroidPlatform;
import net.erdfelt.android.sdkfido.local.AndroidPlatformNotFoundException;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.logging.Logging;
import net.erdfelt.android.sdkfido.sdks.AndroidSdks;
import net.erdfelt.android.sdkfido.sdks.SDKNotFoundException;
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
    @Argument(index = 0, metaVar = "<sdk version>", usage = "SDK to fetch")
    private String  sdkVersion;
    @Option(name = "--verbose")
    private boolean verbose            = false;
    @Option(name = "--debug")
    private boolean debug              = false;
    @Option(name = "--dry-run", usage = "Show task list that would run")
    private boolean dryRun             = false;

    public static void main(String[] args) {
        Logging.config();

        Main main = new Main();
        CmdLineParser parser = new CmdLineParser(main);

        try {
            parser.parseArgument(args);
            main.execute();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar sdkfido-cli.jar [options...] <sdk version>");
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
            AndroidPlatform platform = fetcher.getPlatforms().getPlatform("android-" + sdk.getApilevel());
            TaskQueue tasks = fetcher.getFetchTasks(sdk, platform);
            while(tasks.hasTask()) {
                Task task = tasks.remove();
                System.out.printf("Task: %s - %s%n", task.getClass().getSimpleName(), task.getName());
                if (dryRun) {
                    continue; // skip furthor processing
                }
                ConsoleTaskListener listener = new ConsoleTaskListener();
                task.run(listener, tasks);
            }
        } catch (AndroidPlatformNotFoundException e) {
            System.err.println("Android Platform [" + e.getMessage() + "] is unavailable. choose another one.");
            showSdkList(fetcher);
        } catch (SDKNotFoundException e) {
            System.err.println("SDK version [" + e.getMessage() + "] is unavailable. choose another one.");
            showSdkList(fetcher);
        } catch (Throwable e) {
            System.err.println("Task Execution Failure.");
            e.printStackTrace(System.err);
        }
    }

    private void showSdkList(Fetcher fetcher) {
        System.out.println("Available Android Java Platforms:");
        LocalAndroidPlatforms platforms = fetcher.getPlatforms();
        AndroidSdks sdks = fetcher.getSdks();

        System.out.printf("Version | API | Status%n");
        for (Sdk sdk : sdks) {
            try {
                AndroidPlatform platform = platforms.getPlatform("android-" + sdk.getApilevel());
                System.out.printf("%-7s | %3d | %s%n", sdk.getVersion(), sdk.getApilevel(), "Available (" + platform.getDescription() + ")");
            } catch (AndroidPlatformNotFoundException e) {
                System.out.printf("%-7s | %3d | %s%n", sdk.getVersion(), sdk.getApilevel(), "Unavailable (go install it in your Android SDK Tools)");
            }
        }
    }
}
