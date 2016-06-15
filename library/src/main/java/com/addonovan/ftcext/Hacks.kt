package com.addonovan.ftcext

import android.content.Context
import android.util.Log
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
// Debugging ClassFinder Hacks
//

/** The directory to read class files from when debugging ClassFinder */
private val DebuggingDir = File( System.getProperty( "user.dir" ) );

/** If the ftcext.debugging flag is set to true. */
val Debugging = System.getProperty( "ftcext.debugging", "false" ).toBoolean();

/** A list of class file names for the ClassFinder when debugging. */
val DebugClasses by lazy()
{
    val list = ArrayList< String >();
    val `package` = System.getProperty( "ftext.debugging.cppackage" );

    for ( file in DebuggingDir.listFiles() )
    {
        if ( file.name.endsWith( ".class" ) )
        {
            val name = file.name.replace( ".class$".toRegex(), "" );
            list.add( "$`package`.$name" );
        }
    }

    list;
}

/** The URLClassLoader used by ClassFinder when debugging. */
val DebugClassLoader by lazy()
{
    val urls = ArrayList<URL>();

    for ( file in DebuggingDir.listFiles() )
    {
        if ( file.name.endsWith( ".class" ) )
        {
            urls += file.toURL();
        }
    }

    URLClassLoader( urls.toArray( arrayOf< URL > () ) );
}