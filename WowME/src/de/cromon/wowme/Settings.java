package de.cromon.wowme;

import android.content.SharedPreferences;

public class Settings {
	public Settings(SharedPreferences prefs)
	{
		mPreferences = prefs;
		DataPort = prefs.getInt("DataPort", 14500);
		DataHost = prefs.getString("DataHost", "0.0.0.0");
	}
	
	public void Save() {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt("DataPort", DataPort);
		editor.putString("DataHost", DataHost);
		
		editor.commit();
	}
	
	private SharedPreferences mPreferences;
	
	public int DataPort;
	public String DataHost;
	
	public static Settings Instance = null;
}
