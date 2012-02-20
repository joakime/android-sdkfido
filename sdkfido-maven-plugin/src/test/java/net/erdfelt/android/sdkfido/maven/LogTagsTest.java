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
package net.erdfelt.android.sdkfido.maven;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.PathAssert;
import org.eclipse.jetty.toolchain.test.TestingDir;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class LogTagsTest {
    @Rule
    public TestingDir testdir = new TestingDir();

    @Test
    public void testParse() throws IOException {
        File tags = MavenTestingUtils.getTestResourceFile("EventLogTags.logtags");
        LogTags logtags = new LogTags(tags);

        Assert.assertEquals("packageName", "com.android.server", logtags.getPackageName());
        Assert.assertEquals("className", "EventLogTags", logtags.getClassName());
    }

    @Test
    public void testWriteJava() throws IOException {
        File tags = MavenTestingUtils.getTestResourceFile("EventLogTags.logtags");
        LogTags logtags = new LogTags(tags);

        testdir.ensureEmpty();

        File outputFile = testdir.getFile(logtags.getClassName() + ".java");
        logtags.writeJavaSource(outputFile);

        PathAssert.assertFileExists("Output Java File", outputFile);
    }
}
