package com.ensiie.adgerodashboard;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by lucas on 05/05/2017.
 * This class is used to store useful static functions.
 */

public class Utils {

    /**
     * Shows a Toast on the device displaying the given text (@see <a href="https://developer.android.com/guide/topics/ui/notifiers/toasts.html">Toasts</a>)
     * @param context The context of the application
     * @param text The text string we want to display
     */
    public static void showToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays the adgero logo in the Action Bar
     * @param activity The activity in which we wish to set up the logo.
     */
    public static void setupLogoInActionBar(AppCompatActivity activity)
    {
        if (activity.getSupportActionBar() == null)
            return;

        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setIcon(R.drawable.logo);
        activity.getSupportActionBar().setTitle("");
    }
}
