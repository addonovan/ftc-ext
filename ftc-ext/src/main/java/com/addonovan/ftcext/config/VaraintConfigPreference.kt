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
package com.addonovan.ftcext.config

import android.os.Bundle
import android.preference.*
import android.text.InputType
import com.addonovan.ftcext.*
import com.addonovan.ftcext.control.OpModes

/**
 * Configures a specific variant's variables acoording to their presumed types.
 */
class VariantConfigPreference : CustomPreferenceFragment()
{

    internal companion object
    {
        var currentVariant: OpModeConfig? = null;
    }

    //
    // Vals
    //

    override val SuperFragment: PreferenceFragment by lazy()
    {
        VariantListPreference.currentOpModeName = variant.OpModeName; // make sure this is correct
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
            VariantListPreference.currentOpModeName = variant.OpModeName;

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
