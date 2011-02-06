/*******************************************************************************
 * Copyright (c) Joakim Erdfelt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution.
 *
 *   The Eclipse Public License is available at 
 *   http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.erdfelt.android.sdkfido.git;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.LargeObjectException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.GitIndex;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefComparator;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.RefUpdate.Result;
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

public class Git {
    private static final Logger LOG = Logger.getLogger(Git.class.getName());
    private File                repoDirectory;
    protected FileRepository    repo;

    public Git(File workDirectory) throws IOException {
        this.repoDirectory = new File(workDirectory, Constants.DOT_GIT);
        repo = new FileRepository(repoDirectory);
    }

    /**
     * Add a 'remote' configuration.
     * 
     * @param remoteName
     *            the name of the remote
     * @param uri
     *            the uri to the remote
     * @throws URISyntaxException
     * @throws IOException
     */
    public void addRemoteConfig(String remoteName, URIish uri) throws URISyntaxException, IOException {
        RemoteConfig rc = new RemoteConfig(repo.getConfig(), remoteName);
        rc.addURI(uri);

        String dest = "+" + Constants.R_HEADS + "*:" + Constants.R_REMOTES + remoteName + "/*";
        LOG.info("Dest RefSpec: " + dest);

        RefSpec refspec = new RefSpec(dest);
        refspec.setForceUpdate(true);
        rc.addFetchRefSpec(refspec);
        rc.update(repo.getConfig());
        repo.getConfig().save();
    }

    /**
     * Checkout a specific Tag
     * 
     * @param remoteName
     *            the name of the remote
     * @param tagId
     *            the tagId to check out
     * @throws URISyntaxException
     * @throws IOException
     */
    public void checkoutTag(String remoteName, String tagId) throws URISyntaxException, IOException {
        LOG.info("pullTag " + remoteName + " " + tagId);

        Ref tagRef = repo.getTags().get(tagId);
        if (tagRef == null) {
            throw new IOException("Unable to find tagId \"" + tagId + "\"");
        }

        // Find the top level commit on this tag
        RevWalk walk = new RevWalk(repo);
        try {
            RevCommit head = walk.parseCommit(repo.resolve(Constants.HEAD));
            RevCommit branch = walk.parseCommit(repo.resolve(tagRef.getName() + "^{commit}"));

            LargeDirCacheCheckout dco = new LargeDirCacheCheckout(repo, head.getTree().getId(), repo.lockDirCache(),
                    branch.getTree().getId());
            dco.setFailOnConflict(true);
            dco.checkout();
        } catch (LargeObjectException e) {
            LOG.severe("Large Object (" + e.getObjectId() + ") is " + getObjectName(e.getObjectId()));
            LOG.log(Level.SEVERE, "Unable to process large object: " + e.getObjectId(), e);
        } finally {
            walk.release();
        }

        RefUpdate refUpdate = repo.updateRef(Constants.HEAD);
        refUpdate.link(tagRef.getName());
    }

    private String getObjectName(ObjectId objectId) {
        try {
            ObjectLoader loader = repo.open(objectId);
            StringBuilder ret = new StringBuilder();

            if (loader.isLarge()) {
                ret.append("LARGE! ");
            }

            switch (loader.getType()) {
            case Constants.OBJ_BAD:
                ret.append("BAD ");
                break;
            case Constants.OBJ_BLOB:
                ret.append("BLOB ");
                break;
            case Constants.OBJ_COMMIT:
                ret.append("COMMIT ");
                break;
            case Constants.OBJ_EXT:
                ret.append("EXT ");
                break;
            case Constants.OBJ_OFS_DELTA:
                ret.append("OFS_DELTA ");
                break;
            case Constants.OBJ_REF_DELTA:
                ret.append("REF_DELTA ");
                break;
            case Constants.OBJ_TAG:
                ret.append("TAG ");
                break;
            case Constants.OBJ_TREE:
                ret.append("TREE ");
                break;
            case Constants.OBJ_TYPE_5:
                ret.append("TYPE_5 ");
                break;
            default:
                ret.append("UNKNOWN[").append(loader.getType()).append("] ");
                break;
            }

            ret.append(String.format("Size=%,d", loader.getSize()));
            return ret.toString();
        } catch (MissingObjectException e) {
            LOG.log(Level.WARNING, "Unable to open objectId: " + objectId, e);
            return "<missing object>";
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Unable to open objectId: " + objectId, e);
            return "<unable to open object>";
        }
    }

    private void setHeadLink(Ref ref) throws IOException {
        LOG.info("Creating .git/HEAD link to " + ref.getName());
        RefUpdate u = repo.updateRef(ref.getName());
        u.disableRefLog();
        Result linkResult = u.link(ref.getName());
        LOG.info("Link result " + linkResult);
    }

    /**
     * Clone a repository.
     * 
     * @param remoteName
     *            the remote name
     * @param remoteUrl
     *            the remote url
     * @throws URISyntaxException
     * @throws IOException
     */
    public void clone(String remoteName, String remoteUrl) throws URISyntaxException, IOException {
        LOG.info("clone: " + remoteUrl);
        URIish uri = new URIish(remoteUrl);

        // Make basic empty repo
        repo.create();

        // Set as non-bare repo
        repo.getConfig().setBoolean("core", null, "bare", false);
        repo.getConfig().save();

        // Add "remote" config settings
        addRemoteConfig(remoteName, uri);

        // Fetch all of the contents from remote
        FetchResult result = fetch(remoteName, null);

        // What branch is at head?
        Ref branch = guessHEAD(result);

        // Checkout head
        checkoutHead(branch);
    }

    public FetchResult fetch(String remoteName, Collection<RefSpec> refs) throws URISyntaxException, IOException {
        if (refs == null) {
            LOG.info("fetch: remote:" + remoteName + " - refs:<all>");
        } else {
            LOG.info("fetch: remote:" + remoteName + " - refs:" + refs);
        }
        Transport tx = Transport.open(repo, remoteName);
        FetchResult result = null;

        try {
            result = tx.fetch(new TerseProgressMonitor(), refs);
        } finally {
            tx.close();
        }

        GitInfo.infoFetchResults(repo, tx, result);
        return result;
    }

    public FetchResult fetchAll(String remoteName) throws URISyntaxException, IOException {
        return fetch(remoteName, null);
    }

    public String getRemoteUrl(String remoteId) {
        return repo.getConfig().getString("remote", remoteId, "url");
    }

    public boolean hasBranchConfig(String branchName) {
        String url = repo.getConfig().getString("branch", branchName, "url");
        return StringUtils.isNotBlank(url);
    }

    public void merge(String remoteName) {
        LOG.info("merge: " + remoteName);
        // TODO Auto-generated method stub

    }

    /**
     * true if it is an active repository
     * 
     * @return
     */
    public boolean repositoryExists() {
        return repo.getConfig().getFile().exists();
    }

    public void addBranchConfig(String branchName, String remoteName, String mergeRef) throws IOException {
        repo.getConfig().setString("branch", branchName, "remote", remoteName);
        repo.getConfig().setString("branch", branchName, "merge", mergeRef);
        repo.getConfig().save();
    }

    private void checkoutHead(Ref ref) throws IOException {
        LOG.info("checkoutHead: ref:" + ref);

        if (ref == null) {
            throw new IllegalArgumentException("Unable to checkout from a null ref");
        }

        if (!Constants.HEAD.equals(ref.getName())) {
            setHeadLink(ref);
        }

        checkoutFromCommit(ref.getObjectId());
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

    @SuppressWarnings("deprecation")
    private void checkoutFromCommit(ObjectId commitId) throws IOException {
        LOG.info("Creating commit mapping of " + commitId);
        RevCommit commit = parseCommit(commitId);
        LOG.info("Commit: " + commit);
        RefUpdate u = repo.updateRef(Constants.HEAD);
        LOG.info("Setting New Object ID: " + commit.getId());
        u.setNewObjectId(commit.getId());
        LOG.info("Forcing Update");
        Result updateResult = u.forceUpdate();
        LOG.info("Update result " + updateResult);

        LOG.info("Creating Index");
        GitIndex index = new GitIndex(repo);
        LOG.info("Index: " + index);
        LOG.info("Creating tree from commit");
        Tree tree = repo.mapTree(commit.getTree());
        LOG.info("Tree: " + tree);
        WorkDirCheckout co = new WorkDirCheckout(repo, repo.getWorkTree(), index, tree);
        LOG.info("Creating WorkDirCheckout: " + co);
        LOG.info("Issuing checkout");
        co.checkout();
        LOG.info("Writing Index");
        index.write();
    }

    /**
     * Attempt to figure out what the HEAD ref is from the fetch results.
     * 
     * @param result
     *            the results to look through
     * @return
     */
    private Ref guessHEAD(FetchResult result) {
        // Use any value advertised by the results as HEAD
        Ref idHead = result.getAdvertisedRef(Constants.HEAD);

        // Collect any "refs/heads/*" entries
        List<Ref> headsRefs = new ArrayList<Ref>();
        for (Ref r : result.getAdvertisedRefs()) {
            String name = r.getName();

            if (!name.startsWith(Constants.R_HEADS)) {
                // Not part of refs/heads/* skip it
                continue;
            }

            // Track any "refs/heads/*" entries
            headsRefs.add(r);
        }

        // Sort by ref
        Collections.sort(headsRefs, RefComparator.INSTANCE);

        if (idHead != null) {
            // If an advertised head does exist, use the topmost hit with
            // the same object id.
            for (Ref ref : headsRefs) {
                if (ref.getObjectId().equals(idHead.getObjectId())) {
                    return ref;
                }
            }
        }

        // Use advertised head instead
        return idHead;
    }
}
