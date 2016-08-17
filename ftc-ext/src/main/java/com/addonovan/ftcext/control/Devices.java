package com.addonovan.ftcext.control;

import com.addonovan.ftcext.hardware.HardwareBundleKt;
import com.qualcomm.robotcore.hardware.HardwareDevice;

/**
 * Class used to get a device by a type.
 *
 * @author addonovan
 * @since 8/16/16
 */
public final class Devices
{
    private Devices() {}

    /**
     * Grabs the device specified by the generics from the correct device mapping.
     * This should only be used in Java, as this is far less efficient than its
     * Kotlin counter-part, which is only possible due to how Kotlin compiles to
     * Java code.
     *
     * @param name
     *          The name of the device.
     * @param <T>
     *          The type of the device.
     * @return The device casted to {@code T}
     */
    @SuppressWarnings( "unchecked" )
    public static < T extends HardwareDevice > T get( String name )
    {
        return ( T ) HardwareMapExtensionsKt.getDeviceByGuess( HardwareBundleKt.getCurrentHardware().getHardwareMap(), name );
    }

}
