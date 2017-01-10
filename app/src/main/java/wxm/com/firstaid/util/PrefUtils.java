package wxm.com.firstaid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Zero on 10/9/2015.
 */
public class PrefUtils {

    public static final String PREF_IS_USE="pref_is_use";

    public static final String PREF_USER_LOGIN="pref_user_login";

    public static boolean isFirstUse(final Context context){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_IS_USE,true);
    }

    public static void markUse(final Context context){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_IS_USE,false).commit();
    }
    public static boolean Logout(final Context context){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_IS_USE,true).commit();
        return true;
    }

    public static void markUserLogin(final Context context,String userId){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_USER_LOGIN, userId).commit();
    }
}
