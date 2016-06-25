package com.addonovan.ftcext.control

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

private inline fun < reified T : HardwareDevice > addToMap( mapping: DeviceMapping< T > )
{
    _deviceClassMap[ T::class.java ] = mapping;
}

fun HardwareMap.getDeviceByType( type: Class< out HardwareDevice>, name: String ): HardwareDevice
{
    throw Exception();
}
