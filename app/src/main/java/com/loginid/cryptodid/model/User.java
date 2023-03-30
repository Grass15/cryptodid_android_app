package com.loginid.cryptodid.model;

import java.io.Serializable;

public class User implements Serializable {
    public String username;
    public String password;
    public String firstname;
    public String lastname;
    public String phone;
    public String address;

    public String id;

    public User(String a,String b,String c){
        this.username = a;
        this.password = b;
        this.id = c;
    }

}
