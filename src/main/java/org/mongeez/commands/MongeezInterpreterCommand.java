package org.mongeez.commands;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.mongeez.dao.MongeezDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(MongeezInterpreterCommand.class);

    public static final Pattern PATTERN = Pattern.compile("db\\.(\\w+)\\.(\\w+)(\\.\\d+)?");

    @Override
    public void run(MongeezDao dao, Properties props) {
        for (String key : props.stringPropertyNames()) {
            final Matcher matcher = PATTERN.matcher(key);
            if (matcher.matches()) {
                LOGGER.info("Executing {}", key);
                String collection = matcher.group(1);
                String operation = matcher.group(2);
                final String value = props.getProperty(key);
                if ("update".equals(operation)) {
                    updateCollection(dao, collection, value);
                } else if ("insert".equals(operation)) {
                    insertCollection(dao, collection, value);
                } else if ("ensureIndex".equals(operation)) {
                    ensureIndex(dao, collection, value);
                } else {
                    LOGGER.warn("Unknown operation: {}", key);
                }
            }
            LOGGER.warn("Unparseable property: {}", key);
        }
    }

    private void ensureIndex(MongeezDao dao, String collection, String value) {
        final DBObject params = (DBObject) JSON.parse(value);
        final DBObject keys = (DBObject) params.get("keys");
        final DBObject options = (DBObject) params.get("options");
        dao.ensureIndex(collection, keys, options);
    }

    private void insertCollection(MongeezDao dao, String collection, String value) {
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
