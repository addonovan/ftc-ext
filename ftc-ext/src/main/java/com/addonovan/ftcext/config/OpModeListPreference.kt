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
import android.preference.PreferenceCategory
import com.addonovan.ftcext.*
import com.addonovan.ftcext.control.OpModes

/**
 * !Description!
 *
 * @author addonovan
 * @since 8/15/16
 */
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

                SelectedOpMode.Name = name;

                // switch to the new fragment
                fragmentManager.beginTransaction().replace( android.R.id.content, VariantListPreference() ).commit();

                true; // click handled
            };

            opModeList.addPreference( opModeScreen );
        }
    }

}
