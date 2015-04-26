package de.itm.stage.abilityprofiling.DB;

/**
 * Created by sven on 06.02.15
 * <p/>
 * 3 cases:
 * a)Respond with an array of profiles/abilities/permissions.
 * b)Respond with no array because of permission error
 * c)Respond with no array because of no results
 */
public class APResponse {

    int type;
    String message;
    String[] responseArray;

    public String[] getResponseArray() {
        return responseArray;
    }

    public void setResponseArray(String[] responseArray) {
        this.responseArray = responseArray;
    }

    public APResponse() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
