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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.mongeez.MongoAuth;
import org.mongeez.commands.ChangeSet;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteConcern;

public class MongeezDao {
    private DB db;
    private MongoAuth auth;
    private List<ChangeSetAttribute> changeSetAttributes;
    private final Logger logger = Logger.getLogger(MongeezDao.class);

    public MongeezDao(Mongo mongo, String databaseName, MongoAuth auth) {
        this.auth = auth;
        db = mongo.getDB(databaseName);
        
        if (this.auth != null){
            db.authenticate(this.auth.getUserName(), this.auth.getPassword().toCharArray());
        }
        
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
        // first check if we have the right index, with all parameters we need
        List<DBObject> indexes = getMongeezCollection().getIndexInfo();
        DBObject changeSetExecIndexInfo = null;
        for(DBObject indexInfo : indexes) {
            DBObject keys = (DBObject)indexInfo.get("key");
            boolean isAttrIndex = false;
            // check the "type" key first
            if(keys.containsField("type")) {
                isAttrIndex = true;
                for(ChangeSetAttribute attribute : changeSetAttributes) {
                    if(!keys.containsField(attribute.name())) {
                        isAttrIndex = false;
                        break;
                    }
                }
            }
            if(!isAttrIndex) {
                continue;
            }
            // if we get here we have the right index
            changeSetExecIndexInfo = indexInfo;
            break;
        }
        BasicDBObject keys = new BasicDBObject();
        keys.append("type", RecordType.changeSetExecution.name());
        for (ChangeSetAttribute attribute : changeSetAttributes) {
            keys.append(attribute.name(), 1);
        }
        BasicDBObject options = new BasicDBObject();
        options.append("unique", Boolean.TRUE);
        if(changeSetExecIndexInfo != null) {
            boolean isDifferent = false;
            if(!changeSetExecIndexInfo.containsField("unique") || Boolean.FALSE.equals(changeSetExecIndexInfo.get("unique"))) {
                isDifferent = true;
            }
            if(isDifferent) {
                logger.info("ChangeSet entry index is not unique, recreating");
                getMongeezCollection().dropIndex(keys);
                options.append("dropDups", Boolean.TRUE);
            }
        }
        getMongeezCollection().ensureIndex(keys, options);
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
        CommandResult result = db.getLastError();
        result.throwOnError();
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
