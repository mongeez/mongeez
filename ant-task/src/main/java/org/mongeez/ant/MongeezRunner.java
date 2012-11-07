package org.mongeez.ant;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.mongeez.Mongeez;
import org.mongeez.MongoAuth;
import org.springframework.core.io.FileSystemResource;

import com.mongodb.Mongo;
import com.mongodb.ServerAddress;

public class MongeezRunner extends Task {
    private String dbName;
    private String host;
    private String urls;
    private String username;
    private String password;
    private Integer port;
    private String filePath;
    private Boolean verbose = false;

    // The method executing the task
    public void execute() {
        if (StringUtils.isNotBlank(filePath)) {
            Mongo mongo = null;
            try {
                if(StringUtils.isNotBlank(host)) {
                    System.out.println("Running mongeez against single mongodb instance...");
                    System.out.println("using following configs: dbName:" + dbName + " host:" + host + " username:" + username
                            + " password:" + password + " filePath:" + filePath + " port:" + port);
                    mongo = new Mongo(host, port);
                } else if(StringUtils.isNotBlank(urls)) {
                    System.out.println("Running mongeez against replicaSet mongodb...");
                    System.out.println("using following configs: dbName:" + dbName + " urls:" + urls + " username:" + username
                            + " password:" + password + " filePath:" + filePath);
                    List<ServerAddress> cluster = new ArrayList<ServerAddress>();
                    String[] hostPortPairs = urls.split(",");
                    if(hostPortPairs.length <= 1) {
                        System.err.println("urls property MUST have more than one host:port pair, separated by commas");
                        return;
                    }
                    for(String hostPortPair : hostPortPairs) {
                        String[] hostPort = hostPortPair.split(":");
                        if(hostPort.length != 2) {
                            System.err.printf("%s in urls property is not of the format host:port!\n");
                            return;
                        }
                        cluster.add(new ServerAddress(hostPort[0], Integer.valueOf(hostPort[1])));
                    }
                    mongo = new Mongo(cluster);
                }
            } catch(UnknownHostException e) {
                throw new BuildException(e);
            }
            
            if(verbose) {
                System.out.println("[verbose logging enabled]");
            }

            Mongeez mongeez = new Mongeez();
            mongeez.setFile(new FileSystemResource(filePath));
            mongeez.setMongo(mongo);
            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                MongoAuth auth = new MongoAuth(username, password);
                mongeez.setAuth(auth);
            }
            mongeez.setDbName(dbName);
            mongeez.setVerbose(verbose);
            mongeez.process();

        } else {
            System.err.print("FilePath is required");
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
    
    public void setUrls(String urls) {
        this.urls = urls;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
<<<<<<< HEAD
    }
    
    public void setVerbose(Boolean verbose) {
        this.verbose = verbose;
=======
>>>>>>> 504bdca... US66869 changes
    }
}
