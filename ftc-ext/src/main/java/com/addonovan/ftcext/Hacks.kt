package com.addonovan.ftcext

import android.app.Activity
import android.content.Context

/**
 * Hacks. You probably don't want to look at this file.
 * Seriously, just stop watching.
 *
 * @author addonovan
 * @since 6/12/16
 */

/**
 * The current application context.
 * This is the equivalent of the HardwareMap.appContext; however, this
 * is intended to be used in places where there is no Hardware map available.
 */
val Context: Context by lazy()
{
    Class.forName( "android.app.ActivityThread" ).getMethod( "currentApplication" ).invoke( null ) as Context;
}

/**
 * The current activity. Please don't look at the source for this.
 * Please please please please please.
 */
val Activity: Activity by lazy()
{
    // just ignore this please
    val activityThreadClass = Class.forName( "android.app.ActivityThread" );
    val activityThread = activityThreadClass.getMethod( "currentActivityThread" ).invoke( null );
    val activitiesField = activityThreadClass.getDeclaredField( "mActivities" );
    activitiesField.isAccessible = true;

    // seriously, stop reading
    var activity: Activity? = null;

    val activities = activitiesField.get( activityThread ) as Map< *, * >;
    for ( activityRecord in activities.values )
    {
        if ( activityRecord == null ) continue;

        // it's only going to get worse
        val activityRecordClass = activityRecord.javaClass;
        val pausedField = activityRecordClass.getDeclaredField( "paused" );
        pausedField.isAccessible = true;

        if ( !pausedField.getBoolean( activityRecord ) )
        {
            val activityField = activityRecordClass.getDeclaredField( "activity" );
            activityField.isAccessible = true;

            activity = activityField.get( activityRecord ) as Activity;
        }
    }

    activity ?: throw NullPointerException( "Failed to find activity!" ); // "not needed" my ass, it errors unless this is here
}