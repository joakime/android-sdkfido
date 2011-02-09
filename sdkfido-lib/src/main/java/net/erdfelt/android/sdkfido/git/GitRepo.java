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

public class GitRepo {
    private static final Logger LOG = Logger.getLogger(GitRepo.class.getName());
    private File                repoDirectory;
    private FileRepository      repo;

    public GitRepo(File workDirectory) throws IOException {
        this.repoDirectory = new File(workDirectory, Constants.DOT_GIT);
        this.repo = new FileRepository(repoDirectory);
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
    public void addRemoteConfig(String remoteName, String remoteUrl) throws URISyntaxException, IOException {
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

    // TODO: Switch to non-deprecated versions
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

    /**
     * Create a new bare repository.
     * 
     * @throws IOException
     *             if unable to create the bare repo
     */
    public void createBareRepo() throws IOException {
        // Make basic empty repo
        repo.create();

        // Set as non-bare repo
        repo.getConfig().setBoolean("core", null, "bare", false);
        repo.getConfig().save();
    }

    /**
     * Returns if the repository exists (or not)
     * 
     * @return true if repository exists.
     */
    public boolean exists() {
        return repo.getConfig().getFile().exists();
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
    public FetchResult fetch(String remoteName) throws URISyntaxException, IOException {
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
    public FetchResult fetch(String remoteName, Collection<RefSpec> refs) throws URISyntaxException, IOException {
        if (refs == null) {
            LOG.info("fetch: remote:" + remoteName + " - refs:<all>");
        } else {
            LOG.info("fetch: remote:" + remoteName + " - refs:" + refs);
        }
        Transport tx = Transport.open(repo, remoteName);
        FetchResult result = null;

        try {
            // TODO: the monitor should be configurable
            result = tx.fetch(new TerseProgressMonitor(), refs);
        } finally {
            tx.close();
        }

        GitInfo.infoFetchResults(repo, tx, result);

        // What branch is at head?
        Ref branchAtHead = guessHEAD(result);

        // Checkout head
        checkoutHead(branchAtHead);

        return result;
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

    private void setHeadLink(Ref ref) throws IOException {
        LOG.info("Creating .git/HEAD link to " + ref.getName());
        RefUpdate u = repo.updateRef(ref.getName());
        u.disableRefLog();
        Result linkResult = u.link(ref.getName());
        LOG.info("Link result " + linkResult);
    }
}
