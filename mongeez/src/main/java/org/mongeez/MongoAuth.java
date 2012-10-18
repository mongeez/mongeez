package org.mongeez;

public class MongoAuth {

	private String userName;
	private String password;
	
	public MongoAuth(String u, String p) {

		userName = u;
		password = p;

	}
	
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}

	
}
