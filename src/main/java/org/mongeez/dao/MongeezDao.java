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

package org.mongeez.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteConcern;
import org.apache.commons.lang.time.DateFormatUtils;
import org.mongeez.commands.ChangeSet;

import java.util.ArrayList;
import java.util.List;

public class MongeezDao {
    private DB db;
    private List<ChangeSetAttribute> changeSetAttributes;

    public MongeezDao(Mongo mongo, String databaseName) {
        db = mongo.getDB(databaseName);
        configure();
    }

    private void configure() {
        addTypeToUntypedRecords();
        loadConfigurationRecord();
        ensureChangeSetExecutionIndex();
    }

    private void addTypeToUntypedRecords() {
        DBObject q = new QueryBuilder().put("type").exists(false).get();
        BasicDBObject o = new BasicDBObject("$set", new BasicDBObject("type", RecordType.changeSetExecution.name()));
        getMongeezCollection().update(q, o, false, true, WriteConcern.SAFE);
    }

    private void loadConfigurationRecord() {
        DBObject q = new QueryBuilder().put("type").is(RecordType.configuration.name()).get();
        DBObject configRecord = getMongeezCollection().findOne(q);
        if (configRecord == null) {
            if (getMongeezCollection().count() > 0L) {
                // We have pre-existing records, so don't assume that they support the latest features
                configRecord =
                        new BasicDBObject()
                                .append("type", RecordType.configuration.name())
                                .append("supportResourcePath", false);
            } else {
                configRecord =
                        new BasicDBObject()
                                .append("type", RecordType.configuration.name())
                                .append("supportResourcePath", true);
            }
            getMongeezCollection().insert(configRecord, WriteConcern.SAFE);
        }
        Object supportResourcePath = configRecord.get("supportResourcePath");

        changeSetAttributes = new ArrayList<ChangeSetAttribute>();
        changeSetAttributes.add(ChangeSetAttribute.file);
        changeSetAttributes.add(ChangeSetAttribute.changeId);
        changeSetAttributes.add(ChangeSetAttribute.author);
        if (Boolean.TRUE.equals(supportResourcePath)) {
            changeSetAttributes.add(ChangeSetAttribute.resourcePath);
        }
    }

    private void ensureChangeSetExecutionIndex() {
        BasicDBObject keys = new BasicDBObject();
        keys.append("type", RecordType.changeSetExecution.name());
        for (ChangeSetAttribute attribute : changeSetAttributes) {
            keys.append(attribute.name(), 1);
        }
        getMongeezCollection().ensureIndex(keys);
    }

    public boolean wasExecuted(ChangeSet changeSet) {
        BasicDBObject query = new BasicDBObject();
        query.append("type", RecordType.changeSetExecution.name());
        for (ChangeSetAttribute attribute : changeSetAttributes) {
            query.append(attribute.name(), attribute.getAttributeValue(changeSet));
        }
        return getMongeezCollection().count(query) > 0;
    }

    private DBCollection getMongeezCollection() {
        return db.getCollection("mongeez");
    }

    public void runScript(String code) {
        db.eval(code);
    }

    public void logChangeSet(ChangeSet changeSet) {
        BasicDBObject object = new BasicDBObject();
        object.append("type", RecordType.changeSetExecution.name());
        for (ChangeSetAttribute attribute : changeSetAttributes) {
            object.append(attribute.name(), attribute.getAttributeValue(changeSet));
        }
        object.append("date", DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(System.currentTimeMillis()));
        getMongeezCollection().insert(object, WriteConcern.SAFE);
    }
}
