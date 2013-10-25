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
import org.mongeez.commands.Script;
import org.mongeez.dao.MongeezDao;

import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ChangeSetExecutor {
    private final Logger logger = LoggerFactory.getLogger(ChangeSetExecutor.class);

    private MongeezDao dao = null;
    private String context = null;

    public ChangeSetExecutor(Mongo mongo, String dbName, String context) {
        this(mongo, dbName, context, null);
    }

    public ChangeSetExecutor(Mongo mongo, String dbName, String context, MongoAuth auth) {
        dao = new MongeezDao(mongo, dbName, auth);
        this.context = context;
    }

    public void execute(List<ChangeSet> changeSets) {
        for (ChangeSet changeSet : changeSets) {
            if (changeSet.canBeAppliedInContext(context)) {
                if (changeSet.isRunAlways() || !dao.wasExecuted(changeSet)) {
                    execute(changeSet);
                    logger.info("ChangeSet " + changeSet.getChangeId() + " has been executed");
                } else {
                    logger.info("ChangeSet already executed: " + changeSet.getChangeId());
                }
            }
            else {
                logger.info("Not executing Changeset {} it cannot run in the context {}", changeSet.getChangeId(), context);
            }
        }
    }

    private void execute(ChangeSet changeSet) {
        try {
            for (Script command : changeSet.getCommands()) {
                command.run(dao);
            }
        } catch (RuntimeException e) {
            if (changeSet.isFailOnError()) {
                throw e;
            } else {
                logger.warn("ChangeSet " + changeSet.getChangeId() + " has failed, but failOnError is set to false", e.getMessage());
            }
        }
        dao.logChangeSet(changeSet);
    }
}
