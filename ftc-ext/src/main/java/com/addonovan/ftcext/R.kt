package com.addonovan.ftcext

import android.util.Log
import android.view.View
import android.widget.*

/**
 * Adds components from the FTC application to our
 * R.id class.
 *
 * @author addonovan
 * @since 6/17/16
 */

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
 *          The name of the field to grab.
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