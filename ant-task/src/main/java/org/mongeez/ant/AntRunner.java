package org.mongeez.ant;

import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.mongeez.Mongeez;
import org.mongeez.MongoAuth;
import org.springframework.core.io.ClassPathResource;
import org.mongeez.MongoAuth;
import org.apache.commons.lang.StringUtils;

import com.mongodb.Mongo;

public class AntRunner extends Task {
    private boolean executeEnabled = false;
    private String dbName;
	private String host;
	private String userName;
	private String passWord;
	private Integer port;
	private String filePath;

    // The method executing the task
    public void execute() throws BuildException {
		System.out.println("using following configs: dbName:"+ dbName +" host:" 
			+  " userName:" + userName + " passWord:" + " filePath:" + filePath 
			+ " port:" + port);
    	Mongeez mongeez = new Mongeez();
    	mongeez.setFile(new ClassPathResource(filePath));
    	try {
            mongeez.setMongo(new Mongo(host, port));
            if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(passWord)){
                MongoAuth auth = new MongoAuth(userName, passWord);
                mongeez.setAuth(auth);
            }
        } catch (UnknownHostException e) {
            throw new BuildException(e);
        }
    	mongeez.setDbName(dbName);
    	mongeez.process();
    }

	public void setExecuteEnabled(boolean executeEnabled) {
		this.executeEnabled = executeEnabled;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
    public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
}