package net.erdfelt.android.sdkfido.git;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.Transport;

public class GitRepo {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(GitRepo.class.getName());
    private File                repoDirectory;
    private FileRepository      repo;
    private ProgressMonitor     progressMonitor;

    public GitRepo(File workDirectory) throws IOException {
        this.repoDirectory = new File(workDirectory, Constants.DOT_GIT);
        this.repo = new FileRepository(repoDirectory);
    }

    public void checkoutBranch(String branchName) throws JGitInternalException, RefAlreadyExistsException,
            RefNotFoundException, InvalidRefNameException {
        CheckoutCommand command = new Git(repo).checkout();
        command.setCreateBranch(false);
        command.setName(branchName);
        command.setForce(false);
        command.call();
    }

    public void clone(String url) throws URISyntaxException, IOException {
        GitCloneCommand command = new GitCloneCommand(repo);
        command.setProgressMonitor(getProgressMonitor());
        command.setRemoteUrl(url);
        command.call();
    }

    /**
     * Returns if the repository exists (or not)
     * 
     * @return true if repository exists.
     */
    public boolean exists() {
        return repo.getConfig().getFile().exists();
    }

    public String getCurrentBranch() throws IOException {
        return repo.getFullBranch();
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

    public void pullRemote(String remoteName) throws JGitInternalException, InvalidRemoteException, IOException {
        Git git = new Git(repo);
        FetchCommand fetch = git.fetch();
        fetch.setCheckFetchedObjects(false);
        fetch.setRemoveDeletedRefs(true);
        List<RefSpec> specs = new ArrayList<RefSpec>();
        fetch.setRefSpecs(specs);
        fetch.setTimeout(-1);
        fetch.setDryRun(false);
        fetch.setRemote(Constants.DEFAULT_REMOTE_NAME);
        fetch.setThin(Transport.DEFAULT_FETCH_THIN);
        fetch.setProgressMonitor(getProgressMonitor());
        
        FetchResult result = fetch.call();
        if (result.getTrackingRefUpdates().isEmpty()) {
            return;
        }
        
        GitInfo.infoFetchResults(repo, result);
    }

    public void setMonitor(ProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }
}
