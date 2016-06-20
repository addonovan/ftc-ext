package com.addonovan.ftcext.config

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

//    private fun createOpModesScreen()
//    {
//        val opModeCategory = findPreference( getString( R.string.pref_opmodes ) ) as PreferenceCategory;
//
//        // create teh subsection for the opmode
//        for ( opMode in getOpModeNames() )
//        {
//            val opModePref = preferenceManager.createPreferenceScreen( this );
//            opModePref.title = opMode;
//
//            createVariantsScreen( opModePref, opMode );
//
//            opModeCategory.addPreference( opModePref );
//        }
//    }
//
//    private fun createVariantsScreen( opModePref: PreferenceScreen, opMode: String )
//    {
//        val variants = getOpModeConfigs( opMode );
//
//        // create the general actions section
//        val generalCategory = PreferenceCategory( this );
//        generalCategory.title = "General";
//
//        // create the menu to delete a variant
//        val deleteVariant = ListPreference( this );
//        deleteVariant.title = "Delete a variant";
//        deleteVariant.entries = Array( variants.size, { i -> variants[ i ].Variant } );
//        generalCategory.addPreference( deleteVariant );
//
//        // create the action to create a variant
//        val createVariant = EditTextPreference( this );
//        createVariant.title = "Create a new variant";
//        createVariant.setOnPreferenceChangeListener { preference, value ->
//
//            getOpModeConfig( opMode, value.toString() );
//
//            // reload this screen
//            opModePref.removeAll();
//            createVariantsScreen( opModePref, opMode );
//
//            true;
//        };
//        generalCategory.addPreference( createVariant );
//
//        val changeActiveVariant = ListPreference( this );
//        changeActiveVariant.title = "Change active variant";
//        changeActiveVariant.entries = Array( variants.size, { i -> variants[ i ].Variant } );
//        generalCategory.addPreference( changeActiveVariant );
//
//        // actual stuff
//
//        val variantsCategory = PreferenceCategory( this );
//        variantsCategory.title = "Variants";
//
//        val activeVariant = getActiveVariant( opMode );
//        for ( variant in variants )
//        {
//            val variantPref = preferenceManager.createPreferenceScreen( this );
//            variantPref.title = variant.Variant;
//
//            createConfigurablesScreen( variantPref, variant );
//
//            variantsCategory.addPreference( variantPref );
//        }
//        setActiveConfig( opMode, activeVariant ); // reset the active variant
//    }
//
//    private fun createConfigurablesScreen( variantPref: PreferenceScreen, config: OpModeConfig )
//    {
//        setActiveConfig( config.OpModeName, config.Variant ); // force this to be the active one
//
//        // create the op mode to fill in the blanks
//        try
//        {
//            OpModes[ config.OpModeName ]!!.newInstance();
//        }
//        catch ( e: Exception )
//        {
//            Log.i( "ftcext.ConfigActivity",
//                    "Encountered a(n) ${e.javaClass.simpleName} when created ${config.OpModeName}",
//                    e );
//        }
//
//        for ( ( key, value ) in config.dataMap )
//        {
//            val inType = guessType( value );
//
//            var preference: Preference;
//
//            // create the specific type of preference based on what was returned by guessType()
//            if ( inType is Boolean )
//            {
//                preference = CheckBoxPreference( this );
//                preference.isChecked = inType;
//            }
//            else if ( inType is Long || inType is Double )
//            {
//                preference = EditTextPreference( this );
//                // TODO force only some characters to be allowed
//                preference.text = inType.toString();
//            }
//            else
//            {
//                preference = EditTextPreference( this );
//                preference.text = inType.toString();
//            }
//
//            preference.title =
//                    if ( inType !is Boolean ) "$key (= ${config[ key ]})";
//                    else                      key;
//
//
//            preference.setOnPreferenceChangeListener { preference, value ->
//                config[ key ] = value.toString();
//
//                if ( value !is Boolean ) preference.title = "$key (= $value)";
//
//                true;
//            };
//
//            variantPref.addPreference( preference ); // add the preference
//        }
//    }
//
//    private fun guessType( value: String ): Any
//    {
//        // try to cast it numerically, in order from least to most restrictive
//        try { return value.toDouble(); } catch ( e: Exception ) {}
//        try { return value.toLong(); } catch ( e: Exception ) {}
//
//        // if it's true or false, it's a boolean
//        if ( value.toLowerCase() == "true" || value.toLowerCase() == "false" ) return value.toBoolean();
//
//        // it's a string
//        return value;
//    }

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

                val fragment = VariantListPreference();
                fragment.Name = name;
                fragment.Class = clazz;

                // switch to the new fragment
                fragmentManager.beginTransaction().replace( android.R.id.content, fragment ).commit();

                true;
            };

            opModeList.addPreference( opModeScreen );
        }
    }

}

class VariantListPreference : PreferenceFragment()
{

    var Name: String? = null;

    var Class: Class< out AbstractOpMode >? = null;

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.prefs_variant_list);

        preferenceScreen.title = "$Name Variants";

        val variantList = findPreference( "variant_list" ) as PreferenceCategory;

        // add the ConfigurableListPreferences
        for ( variant in getOpModeConfigs( Name!! ) )
        {
            val variantScreen = preferenceManager.createPreferenceScreen( activity );
            variantScreen.title = variant.Variant;

            // TODO add VariantPreferences fragments on press

            variantList.addPreference( variantScreen );
        }
    }

}

class VariantPreferences : PreferenceFragment()
{

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.prefs_variant);

        // add the preferences
    }

}