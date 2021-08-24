package cryptos.cryptocurrency.vsam.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;

public class AppPreference {

    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEditor;;

    public static boolean isUserLoggedIn(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getBoolean("is_logged_in", false);
    }

    public static String getFirebaseToken(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("firebaseToken", "");
    }

    public static void setUserDetails(Context context, User user) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mPrefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mPrefsEditor.putString("userData", json);
        mPrefsEditor.apply();
    }

    public static void setUserLoggedIn(Context ctx, Boolean value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("is_logged_in", value);
        mPrefsEditor.apply();
    }
}
