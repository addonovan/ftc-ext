package com.addonovan.ftcext.control

import com.addonovan.ftcext.*
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping
import java.util.*

/**
 * HardwareMap Extension functions
 *
 * @author addonovan
 * @since 6/25/16
 */

private val _deviceClassMap = HashMap< Class< out HardwareDevice >, DeviceMapping< out HardwareDevice > >();

/** A map of the base hardware class versus the  */
private val HardwareMap.deviceClassMap: HashMap< Class< out HardwareDevice >, DeviceMapping< out HardwareDevice > >
    get()
    {
        // initialize the map the first time around
        if ( _deviceClassMap.size == 0 )
        {
             addToMap( dcMotorController );
             addToMap( dcMotor );
             addToMap( servoController );
             addToMap( servo );
             addToMap( legacyModule );
             addToMap( touchSensorMultiplexer );
             addToMap( deviceInterfaceModule );
             addToMap( analogInput );
             addToMap( digitalChannel );
             addToMap( opticalDistanceSensor );
             addToMap( touchSensor );
             addToMap( pwmOutput );
             addToMap( i2cDevice );
             addToMap( analogOutput );
             addToMap( colorSensor );
             addToMap( led );
             addToMap( accelerationSensor );
             addToMap( compassSensor );
             addToMap( gyroSensor );
             addToMap( irSeekerSensor );
             addToMap( lightSensor );
             addToMap( ultrasonicSensor );
             addToMap( voltageSensor );
        }

        return _deviceClassMap;
    }

/**
 * Used to insert a device mapping into the backing device class mapping
 *
 * @param[mapping]
 *          The device mapping to add to the map.
 */
private inline fun < reified T : HardwareDevice > addToMap( mapping: DeviceMapping< T > )
{
    _deviceClassMap[ T::class.java ] = mapping;
}

/**
 * Gets the correct device from the Hardware device mappings in the HardwareMap by
 * using the type of HardwareDevice to find the correct hardware device mapping
 * and the name to get the correct value out of it.
 *
 * @param[type]
 *          The type of hardware device to find.
 * @param[name]
 *          The name of the hardware device to return.
 *
 * @return The hardware device with the given type and name.
 *
 * @throws IllegalArgumentException
 *          If [type] had no DeviceMapping associated with it.
 * @throws NullPointerException
 *          If there was no entry for [name] with the type [type].
 */
@Suppress( "unchecked_cast" )
fun HardwareMap.getDeviceByType( type: Class< out HardwareDevice >, name: String ): HardwareDevice
{
    // keep going up the hierarchy for hardware devices until:
    // 1. the base type is in the class map
    // 2. the base type's superclass is HardwareDevice itself
    //
    // after the loop breaks we know that:
    // if there's a key for "baseType" that we can find the correct device mapping for the type
    // otherwise, we need to throw an exception because there's no map for the given type

    var baseType = type;
    while ( !deviceClassMap.containsKey( baseType ) && baseType.superclass != HardwareDevice::class.java )
    {
        baseType = baseType.superclass as Class< out HardwareDevice >;
    }

    // the second condition was met, but the device is still not a
    // direct decendent of anything in the hardware device mappings
    // so we can't find the correct map for it
    if ( !deviceClassMap.containsKey( baseType ) )
    {
        e( "Can't find a device mapping for the given type: ${type.name}" );
        e( "This means that there's no way to get the hardware device from the hardware map!" );
        throw IllegalArgumentException( "No hardware device mapping for type: ${type.canonicalName}!" );
    }

    val deviceMapping = deviceClassMap[ type ]!!; // we're ensured that one exists by the previous block

    try
    {
        return deviceMapping[ name ];
    }
    catch ( e: Exception )
    {
        e( "Failed to find the device by name $name!", e );
        throw NullPointerException( "No device with the type ${type.simpleName} by the name \"$name\"" );
    }
}
