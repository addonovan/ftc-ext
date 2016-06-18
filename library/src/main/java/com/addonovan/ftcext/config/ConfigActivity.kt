package com.addonovan.ftcext.config

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.addonovan.ftcext.R
import kotlinx.android.synthetic.main.activity_config.*

/**
 * The activity used to edit and create new OpMode configurations.
 */
class ConfigActivity : AppCompatActivity()
{

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_config );
    }

}
