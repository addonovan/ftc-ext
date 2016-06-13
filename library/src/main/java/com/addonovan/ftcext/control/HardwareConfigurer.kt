package com.addonovan.ftcext.control

import com.addonovan.ftcext.*
import com.addonovan.ftcext.annotation.Hardware
import com.addonovan.ftcext.reflection.FieldFinder
import com.qualcomm.robotcore.hardware.HardwareMap
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*

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

    /** The fields (if non-Kotlin) that are marked with @Hardware to be configured. */
    private val fieldFinder: FieldFinder?;

    /** The fields for the hardware maps that we can access via the [OpMode.hardwareMaps] object. */
    private val hardwareMaps: ArrayList< Field >;

    //
    // Constructor
    //

    init
    {
        i( "Configuring Hardware for ${getRegisterName( opMode )}" );

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
            fieldFinder = FieldFinder( opMode ).with( Hardware::class.java )
                                    .`is`( Modifier.STATIC )
                                    .isNot( Modifier.FINAL, Modifier.ABSTRACT, lenient = false );
        }

        // hardware maps are found the same way
        hardwareMaps = FieldFinder( opMode.hardwareMap ).inheritsFrom( HardwareMap.DeviceMapping::class.java ).get();
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
            // the only reason I make this assertion is because I don't want to use
            // the nasty-ass ?. operators later on.
            if ( fieldFinder == null )
            {
                wtf( "fieldFinder == null! This should NEVER be true if this is a java file!" );
                wtf( "Unable to configure hardware devices because of this!" );
                return;
            }

            // loop over all the hardware maps we have
            hardwareMaps.forEach { field ->

                // get the device mapping from the list
                val map = field.get( opMode.hardwareMap ) as HardwareMap.DeviceMapping< * >;

                // find only the fields that match the generic type of the DeviceMapping
                // (we duplicate() so that we can reuse the fieldFinder for other types)
                val fields = fieldFinder.duplicate().inheritsFrom( getGenericType( map ) ).get();

                // with all of our fields of this type, set the value of the field
                // to what this map says the value for the name of the field is
                fields.forEach { field ->
                    field.set( opMode, map[ getName( field ) ] );
                };
            }
        }
    }

    /**
     * @return The value of the @Hardware's name property, or, if it's blank, the name of the field.
     */
    private fun getName( field: Field ): String
    {
        val name = getHardwareName( field );

        return if ( name.isNullOrBlank() ) field.name;
               else                        name;
    }

}