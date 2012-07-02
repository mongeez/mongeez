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
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import org.apache.commons.lang.time.DateFormatUtils;
import org.mongeez.commands.ChangeSet;

import java.util.List;

public class MongeezDao {
    private DB db;
    private List<ChangeSetAttribute> changeSetAttributes;

    public MongeezDao(Mongo mongo, String databaseName) {
        db = mongo.getDB(databaseName);

        BasicDBObject keys = new BasicDBObject();
        for (ChangeSetAttribute attribute : changeSetAttributes) {
            keys.append(attribute.name(), 1);
        }
        getMongeezCollection().ensureIndex(keys);
    }

    public boolean wasExecuted(ChangeSet changeSet) {
        BasicDBObject query = new BasicDBObject();
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
        for (ChangeSetAttribute attribute : changeSetAttributes) {
            object.append(attribute.name(), attribute.getAttributeValue(changeSet));
        }
        object.append("date", DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(System.currentTimeMillis()));
        getMongeezCollection().insert(object, WriteConcern.SAFE);
    }
}
