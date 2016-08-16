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
import com.addonovan.ftcext.*

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
        SelectedOpMode.Name!!;
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

                SelectedOpMode.Profile = config;

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
        SelectedOpMode.Name = opModeName;

        // restart the fragment to handle the new variant
        fragmentManager.beginTransaction().replace( android.R.id.content, VariantListPreference() ).commit();

        return true; // the preference can be updated
    }

}
