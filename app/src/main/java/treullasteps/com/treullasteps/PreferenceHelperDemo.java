package treullasteps.com.treullasteps;

import android.content.Context;
import android.content.SharedPreferences;

//Class for creating the shared preferences for the registration
public class PreferenceHelperDemo {

    private Context context;
    private String MY_PREF = "myPref";

    public PreferenceHelperDemo(Context context) {
        this.context = context;
    }

//Methods to manage the shared preferences(read, update)
    public void setKey(String key, String val) {

        SharedPreferences sharedPref = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, val);
        editor.commit();

    }

    public void setBool(String key, boolean val) {
        SharedPreferences sharedPref = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public boolean getBool(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        boolean val = sharedPref.getBoolean(key, true);
        return val;
    }

    public String getKey(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        String val = sharedPref.getString(key, "");
        return val;
    }
}