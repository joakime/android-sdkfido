package net.erdfelt.android.sdkfido;

import java.io.IOException;

import net.erdfelt.android.sdkfido.configer.CmdLineParseException;
import net.erdfelt.android.sdkfido.configer.ConfigCmdLineParser;
import net.erdfelt.android.sdkfido.local.AndroidPlatform;
import net.erdfelt.android.sdkfido.local.AndroidPlatformNotFoundException;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.logging.Logging;
import net.erdfelt.android.sdkfido.sdks.AndroidSdks;
import net.erdfelt.android.sdkfido.sdks.SDKNotFoundException;
import net.erdfelt.android.sdkfido.sdks.Sdk;

public class Main {
    public static void main(String[] args) {
        Logging.config();

        Main main = new Main();
        try {
            main.execute(args);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    private void execute(String[] args) throws IOException {
        FetcherConfig config = new FetcherConfig();

        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        try {
            parser.parse(args);
        } catch (CmdLineParseException e) {
            parser.usage(e);
            return;
        }

        if (config.getPlatformsDir() == null) {
            parser.usage();
            throw new IllegalStateException(
                    "Unable to find Android Home: Please set the --platformsDir option to continue.");
        }

        Fetcher fetcher = new Fetcher();

        if (config.getFetchTargets().isEmpty()) {
            showFetchTargetList(fetcher);
            return;
        }

        fetcher.setConfig(config);
        for (String fetchTarget : config.getFetchTargets()) {
            fetchTarget(fetcher, fetchTarget);
        }
    }

    private void fetchTarget(Fetcher fetcher, String fetchTarget) {
        try {
            Sdk sdk = fetcher.getSdks().getSdkByVersion("2.0");
            AndroidPlatform platform = fetcher.getPlatforms().getPlatform("android-" + sdk.getApilevel());
            TaskQueue tasks = fetcher.getFetchTasks(sdk, platform);
            while (tasks.hasTask()) {
                Task task = tasks.remove();
                System.out.printf("Task: %s - %s%n", task.getClass().getSimpleName(), task.getName());
                if (fetcher.getConfig().isDryRun()) {
                    continue; // skip further processing
                }
                // ConsoleTaskListener listener = new ConsoleTaskListener();
                // task.run(listener, tasks);
            }
        } catch (AndroidPlatformNotFoundException e) {
            System.err.println("Android Platform [" + e.getMessage() + "] is unavailable. choose another one.");
            showFetchTargetList(fetcher);
        } catch (SDKNotFoundException e) {
            System.err.println("SDK version [" + e.getMessage() + "] is unavailable. choose another one.");
            showFetchTargetList(fetcher);
        } catch (Throwable e) {
            System.err.println("Task Execution Failure.");
            e.printStackTrace(System.err);
        }
    }

    private void showFetchTargetList(Fetcher fetcher) {
        System.out.println("Available Android Java Fetch Targets:");
        LocalAndroidPlatforms platforms = fetcher.getPlatforms();
        AndroidSdks sdks = fetcher.getSdks();

        System.out.printf("Version | API | Status%n");
        for (Sdk sdk : sdks) {
            try {
                AndroidPlatform platform = platforms.getPlatform("android-" + sdk.getApilevel());
                System.out.printf("%-7s | %3d | %s%n", sdk.getVersion(), sdk.getApilevel(),
                        "Available (" + platform.getDescription() + ")");
            } catch (AndroidPlatformNotFoundException e) {
                System.out.printf("%-7s | %3d | %s%n", sdk.getVersion(), sdk.getApilevel(),
                        "Unavailable (go install it in your Android SDK Tools)");
            }
        }
    }
}
