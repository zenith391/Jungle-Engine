package org.jungle;

public class Jungle {

	public static final int VERSION_MAJOR = 0;
	public static final int VERSION_MINOR = 1;
	public static final int VERSION_PATCH = 3;
	public static final String BUILD_TYPE = "alpha";
	
	public String getVersion() {
		return VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH + " " + BUILD_TYPE;
	}
	
}