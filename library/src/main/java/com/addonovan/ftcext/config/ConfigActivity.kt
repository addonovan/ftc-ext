package com.addonovan.ftcext.config

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.addonovan.ftcext.*

/**
 * The activity used to edit and create new OpMode configurations.
 */
class ConfigActivity : AppCompatActivity()
{

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_config );

        val adapter = ListItemAdapter( this );
        adapter.addAll( getOpModeNames() ); // add all of the opmodes to the custom adapter
        ( findViewById( R.id.config_opmode_list ) as ListView ).adapter = adapter;
    }

}

internal class ListItemAdapter( context: Context ) : ArrayAdapter< String >( context, 0 )
{

    private val inflater by lazy()
    {
        ( context as Activity ).layoutInflater;
    }

    override fun getView( position: Int, convertView: View?, parent: ViewGroup? ): View
    {
        val opMode = getItem( position );
        val activeVariant = getActiveVariant( opMode );

        // only create a new view if necessary
        val view = convertView ?: inflater.inflate( R.layout.opmode_list_item, parent, false );

        // add the view information
        ( view.findViewById( R.id.opmode_name ) as TextView ).text = opMode;
        ( view.findViewById( R.id.active_variant ) as TextView ).text = activeVariant;

        return view;
    }

}