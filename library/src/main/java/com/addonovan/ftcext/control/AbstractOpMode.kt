package com.addonovan.ftcext.control

import com.addonovan.ftcext.*
import com.addonovan.ftcext.reflection.FieldFinder
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.robocol.Telemetry
import java.util.*

/**
 * The OpMode class built upon Qualcomm's. This class provides more utility
 * than the standard class does, and *must* be used in order to be able
 * to access the other functionality of this library.
 *
 * @see OpMode
 * @see LinearOpMode
 * @see Register
 *
 * @author addonovan
 * @since 6/12/16
 */
abstract class AbstractOpMode()
{

    //
    // Values
    //

    /** The first controller. */
    val gamepad1: Gamepad
            get() = Hardware.gamepad1;

    /** The second controller. */
    val gamepad2: Gamepad
            get() = Hardware.gamepad2;

    /** Telemetry for sending back current states and what not. */
    val telemetry: Telemetry
            get() = Hardware.telemetry;

    /** Direct access to the hardware map. Directly referencing this is discouraged. */
    val hardwareMap: HardwareMap
            get() = Hardware.hardwareMap;

    //
    // Things from Original OpMode
    //

    /**
     * Initialize the robot. This occurs directly after the OpMode
     * is selected.
     */
    abstract fun init();

    /**
     * Called the moment that the start button is pressed on the
     * Driver Station remote. This is the first code that is executed
     * when an OpMode is running.
     */
    open fun start() {};

    /**
     * Called the moment that the stop button is pressed (or the
     * timer runs our) on the Driver Station remote. This is the
     * last code that is executed when an OpMode is running.
     */
    open fun stop() {};

    //
    // Device Fetching
    //

    /** All the different types of DeviceMappings in the hardware map */
    private val hardwareMaps by lazy()
    {
        FieldFinder( hardwareMap ).inheritsFrom( HardwareMap.DeviceMapping::class.java ).get();
    }

    /**
     * The map of cached device mappings. Awful name for it, I know.
     * The key is the class in the generics of the value, i.e.
     * `deviceMappingMap[ Int.javaClass ] = HardwareMap.DeviceMapping< Int >`
     */
    private val deviceMappingMap = HashMap< Class< * >, HardwareMap.DeviceMapping< * > >();

    /**
     * Finds the device with the given name in the correct device mapping based off of
     * the type parameters. For example:
     *
     * `private final DcMotor left_motor = getDevice< DcMotor >( "left_motor" );`
     *
     * instead of
     *
     * `private final DcMotor left_motor = getHardwareMap().dcMotor.get( "left_motor" );`
     *
     * On top of being (ever-so-slightly) shorter, this replaces multiple different
     * method and field calls (i.e. [hardwareMap.dcMotor], [hardwareMap.irSeekerSensor], etc),
     * and ensures that the device will never be `null`, because this method will error
     * immediately on class initialization if it is, instead of whenever the first
     * methods are invoked on it.
     *
     * @param[name]
     *          The name of the hardware device.
     * @return The device with the given name and the specified type (via type parameters).
     *
     * @throws NullPointerException
     *          If no hardware device with the given name could be found.
     * @throws IllegalArgumentException
     *          If the generic type wasn't a supported type in the hardware map.
     */
    final fun < T : HardwareDevice > getDevice( name: String ): T
    {
        val type = getGenericType( ArrayList< T >() ); // oh god, this is such a hack
        val map = getDeviceMapping< T >( type ); // the corresponding map for this type

        val value = map[ name ] ?: throw NullPointerException( "No hardware of type ${type.simpleName} with the name $name" );

        return value;
    }

    /**
     * Gets the correct [HardwareMap.DeviceMapping] for the given type.
     *
     * @param[type]
     *          The class type (in reality, it's the class of `T`, but that's not checked).
     * @return The correct [HardwareMap.DeviceMapping] for type `T`.
     *
     * @throws IllegalArgumentException
     *          If the generic type wasn't a supported type in the hardware map.
     */
    @Suppress( "unchecked_cast" ) // the cast is checked via reflections
    private fun < T > getDeviceMapping( type: Class< * > ): HardwareMap.DeviceMapping< T >
    {
        // if we've already found the device mapping before, just look it up
        if ( deviceMappingMap.containsKey( type ) )
        {
            return deviceMappingMap[ type ] as HardwareMap.DeviceMapping< T >; // this should be ensured by how the data is entered into the map
        }

        // search for the correct hardware map manually
        for ( field in hardwareMaps )
        {
            val deviceMapping = field.get( hardwareMap ) as HardwareMap.DeviceMapping< * >; // find the specific map for this OpMode

            // if you didn't want to see hacks, you shouldn't've come here :/

            // find the data-backing hashmap's field
            val mapField = deviceMapping.javaClass.getDeclaredField( "a" );
            mapField.isAccessible = true; // force it to be accessible

            // get the value of the field
            val map = mapField.get( deviceMapping ) as HashMap< String, * >;

            // if the type of the map is the same one as the one we're given
            if ( getGenericType( map.values ).isAssignableFrom( type ) )
            {
                val value = deviceMapping as HardwareMap.DeviceMapping< T >;
                deviceMappingMap[ type ] = value; // cache it in the map so we don't have to do this loop again
                return value;
            }
        }

        // doing this ensures that we'll never have to check for null types anywhere!
        // also, if it somehow gets to this, the user is an idiot anyways, and needs
        // to immediately see that it fails.
        throw IllegalArgumentException( "No hardware map available for type ${type.simpleName}" );
    }

}
