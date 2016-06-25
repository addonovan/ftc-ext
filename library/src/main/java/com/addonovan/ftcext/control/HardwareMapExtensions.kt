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
            // TODO
        }

        throw Exception();
    }

fun HardwareMap.getDeviceByType( type: Class< out HardwareDevice>, name: String ): HardwareDevice
{
    throw Exception();
}
