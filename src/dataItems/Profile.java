package dataItems;

public class Profile {

	private String id;
	private String uName;
	private String Name;
	private String profile;
	private String outRelationship;

	public Profile(String id, String name, String uName, String profile_url,
			String outRelationship) {
		setId(id);
		setName(name);
		setUName(uName);
		setProfilePicURL(profile_url);
		setOutRelationship(outRelationship);
	}

	public Profile() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUName() {
		return uName;
	}

	public void setUName(String uName) {
		this.uName = uName;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getProfilePicURL() {
		return profile;
	}

	public void setProfilePicURL(String profile) {
		this.profile = profile;
	}

	public String getOutRelationship() {
		return outRelationship;
	}
	
	public void setOutRelationship(String Relationship) {
		this.outRelationship = Relationship;
	}

}
