package org.mongeez.ant;

import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.mongeez.Mongeez;
import org.mongeez.MongoAuth;
import org.springframework.core.io.FileSystemResource;

import com.mongodb.Mongo;

public class MongeezRunner extends Task {
    private String dbName;
    private String host;
    private String username;
    private String password;
    private Integer port;
    private String filePath;

    // The method executing the task
    public void execute() {
        System.out.println("using following configs: dbName:" + dbName + " host:" + host + " username:" + username
                + " password:" + password + " filePath:" + filePath + " port:" + port);
        if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(filePath)) {

            Mongeez mongeez = new Mongeez();
            mongeez.setFile(new FileSystemResource(filePath));
            try {
                mongeez.setMongo(new Mongo(host, port));
                if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                    MongoAuth auth = new MongoAuth(username, password);
                    mongeez.setAuth(auth);
                }
            } catch (UnknownHostException e) {
                throw new BuildException(e);
            }
            mongeez.setDbName(dbName);
            mongeez.setVerbose(true);
            mongeez.process();

        } else {
            System.err.print("Host and FilePath is required");
        }
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
