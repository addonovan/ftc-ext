package com.addonovan.ftcext

import android.util.Log
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

// All Logcat commands are now just methods of every object!
@Suppress( "unused" ) fun Any.v( tag: String, data: String ) = Log.v( tag, data );
@Suppress( "unused" ) fun Any.d( tag: String, data: String ) = Log.d( tag, data );
@Suppress( "unused" ) fun Any.i( tag: String, data: String ) = Log.i( tag, data );
@Suppress( "unused" ) fun Any.w( tag: String, data: String ) = Log.w( tag, data );
@Suppress( "unused" ) fun Any.e( tag: String, data: String ) = Log.e( tag, data );
@Suppress( "unused" ) fun Any.wtf( tag: String, data: String ) = Log.wtf( tag, data );

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