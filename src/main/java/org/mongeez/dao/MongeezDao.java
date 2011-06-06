package org.mongeez.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import org.apache.commons.lang.time.DateFormatUtils;
import org.mongeez.commands.ChangeSet;

public class MongeezDao {
    private DB db;

    public MongeezDao(Mongo mongo, String databaseName) {
        db = mongo.getDB(databaseName);

        BasicDBObject keys = new BasicDBObject();
        keys.append("file", 1);
        keys.append("changeId", 1);
        keys.append("author", 1);
        getMongeezCollection().ensureIndex(keys);
    }

    public boolean wasExecuted(ChangeSet changeSet) {
        BasicDBObject query = new BasicDBObject();
        query.append("file", changeSet.getFile());
        query.append("changeId", changeSet.getChangeId());
        query.append("author", changeSet.getAuthor());

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
        object.append("file", changeSet.getFile());
        object.append("changeId", changeSet.getChangeId());
        object.append("author", changeSet.getAuthor());
        object.append("date", DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(System.currentTimeMillis()));

        getMongeezCollection().insert(object, WriteConcern.SAFE);
    }
}
