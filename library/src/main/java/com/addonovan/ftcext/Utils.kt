package com.addonovan.ftcext

import android.app.AlertDialog
import android.content.DialogInterface
import com.addonovan.ftcext.control.Register

/**
 * A small file for odds and ends that don't really belong anywhere else.
 *
 * @author addonovan
 * @since 6/12/16
 */

//
// Annotations
//

/**
 * @param[clazz]
 *          The class with a [Register] annotation.
 * @return The value of the [Register.name], or `null` if the annoation isn't present.
 */
fun getRegisterName( clazz: Class< * > ): String?
{
    if ( !clazz.isAnnotationPresent( Register::class.java ) ) return null;

    return clazz.getAnnotation( Register::class.java ).name;
}

//
// Android
//

/**
 * Displays a dialog to the user with the given title, message, and options.
 *
 * @param[title]
 *          The title of the dialog.
 * @param[message]
 *          The message of the dialog.
 * @param[onClick]
 *          The function to invoke when an option is chosen, the parameter is the
 *          text of the chosen button.
 * @param[options]
 *          The dialog buttons (by default "Cancel" and "Set").
 */
fun alert( title: String, message: String, onClick: ( String ) -> Unit, vararg options: String = arrayOf( "Cancel", "Set" ) )
{
    // the basics of the window
    val builder = AlertDialog.Builder( Context );
    builder.setTitle( title );
    builder.setMessage( message );

    // set up the buttons
    builder.setItems( options, { dialog, which ->
        onClick.invoke( options[ which ] );
    } );

    // don't let the user cancel it, they have to choose a button
    builder.setCancelable( false );

    builder.show();
}