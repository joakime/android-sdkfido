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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefComparator;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.TrackingRefUpdate;

public class GitInfo {
    private static final Logger LOG        = Logger.getLogger(GitInfo.class.getName());
    private static final int    ABBREV_LEN = 8;

    public static void infoAll(InternalGit git) throws IOException {
        infoAll(git.getRepo());
    }

    public static void infoAll(Repository db) throws IOException {
        System.out.printf("Repository - %s%n", db.getDirectory());
        System.out.printf("      state: %s%n", db.getRepositoryState());
        System.out.printf("     branch: %s%n", db.getBranch());
        System.out.printf("full-branch: %s%n", db.getFullBranch());
        infoRefs(db);
        infoBranches(db);
        infoTags(db);
    }

    public static void infoBranches(Repository db) {
        // TODO Auto-generated method stub

    }

    private static void infoRefs(Repository db) {
        Map<String, Ref> refs = db.getAllRefs();
        System.out.printf("%nAll Refs (%d)%n", refs.size());
        Ref head = refs.get(Constants.HEAD);

        if (head == null) {
            System.out.println(" HEAD ref is dead and/or non-existent?");
            return;
        }

        Map<String, Ref> printRefs = new TreeMap<String, Ref>();

        String current = head.getLeaf().getName();
        if (current.equals(Constants.HEAD)) {
            printRefs.put("(no branch)", head);
        }

        for (Ref ref : RefComparator.sort(refs.values())) {
            String name = ref.getName();
            if (name.startsWith(Constants.R_HEADS) || name.startsWith(Constants.R_REMOTES)) {
                printRefs.put(name, ref);
            }
        }

        int maxLength = 0;
        for (String name : printRefs.keySet()) {
            maxLength = Math.max(maxLength, name.length());
        }

        System.out.printf("Refs (Heads/Remotes) (%d)%n", printRefs.size());
        for (Entry<String, Ref> e : printRefs.entrySet()) {
            Ref ref = e.getValue();
            ObjectId objectId = ref.getObjectId();
            System.out.printf("%c %-" + maxLength + "s %s%n", (current.equals(ref.getName()) ? '*' : ' '), e.getKey(),
                    objectId.abbreviate(ABBREV_LEN).name());
        }
    }

    private static void infoTags(Repository db) {
        System.out.printf("%nAll Tags (%d)%n", db.getTags().size());

        Map<String, Ref> sorted = new TreeMap<String, Ref>();
        sorted.putAll(db.getTags());

        int maxRefLength = 0;
        for (Entry<String, Ref> entry : sorted.entrySet()) {
            maxRefLength = Math.max(maxRefLength, entry.getValue().getName().length());
        }

        for (Entry<String, Ref> entry : db.getTags().entrySet()) {
            Ref ref = entry.getValue();
            System.out.printf("%-" + maxRefLength + "s %s%n", ref.getName(), ref.getObjectId().abbreviate(ABBREV_LEN)
                    .name());
        }
    }

    public static void infoObjectId(Repository db, ObjectId oid) throws IOException {
        System.out.printf("ObjectID: %s%n", oid.getName());
        ObjectLoader or = db.open(oid);
        if (or == null) {
            System.out.println("  Object not found!");
        }
        System.out.printf("  .type: %s%n", asObjectType(or.getType()));
        System.out.printf("  .size: %,d%n", or.getSize());
    }

    public static String asObjectType(int objectType) {
        switch (objectType) {
        case Constants.OBJ_BAD:
            return "BAD";
        case Constants.OBJ_BLOB:
            return "BLOB";
        case Constants.OBJ_COMMIT:
            return "COMMIT";
        case Constants.OBJ_EXT:
            return "EXT(future)";
        case Constants.OBJ_OFS_DELTA:
            return "OFS_DELTA(offset_delta)";
        case Constants.OBJ_REF_DELTA:
            return "REF_DELTA(reference_delta)";
        case Constants.OBJ_TAG:
            return "TAG";
        case Constants.OBJ_TREE:
            return "TREE";
        case Constants.OBJ_TYPE_5:
            return "TYPE_S(future)";
        default:
            return "[" + objectType + "]";
        }
    }

    public static void infoFetchResults(Repository db, FetchResult result) throws IOException {
        boolean headerDisplayed = false;

        for (TrackingRefUpdate update : result.getTrackingRefUpdates()) {
            if (update.getResult() == RefUpdate.Result.NO_CHANGE) {
                // skip if not changed
                continue;
            }

            char idChar = asIdChar(update.getResult());
            String idLong = asIdLong(db, update);
            String remoteRef = abbreviateRef(update.getRemoteName());
            String localRef = abbreviateRef(update.getLocalName());

            if (!headerDisplayed) {
                System.out.printf("Fetch Results from URI: %s%n", result.getURI());
                headerDisplayed = true;
            }

            System.out.printf(" %c %-20s %-18s > %s%n", idChar, idLong, remoteRef, localRef);
        }

        // Now show any remote messages
        StringReader reader = null;
        BufferedReader buf = null;
        try {
            reader = new StringReader(result.getMessages());
            buf = new BufferedReader(reader);
            String line;
            while ((line = buf.readLine()) != null) {
                System.out.printf("[remote] %s%n", line);
            }
        } finally {
            IOUtils.closeQuietly(buf);
            IOUtils.closeQuietly(reader);
        }
    }

    public static String abbreviateRef(String ref) {
        if (ref.startsWith(Constants.R_HEADS)) {
            return ref.substring(Constants.R_HEADS.length());
        }
        if (ref.startsWith(Constants.R_TAGS)) {
            return ref.substring(Constants.R_TAGS.length());
        }
        return ref;
    }

    public static String asIdLong(Repository db, TrackingRefUpdate update) {
        RefUpdate.Result result = update.getResult();

        // Fast IDs
        switch (result) {
        case LOCK_FAILURE:
            return "[lock failure]";
        case IO_FAILURE:
            return "[i/o error]";
        case REJECTED:
            return "[rejected]";
        }

        // Deleted Check Next
        if (ObjectId.zeroId().equals(update.getNewObjectId())) {
            return "[deleted]";
        }

        // All other results
        switch (result) {
        case NEW:
            if (update.getRemoteName().startsWith(Constants.R_HEADS)) {
                return "[new branch]";
            }

            if (update.getLocalName().startsWith(Constants.R_TAGS)) {
                return "[new tag]";
            }

            return "[new]";
        case FORCED: {
            String oldOID = update.getOldObjectId().abbreviate(ABBREV_LEN).name();
            String newOID = update.getNewObjectId().abbreviate(ABBREV_LEN).name();
            return oldOID + "..." + newOID;
        }
        case FAST_FORWARD: {
            String oldOID = update.getOldObjectId().abbreviate(ABBREV_LEN).name();
            String newOID = update.getNewObjectId().abbreviate(ABBREV_LEN).name();
            return oldOID + ".." + newOID;
        }
        case NO_CHANGE:
            return "[up to date]";
        default:
            return "[" + result.name() + "]";
        }
    }

    public static String getObjectName(Repository repo, ObjectId objectId) {
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

    public static char asIdChar(RefUpdate.Result result) {
        switch (result) {
        case FAST_FORWARD:
            return ' ';
        case LOCK_FAILURE:
            return '!';
        case IO_FAILURE:
            return '!';
        case NEW:
            return '*';
        case FORCED:
            return '+';
        case NO_CHANGE:
            return '=';
        case REJECTED:
            return '!';
        default:
            return ' ';
        }
    }
}
