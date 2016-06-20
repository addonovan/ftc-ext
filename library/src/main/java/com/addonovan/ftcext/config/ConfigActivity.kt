package com.addonovan.ftcext.config

import android.app.Fragment
import android.os.Bundle
import android.preference.*
import com.addonovan.ftcext.*
import com.addonovan.ftcext.control.AbstractOpMode
import com.addonovan.ftcext.control.OpModes
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.robocol.Telemetry

/**
 * The activity used to edit and create new OpMode configurations.
 */
class ConfigActivity : PreferenceActivity()
{

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        System.setProperty( "ftcext.inconfig", "true" ); // makes sure some errors don't happen
        Hardware = HardwareBundle( Gamepad(), Gamepad(), Telemetry(), FalseHardwareMap( this ) ); // spoof hardware info

        // add the main fragment then let it sort it out from here
        fragmentManager.beginTransaction().replace( android.R.id.content, OpModeListPreference() ).commit();

        System.setProperty( "ftcext.inconfig", "false" ); // unset the flag
    }

}

class OpModeListPreference : PreferenceFragment()
{

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.prefs_opmode_list);

        val opModeList = findPreference( "opmode_list" ) as PreferenceCategory;

        // add the VariantListPreferences
        for ( ( name, clazz ) in OpModes )
        {
            val opModeScreen = preferenceManager.createPreferenceScreen( activity );
            opModeScreen.title = name;
            opModeScreen.setOnPreferenceClickListener {

                currentOpModeName = name;

                // switch to the new fragment
                fragmentManager.beginTransaction().replace( android.R.id.content, VariantListPreference() ).commit();

                true; // click handeled
            };

            opModeList.addPreference( opModeScreen );
        }
    }

}



/** Storage for the VariantListPreference fragment. */
private var currentOpModeName: String? = null;

class VariantListPreference : PreferenceFragment()
{

    private val opModeName by lazy()
    {
        currentOpModeName!!;
    }

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.prefs_variant_list );

        val configs = getOpModeConfigs( opModeName ); // all the configurations for the opmode

        // add action for clicking activate variant
        val chooseVariant = findPreference( "choose_variant" ) as ListPreference;
        chooseVariant.entries = Array( configs.size, { i -> configs[ i ].Variant } ); // create a list of all variants to select from
        chooseVariant.entryValues = chooseVariant.entries; // they're the same
        chooseVariant.value = getActiveVariant( opModeName );

        // change the active config when the preference is updated
        chooseVariant.setOnPreferenceChangeListener { preference, value ->
            setActiveConfig( opModeName, value as String );
            true;
        };

        // add action for creating a variant
        val createVariant = findPreference( "create_variant" ) as EditTextPreference;
        createVariant.setOnPreferenceChangeListener { preference, value -> addVariant( value as String ); };
        createVariant.text = ""; // it should always be blank

        // the category for variants
        val variantList = findPreference( "variant_list" ) as PreferenceCategory;

        for ( config in configs )
        {
            val variantScreen = preferenceManager.createPreferenceScreen( activity );
            variantScreen.title = config.Variant;

            variantScreen.setOnPreferenceClickListener {

                currentVariant = config;

                // switch fragments
                fragmentManager.beginTransaction().replace( android.R.id.content, VariantConfigPreference() ).commit();

                true;
            };

            variantList.addPreference( variantScreen );
        }
    }

    /**
     * Adds a new variant to the list of configurations.
     *
     * @param[name]
     *          The name of the new variant.
     * @return `true` if the name was okay, `false`, if it wasn't.
     */
    private fun addVariant( name: String ): Boolean
    {
        val variantName = name.trim();

        // the name isn't acceptable if it's:
        // 1. blank or empty
        // 2. 'default' in any case
        // 3. Already the name of a variant (case insensitive)
        if ( variantName.isBlank() || variantName.equals( "default", ignoreCase = true ) ) return false;

        // is this already a variant?
        for ( exisingConfig in getOpModeConfigs( opModeName ) )
        {
            if ( name.equals( exisingConfig.Variant, ignoreCase = true ) ) return false;
        }

        getOpModeConfig( opModeName, name ); // create the variant entry
        currentOpModeName = opModeName;

        // restart the fragment to handle the new variant
        fragmentManager.beginTransaction().replace( android.R.id.content, VariantListPreference() ).commit();

        return true; // the preference can be updated
    }

}



/** Storage for the VariantConfigPReference fragment. */
private var currentVariant: OpModeConfig? = null;

class VariantConfigPreference : PreferenceFragment()
{

    private val variant by lazy()
    {
        currentVariant!!;
    }

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.prefs_variant );

        val deleteVariant = findPreference( "delete_variant" ) as PreferenceScreen;
        deleteVariant.setOnPreferenceClickListener {

            // TODO ask the user to confirm
            variant.delete();

            // if this is default, recreate the preference
            if ( variant.Variant.equals( "default", ignoreCase = true ) )
            {
                getOpModeConfig( variant.OpModeName, "default" );
            }

            // go back to the previous screen
            currentOpModeName = variant.OpModeName;

            fragmentManager.beginTransaction().replace( android.R.id.content, VariantListPreference() ).commit();

            true;
        };

        setDefaults();

        val configList = findPreference( "config_list" ) as PreferenceCategory;
        val config = variant.dataMap;

        // iterate over the values
        for ( ( key, value ) in config )
        {
            val inType = guessType( value );

            var preference: Preference;

            // create the specific type of preference based on what was returned by guessType()
            if ( inType is Boolean )
            {
                preference = CheckBoxPreference( activity );
                preference.isChecked = inType;
            }
            else if ( inType is Long || inType is Double )
            {
                preference = EditTextPreference( activity );
                // TODO force only some characters to be allowed
                preference.text = inType.toString();
            }
            else
            {
                preference = EditTextPreference( activity );
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

            configList.addPreference( preference ); // add the preference
        }
    }

    /**
     * Sets the default values to the current variant if they weren't already
     * configured.
     */
    private fun setDefaults()
    {
        val name = variant.OpModeName;
        val realActiveVariant = getActiveVariant( name );

        variant.activate();

        // when instantiated, all the variables will be set and our temporarily-active-config
        // will have all the default values, if it didn't already have the entries
        try
        {
            val instance = OpModes[ variant.OpModeName ]!!.newInstance();
            instance.init(); // just to be sure, initialize the class as well
        }
        catch ( e: Exception )
        {
            w( "Exception instantiating OpMode (${variant.OpModeName}) for configuration", e );
            w( "Some values may not be present in the configurator" ); // configurator is a word?
        }

        setActiveConfig( name, realActiveVariant ); // undo our cheat
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