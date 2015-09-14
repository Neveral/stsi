package com.neveral;

/**
 * Created by Neveral on 19.11.14.
 */
public class Login {
    public static boolean authenticate(String username, String password) {
        if(username.equals("admin") && password.equals("admin"))
            return true;
        else
            return false;
    }
}
