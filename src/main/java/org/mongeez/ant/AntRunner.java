package org.mongeez.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.mongeez.Mongo;
import org.mongeez.Resource;
import org.mongeez.reader.ChangeSetFileProvider;

public class AntRunner extends Task {
    private boolean executeEnabled = false;
    private String dbName;
	private String host;
	private String port;
	private String filePath;

    // The method executing the task
    public void execute() throws BuildException {
    	Mongeez mongeez = new Mongeez();
    	mongeez.setFile(new ClassPathResource(filePath));
    	mongeez.setMongo(new Mongo(host, port));
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
}