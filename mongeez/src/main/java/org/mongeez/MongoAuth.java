package org.mongeez;

public class MongoAuth {
	private String username;
	private String password;
	
	public MongoAuth(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}
