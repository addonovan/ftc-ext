package com.addonovan.ftcext

import android.util.Log

/**
 * Hacks. You probably don't want to look at this file.
 * Seriously, just stop watching.
 *
 * @author addonovan
 * @since 6/12/16
 */

// All Logcat commands are now just methods of every object!
fun Any.v( tag: String, data: String ) = Log.v( tag, data );
fun Any.d( tag: String, data: String ) = Log.d( tag, data );
fun Any.i( tag: String, data: String ) = Log.i( tag, data );
fun Any.w( tag: String, data: String ) = Log.w( tag, data );
fun Any.e( tag: String, data: String ) = Log.e( tag, data );
fun Any.wtf( tag: String, data: String ) = Log.wtf( tag, data );

// assumes it's an ftcext item and just grabs the class name and makes that the tag
fun Any.v( data: String ) = Log.v( "ftcext.${javaClass.simpleName}", data );
fun Any.d( data: String ) = Log.d( "ftcext.${javaClass.simpleName}", data );
fun Any.i( data: String ) = Log.i( "ftcext.${javaClass.simpleName}", data );
fun Any.w( data: String ) = Log.w( "ftcext.${javaClass.simpleName}", data );
fun Any.e( data: String ) = Log.e( "ftcext.${javaClass.simpleName}", data );
fun Any.wtf( data: String ) = Log.wtf( "ftcext.${javaClass.simpleName}", data );