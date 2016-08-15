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
        if ( fragment == null || fragment is OpModeListPreference )
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

/**
 * A custom type of preference fragment that holds a little bit of extra data and
 * supplies a way to change the title in the toolbar and also registers the
 * fragment as the [ConfigActivity.CurrentFragment] when created.
 */
abstract class CustomPreferenceFragment : PreferenceFragment()
{

    /** The fragment for the next level up. */
    abstract val SuperFragment: PreferenceFragment?;

    //
    // Activity Overrides
    //

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        ( activity as ConfigActivity ).CurrentFragment = this; // register this with the activity so it can handle back presses
    }

    //
    // Actions
    //

    /**
     * Sets the title in the toolbar to the given text.
     *
     * @param[title]
     *          The new title of the activity.
     */
    fun setTitle( title: String )
    {
        ( activity as ConfigActivity ).actionBar?.title = title;
        ( activity as ConfigActivity ).supportActionBar?.title = title;
    }

}

//
// OpMode List
//

/**
 * A list of the opmodes. When a user clicks on one, it switches
 * to the [VariantListPreference] to edit the variants.
 */
class OpModeListPreference : CustomPreferenceFragment()
{

    /** There is no fragment above this. */
    override val SuperFragment = null;

    //
    // Activity Overrides
    //

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.prefs_opmode_list);

        setTitle( "OpModes" );

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

                true; // click handled
            };

            opModeList.addPreference( opModeScreen );
        }
    }

}

//
// OpMode Editor
//

/** Storage for the VariantListPreference fragment. */
private var currentOpModeName: String? = null;

/**
 * Lists all the variants of the opmode and allows the user
 * to edit a variant as well as create new ones and change
 * the active one.
 */
class VariantListPreference : CustomPreferenceFragment()
{

    //
    // Vals
    //

    /** We just go back to the OpMode list. */
    override val SuperFragment = OpModeListPreference();

    /** The name of the opmode we're editing. */
    private val opModeName by lazy()
    {
        currentOpModeName!!;
    }

    //
    // Activity Overrides
    //

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.prefs_variant_list );

        setTitle( "$opModeName" );
        val configs = getOpModeConfigs( opModeName ); // all the configurations for the opmode

        // add action for clicking activate variant
        val chooseVariant = findPreference( "choose_variant" ) as ListPreference;
        chooseVariant.entries = Array( configs.size, { i -> configs[ i ].VariantName } ); // create a list of all variants to select from
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
            variantScreen.title = config.VariantName;

            variantScreen.setOnPreferenceClickListener {

                currentVariant = config;

                // switch fragments
                fragmentManager.beginTransaction().replace( android.R.id.content, VariantConfigPreference() ).commit();

                true;
            };

            variantList.addPreference( variantScreen );
        }
    }

    //
    // Actions
    //

    /**
     * Adds a new variant to the list of configurations.
     *
     * @param[name]
     *          The name of the new variant.
     * @return `true` if the name was okay, `false`, if it wasn't.
     */
    private fun addVariant( name: String ): Boolean
    {
        val trimmedName = name.trim();

        // the name isn't acceptable if it's:
        // 1. blank or empty
        // 2. 'default' in any case
        // 3. Already the name of a variant (case insensitive)
        if ( trimmedName.isBlank() || trimmedName.equals( "default", ignoreCase = true ) ) return false;

        // is this already a variant?
        for ( existingConfig in getOpModeConfigs( opModeName ) )
        {
            if ( trimmedName.equals( existingConfig.VariantName, ignoreCase = true ) ) return false;
        }

        getOpModeConfig( opModeName, trimmedName ); // create the variant entry
        currentOpModeName = opModeName;

        // restart the fragment to handle the new variant
        fragmentManager.beginTransaction().replace( android.R.id.content, VariantListPreference() ).commit();

        return true; // the preference can be updated
    }

}

//
// Actual Configuration
//

/** Storage for the VariantConfigPReference fragment. */
private var currentVariant: OpModeConfig? = null;

/**
 * Configures a specific variant's variables acoording to their presumed types.
 */
class VariantConfigPreference : CustomPreferenceFragment()
{

    //
    // Vals
    //

    override val SuperFragment by lazy()
    {
        currentOpModeName = variant.OpModeName; // make sure this is correct
        val fragment = VariantListPreference();
        fragment;
    }

    private val variant by lazy()
    {
        currentVariant!!;
    }

    //
    // Activity overrides
    //

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.prefs_variant );

        setTitle( "$variant" );

        val deleteVariant = findPreference( "delete_variant" ) as PreferenceScreen;
        deleteVariant.setOnPreferenceClickListener {

            // TODO ask the user to confirm

            variant.delete();

            // if this is default, recreate the preference
            // just in case we somehow managed to get here
            if ( variant.VariantName.equals( "default", ignoreCase = true ) )
            {
                getOpModeConfig( variant.OpModeName, "default" );
            }

            // go back to the previous screen
            currentOpModeName = variant.OpModeName;

            fragmentManager.beginTransaction().replace( android.R.id.content, VariantListPreference() ).commit();

            true;
        };
        deleteVariant.isEnabled = !variant.VariantName.equals( "default", ignoreCase = true ); // disabled for default profiles

        val resetVariant = findPreference( "reset_variant" ) as PreferenceScreen;
        resetVariant.setOnPreferenceClickListener {

            // TODO ask the user to confirm

            variant.clear(); // wipe the values

            // create a new fragment for handling the rest
            fragmentManager.beginTransaction().replace( android.R.id.content, VariantConfigPreference() ).commit();

            true;
        };

        setDefaults();

        val configList = findPreference( "config_list" ) as PreferenceCategory;
        val config = variant.getData();

        // iterate over the values
        for ( ( key, value ) in config )
        {

            // create the correct editor based on the type of the value
            val preference =
                     if ( value is Long )    createNumericPreference( key, value, null );
                else if ( value is Double )  createNumericPreference( key, null, value );
                else if ( value is Boolean ) createBooleanPreference( key, value );
                else                         createStringPreference( key, value.toString() );

            configList.addPreference( preference ); // add the preference
        }
    }

    //
    // Actions
    //

    private fun createBooleanPreference(key: String, value: Boolean ): CheckBoxPreference
    {
        val checkbox = CheckBoxPreference( activity );
        checkbox.title = key;
        checkbox.isChecked = value;
        checkbox.setOnPreferenceChangeListener { preference, value ->

            variant[ key ] = value as Boolean;
            i( "Changed $key (boolean) to $value ($variant)" );

            true;
        };

        return checkbox;
    }

    private fun createStringPreference( key: String, value: String ): EditTextPreference
    {
        val textbox = EditTextPreference( activity );
        textbox.title = "$key";
        textbox.summary = "\t$value";
        textbox.text = value;
        textbox.setOnPreferenceChangeListener { preference, value ->

            variant[ key ] = value as String;
            i( "Changed $key (string) to $value ($variant" );
            textbox.summary = "\t$value";

            true;
        };

        return textbox;
    }

    private fun createNumericPreference( key: String, long: Long?, double: Double? ): EditTextPreference
    {
        if ( !( ( long != null ) xor ( double != null ) ) )
        {
            throw IllegalArgumentException( "Only one value can be null: `long` != null XOR `double` != null" );
        }

        val isLong = long != null;

        val value = ( long ?: double )!!;
        val textbox = EditTextPreference( activity );
        textbox.title = "$key";
        textbox.summary = "\t$value";
        textbox.text = value.toString();

        // allow only signed numbers, and decimals (only if a double)
        textbox.editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED;

        // add the decimal flag if it's a double
        if ( !isLong )
        {
            textbox.editText.inputType = textbox.editText.inputType or InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }


        textbox.setOnPreferenceChangeListener { preference, value ->

            if ( isLong )
            {
                try
                {
                    variant[ key ] = ( value as String ).toLong();
                    i( "Changed $key (long) to $value ($variant)")

                    textbox.summary = "\t$value";

                    true; // this was a valid value
                }
                catch ( e: NumberFormatException )
                {
                    false; // this was an invalid value
                }
            }
            else
            {
                try
                {
                    variant[ key ] = ( value as String ).toDouble();
                    i( "Changed $key (double) to $value ($variant)" );

                    textbox.summary = "\t$value";

                    true;
                }
                catch ( e: NumberFormatException )
                {
                    false; // invalid number format
                }
            }
        };

        return textbox;
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
        catch ( e: Throwable )
        {
            e( "Exception instantiating OpMode (${variant.OpModeName}) for configuration", e );
            e( "Some values may not be present in the configurator" ); // configurator is a word?
        }

        setActiveConfig( name, realActiveVariant ); // undo our cheat
    }

}