package de.itm.stage.abilityprofiling.DB;

/**
 * Created by sven on 06.02.15.
 */
public class DataBaseRequests {

    public static int GET_ALL_ABILITIES = 1;
    public static int GET_ALL_ICF_CODES = 1;

    public static int GET_ALL_PROFILES = 2;

    public static int GET_ALL_PERMISSIONS = 3;

    public static int CREATE_PROFILE = 4;

    public static int GET_ABILITY_BY_CODE = 5;
    public static int GET_PROFILES_BY_CODE = 6;

    public static int GET_RELATED_PROFILES_BY_CODE = 7;
    public static int GET_RELATED_PROFILES_BY_PROFILE = 8;

    public static int GET_PERMISSION_BY_APPNAME = 9;

    public static int GET_USER_ID = 10;

    public static int GET_ALL_TABLES = 11;

    //ADMIN-REQUESTS
    public static int CREATE_PERMISSION = 96;
    public static int DELETE_PROFILE = 97;
    public static int DELETE_PERMISSION = 98;
    public static int RESET_DATABASE = 99;

}
