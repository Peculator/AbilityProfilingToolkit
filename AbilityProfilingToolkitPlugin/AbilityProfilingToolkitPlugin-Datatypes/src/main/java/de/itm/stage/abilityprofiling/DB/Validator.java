package de.itm.stage.abilityprofiling.DB;

/**
 * Created by sven on 19.02.15.
 */
public class Validator {

    public static boolean validate(APParameters parameters) {
        int functionID = parameters.getFunctionID();
        //Check function id and parameters
        if (functionID == DataBaseRequests.GET_ALL_TABLES && parameters.getAppname() != null && parameters.getAppname() != "") {
            return true;
        } else if (functionID == DataBaseRequests.GET_ALL_ABILITIES && parameters.getAppname() != null && parameters.getAppname() != "") {
            return true;
        } else if (functionID == DataBaseRequests.GET_ALL_PROFILES && parameters.getAppname() != null && parameters.getAppname() != "") {
            return true;
        } else if (functionID == DataBaseRequests.GET_ALL_PERMISSIONS && parameters.getAppname() != null && parameters.getAppname() != "") {
            return true;
        } else if (functionID == DataBaseRequests.CREATE_PROFILE && parameters.getAppname() != null && parameters.getAppname() != "" && parameters.getProfile() != null) {
            return true;
        } else if (functionID == DataBaseRequests.DELETE_PROFILE && parameters.getAppname() != null && parameters.getAppname() != "" && parameters.getProfile() != null) {
            return true;
        } else if (functionID == DataBaseRequests.CREATE_PERMISSION && parameters.getAppname() != null && parameters.getAppname() != "" && parameters.getPermission() != null) {
            return true;
        } else if (functionID == DataBaseRequests.RESET_DATABASE && parameters.getAppname() != null && parameters.getAppname() != "") {
            return true;
        } else if (functionID == DataBaseRequests.GET_ABILITY_BY_CODE && parameters.getAppname() != null && parameters.getAppname() != "" && parameters.getICFcode() != null && parameters.getICFcode() != "") {
            return true;
        } else if (functionID == DataBaseRequests.GET_PROFILES_BY_CODE && parameters.getAppname() != null && parameters.getAppname() != "" && parameters.getICFcode() != null && parameters.getICFcode() != "") {
            return true;
        } else if (functionID == DataBaseRequests.GET_RELATED_PROFILES_BY_CODE && parameters.getAppname() != null && parameters.getAppname() != "" && parameters.getICFcode() != null && parameters.getICFcode() != ""
                && parameters.getDepth() != -1 && parameters.getRefTime() != -1 && parameters.getTimeDistance() != -1) {
            return true;
        } else if (functionID == DataBaseRequests.GET_RELATED_PROFILES_BY_PROFILE && parameters.getAppname() != null && parameters.getAppname() != "" && parameters.getProfile() != null
                && parameters.getTimeDistance() != -1) {
            return true;
        } else if (functionID == DataBaseRequests.GET_USER_ID && parameters.getAppname() != null && parameters.getAppname() != "") {
            return true;
        }
        return false;
    }

}
