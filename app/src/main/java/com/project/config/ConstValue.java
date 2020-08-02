package com.project.config;

import org.json.JSONObject;

public class ConstValue {

    public static final String MAIN_PREF = "LightCare";

    public static String SITE_URL = "https://advanced-platforms-project.herokuapp.com"; //DEV
    //public static String SITE_URL = "http://localhost:3000";   //LOCAL
    public static String LOGIN = SITE_URL + "/api/login/";
    public static String USER = SITE_URL + "/api/user/";
    public static String INFORMATION = SITE_URL + "/api/information/";
    public static String IMAGE = SITE_URL + "/api/image/";

    public static JSONObject serverResponse;
    public static JSONObject getServerResponse() { return serverResponse; } public static void setServerResponse(JSONObject serverResponse) { ConstValue.serverResponse = serverResponse; }

    //@GeneralUser
    public static String userId;
    public static String getUserId() { return userId; }public static void setUserId(String userId) { ConstValue.userId = userId; }

    public static String company;
    public static String getUserCompany() { return company; }public static void setUserCompany(String company) { ConstValue.company = company; }

    public static String name;
    public static String getName() { return name; } public static void setName(String name) { ConstValue.name = name; }

    //PhotoPath
    public static String photoPath;
    public static String getPhotoPath() { return photoPath; } public static void setPhotoPath(String photoPath) { ConstValue.photoPath = photoPath; }
}
