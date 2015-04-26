package de.itm.stage.abilityprofiling.DB;

import java.util.Date;

public class Profile extends ResponseType {
	private long id;
	private String icfcode;
	private String appName;
	private String rating;
	private long timeStamp;

    public Profile(String icfcode, String appName, String rating, long timeStamp) {
        this.icfcode = icfcode;
        this.appName = appName;
        this.rating = rating;
        this.timeStamp = timeStamp;
    }

    public Profile(){}

    public Profile(String icfcode, String appName, String rating) {
        this.icfcode = icfcode;
        this.appName = appName;
        this.rating = rating;
        this.timeStamp = new Date().getTime();
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getICFCode() {
		return icfcode;
	}

	public void setICFCode(String code) {
		this.icfcode = code;
	}

	public void setAppName(String title) {
		this.appName = title;
	}

	public String getAppName() {
		return appName;
	}
	
	public void setRating(String des) {
		this.rating = des;
	}

	public String getRating() {
		return rating;
	}
	
	public void setTimeStamp(long date) {
		this.timeStamp = date;
	}
	
	public Long getTimeStamp() {
		return timeStamp;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return id+"&"+icfcode+"&"+appName+"&"+rating+"&"+timeStamp;
	}

    public static Profile toProfile(String string) {
        Profile p = new Profile();
        String[] results = string.split("&");
        p.setId(Integer.parseInt(results[0].trim()));
        p.setICFCode(results[1].trim());
        p.setAppName(results[2].trim());
        p.setRating(results[3].trim());
        p.setTimeStamp(Long.parseLong(results[4].trim()));

        return p;
    }
}