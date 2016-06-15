package com.addonovan.ftcext.control

import com.addonovan.ftcext.*
import com.addonovan.ftcext.annotation.UsesKotlin
import com.addonovan.ftcext.reflection.FieldFinder
import com.qualcomm.robotcore.hardware.HardwareMap
import java.util.*

/**
 * The OpMode class built upon Qualcomm's. This class provides more utility
 * than the standard class does, and _must_ be used in order to be able
 * to access the other functionality of this library.
 *
 * @author addonovan
 * @since 6/12/16
 */
abstract class OpMode : com.qualcomm.robotcore.eventloop.opmode.OpMode()
{

    //
    // Initializations
    //

    /**
     * The final override of the init method.
     * We need this in our abstract class to get basic things done that will
     * make the subclass(es) easier to use.
     */
    final override fun init()
    {
        HardwareConfigurer( this, isKotlin( javaClass ) ).configure(); // configure the hardwares in this object
        initRobot(); // perform regular initialization
    }

    //
    // Device Fetching
    //

    /** All the different types of DeviceMappings in the hardware map */
    private val hardwareMaps by lazy()
    {
        FieldFinder( hardwareMap ).inheritsFrom( HardwareMap.DeviceMapping::class.java ).get();
    }

    /**
     * Finds the device with the given name in the correct device mapping based off of
     * the type parameters. For example:
     * ```java
     * private final DcMotor left_motor = getDevice< DcMotor >( "left_motor" );
     * ```
     * instead of
     * ```java
     * private final DcMotor left_motor = hardwareMap.dcMotor.get( "left_motor" );
     * ```
     * On top of being (ever-so-slightly) shorter, this replaces multiple different
     * method and field calls (i.e. [hardwareMap.dcMotor], [hardwareMap.irSeekerSensor], etc),
     * and ensures that the device will never be `null`, because this method will error
     * immediately on class initialization if it is, instead of whenever the first
     * methods are invoked on it.
     *
     * @param[name]
     *          The name of the hardware device.
     * @return The device with the given name and the specified type (via type parameters).
     */
    final fun < T > getDevice( name: String ): T
    {
        val type = getGenericType( ArrayList< T >() ); // oh god, this is such a hack
        val map = getDeviceMapping< T >( type ); // the corresponding map for this type

        val value = map[ name ] ?: throw NullPointerException( "No hardware of type ${type.simpleName} with the name $name" );

        return value;
    }

    /**
     * Gets the correct [DeviceMapping] for the given type.
     *
     * @param[type]
     *          The class type (in reality, it's the class of `T`, but that's not checked).
     * @return The correct [DeviceMapping] for type `T`.
     */
    @Suppress( "unchecked_cast" ) // the cast is checked via reflections
    private fun < T > getDeviceMapping( type: Class< * > ): HardwareMap.DeviceMapping< T >
    {
        for ( field in hardwareMaps )
        {
            val map = field.get( hardwareMap ) as HardwareMap.DeviceMapping< * >; // find the specific map for this OpMode

            // if the type of the map is the same one as the one we're given
            if ( getGenericType( map ).isAssignableFrom( type ) )
            {
                return map as HardwareMap.DeviceMapping< T >;
            }
        }

        // doing this ensures that we'll never have to check for null types anywhere!
        // also, if it somehow gets to this, the user is an idiot anyways, and needs
        // to immediately see that it fails.
        throw IllegalArgumentException( "No hardware map available for type ${type.simpleName}" );
    }

    //
    // Abstracts
    //

    /**
     * Initializes the robot as the `init()` method would in the standard
     * `OpMode` class.
     */
    abstract fun initRobot();

    abstract override fun loop(); // just here so it's easier to tell what the subclass needs to override

}

/**
 * Determines if the given class uses Kotlin or not.
 *
 * @param[class]
 *          The class to test for the `@UsesKotlin` control.
 * @return If `class` has the `@UsesKotlin` control on it.
 */
private fun isKotlin( `class`: Class< out OpMode > ) = `class`.isAnnotationPresent( UsesKotlin::class.java )
