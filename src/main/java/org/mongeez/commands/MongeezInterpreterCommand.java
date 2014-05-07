package org.mongeez.commands;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.mongeez.dao.MongeezDao;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MongeezInterpreterCommand.
 *
 * @author gpanthe
 * @since 7/05/2014
 */
public class MongeezInterpreterCommand implements CustomMongeezCommand {

    public static final Pattern PATTERN = Pattern.compile("db\\.(\\w+)\\.(\\w+)(\\.\\d+)?");

    @Override
    public void run(MongeezDao dao, Properties props) {
        for (String key : props.stringPropertyNames()) {
            final Matcher matcher = PATTERN.matcher(key);
            if (matcher.matches()) {
                String collection = matcher.group(1);
                String operation = matcher.group(2);
                final String value = props.getProperty(key);
                if ("update".equals(operation)) {
                    updateCollection(dao, collection, value);
                } else if ("insert".equals(operation)) {
                    insertCollection(dao, collection, value);
                }
            }
        }
    }

    private void insertCollection(MongeezDao dao, String collection, String value) {
        final DBObject params = (DBObject) JSON.parse(value);
        dao.insertCollection(collection, params);
    }

    private void aggregateCollection(MongeezDao dao, String collection, String value) {
        final DBObject params = (DBObject) JSON.parse(value);
        dao.insertCollection(collection, params);
    }

    private void updateCollection(MongeezDao dao, String collection, String value) {
        final DBObject params = (DBObject) JSON.parse(value);
        final DBObject query = (DBObject) params.get("query");
        final DBObject update = (DBObject) params.get("update");
        final BasicDBObject options = (BasicDBObject) params.get("options");
        final boolean upsert = options != null && options.getBoolean("upsert");
        final boolean multi = options != null && options.getBoolean("multi");
        dao.updateCollection(collection, query, update, upsert, multi);
    }

}
