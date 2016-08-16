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
package com.addonovan.ftcext.config;

import android.os.Bundle
import android.preference.PreferenceFragment

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
