package com.ensiie.adgerodashboard.Structures;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.ensiie.adgerodashboard.R;

/**
 * Created by lucas on 08/05/2017.
 * This enum describes the three possible values for the card status.
 * It associates the correct drawable and string with each status.
 */

public enum AppStatus {
    INACTIVE (0, R.drawable.status_inactive, R.string.app_status_inactive),
    STANDBY (1, R.drawable.status_standby, R.string.app_status_standby),
    ACTIVE (2, R.drawable.status_ok, R.string.app_status_ok);

    /**
     * Integer value for this status
     */
    private int m_Value;

    /**
     * The drawable resource id for this status
     */
    private int m_DrawableResId;

    /**
     * The string resource id for this status
     */
    private int m_StringResId;

    /**
     * Basic constructor
     * @param value integer value
     * @param drawableResId drawable resource id
     * @param stringResId string resource id
     */
    AppStatus(int value, int drawableResId, int stringResId)
    {
        m_Value = value;
        m_DrawableResId = drawableResId;
        m_StringResId = stringResId;
    }

    /**
     * Get the instance of the AppStatus matching the given value
     * @param value integer value of the status we want
     * @return the matching AppStatus.
     */
    public static AppStatus getInstance(int value)
    {
        switch(value)
        {
            case 1:
                return STANDBY;
            case 2:
                return ACTIVE;
        }

        // Return INACTIVE by default
        return INACTIVE;
    }

    /**
     * Get the integer value of this status
     * @return the integer value of this status
     */
    public int getValue()
    {
        return m_Value;
    }

    /**
     * Get the drawable associated to this status
     * @param context context
     * @return the drawable
     */
    public Drawable getDrawable(Context context)
    {
        if (context != null)
            return ContextCompat.getDrawable(context, m_DrawableResId);

        return null;
    }

    /**
     * Get the string associated to this status
     * @param context context
     * @return the string
     */
    public String getString(Context context)
    {
        if (context != null)
            return context.getResources().getString(m_StringResId);

        return null;
    }
}
