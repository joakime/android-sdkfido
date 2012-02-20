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
package net.erdfelt.android.sdkfido.configer;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import net.erdfelt.android.sdkfido.FetcherConfig;
import net.erdfelt.android.sdkfido.logging.Logging;
import net.erdfelt.android.sdkfido.project.OutputProjectType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.PathAssert;
import org.eclipse.jetty.toolchain.test.TestingDir;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class ConfigCmdLineParserTest {
    static {
        Logging.config();
    }
    @Rule
    public TestingDir testingdir = new TestingDir();

    @Test
    public void testHelp() throws CmdLineParseException {
        FetcherConfig config = new FetcherConfig();

        StringWriter capture = new StringWriter();
        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        parser.setOut(capture);
        String[] args = { "--help" };
        parser.parse(args);

        assertNoThrowable(capture);
        assertContains(capture, "Usage: ");
    }

    @Test
    public void testInvalidOption() {
        FetcherConfig config = new FetcherConfig();

        StringWriter capture = new StringWriter();
        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        parser.setOut(capture);
        String[] args = { "--bogus-thing" };
        try {
            parser.parse(args);
            Assert.fail("Expected " + CmdLineParseException.class.getName() + "\n" + capture.getBuffer());
        } catch (CmdLineParseException e) {
            parser.usage(e);
            assertHasThrowable(capture, CmdLineParseException.class);
            assertContains(capture, "Invalid Option: --bogus-thing");
            assertContains(capture, "Usage: ");
        }
    }

    @Test
    public void testSetOption() throws CmdLineParseException {
        FetcherConfig config = new FetcherConfig();

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(false));

        StringWriter capture = new StringWriter();
        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        parser.setOut(capture);
        String[] args = { "--dryRun", "true" };
        parser.parse(args);

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(true));
    }

    @Test
    public void testSetMultipleOptions() throws CmdLineParseException {
        FetcherConfig config = new FetcherConfig();

        File expectedDir = new File(SystemUtils.getUserHome(), FilenameUtils.separatorsToSystem(".sdkfido/work"));

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(false));
        Assert.assertThat("Config.workDir", config.getWorkDir(), is(expectedDir));

        File otherWork = testingdir.getFile("work");

        StringWriter capture = new StringWriter();
        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        parser.setOut(capture);
        String[] args = { "--dryRun", "true", "--workDir", otherWork.getAbsolutePath() };
        parser.parse(args);

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(true));
        Assert.assertThat("Config.workDir", config.getWorkDir(), is(otherWork));
    }

    @Test
    public void testSetDeepOption() throws CmdLineParseException {
        FetcherConfig config = new FetcherConfig();

        File expectedDir = new File(SystemUtils.getUserHome(), FilenameUtils.separatorsToSystem(".sdkfido/work"));

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(false));
        Assert.assertThat("Config.workDir", config.getWorkDir(), is(expectedDir));
        Assert.assertThat("Config.maven.groupId", config.getMaven().getGroupId(), is("com.android.sdk"));

        File otherWork = testingdir.getFile("work");

        StringWriter capture = new StringWriter();
        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        parser.setOut(capture);
        String[] args = { "--dryRun", "true", "--workDir", otherWork.getAbsolutePath(), "--maven.groupId",
                "com.android.sdk.testee" };
        parser.parse(args);

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(true));
        Assert.assertThat("Config.workDir", config.getWorkDir(), is(otherWork));
        Assert.assertThat("Config.maven.groupId", config.getMaven().getGroupId(), is("com.android.sdk.testee"));
    }

    @Test
    public void testSetEnumOption() throws CmdLineParseException {
        FetcherConfig config = new FetcherConfig();

        File expectedDir = new File(SystemUtils.getUserHome(), FilenameUtils.separatorsToSystem(".sdkfido/work"));

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(false));
        Assert.assertThat("Config.workDir", config.getWorkDir(), is(expectedDir));
        Assert.assertThat("Config.outputType", config.getOutputType(), is(OutputProjectType.SDK));

        File otherWork = testingdir.getFile("work");

        StringWriter capture = new StringWriter();
        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        parser.setOut(capture);
        String[] args = { "--dryRun", "true", "--workDir", otherWork.getAbsolutePath(), "--outputType", "SDK" };
        parser.parse(args);

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(true));
        Assert.assertThat("Config.workDir", config.getWorkDir(), is(otherWork));
        Assert.assertThat("Config.outputType", config.getOutputType(), is(OutputProjectType.SDK));
    }

    @Test
    public void testFetchTargets() throws CmdLineParseException {
        FetcherConfig config = new FetcherConfig();

        StringWriter capture = new StringWriter();
        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        parser.setOut(capture);
        String[] args = { "2.1", "froyo", "android-sdk-2.0_r1", "9" };
        parser.parse(args);

        Assert.assertThat("output should not be usage", capture.getBuffer().toString(), not(containsString("Usage: ")));

        List<String> targets = config.getFetchTargets();
        Assert.assertThat("config.fetchTargets", targets, notNullValue());
        Assert.assertThat("config.fetchTarget.size", targets.size(), is(4));

        Assert.assertThat("config.fetchTarget", targets, hasItem("2.1"));
        Assert.assertThat("config.fetchTarget", targets, hasItem("froyo"));
        Assert.assertThat("config.fetchTarget", targets, hasItem("android-sdk-2.0_r1"));
        Assert.assertThat("config.fetchTarget", targets, hasItem("9"));
    }

    @Test
    public void testSaveConfig() throws CmdLineParseException, IOException {
        testingdir.ensureEmpty();

        FetcherConfig config = new FetcherConfig();

        File expectedDir = new File(SystemUtils.getUserHome(), FilenameUtils.separatorsToSystem(".sdkfido/work"));

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(false));
        Assert.assertThat("Config.workDir", config.getWorkDir(), is(expectedDir));
        Assert.assertThat("Config.outputType", config.getOutputType(), is(OutputProjectType.SDK));

        File confFile = testingdir.getFile("config.properties");

        StringWriter capture = new StringWriter();
        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        parser.setOut(capture);
        String[] args = { "--save-config", "--config", confFile.getAbsolutePath() };
        parser.parse(args);

        PathAssert.assertFileExists("Config File", confFile);

        Properties props = loadProps(confFile);

        Assert.assertThat("props[dryRun]", props.getProperty("dryRun"), is("false"));
        Assert.assertThat("props[workDir]", props.getProperty("workDir"), is(expectedDir.getAbsolutePath()));
        Assert.assertThat("props[outputType]", props.getProperty("outputType"), is("SDK"));
    }

    @Test
    public void testLoadConfig() throws CmdLineParseException, IOException {
        FetcherConfig config = new FetcherConfig();

        File expectedDir = new File(SystemUtils.getUserHome(), FilenameUtils.separatorsToSystem(".sdkfido/work"));

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(false));
        Assert.assertThat("Config.workDir", config.getWorkDir(), is(expectedDir));
        Assert.assertThat("Config.outputType", config.getOutputType(), is(OutputProjectType.SDK));

        File confFile = MavenTestingUtils.getTestResourceFile("config2.properties");

        StringWriter capture = new StringWriter();
        ConfigCmdLineParser parser = new ConfigCmdLineParser(this, config);
        parser.setOut(capture);
        String[] args = { "--config", confFile.getAbsolutePath() };
        parser.parse(args);

        PathAssert.assertFileExists("Config File", confFile);

        Assert.assertThat("Config.dryRun", config.isDryRun(), is(true));
        Assert.assertThat("config.maven.groupId", config.getMaven().getGroupId(), is("com.android.sdk.testee"));
        Assert.assertThat("config.maven.artifactId", config.getMaven().getArtifactId(), is("artifact-test"));
        Assert.assertThat("config.maven.includeStubJar", config.getMaven().isIncludeStubJar(), is(false));
        Assert.assertThat("config.outputType", config.getOutputType(), is(OutputProjectType.MAVEN_MULTI));
    }

    private Properties loadProps(File propfile) throws IOException {
        FileReader reader = null;
        try {
            reader = new FileReader(propfile);
            Properties props = new Properties();
            props.load(reader);
            return props;
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private void assertHasThrowable(StringWriter capture, Class<? extends Throwable> t) {
        String buf = capture.getBuffer().toString();
        Assert.assertThat(buf, containsString(t.getSimpleName()));
    }

    private void assertContains(StringWriter capture, String content) {
        String buf = capture.getBuffer().toString();
        Assert.assertThat(buf, notNullValue());
        if (!buf.contains(content)) {
            Assert.fail("Expected a string \"" + content + "\"\n" + buf);
        }
    }

    private void assertNoThrowable(StringWriter capture) {
        String buf = capture.getBuffer().toString();
        Assert.assertThat(buf, not(containsString("Throwable")));
        Assert.assertThat(buf, not(containsString("Exception")));
    }
}
