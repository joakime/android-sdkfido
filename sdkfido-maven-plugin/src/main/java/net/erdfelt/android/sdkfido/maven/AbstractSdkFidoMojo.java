package net.erdfelt.android.sdkfido.maven;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

public abstract class AbstractSdkFidoMojo extends AbstractMojo {
    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    public MavenProject project;

    protected void ensureDirExists(File dir) throws MojoExecutionException {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                return; // dir exists.
            }
        }
        if (dir.mkdirs() == false) {
            throw new MojoExecutionException("Unable to create directory: " + dir);
        }
    }

    protected void execCommandline(Commandline cli, String exe) throws MojoExecutionException {
        StreamConsumer consumer = new DefaultConsumer();
        try {
            getLog().debug("Running: " + cli);
            int retval = CommandLineUtils.executeCommandLine(cli, consumer, consumer);
            if (retval != 0) {
                getLog().warn("Return val: " + retval);
                throw new MojoExecutionException("Received " + exe + " failure return code of " + retval
                        + " on command line\n" + cli);
            } else {
                getLog().debug("Success");
            }
        } catch (CommandLineException e) {
            throw new MojoExecutionException("Failed to run " + exe + ": " + cli, e);
        }
    }

    protected String OS(String rawpath) {
        return rawpath.replaceAll("[/\\\\]", File.separator);
    }

    protected String toString(List<String> strs) {
        if (strs == null) {
            return "<null>";
        }
        StringBuilder buf = new StringBuilder();
        buf.append('[');
        boolean delim = false;
        for (String str : strs) {
            if (delim) {
                buf.append(", ");
            }
            buf.append('"').append(str).append('"');
        }
        buf.append(']');
        return buf.toString();
    }

    protected String toString(String[] strs) {
        if (strs == null) {
            return "<null>";
        }
        StringBuilder buf = new StringBuilder();
        buf.append('[');
        boolean delim = false;
        for (String str : strs) {
            if (delim) {
                buf.append(", ");
            }
            buf.append('"').append(str).append('"');
        }
        buf.append(']');
        return buf.toString();
    }
}
