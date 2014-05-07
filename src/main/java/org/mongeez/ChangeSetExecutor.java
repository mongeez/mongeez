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

import com.mongodb.Mongo;
import org.mongeez.commands.CustomMongeezCommand;
import org.mongeez.dao.MongeezDao;
import org.mongeez.dao.impl.MongeezDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ChangeSetExecutor {
    private final Logger logger = LoggerFactory.getLogger(ChangeSetExecutor.class);
    private final Map<String, CustomMongeezCommand> beanFactory;

    private MongeezDao dao = null;
    private String context = null;

    public ChangeSetExecutor(MongeezDao dao, String context, Map<String, CustomMongeezCommand> beanFactory) {
        this.beanFactory = beanFactory;
        this.dao = dao;
        this.context = context;
    }

    public void execute(List<ChangeSet> changeSets) throws IOException {
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

    private void execute(ChangeSet changeSet) throws IOException {
        try {
            for (Command command : changeSet.getCommands()) {
                command.run(dao, beanFactory);
            }
        } catch (RuntimeException e) {
            if (changeSet.isFailOnError()) {
                throw e;
            } else {
                logger.warn("ChangeSet " + changeSet.getChangeId() + " has failed, but failOnError is set to false", e.getMessage());
            }
        } catch (IOException e) {
            if (changeSet.isFailOnError()) {
                throw e;
            } else {
                logger.warn("ChangeSet " + changeSet.getChangeId() + " has failed, but failOnError is set to false", e.getMessage());
            }
        }
        dao.logChangeSet(changeSet);
    }
}
