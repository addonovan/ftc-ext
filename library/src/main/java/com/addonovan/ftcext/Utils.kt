package com.addonovan.ftcext

import android.app.AlertDialog
import android.content.DialogInterface
import android.text.InputType
import android.widget.*
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
 * @return The value of the [Register.name].
 */
fun getRegisterName( clazz: Class< * > ) = clazz.getAnnotation( Register::class.java ).name;

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
 * @param[positive]
 *          The text for the positive button. Default is "Yes"
 * @param[neutral]
 *          The text for the neutral button (null for none). Default is `null`
 * @param[negative]
 *          The text for the negative button. Default is "No"
 * @param[onClick]
 *          The function to invoke when an option is chosen, the parameter is the
 *          text of the chosen button.
 */
fun alert( title: String, message: String,
           positive: String = "Yes", neutral: String? = null, negative: String = "No",
           onClick: ( String ) -> Unit )
{
    Activity.runOnUiThread {
        // the basics of the window
        val builder = AlertDialog.Builder( Context );
        builder.setTitle( title );
        builder.setMessage( message );

        // set up the buttons
        builder.setNegativeButton( negative, { dialog, which ->
            onClick( negative );
            dialog.dismiss();
        } );

        builder.setPositiveButton( positive, { dialog, which ->
            onClick( positive );
            dialog.dismiss();
        } );

        if ( neutral != null )
        {
            builder.setNeutralButton( neutral, { dialog, which ->
                onClick( neutral );
                dialog.dismiss();
            } );
        }

        // don't let the user cancel it, they have to choose a button
        builder.setCancelable( false );
        builder.show();
    }
}

/**
 * Creates a spinner dialog where the user chooses from multiple items.
 *
 * @param[title]
 *          The title of the dialog.
 * @param[message]
 *          The message of the dialog.
 * @param[items]
 *          An array of the text of the items for the spinner.
 * @param[onClick]
 *          The function to invoke when an option is chosen, the parameter is the
 *          text of the chosen button.
 */
fun spinnerDialog( title: String, message: String,
                   items: Array< String >,
                   onClick: ( String ) -> Unit )
{
    Activity.runOnUiThread {
        // the basics of the window
        val builder = AlertDialog.Builder( Activity );
        builder.setTitle( title );
        builder.setMessage( message );

        builder.setItems( items, { dialog, which ->
            onClick( items[ which ] );
            dialog.dismiss();
        } );

        builder.setCancelable( false );
        builder.show();
    }
}

/**
 * Creates a dialog that prompts the user for textual input.
 *
 * param[title]
 *          The title of the dialog.
 * @param[message]
 *          The message of the dialog.
 * @param[positive]
 *          The text for the positive button.
 * @param[onClick]
 *          The function to invoke when an option is chosen, the parameter is the
 *          text of the chosen button.
 */
fun prompt( title: String, message: String, positive: String, onClick: ( String ) -> Unit )
{
    Activity.runOnUiThread {
        // the basics of the window
        val builder = AlertDialog.Builder( Activity );
        builder.setTitle( title );
        builder.setMessage( message );

        // create the input field
        val input = EditText( Context );
        input.inputType = InputType.TYPE_CLASS_TEXT;
        builder.setView( input );

        // set up the positive button
        builder.setPositiveButton( positive, { dialog, which ->
            onClick.invoke( input.text.toString() )
            dialog.dismiss();
        } );

        builder.show();
    }
}