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
package net.erdfelt.android.sdkfido;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.erdfelt.android.sdkfido.configer.CmdLineParseException;
import net.erdfelt.android.sdkfido.configer.ConfigCmdLineParser;
import net.erdfelt.android.sdkfido.console.TablePrinter;
import net.erdfelt.android.sdkfido.local.AndroidPlatform;
import net.erdfelt.android.sdkfido.local.AndroidPlatformNotFoundException;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.logging.Logging;
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
            FetchTarget target = fetcher.getSourceOrigins().getFetchTarget(targetName);
            System.out.printf("Requested Target: \"%s\" ...%n", targetName);
            if (target == null) {
                System.out.println("Target Not Found");
                continue; // skip
            }
            System.out.println("Using Following Target Details ...");
            System.out.printf("  > Type        : %s%n", target.getType().name());
            System.out.printf("  > Api Level   : %s%n", target.getApilevel());
            System.out.printf("  > Codename    : %s%n", target.getCodename());
            System.out.printf("  > Version     : %s%n", target.getVersion());
            System.out.printf("  > Branch Name : %s%n", target.getBranchname());
            String sdkAvailability = "(Undetermined)";
            if (StringUtils.isBlank(target.getApilevel())) {
                sdkAvailability = "UNAVAILABLE (No ApiLevel to work off of)";
            } else if (fetcher.getPlatforms().hasApiLevel(target.getApilevel())) {
                try {
                    AndroidPlatform platform = fetcher.getPlatforms().getPlatform(target.getApilevel());
                    sdkAvailability = platform.getDir().getAbsolutePath();
                } catch (AndroidPlatformNotFoundException e) {
                    sdkAvailability = "NOT FOUND LOCALLY (Go Download it with the Android SDK and SVD Manager)";
                }
            } else {
                sdkAvailability = "NOT AVAILABLE LOCALLY (Go Download it with the Android SDK and SVD Manager)";
            }
            System.out.printf("  > SDKPlatform : %s%n", sdkAvailability);
            fetchTarget(fetcher, target);
        }
        System.out.println("All Fetch Tasks Complete.");
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
                task.run(tasks);
            }
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
