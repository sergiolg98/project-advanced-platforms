package com.project.config;

public class ConstValue {

    public static String MAIN_PREF = "S";

    public static String SITE_URL = "https://api-rest-aquamajes.herokuapp.com";
    public static String JSON_LOGIN = SITE_URL + "/api/login/";

    //@GeneralUser
    public static String userId;
    public static String getUserId() { return userId; }public static void setUserId(String userId) { ConstValue.userId = userId; }

    public static String company;
    public static String getUserCompany() { return company; }public static void setUserCompany(String company) { ConstValue.company = company; }

    public static String name;
    public static String getName() { return name; } public static void setName(String name) { ConstValue.name = name; }

}
