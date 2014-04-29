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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import org.mongeez.reader.ChangeSetFileProvider;
import org.mongeez.validation.ChangeSetsValidator;
import org.mongeez.validation.DefaultChangeSetsValidator;

/**
 * @author oleksii
 * @since 5/2/11
 */
public class MongeezRunner implements InitializingBean {
    private boolean executeEnabled = false;
    private Mongo mongo;
    private String dbName;
    private Resource file;

    private String userName;
    private String passWord;
    private String authDb;
    
    private ChangeSetFileProvider changeSetFileProvider;

    private ChangeSetsValidator changeSetsValidator;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isExecuteEnabled()) {
            execute();
        }
    }

    public void execute() {
        Mongeez mongeez = new Mongeez();
        mongeez.setMongo(mongo);
        mongeez.setDbName(dbName);
        
        if(changeSetsValidator != null) {
            mongeez.setChangeSetsValidator(changeSetsValidator);
        }
        else {
            mongeez.setChangeSetsValidator(new DefaultChangeSetsValidator());
        }
        
        if (changeSetFileProvider != null) {
            mongeez.setChangeSetFileProvider(changeSetFileProvider);
        } else {
            mongeez.setFile(file);

            if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(passWord)){
            	MongoAuth auth = new MongoAuth(userName, passWord, authDb);
                mongeez.setAuth(auth);
            }
        }

        mongeez.process();
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

    public void setChangeSetFileProvider(ChangeSetFileProvider changeSetFileProvider) {
        this.changeSetFileProvider = changeSetFileProvider;
    }

    public String getDbName() {
        return dbName;
    }

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

    public void setAuthDb(String authDb) {
        this.authDb = authDb;
    }
}
