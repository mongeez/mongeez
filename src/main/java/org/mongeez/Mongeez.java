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

import com.mongodb.Mongo;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mongeez.commands.ChangeSet;
import org.mongeez.commands.Script;
import org.mongeez.reader.ChangeSetReaderFactory;
import org.mongeez.reader.FilesetXMLReader;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;


public class Mongeez {
    private final static Logger logger = Logger.getLogger(Mongeez.class);

    private Mongo mongo = null;
    private String dbName;
    private Resource file = null;

    private boolean isVerbose = false;

    public void process() {
        List<ChangeSet> changeSets = getChangeSets();
        new ChangeSetExecutor(mongo, dbName).execute(changeSets);
    }

    private List<ChangeSet> getChangeSets() {
        List<Resource> files = new FilesetXMLReader().getFiles(file);
        List<ChangeSet> changeSets = new ArrayList<ChangeSet>();

        ChangeSetReaderFactory readerFactory = ChangeSetReaderFactory.getInstance();
        for (Resource file : files) {
            changeSets.addAll(readerFactory.getChangeSetReader(file).getChangeSets(file));
        }
        logChangeSets(changeSets);
        return changeSets;
    }

    private void logChangeSets(List<ChangeSet> changeSets) {
        if (isVerbose) {
            for (ChangeSet changeSet : changeSets) {
                logger.trace("Changeset");
                logger.trace("id: " + changeSet.getChangeId());
                logger.trace("author: " + changeSet.getAuthor());
                for (Script command : changeSet.getCommands()) {
                    logger.trace("script");
                    logger.trace(command.getBody());
                }
            }
        }
    }

    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setFile(Resource file) {
        this.file = file;
    }

    public void setVerbose(boolean isVerbose) {
        this.isVerbose = isVerbose;
        if (isVerbose) {
            logger.setLevel(Level.TRACE);
        }
    }
}
