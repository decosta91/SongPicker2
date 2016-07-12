package com.example.songpicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.songpicker.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mark-00 on 01.04.2015.
 */
public class GeneralUtils {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void dealWithVolleyErrorMessages(VolleyError error, String requestURL, Context context) {
        try {
            if (error instanceof NoConnectionError) {
                Toast.makeText(context,
                        R.string.general_utils_network_not_available, Toast.LENGTH_LONG)
                        .show();
            } else if (error instanceof TimeoutError) {
                Toast.makeText(context,
                        R.string.general_utils_timeout, Toast.LENGTH_LONG)
                        .show();
                Log.d("TIMEOUT", requestURL);
            } else if (error instanceof com.android.volley.NetworkError) {
                Toast.makeText(context, R.string.general_utils_a_network_error_occured, Toast.LENGTH_LONG)
                        .show();
            } else if (error instanceof com.android.volley.AuthFailureError) {
                Toast.makeText(context, R.string.general_utils_an_authentication_error_occured, Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(context,
                        R.string.common_something_went_wrong, Toast.LENGTH_LONG)
                        .show();

                Log.d("Error", error.getMessage());

            }
        } catch (Exception e) {
            Log.e("VOLLEY", "Eh..", e);
        }
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern INVALID_USERNAME_CHARS =
            Pattern.compile("[^a-zA-Z0-9._%+-@]+", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean usernameContainsInvalidChars(String usernameStr) {
        Matcher matcher = INVALID_USERNAME_CHARS.matcher(usernameStr);
        return matcher.find();
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
}
