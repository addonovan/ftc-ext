package com.addonovan.ftcext

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.*
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.robocol.Telemetry
import java.lang.reflect.ParameterizedType

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

/**
 * Returns the class of the first generic type parameter.
 * For example, if passed an `ArrayList< String >`, this
 * will return [String.class] (Java) or [String.class.javaClass] (Kotlin).
 *
 * @param[thing]
 *          The generic object to get the type parameter from.
 * @return The type parameter of the object.
 */
fun getGenericType( thing: Any ) = ( thing.javaClass.genericSuperclass as ParameterizedType ).actualTypeArguments[ 0 ].javaClass;

/**
 * The current application context.
 * This is the equivalent of the [HardwareMap.appContext]; however, this
 * is intended to be used in places where there is no Hardware map available.
 */
val Context by lazy()
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

    // activity is really hard to spell
    if ( activity == null ) throw NullPointerException( "Failed to find activity!" );

    activity as Activity; // "not needed" my ass, it errors unless this is here
}

val RobotIcon by lazy()
{
    if ( Activity.javaClass.simpleName != "FtcRobotControllerActivity" )
    {
        wtf( "ftcext.ConfigSetup", "Current Activity isn't FtcRobotControllerActivity! Can't attach configuration listeners!" );
        throw IllegalStateException( "Current activity isn't FtcRobotControllerActivity" );
    }

    v( "ftcext.ConfigSetup", "Retrieving the robot icon's resource id" );
    // In order to get to the robot icon on the robot controller activity:

    // the device name is a field in the FtcRobotControllerActivity, so get to that
    val deviceNameField = Activity.javaClass.getDeclaredField( "textDeviceName" );
    deviceNameField.isAccessible = true; // remove the private thing
    val deviceName = deviceNameField.get( Activity ) as TextView;

    // access the device name label's layout parameters (one of them is RIGHT_OF robot icon)
    val layoutParams = deviceName.layoutParams as RelativeLayout.LayoutParams;

    // access the rules for the layout parameters
    val layoutRulesField = layoutParams.javaClass.getDeclaredField( "mRules" );
    layoutRulesField.isAccessible = true; // remove the private thing again
    val layoutRules = layoutRulesField.get( layoutParams ) as IntArray; // IntArray == int[]

    // pull the id for the robot icon out of the rules
    val iconId = layoutRules[ RelativeLayout.RIGHT_OF ];

    // the next if statement will take care of the actual registration
    i( "ftcext.ConfigSetup", "Found the robot icon's resource id" );

    Activity.findViewById( iconId ) as ImageView;
}

//
// OpMode Hacks
//

/**
 * A bundle of hardware information for an OpMode.
 * This contains the two gamepads, the telemetry object, and the hardware map for
 * the OpMode to use.
 */
data class HardwareBundle( val gamepad1: Gamepad, val gamepad2: Gamepad, val telemetry: Telemetry, val hardwareMap: HardwareMap );

/** The backing nullable field for [Hardware] */
private var _hardware: HardwareBundle? = null;

/**
 * A bundle of hardware information.
 * This is ensured to be non-null, because if the backing field
 * is null, a [NullPointerException] will be thrown.
 *
 * @throws NullPointerException
 *          If the backing field hasn't been set yet.
 */
var Hardware: HardwareBundle
    get()
    {
        if ( _hardware == null ) throw NullPointerException( "HardwareBundle is being accessed before it's been set!" );
        return _hardware as HardwareBundle; // this will probably never throw an exception, hopefully
    }
    set( value )
    {
        _hardware = value;
    }