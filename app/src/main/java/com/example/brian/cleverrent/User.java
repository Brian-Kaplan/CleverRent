package com.example.brian.cleverrent;

/**
 * Created by brian on 3/18/16.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class User {
    String displayName;
    String email;
    String userName;
    String provider;

    public User() {}

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getProvider() {
        return provider;
    }
}
