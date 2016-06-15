package com.addonovan.ftcext

import android.content.Context
import android.util.Log
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.robocol.Telemetry
import java.io.File
import java.lang.reflect.ParameterizedType
import java.net.URL
import java.net.URLClassLoader
import java.util.*

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

// All logcat commands are just functions, withou the Log. why? because
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
 * @param[thing]
 *          The generic object to get the type parameter from.
 * @return The type parameter of the object.
 */
fun getGenericType( thing: Any ) = ( thing.javaClass.genericSuperclass as ParameterizedType ).actualTypeArguments[ 0 ].javaClass;

/**
 * The current application context.
 */
val Context by lazy()
{
    Class.forName( "android.app.ActivityThread" ).getMethod( "currentApplication" ).invoke( null ) as Context;
}

//
// OpMode Hacks
//

/**
 * A bundle of hardware information.
 */
data class HardwareBundle( val gamepad1: Gamepad, val gamepad2: Gamepad, val telemetry: Telemetry, val hardwareMap: HardwareMap );

/** The backing nullable field for [Hardware] */
private var _hardware: HardwareBundle? = null;

/** A bundle of hardware information. */
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