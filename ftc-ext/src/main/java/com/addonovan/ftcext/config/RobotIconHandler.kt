package com.addonovan.ftcext.config

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.widget.Toast
import com.addonovan.ftcext.*

/**
 * This file handles the RobotIcon stuff.
 *
 * @author addonovan
 * @since 6/20/16
 */

/**
 * Attaches the click listener which starts [ConfigActivity] to the
 * [RobotIcon]. Also changes the icon color to show that you can
 * press it.
 */
fun attachRobotIconListener()
{
    // probably the user's first run with this framework if this is true
    if ( !CONFIG_FILE.exists() )
    {
        val message = "Click on the robot icon when the button is flashing to configure OpModes";
        Toast.makeText( Activity, message, Toast.LENGTH_LONG ).show();
    }

    getLog( "ConfigHelper").i( "Attaching robot icon's click listener" );
    RobotIcon.setOnClickListener { view ->

        getLog( "ConfigHelper").i( "Switching to ConfigActivity" );
        val intent = Intent( Activity, ConfigActivity::class.java );
        Activity.startActivity( intent );

    };

    Activity.runOnUiThread {
        RobotIcon.setBackgroundResource( R.drawable.animated_robot_icon );

        ( RobotIcon.background as AnimationDrawable ).start();
    };
}

/**
 * Detaches the click listener that started the [ConfigActivity] from
 * the [RobotIcon]. Also reverts the icon color to show that it can't
 * be pressed anymore.
 */
fun detachRobotIconListener()
{
    getLog( "ConfigHelper").i( "Detaching robot icon's click listener" );
    RobotIcon.setOnClickListener { view ->
        // do nothing, essentially the same thing as being detached
    };

    Activity.runOnUiThread {
        RobotIcon.setBackgroundResource( R.drawable.robot_icon_off);
    };
}