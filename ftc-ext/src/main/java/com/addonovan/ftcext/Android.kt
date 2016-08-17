package com.addonovan.ftcext

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*

/**
 * File for assorted android access things.
 *
 * @author addonovan
 * @since 6/17/16
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

//
// View finding tricks
//

/** The com.qualcomm.ftcrobotcontroller.R.id class. */
private val rIdClass: Class< * > by lazy()
{
    Log.v( "ftcext.R", "Locating com.qualcomm.ftcrobotcontroller.R.id" );

    val rClass = Class.forName( "com.qualcomm.ftcrobotcontroller.R" )!!;
    val classes = rClass.declaredClasses;

    var idClass: Class< * >? = null; // find the idClass from the declared classes

    // search for the "id" class
    for ( clazz in classes )
    {
        if ( clazz.simpleName == "id" )
        {
            idClass = clazz;
            break;
        }
    }

    idClass!!;
}

/**
 * Gets the view from the Qualcomm `R.id` class that has the
 * given name.
 *
 * @param[name]
 *          The name of the field to get.
 * @return The View with the given name.
 */
private fun getView( name: String ): View
{
    val field = rIdClass.getDeclaredField( name );
    field.isAccessible = true; // just in case
    val viewId = field.get( null ) as Int; // should be static, so no instance is required

    return Activity.findViewById( viewId );
}

/** The robot icon ImageView */
val RobotIcon: ImageView by lazy()
{
    getView( "robotIcon" ) as ImageView;
}

/** The label for the current OpMode.s */
val OpModeLabel: TextView by lazy()
{
    getView( "textOpMode" ) as TextView;
}