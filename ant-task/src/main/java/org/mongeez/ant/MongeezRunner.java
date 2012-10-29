package org.mongeez.ant;

import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.mongeez.Mongeez;
import org.mongeez.MongoAuth;
import org.springframework.core.io.ClassPathResource;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

public class MongeezRunner extends Task {
    private String dbName;
	private String host;
	private String userName;
	private String passWord;
	private Integer port;
	private String filePath;
	private boolean writeSave = true;

    // The method executing the task
    public void execute() {
    	try
		{
    	System.out.println("using following configs: dbName:"+ dbName +" host:" +host
			+  " userName:" + userName + " passWord:" + passWord + " filePath:" + filePath 
			+ " port:" + port);
		if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(filePath))
		{
			
			Mongeez mongeez = new Mongeez();
			mongeez.setFile(new ClassPathResource(filePath));
			try {
				mongeez.setMongo(new Mongo(host, port));
				if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(passWord)){
					MongoAuth auth = new MongoAuth(userName, passWord);
					mongeez.setAuth(auth);
				}
			} catch (UnknownHostException e) {
				throw new BuildException(e);
			}
			mongeez.setDbName(dbName);
			mongeez.process();
			
		}
		else
		{
			System.err.print("Host and FilePath is required");
		}
		}
		catch (Exception e) {
			System.err.print("Host and FilePath is required : " + e.getMessage());
			// TODO: handle exception in ant manner
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
    public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	public void setWriteSafe(boolean writeSafe) {
	    this.writeSave = writeSafe;
	}
}