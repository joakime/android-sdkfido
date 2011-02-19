package net.erdfelt.android.sdkfido;

import java.io.IOException;
import java.util.List;

import net.erdfelt.android.sdkfido.configer.CmdLineParseException;
import net.erdfelt.android.sdkfido.configer.ConfigCmdLineParser;
import net.erdfelt.android.sdkfido.console.TablePrinter;
import net.erdfelt.android.sdkfido.local.AndroidPlatformNotFoundException;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.logging.Logging;
import net.erdfelt.android.sdkfido.sdks.SDKNotFoundException;
import net.erdfelt.android.sdkfido.sdks.SourceOrigins;
import net.erdfelt.android.sdkfido.sdks.SourceOriginsLoader;

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

        SourceOrigins origins = SourceOriginsLoader.load();

        Fetcher fetcher = new Fetcher();
        fetcher.setSourceOrigins(origins);
        fetcher.setConfig(config);

        if (config.getFetchTargets().isEmpty()) {
            parser.usage();
            System.out.println();
            showFetchTargetList(fetcher);
            return;
        }

        for (String targetName : config.getFetchTargets()) {
            FetchTarget target = fetcher.getFetchTarget(targetName);
            fetchTarget(fetcher, target);
        }
    }

    private void fetchTarget(Fetcher fetcher, FetchTarget target) {
        try {
            TaskQueue tasks = fetcher.getFetchTasks(target);
            while (tasks.hasTask()) {
                Task task = tasks.remove();
                System.out.printf("Task: %s - %s%n", task.getClass().getSimpleName(), task.getName());
                if (fetcher.getConfig().isDryRun()) {
                    continue; // skip further processing
                }
                // TODO: Enable actually running of tasks.
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
        System.out.println("(Sorted newest to oldest within type groupings)");
        System.out.println();
        LocalAndroidPlatforms platforms = fetcher.getPlatforms();
        List<FetchTarget> targets = fetcher.getSourceOrigins().getFetchTargets();

        TablePrinter table = new TablePrinter("Target", "Type", "API", "Version", "Codename", "Branch", "SDK Avail?");

        for (FetchTarget target : targets) {
            table.startRow();
            table.addCell(target.getId());
            table.addCell(target.getType());
            table.addCell(target.getApilevel());
            table.addCell(target.getVersion());
            table.addCell(target.getCodename());
            table.addCell(target.getBranchname());

            if (platforms.hasApiLevel(target.getApilevel())) {
                table.addCell("Available");
            } else {
                table.addCell("not in sdk dir");
            }
            table.endRow();
        }

        table.print(System.out);
    }
}
