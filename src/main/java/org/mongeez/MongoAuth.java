package org.mongeez;

public class MongoAuth {
    private final String username;
    private final String password;
    private final String authenticationDatabase;

    public MongoAuth(String username, String password, String authenticationDatabase) {
        this.username = username;
        this.password = password;
        this.authenticationDatabase = authenticationDatabase;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthenticationDatabase() {
        return authenticationDatabase;
    }
}
