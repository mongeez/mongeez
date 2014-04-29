package org.mongeez;

public class MongoAuth {
	private String username;
	private String password;
    private String authDb;
	
    public MongoAuth(String username, String password, String authDb) {
        this.username = username;
        this.password = password;
        this.authDb = authDb;

    }

    public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}

    public String getAuthDb() {
        return authDb;
    }
}
