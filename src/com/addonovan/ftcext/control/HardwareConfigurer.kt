package com.addonovan.ftcext.control

import com.addonovan.ftcext.annotation.Hardware
import com.addonovan.ftcext.reflection.FieldFinder
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*
import kotlin.reflect.KProperty

/**
 * Configures the Hardware devices in the OpMode class.
 *
 * @author addonovan
 * @since 6/12/16
 */
class HardwareConfigurer( val opMode: OpMode, private val isKotlin: Boolean )
{

    //
    // Vals
    //

    /** The fields for a nonKotlin OpMode */
    private val fields: ArrayList< Field > = ArrayList();

    //
    // Constructor
    //

    init
    {
        if ( isKotlin )
        {
            throw UnsupportedOperationException( "Kotlin properties are not yet able to be set by the HardwareConfigurer" );
        }
        else
        {
            // find only the hardware fields with the correct modifiers
            // must have @Hardware
            // must be public AND static
            // must be neither final NOR abstract
            val hardwareFields = FieldFinder( opMode ).with( Hardware::class.java )
                                    .`is`( Modifier.STATIC )
                                    .isNot( Modifier.FINAL, Modifier.ABSTRACT, lenient = false )
                                    .get();

            fields.addAll( hardwareFields ); // add all the hardware fields
        }
    }

    //
    // Actions
    //

    fun configure()
    {
        if ( isKotlin )
        {
            throw UnsupportedOperationException( "Kotlin properties are not yet able to be set by the HardwareConfigurer" );
        }
        else
        {

        }
    }

}