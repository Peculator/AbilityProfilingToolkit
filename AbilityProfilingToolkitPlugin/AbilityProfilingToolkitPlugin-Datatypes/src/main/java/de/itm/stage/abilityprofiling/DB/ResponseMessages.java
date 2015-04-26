package de.itm.stage.abilityprofiling.DB;

/**
 * Created by sven on 06.02.15.
 */
public class ResponseMessages {
    public static String SUCCESS = "SUCCESS";
    public static String NO_RESULT = "NO_RESULT";
    public static String DATABASE_READY = "DATABASE_READY";
    public static String USER_ID = "USER_ID:";

    public static int TYPE_ABILITY = 1;
    public static int TYPE_PROFILE = 2;
    public static int TYPE_PERMISSION = 3;
    public static int TYPE_EXCEPTION = 4;
    public static int TYPE_NONE = 5;
    public static int TYPE_ALL = 6;

    public static String EXCEPTION_DB_NOT_READY = "DATABASE_NOT_READY_EXCEPTION";
    public static String EXCEPTION_NO_PERMISSION = "NO_PERMISSION_EXCEPTION";
    public static String EXCEPTION_WRONG_FORMAT = "WRONG_FORMAT_EXCEPTION";
}
