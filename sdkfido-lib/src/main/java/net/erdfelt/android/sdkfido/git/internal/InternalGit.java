package net.erdfelt.android.sdkfido.git.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.git.GitException;
import net.erdfelt.android.sdkfido.git.GitMirrors;
import net.erdfelt.android.sdkfido.git.IGit;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.Transport;

/**
 * Implementation of GIT that uses jgit (git implemented in 100% java)
 */
public class InternalGit implements IGit {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(InternalGit.class.getName());
    private File                repoDirectory;
    private FileRepository      repo;
    private ProgressMonitor     progressMonitor;
    private GitMirrors          mirrors;

    public InternalGit(File workDirectory, GitMirrors mirrors) throws GitException {
        try {
            this.mirrors = mirrors;
            this.repoDirectory = new File(workDirectory, Constants.DOT_GIT);
            this.repo = new FileRepository(repoDirectory);
        } catch (IOException e) {
            throw new GitException(e.getMessage(), e);
        }
    }

    @Override
    public void checkoutBranch(String branchName) throws GitException {
        try {
            CheckoutCommand command = new Git(repo).checkout();
            command.setCreateBranch(false);
            command.setName(branchName);
            command.setForce(false);
            command.call();
        } catch (Throwable t) {
            throw new GitException(t.getMessage(), t);
        }
    }

    @Override
    public void clone(String url) throws GitException {
        try {
            GitCloneCommand command = new GitCloneCommand(repo);
            command.setProgressMonitor(getProgressMonitor());
            String giturl = mirrors.getNewUrl(url);
            command.setRemoteUrl(giturl);
            command.call();
        } catch (Throwable t) {
            throw new GitException(t.getMessage(), t);
        }
    }

    @Override
    public boolean exists() {
        return repo.getConfig().getFile().exists();
    }

    @Override
    public String getCurrentBranch() throws GitException {
        try {
            return repo.getFullBranch();
        } catch (Throwable t) {
            throw new GitException(t.getMessage(), t);
        }
    }

    public ProgressMonitor getProgressMonitor() {
        if (this.progressMonitor == null) {
            this.progressMonitor = new TextProgressMonitor();
        }
        return this.progressMonitor;
    }

    public Repository getRepo() {
        return this.repo;
    }

    public RepositoryState getState() {
        return repo.getRepositoryState();
    }

    @Override
    public void pullRemote() throws GitException {
        try {
            Git git = new Git(repo);
            FetchCommand fetch = git.fetch();
            fetch.setCheckFetchedObjects(false);
            fetch.setRemoveDeletedRefs(true);
            List<RefSpec> specs = new ArrayList<RefSpec>();
            fetch.setRefSpecs(specs);
            fetch.setTimeout(5000);
            fetch.setDryRun(false);
            fetch.setRemote(IGit.REMOTE_NAME);
            fetch.setThin(Transport.DEFAULT_FETCH_THIN);
            fetch.setProgressMonitor(getProgressMonitor());

            FetchResult result = fetch.call();
            if (result.getTrackingRefUpdates().isEmpty()) {
                return;
            }

            GitInfo.infoFetchResults(repo, result);
        } catch (Throwable t) {
            throw new GitException(t.getMessage(), t);
        }
    }

    public void setMonitor(ProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    @Override
    public File getDir() {
        return this.repo.getWorkTree();
    }
}
