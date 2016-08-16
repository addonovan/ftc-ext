package com.addonovan.ftcext.config

import android.os.Bundle
import android.preference.*
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import com.addonovan.ftcext.*
import com.addonovan.ftcext.control.*
import com.addonovan.ftcext.hardware.CurrentHardware
import com.addonovan.ftcext.hardware.HardwareBundle
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.robocol.Telemetry

/**
 * The activity used to edit and create new OpMode configurations.
 */
class ConfigActivity : AppCompatActivity()
{

    //
    // Vars
    //

    /** The current fragment that we're looking at (used in [onBackPressed]).*/
    internal var CurrentFragment: CustomPreferenceFragment? = null;

    //
    // Overrides
    //

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        System.setProperty( "ftcext.inconfig", "true" ); // makes sure some errors don't happen
        CurrentHardware = HardwareBundle( Gamepad(), Gamepad(), Telemetry(), FalseHardwareMap( this ) ); // spoof hardware info

        // add the main fragment then let it sort it out from here
        fragmentManager.beginTransaction().replace( android.R.id.content, OpModeListPreference() ).commit();
    }

    override fun onBackPressed()
    {
        val fragment = CurrentFragment;

        // if it's null (or the top-level fragment), just let the super handle it
        if ( fragment == null || fragment is OpModeListPreference)
        {
            super.onBackPressed();
            return;
        }

        // switch to the super fragment
        fragmentManager.beginTransaction().replace( android.R.id.content, fragment.SuperFragment ).commit();
    }

    override fun onDestroy()
    {
        writeConfigs( CONFIG_FILE ); // save our edits
        System.setProperty( "ftcext.inconfig", "false" ); // unset the flag
        super.onDestroy();
    }
}

//
// OpMode Editor
//

//
// Actual Configuration
//

