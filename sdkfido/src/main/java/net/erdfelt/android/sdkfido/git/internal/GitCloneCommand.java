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
package net.erdfelt.android.sdkfido.git.internal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


import net.erdfelt.android.sdkfido.git.IGit;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.GitIndex;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefComparator;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.lib.Tree;
import org.eclipse.jgit.lib.WorkDirCheckout;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.URIish;

@SuppressWarnings("deprecation")
public class GitCloneCommand {
    private static final Logger LOG = Logger.getLogger(GitCloneCommand.class.getName());
    private String              remoteUrl;
    private ProgressMonitor     progressMonitor;
    private FileRepository      repo;

    public GitCloneCommand(FileRepository repo) {
        this.repo = repo;
        LOG.info("local repo: " + repo.getDirectory());
    }

    public ProgressMonitor getProgressMonitor() {
        if (progressMonitor == null) {
            progressMonitor = new TextProgressMonitor();
        }
        return progressMonitor;
    }

    public void setProgressMonitor(ProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public void call() throws URISyntaxException, IOException {
        LOG.info("clone: " + remoteUrl);
        createBareRepo();
        addRemoteConfig(IGit.REMOTE_NAME, remoteUrl);
        FetchResult result = fetch(IGit.REMOTE_NAME);

        // What branch is at head?
        Ref branchAtHead = guessHEAD(result);
        // Checkout head
        checkoutRef(branchAtHead);
    }

    private Ref guessHEAD(final FetchResult result) {
        final Ref idHEAD = result.getAdvertisedRef(Constants.HEAD);
        final List<Ref> availableRefs = new ArrayList<Ref>();
        Ref head = null;
        for (final Ref r : result.getAdvertisedRefs()) {
            final String n = r.getName();
            if (!n.startsWith(Constants.R_HEADS))
                continue;
            availableRefs.add(r);
            if (idHEAD == null || head != null)
                continue;
            if (r.getObjectId().equals(idHEAD.getObjectId()))
                head = r;
        }
        Collections.sort(availableRefs, RefComparator.INSTANCE);
        if (idHEAD != null && head == null)
            head = idHEAD;
        return head;
    }

    private RevCommit parseCommit(ObjectId objId) throws MissingObjectException, IncorrectObjectTypeException,
            IOException {
        RevWalk rw = new RevWalk(repo);
        RevCommit commit;
        try {
            commit = rw.parseCommit(objId);
        } finally {
            rw.release();
        }
        return commit;
    }

    private void checkoutRef(Ref ref) throws IOException {
        LOG.info("checkoutRef: ref:" + ref);

        if (ref == null) {
            throw new IllegalArgumentException("Unable to checkout from a null ref");
        }

        if (!Constants.HEAD.equals(ref.getName())) {
            RefUpdate u = repo.updateRef(Constants.HEAD);
            u.disableRefLog();
            u.link(ref.getName());
        }
        ObjectId commitId = ref.getObjectId();

        RevCommit commit = parseCommit(commitId);
        RefUpdate u = repo.updateRef(Constants.HEAD);
        u.setNewObjectId(commit.getId());
        u.forceUpdate();

        GitIndex index = new GitIndex(repo);
        Tree tree = repo.mapTree(commit.getTree());
        WorkDirCheckout co = new WorkDirCheckout(repo, repo.getWorkTree(), index, tree);
        co.checkout();
        index.write();
    }

    /**
     * Create a new bare repository.
     * 
     * @throws IOException
     *             if unable to create the bare repo
     */
    private void createBareRepo() throws IOException {
        // Make basic empty repo
        repo.create();

        // Set as non-bare repo
        repo.getConfig().setBoolean("core", null, "bare", false);
        repo.getConfig().save();
    }

    /**
     * Add a 'remote' configuration.
     * 
     * @param remoteName
     *            the name of the remote
     * @param remoteUri
     *            the uri to the remote
     * @throws URISyntaxException
     *             if unable to process remoteUrl
     * @throws IOException
     *             if unable to add remote config
     */
    private void addRemoteConfig(String remoteName, String remoteUrl) throws URISyntaxException, IOException {
        URIish uri = new URIish(remoteUrl);
        addRemoteConfig(remoteName, uri);
    }

    /**
     * Add a 'remote' configuration.
     * 
     * @param remoteName
     *            the name of the remote
     * @param uri
     *            the uri to the remote
     * @throws URISyntaxException
     *             if unable to process uri
     * @throws IOException
     *             if unable to add remote config
     */
    private void addRemoteConfig(String remoteName, URIish uri) throws URISyntaxException, IOException {
        RemoteConfig rc = new RemoteConfig(repo.getConfig(), remoteName);
        rc.addURI(uri);

        String dest = Constants.R_HEADS + "*:" + Constants.R_REMOTES + remoteName + "/*";

        RefSpec refspec = new RefSpec(dest);
        refspec.setForceUpdate(true);
        rc.addFetchRefSpec(refspec);
        rc.update(repo.getConfig());
        repo.getConfig().save();
    }

    /**
     * Fetch all refs from the remoteName.
     * 
     * @param remoteName
     *            the remote name to fetch from
     * @return the results of the fetch
     * @throws URISyntaxException
     *             if unable to process the URL for the remote name
     * @throws IOException
     *             if unable to fetch
     */
    private FetchResult fetch(String remoteName) throws URISyntaxException, IOException {
        return fetch(remoteName, null);
    }

    /**
     * Fetch only specific refs from the remoteName.
     * 
     * @param remoteName
     *            the remote name to fetch from
     * @param refs
     *            the specific refs to fetch, or null for all refs
     * @return the results of the fetch
     * @throws URISyntaxException
     *             if unable to process the URL for the remote name
     * @throws IOException
     *             if unable to fetch
     */
    private FetchResult fetch(String remoteName, Collection<RefSpec> refs) throws URISyntaxException, IOException {
        if (refs == null) {
            LOG.info("fetch: remote:" + remoteName + " - refs:<all>");
        } else {
            LOG.info("fetch: remote:" + remoteName + " - refs:" + refs);
        }
        Transport tx = Transport.open(repo, remoteName);
        FetchResult result = null;

        try {
            result = tx.fetch(getProgressMonitor(), refs);
        } finally {
            tx.close();
        }

        GitInfo.infoFetchResults(repo, result);
        return result;
    }
}
