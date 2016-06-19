package com.addonovan.ftcext.config

import android.os.Bundle
import android.preference.*
import com.addonovan.ftcext.*
import com.addonovan.ftcext.control.OpModes
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.robocol.Telemetry

/**
 * The activity used to edit and create new OpMode configurations.
 */
class ConfigActivity : PreferenceActivity()
{

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.opmode_preferences );

        System.setProperty( "ftcext.inconfig", "true" ); // disable the flag
        val opModeList = findPreference( getString( R.string.pref_opmodes ) ) as PreferenceCategory;
        addOpModes( opModeList );
        System.setProperty( "ftcext.inconfig", "false" ); // disable the flag
    }

    private fun addOpModes( list: PreferenceCategory )
    {
        for ( opMode in getOpModeNames() )
        {
            val subScreen = preferenceManager.createPreferenceScreen( this );
            subScreen.title = opMode;

            val variantList = PreferenceCategory( this );
            variantList.title = "$opMode Configuration Variants";
            subScreen.addPreference( variantList );

            addVariants( variantList, opMode );

            list.addPreference( subScreen );
        }
    }

    private fun addVariants( list: PreferenceCategory, opMode: String )
    {
        for ( variant in getOpModeConfigs( opMode ) )
        {
            val subScreen = preferenceManager.createPreferenceScreen( this );
            subScreen.title = variant.Variant;

            // TODO add opmode configurables
            val configurableList = PreferenceCategory( this );
            configurableList.title = "$opMode (${variant.Variant} variant) Settings";
            subScreen.addPreference( configurableList );

            val activeVariant = getActiveVariant( opMode );
            addConfigurables( configurableList, variant );
            setActiveConfig( opMode, activeVariant ); // reset the active variant to the real one

            list.addPreference( subScreen );
        }
    }

    private fun addConfigurables( list: PreferenceCategory, config: OpModeConfig )
    {
        Hardware = HardwareBundle( Gamepad(), Gamepad(), Telemetry(), HardwareMap( this ) );

        setActiveConfig( config.OpModeName, config.Variant ); // set this to be the active config so it gets used

        // instantiate the opmode so that the config has all of the values
        try
        {
            v( "Instantiating ${config.OpModeName} for configuration" );
            OpModes[ config.OpModeName ]!!.newInstance();
        }
        catch ( e: Exception )
        {
            v( "Encountered a(n) ${e.javaClass.simpleName} when instantiated ${config.OpModeName}" );
        }

        for ( ( key, value ) in config.dataMap )
        {
            val inType = guessType( value );

            var preference: Preference;

            // create the specific type of preference based on what was returned by guessType()
            if ( inType is Boolean )
            {
                preference = CheckBoxPreference( this );
                preference.isChecked = inType;
            }
            else if ( inType is Long || inType is Double )
            {
                preference = EditTextPreference( this );
                // TODO force only some characters to be allowed
                preference.text = inType.toString();
            }
            else
            {
                preference = EditTextPreference( this );
                preference.text = inType.toString();
            }

            preference.title =
                    if ( inType !is Boolean ) "$key (= ${config[ key ]})";
                    else                      key;


            preference.setOnPreferenceChangeListener { preference, value ->
                config[ key ] = value.toString();

                if ( value !is Boolean ) preference.title = "$key (= $value)";

                true;
            };

            list.addPreference( preference ); // add the preference
        }
    }

    private fun guessType( value: String ): Any
    {
        // try to cast it numerically, in order from least to most restrictive
        try { return value.toDouble(); } catch ( e: Exception ) {}
        try { return value.toLong(); } catch ( e: Exception ) {}

        // if it's true or false, it's a boolean
        if ( value.toLowerCase() == "true" || value.toLowerCase() == "false" ) return value.toBoolean();

        // it's a string
        return value;
    }

}