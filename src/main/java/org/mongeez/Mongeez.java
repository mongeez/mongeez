/*
 * Copyright 2011 SecondMarket Labs, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.mongeez;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mongeez.commands.ChangeSet;
import org.mongeez.commands.Script;
import org.mongeez.reader.ChangeSetFileProvider;
import org.mongeez.reader.ChangeSetReaderFactory;
import org.mongeez.reader.FilesetXMLChangeSetFileProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Mongo;


public class Mongeez {
    private static final Logger logger = LoggerFactory.getLogger(Mongeez.class);

    private Mongo mongo = null;
    private String dbName;
    private MongoAuth auth = null;
    private ChangeSetFileProvider changeSetFileProvider;
    private String context = null;

    public final void process() {
        List<ChangeSet> changeSets = getChangeSets();
        new ChangeSetExecutor(mongo, dbName, context, auth).execute(changeSets);
    }

    private List<ChangeSet> getChangeSets() {
        List<File> files = changeSetFileProvider.getChangeSetFiles();
        List<ChangeSet> changeSets = new ArrayList<ChangeSet>();

        ChangeSetReaderFactory readerFactory = ChangeSetReaderFactory.getInstance();
        for (File file : files) {
            changeSets.addAll(readerFactory.getChangeSetReader(file).getChangeSets(file));
        }
        logChangeSets(changeSets);
        return changeSets;
    }

    private void logChangeSets(final List<ChangeSet> changeSets) {
        if (logger.isTraceEnabled()) {
            for (ChangeSet changeSet : changeSets) {
                logger.trace("Changeset");
                logger.trace("id: " + changeSet.getChangeId());
                logger.trace("author: " + changeSet.getAuthor());
                if (! "".equals(changeSet.getContexts())) {
                    logger.trace("contexts: {}", changeSet.getContexts());
                }
                for (Script command : changeSet.getCommands()) {
                    logger.trace("script");
                    logger.trace(command.getBody());
                }
            }
        }
    }

    public final void setMongo(final Mongo mongo) {
        this.mongo = mongo;
    }

    public final void setDbName(final String dbName) {
        this.dbName = dbName;
    }

    public final void setAuth(final MongoAuth auth) {
        this.auth = auth;
    }

    /**
     * Convenience method to set the ChangeSetFileProvider to an XML fileset based on the specified file
     */
    public final void setFile(final File file) {
        setChangeSetFileProvider(new FilesetXMLChangeSetFileProvider(file));
    }

    public final void setChangeSetFileProvider(final ChangeSetFileProvider changeSetFileProvider) {
        this.changeSetFileProvider = changeSetFileProvider;
    }

    public final void setContext(final String context) {
        this.context = context;
    }

}
