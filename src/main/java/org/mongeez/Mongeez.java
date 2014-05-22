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

import org.mongeez.commands.ChangeSet;
import org.mongeez.commands.Command;
import org.mongeez.commands.CustomMongeezCommand;
import org.mongeez.dao.MongeezDao;
import org.mongeez.dao.impl.MongeezDaoImpl;
import org.mongeez.reader.ChangeSetFileProvider;
import org.mongeez.reader.ChangeSetReaderFactory;
import org.mongeez.reader.FilesetXMLChangeSetFileProvider;

import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Mongeez {
    private final static Logger logger = LoggerFactory.getLogger(Mongeez.class);

    private Mongo mongo = null;
    private String dbName;
    private MongoAuth auth = null;
    private ChangeSetFileProvider changeSetFileProvider;
    private String context = null;
    private Map<String, CustomMongeezCommand> customCommands;

    public void process() throws IOException {
        List<ChangeSet> changeSets = getChangeSets();
        MongeezDao dao = new MongeezDaoImpl(mongo, dbName, auth);
        new ChangeSetExecutor(dao, context, customCommands).execute(changeSets);
    }

    private List<ChangeSet> getChangeSets() {
        List<Resource> files = changeSetFileProvider.getChangeSetFiles();
        List<ChangeSet> changeSets = new ArrayList<ChangeSet>();

        ChangeSetReaderFactory readerFactory = ChangeSetReaderFactory.getInstance();
        for (Resource file : files) {
            changeSets.addAll(readerFactory.getChangeSetReader(file).getChangeSets(file));
        }
        logChangeSets(changeSets);
        return changeSets;
    }

    private void logChangeSets(List<ChangeSet> changeSets) {
        if (logger.isTraceEnabled()) {
            for (ChangeSet changeSet : changeSets) {
                logger.trace("Changeset");
                logger.trace("id: " + changeSet.getChangeId());
                logger.trace("author: " + changeSet.getAuthor());
                if (! "".equals(changeSet.getContexts())) {
                    logger.trace("contexts: {}", changeSet.getContexts());
                }
                for (Command command : changeSet.getCommands()) {
                    logger.trace("command");
                    logger.trace(command.toString());
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

    public void setAuth(MongoAuth auth) {
        this.auth = auth;
    }

    /**
     * Convenience method to set the ChangeSetFileProvider to an XML fileset based on the specified file
     */
    public void setFile(Resource file) {
        setChangeSetFileProvider(new FilesetXMLChangeSetFileProvider(file));
    }

    public void setChangeSetFileProvider(ChangeSetFileProvider changeSetFileProvider) {
        this.changeSetFileProvider = changeSetFileProvider;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setCustomCommands(Map<String, CustomMongeezCommand> customCommands) {
        this.customCommands = customCommands;
    }
}
