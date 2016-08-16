package com.addonovan.ftcext

import android.app.Activity
import android.content.Context
import android.util.Log

/**
 * Hacks. You probably don't want to look at this file.
 * Seriously, just stop watching.
 *
 * @author addonovan
 * @since 6/12/16
 */

// ahhh
// the sweet sound of suppressed warnings
// I sure do love my suppression!

// All logcat commands are just functions, without the Log. why? because
@Suppress( "unused" ) fun v( tag: String, data: String ) = Log.v( tag, data );
@Suppress( "unused" ) fun d( tag: String, data: String ) = Log.d( tag, data );
@Suppress( "unused" ) fun i( tag: String, data: String ) = Log.i( tag, data );
@Suppress( "unused" ) fun w( tag: String, data: String ) = Log.w( tag, data );
@Suppress( "unused" ) fun e( tag: String, data: String ) = Log.e( tag, data );
@Suppress( "unused" ) fun wtf( tag: String, data: String ) = Log.wtf( tag, data );

// assumes it's an ftcext item and just grabs the class name and makes that the tag
@Suppress( "unused" ) fun Any.v( data: String ) = Log.v( "ftcext.${javaClass.simpleName}", data );
@Suppress( "unused" ) fun Any.d( data: String ) = Log.d( "ftcext.${javaClass.simpleName}", data );
@Suppress( "unused" ) fun Any.i( data: String ) = Log.i( "ftcext.${javaClass.simpleName}", data );
@Suppress( "unused" ) fun Any.w( data: String ) = Log.w( "ftcext.${javaClass.simpleName}", data );
@Suppress( "unused" ) fun Any.e( data: String ) = Log.e( "ftcext.${javaClass.simpleName}", data );
@Suppress( "unused" ) fun Any.wtf( data: String ) = Log.wtf( "ftcext.${javaClass.simpleName}", data );

@Suppress( "unused" ) fun Any.v( data: String, throwable: Throwable ) = Log.v( "ftcext.${javaClass.simpleName}", data, throwable );
@Suppress( "unused" ) fun Any.d( data: String, throwable: Throwable ) = Log.d( "ftcext.${javaClass.simpleName}", data, throwable );
@Suppress( "unused" ) fun Any.i( data: String, throwable: Throwable ) = Log.i( "ftcext.${javaClass.simpleName}", data, throwable );
@Suppress( "unused" ) fun Any.w( data: String, throwable: Throwable ) = Log.w( "ftcext.${javaClass.simpleName}", data, throwable );
@Suppress( "unused" ) fun Any.e( data: String, throwable: Throwable ) = Log.e( "ftcext.${javaClass.simpleName}", data, throwable );
@Suppress( "unused" ) fun Any.wtf( data: String, throwable: Throwable ) = Log.wtf( "ftcext.${javaClass.simpleName}", data, throwable );


/**
 * The current application context.
 * This is the equivalent of the [HardwareMap.appContext]; however, this
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