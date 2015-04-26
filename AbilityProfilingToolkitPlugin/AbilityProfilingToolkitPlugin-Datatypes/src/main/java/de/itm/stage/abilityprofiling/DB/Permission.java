package de.itm.stage.abilityprofiling.DB;


public class Permission extends ResponseType{
	
	public enum PermissionStates{
		YES,NO,ADMIN
	}

    public Permission(String appName, String permissionstate) {
        this.appName = appName;
        this.permissionstate = permissionstate;
    }

    public Permission(){}

    private long id;
	private String appName;
	private String permissionstate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setAppName(String title) {
		this.appName = title;
	}

	public String getAppName() {
		return appName;
	}
	
	public void setPermissionState(String state) {
		this.permissionstate = state;
	}

	public String getPermissionState() {
		return permissionstate;
	}
		

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return id+"&"+appName+"&"+permissionstate;
	}


    public static Permission toPermission(String string) {
        Permission p = new Permission();
        String[] results = string.split("&");
        p.setId(Integer.parseInt(results[0].trim()));
        p.setAppName(results[1].trim());
        p.setPermissionState(results[2].trim());

        return p;
    }
}