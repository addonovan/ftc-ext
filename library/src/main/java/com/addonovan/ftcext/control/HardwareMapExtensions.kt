package com.addonovan.ftcext.control

import com.addonovan.ftcext.*
import com.addonovan.ftcext.hardware.*
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping
import java.lang.reflect.*
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
 * Finds the correct Hardware extension constructor.
 *
 * @param[type]
 *          The hardware extension class.
 *
 * @return The constructor for the extension, if one exists.
 */
private fun HardwareMap.findExtensionConstructor( type: Class< out HardwareDevice > ): Constructor< out HardwareDevice >
{
    var constructor: Constructor< out HardwareDevice >? = null;

    // If the class is an @HardwareExtension, we can grab the base type right out of the
    // annotation parameters

    val baseType = type.getHardwareMapType().java;

    // ensure the base type is a valid one
    if ( !deviceClassMap.containsKey( baseType ) )
    {
        e( "The @HardwareExtension.hardwareMapType value for class \"${type.simpleName}\" isn't supported!" );
        throw IllegalAnnotationValueException( HardwareExtension::class.java, "hardwareMapType", type );
    }

    // ensure that there's a correct constructor for the extension class
    try
    {
        constructor = type.getConstructor( baseType );
    }
    catch ( nsme: NoSuchMethodException ) {}

    // try to select a more specific one, but fall back to the other one
    try
    {
        constructor = type.getConstructor( baseType, String::class.java );
    }
    catch ( nsme: NoSuchMethodException ) {}

    // if it's still null at this point
    if ( constructor == null )
    {
        e( "Failed to find a constructor of either type in the hardware extension class: ${type.simpleName}!" );
        throw IllegalClassSetupException(
                type, "HardwareExtension devices must conform to certain constructor requirements!"
        );
    }

    return constructor;
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
 * @throws IllegalAnnotationValueException
 *          If the [type] class had a [HardwareExtension] annotation on it that had
 *          an invalid value (that is, one that doesn't have a value in the backing
 *          map of classes and `DeviceMapping`s) assigned to the `hardwareMapType`
 *          parameter.
 *          This should *never* happen to an end-user, that would mean that the
 *          developer who wrote the extension did so wrongly.
 * @throws IllegalClassSetupException
 *          If [type] is a [HardwareExtension] that has been insufficiently
 *          set up, which could be for a multitude of reasons, check the
 *          error message if this occurs for more details.
 */
@Suppress( "unchecked_cast" )
fun HardwareMap.getDeviceByType( type: Class< out HardwareDevice >, name: String ): HardwareDevice
{
    var baseType = type;
    val isExtension = type.isHardwareExtension();
    var constructor: Constructor< out HardwareDevice >? = null;

    if ( isExtension )
    {
        constructor = findExtensionConstructor( type );
        baseType = type.getHardwareMapType().java;
    }
    else
    {
        // keep going up the hierarchy for hardware devices until:
        // 1. the base type is in the class map
        // 2. the base type's superclass is HardwareDevice itself
        //
        // after the loop breaks we know that:
        // if there's a key for "baseType" that we can find the correct device mapping for the type
        // otherwise, we need to throw an exception because there's no map for the given type

        while ( !deviceClassMap.containsKey( baseType ) && baseType.superclass != HardwareDevice::class.java )
        {
            baseType = baseType.superclass as Class< out HardwareDevice >;
        }
    }

    // the second condition was met, but the device is still not a
    // direct descendant of anything in the hardware device mappings
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
        val device = deviceMapping[ name ]!!;

        if ( isExtension )
        {
            // if it's an extension, create the extension object and return it

            // 1 parameter is just the hardware device
            if ( constructor!!.typeParameters.size == 1 )
            {
                return constructor.newInstance( device );
            }
            // 2 parameters is the hardware device AND its name
            else if ( constructor.typeParameters.size == 2 )
            {
                return constructor.newInstance( device, name );
            }
            else
            {
                throw IllegalStateException( "huh but how?" ); // not descriptive, but will probably never occur
            }
        }
        else
        {
            // if it's not an extension, just return the value from the map
            return device;
        }
    }
    // catch an IllegalArgumentException from the deviceMapping.get() method
    catch ( e: IllegalArgumentException )
    {
        e( "Failed to find the device by name $name!", e );
        throw NullPointerException( "No device with the type ${type.simpleName} by the name \"$name\"" );
    }
    // catch the other exceptions
    catch ( e: Throwable )
    {
        // if they're from the newInstance invocation
        if ( e is InstantiationError ) throw IllegalClassSetupException( type, "class is uninstantiable" );
        if ( e is IllegalAccessException ) throw IllegalClassSetupException( type, "constructor isn't accessible" );
        // IllegalArgumentException can't happen as the constructor has already been chosen for the specified arguments

        if ( e is InvocationTargetException ) e( "Exception while invoking HardwareExtension constructor: ${e.javaClass.name}" );

        throw e; // throw it again
    }
}
