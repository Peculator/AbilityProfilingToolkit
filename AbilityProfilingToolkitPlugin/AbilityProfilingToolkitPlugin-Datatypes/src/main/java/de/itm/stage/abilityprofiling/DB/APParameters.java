package de.itm.stage.abilityprofiling.DB;

import android.os.Bundle;

/**
 * Created by sven on 06.02.15.
 */
public class APParameters {
    public Profile profile;
    public String ICFcode;
    public String appname;
    public Permission permission;

    public int depth = -1;
    public int functionID = -1;
    public long startTime = -1;
    public long endTime = -1;
    public long refTime = -1;
    public long timeDistance = -1;

    public static String PERMISSION = "PERMISSION";
    public static String PROFILE = "PROFILE";
    public static String ICFCODE = "ICFCODE";
    public static String APPNAME = "APPNAME";
    public static String DEPTH = "DEPTH";
    public static String STARTTIME = "STARTTIME";
    public static String ENDTIME = "ENDTIME";
    public static String REFTIME = "REFTIME";
    public static String TIMEDISTANCE = "TIMEDISTANCE";
    public static String FUNCTIONID = "FUNCTIONID";


    public APParameters() {
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void setProfile(Profile p) {
        this.profile = p;
    }

    public void setICFcode(String ICFcode) {
        this.ICFcode = ICFcode;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getICFcode() {
        return ICFcode;
    }

    public String getAppname() {
        return appname;
    }

    public long getRefTime() {
        return refTime;
    }

    public void setRefTime(long refTime) {
        this.refTime = refTime;
    }

    public long getTimeDistance() {
        return timeDistance;
    }

    public void setTimeDistance(long timeDistance) {
        this.timeDistance = timeDistance;
    }

    public int getDepth() {
        return depth;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getFunctionID() {
        return functionID;
    }

    public void setFunctionID(int functionID) {
        this.functionID = functionID;
    }


    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        if (permission != null)
            bundle.putString(PERMISSION, permission.toString());

        if (profile != null)
            bundle.putString(PROFILE, profile.toString());

        if (ICFcode != null)
            bundle.putString(ICFCODE, ICFcode.toString());

        if (appname != null)
            bundle.putString(APPNAME, appname.toString());

        if (depth != -1)
            bundle.putInt(DEPTH, depth);

        if (functionID != -1)
            bundle.putInt(FUNCTIONID, functionID);

        if (refTime != -1)
            bundle.putLong(REFTIME, refTime);

        if (startTime != -1)
            bundle.putLong(STARTTIME, startTime);

        if (endTime != -1)
            bundle.putLong(ENDTIME, endTime);

        if (timeDistance != -1)
            bundle.putLong(TIMEDISTANCE, timeDistance);

        return bundle;
    }


    public static APParameters fromBundle(Bundle b) {
        APParameters para = new APParameters();

        if (b.getString(PERMISSION) != null)
            para.setPermission(Permission.toPermission(b.getString(PERMISSION)));

        if (b.getString(PROFILE) != null)
            para.setProfile(Profile.toProfile(b.getString(PROFILE)));

        if (b.getString(ICFCODE) != null)
            para.setICFcode(b.getString(ICFCODE));

        if (b.getString(APPNAME) != null)
            para.setAppname(b.getString(APPNAME));

        if (b.getInt(DEPTH) != 0)
            para.setDepth(b.getInt(DEPTH));

        if (b.getInt(FUNCTIONID) != 0)
            para.setFunctionID(b.getInt(FUNCTIONID));

        if (b.getLong(REFTIME) != 0)
            para.setRefTime(b.getLong(REFTIME));

        if (b.getLong(STARTTIME) != 0)
            para.setStartTime(b.getLong(STARTTIME));

        if (b.getLong(ENDTIME) != 0)
            para.setEndTime(b.getLong(ENDTIME));

        if (b.getLong(TIMEDISTANCE) != 0)
            para.setTimeDistance(b.getLong(TIMEDISTANCE));

        return para;
    }
}

