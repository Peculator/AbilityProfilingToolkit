package de.itm.stage.abilityprofiling.DB;

public class Ability extends ResponseType{
	private long id;
	private String icfcode;
	private String title;
	private String description;
	
	public Ability(){}
	public Ability(String code, String title, String des){
		this.icfcode = code;
		this.title = title;
		this.description = des;
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

	public void setAbilityTitle(String title) {
		this.title = title;
	}

	public String getAbilityTitle() {
		return title;
	}
	
	public void setAbilityDescription(String des) {
		this.description = des;
	}

	public String getAbilityDescription() {
		return description;
	}

	//  Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return id+"&"+icfcode+"&"+title+"&"+description;
	}


    public static Ability toAbility(String string) {
        Ability p = new Ability();
        String[] results = string.split("&");
        p.setId(Integer.parseInt(results[0].trim()));
        p.setICFCode(results[1].trim());
        p.setAbilityTitle(results[2].trim());
        p.setAbilityDescription(results[3].trim());

        return p;
    }
}