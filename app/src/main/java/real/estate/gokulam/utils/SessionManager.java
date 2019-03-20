package real.estate.gokulam.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentActivity;

import java.util.HashMap;

import real.estate.gokulam.R;
import real.estate.gokulam.views.registerLogin.LoginFragment;

public class SessionManager {

    private static String KEY_EMAIL = "";
    private SharedPreferences pref;
    private Editor editor;
    private FragmentActivity context;
    private int PRIVATE_MODE = 0;

    private static String PREF_NAME = "TEMPLATES";
    public static String PREF_MOBILE = "9999999999";
    public static String KET_PROFILE_PHOTO = "";
    private static String IS_LOGIN = "IsLogedIn";
    public static String KEY_NO = "PURPLE";
    public static String KEY_ID = "14001001";
    public static String PREF_TOKEN = "token";
    public static String PREF_TYPE = "type";

    public SessionManager(FragmentActivity context) {
        this.context = context;
        this.pref = context.getSharedPreferences(KEY_NO, PRIVATE_MODE);
        this.editor = pref.edit();
    }

    public SessionManager() {

    }

    public void createLoginSession(String name, String email, String mobile, String id, String token, String usert_type) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(PREF_NAME, name);
        //editor.putString(KET_PROFILE_PHOTO, photo);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(PREF_MOBILE, mobile);
        editor.putString(PREF_TOKEN, token);
        editor.putString(PREF_TYPE, usert_type);
        editor.commit();

    }

    public boolean checkLogin() {
        if (!this.isLoggedIn()) {
            // context.getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new Login()).commit();
            return false;
        } else {
            //context.getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new Attendace()).commit();
            return true;
        }

    }

    public static String getUserId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(KEY_NO, 0);
        return pref.getString(KEY_ID, KEY_ID);
    }

    public static String getUserType(Context context) {
        SharedPreferences pref = context.getSharedPreferences(KEY_NO, 0);
        return pref.getString(PREF_TYPE, PREF_TYPE);
    }

    public static String getToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(KEY_NO, 0);
        return pref.getString(PREF_TOKEN, PREF_TOKEN);
    }


    public static String getMobile(Context context) {
        SharedPreferences pref = context.getSharedPreferences(KEY_NO, 0);
        return pref.getString(PREF_MOBILE, PREF_MOBILE);
    }

    public static String getEmailId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(KEY_NO, 0);
        return pref.getString(KEY_EMAIL, KEY_EMAIL);
    }

    public static String getName(Context context) {
        SharedPreferences pref = context.getSharedPreferences(KEY_NO, 0);
        return pref.getString(PREF_NAME, PREF_NAME);
    }

    public static String getProfileImg(Context context) {
        SharedPreferences pref = context.getSharedPreferences(KEY_NO, 0);
        return pref.getString(KET_PROFILE_PHOTO, KET_PROFILE_PHOTO);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("profilePhoto", pref.getString(KET_PROFILE_PHOTO, KET_PROFILE_PHOTO));
        user.put("mobileNumber", pref.getString(PREF_MOBILE, PREF_MOBILE));
        user.put("email", pref.getString(KEY_EMAIL, KEY_EMAIL));
        user.put("name", pref.getString(PREF_NAME, PREF_NAME));
        user.put("id", pref.getString(KEY_ID, KEY_ID));

        return user;
    }

    public void logoutUser(FragmentActivity fragmentActivity) {
        editor.clear();
        editor.commit();
        fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new LoginFragment()).commit();
        //LoginActivity.startLogin(context);
        //context.finish();
    }

    public void upDate(String Employeephoto) {
        editor.putString(KET_PROFILE_PHOTO, Employeephoto);
        editor.commit();
    }


    public static void upDateProfileImage(String Employeephoto, Context context) {
        SharedPreferences pref = context.getSharedPreferences(KEY_NO, 0);
        Editor editor = pref.edit();
        editor.putString(KET_PROFILE_PHOTO, Employeephoto);
        editor.commit();
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


}
