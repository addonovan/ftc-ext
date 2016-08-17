/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Austin Donovan (addonovan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.addonovan.ftcext

import android.util.Log as AndroidLog
import java.util.*
import kotlin.reflect.KClass

/**
 * An interface which handles the minor annoyances of having to write the log tags.
 *
 * @author addonovan
 * @since 8/16/16
 */
interface ILog
{

    val LogTag: String;

    @Suppress( "unused" ) fun v( msg: String ) = AndroidLog.v( LogTag, msg );
    @Suppress( "unused" ) fun d( msg: String ) = AndroidLog.d( LogTag, msg );
    @Suppress( "unused" ) fun i( msg: String ) = AndroidLog.i( LogTag, msg );
    @Suppress( "unused" ) fun w( msg: String ) = AndroidLog.w( LogTag, msg );
    @Suppress( "unused" ) fun e( msg: String ) = AndroidLog.e( LogTag, msg );
    @Suppress( "unused" ) fun wtf( msg: String ) = AndroidLog.e( LogTag, msg );

    @Suppress( "unused" ) fun v( msg: String, error: Throwable ) = AndroidLog.v( LogTag, msg, error );
    @Suppress( "unused" ) fun d( msg: String, error: Throwable ) = AndroidLog.d( LogTag, msg, error );
    @Suppress( "unused" ) fun i( msg: String, error: Throwable ) = AndroidLog.i( LogTag, msg, error );
    @Suppress( "unused" ) fun w( msg: String, error: Throwable ) = AndroidLog.w( LogTag, msg, error );
    @Suppress( "unused" ) fun e( msg: String, error: Throwable ) = AndroidLog.e( LogTag, msg, error );
    @Suppress( "unused" ) fun wtf( msg: String, error: Throwable ) = AndroidLog.wtf( LogTag, msg, error );

}

/** Class that implements the ILog interface. */
private class Log( override val LogTag: String ) : ILog;

/** Stores all of the log instances. */
private val logMap = HashMap< String, ILog >();

/**
 * Gets the log for the given class, if one doesn't exist, then it is made.
 *
 * @param[kClass]
 *          The class to get the log for.
 * @return The log for the class.
 */
fun getLog( kClass: KClass< * > ): ILog
{
    val name = kClass.java.name;

    if ( !logMap.containsKey( name ) )
    {
        logMap[ name ] = Log( "ftcext.${kClass.java.simpleName}" );
    }

    return logMap[ name ]!!;
}

/**
 * Gets a log for the given id. This is generally used for when classes are
 * not accessible for some reason.
 *
 * @param[id]
 *          The id of the logger.
 * @return The log for the id.
 */
fun getLog( id: String ): ILog
{
    if ( !logMap.containsKey( id ) )
    {
        logMap[ id ] = Log( "ftcext.$id" );
    }

    return logMap[ id ]!!;
}
