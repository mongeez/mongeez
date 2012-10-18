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
import org.apache.log4j.Logger;
import org.mongeez.commands.ChangeSet;
import org.mongeez.commands.Script;
import org.mongeez.dao.MongeezDao;

import java.util.List;


public class ChangeSetExecutor {
    private final Logger logger = Logger.getLogger(ChangeSetExecutor.class);

    private MongeezDao dao = null;

    public ChangeSetExecutor(Mongo mongo, String dbName) {
        dao = new MongeezDao(mongo, dbName);
    }

    public void execute(List<ChangeSet> changeSets) {
        for (ChangeSet changeSet : changeSets) {
            if (changeSet.isRunAlways() || !dao.wasExecuted(changeSet)) {
                execute(changeSet);
                logger.info("ChangeSet " + changeSet.getChangeId() + " has been executed");
            } else {
                logger.info("ChangeSet already executed: " + changeSet.getChangeId());
            }
        }
    }

    private void execute(ChangeSet changeSet) {
        for (Script command : changeSet.getCommands()) {
            command.run(dao);
        }
        dao.logChangeSet(changeSet);
    }
}
