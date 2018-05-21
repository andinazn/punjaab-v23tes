package com.andinazn.sensordetectionv2;

/**
 * Created by Amira Maulina on 4/7/2018.
 */

public class CreateUser {
    public CreateUser()
    {}

    public String name;
    public int age;

    public CreateUser(String name, int age, String email, String password, String code, String isSharing, String lat, String lng, String userid, String fallstate, String hrstate, String emergencynumber1, String emergencynumber2, String emergencynumber3, String emergencynumber4, String emergencynumber5)
    {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
        this.code = code;
        this.isSharing = isSharing;
        this.lat = lat;
        this.lng = lng;
        this.userid = userid;
        this.fallstate = fallstate;
        this.hrstate = hrstate;
        this.emergencynumber1 = emergencynumber1;
        this.emergencynumber2 = emergencynumber2;
        this.emergencynumber3 = emergencynumber3;
        this.emergencynumber4 = emergencynumber4;
        this.emergencynumber5 = emergencynumber5;
    }

    public String email;
    public String password;
    public String code;
    public String isSharing;
    public String lat;
    public String lng;
    public String userid;
    public String fallstate;
    public String hrstate;
    public String emergencynumber1;
    public String emergencynumber2;
    public String emergencynumber3;
    public String emergencynumber4;
    public String emergencynumber5;


}