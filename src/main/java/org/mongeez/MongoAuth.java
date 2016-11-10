package org.mongeez;

public class MongoAuth {
    private String username;
    private String password;
    private String authDb;
    private String authMechanism = null;

    public MongoAuth(String username, String password, String authDb) {
        this.username = username;
        this.password = password;
        this.authDb = authDb;
    }

    public MongoAuth(String username, String password, String authDb, String authMechanism) {
        this.username = username;
        this.password = password;
        this.authDb = authDb;
        this.authMechanism = authMechanism;
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

    public String getAuthMechanism() {
        return authMechanism;
    }
}