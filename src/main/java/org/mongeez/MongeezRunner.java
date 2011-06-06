/*************************************************************************
 *
 * RST GROUP CONFIDENTIAL
 * ______________________
 *
 * [2007] - [2011] RST Group Inc.
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property
 * of RST Group Inc. and its suppliers, if any. The intellectual and
 * technical concepts contained herein are proprietary to RST Group Inc.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret and copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from RST Group Inc.
 */
package org.mongeez;

import com.mongodb.Mongo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * @author oleksii
 * @since 5/2/11
 */
public class MongeezRunner implements InitializingBean {
    private boolean executeEnabled = false;
    private Mongo mongo;
    private String dbName;
    private Resource file;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isExecuteEnabled()) {
            Mongeez mongeez = new Mongeez();
            mongeez.setMongo(mongo);
            mongeez.setDbName(dbName);
            mongeez.setFile(file);

            mongeez.process();
        }
    }

    public boolean isExecuteEnabled() {
        return executeEnabled;
    }

    public void setExecuteEnabled(boolean executeEnabled) {
        this.executeEnabled = executeEnabled;
    }

    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setFile(Resource file) {
        this.file = file;
    }
}
