package edu.psut.reviconf;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;

/**
 * Created by Oweis on 1/5/2015.
 */
public class SaveSharedPreference {

    static final String PREF_USER_NAME = "username";
    static JSONArray jsonArr;
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setJsonArr(Context ctx, JSONArray jsonArray)
    {

        jsonArr = jsonArray;
    }

    public static JSONArray getJsonArr(Context ctx)
    {
      return jsonArr;
    }

}
